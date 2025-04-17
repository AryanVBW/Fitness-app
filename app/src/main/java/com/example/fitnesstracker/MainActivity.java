package com.example.fitnesstracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import android.content.SharedPreferences;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.fitnesstracker.database.FitnessDbHelper;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String PREFS_NAME = "FitnessPrefs";
    private static final String LAST_WEIGHT_DATE_KEY = "last_weight_date";
    private static final String STEPS_COUNT_KEY = "steps_count";
    
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int totalSteps = 0;
    private int previousTotalSteps = 0;

    private TextView stepsTextView, caloriesTextView, weightTextTop;
    private EditText weightInput;
    private Button saveWeightBtn;

    private double lastWeight = 0.0;
    private FitnessDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize database helper
        dbHelper = new FitnessDbHelper(this);

        stepsTextView = findViewById(R.id.stepsTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        // weightTextView = findViewById(R.id.weightTextView); // No longer in layout
        weightTextTop = findViewById(R.id.weightTextTop);
        weightInput = findViewById(R.id.weightInput);
        saveWeightBtn = findViewById(R.id.saveWeightButton); // Updated ID
        // Using card clicks for workouts

        // Animate main stats
        stepsTextView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        caloriesTextView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        // weightTextView no longer exists
        weightTextTop.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        saveWeightBtn.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_in));
        
        // Animate workout cards
        findViewById(R.id.pushupWorkoutCard).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_in));
        findViewById(R.id.cardioWorkoutCard).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_in));
        findViewById(R.id.chestWorkoutCard).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_in));
        // Set up click listeners for workout cards
        findViewById(R.id.chestWorkoutCard).setOnClickListener(v -> {
            // Log workout in database (30 minutes of chest workout burns ~150 calories)
            dbHelper.addWorkout("Chest Workout", 30, 150);
            try {
                Intent intent = new Intent(MainActivity.this, ChestWorkoutDetailActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Chest workout details coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        
        findViewById(R.id.pushupWorkoutCard).setOnClickListener(v -> {
            // Log workout in database (15 minutes of pushups burns ~80 calories)
            dbHelper.addWorkout("Pushups", 15, 80);
            try {
                Intent intent = new Intent(MainActivity.this, PushupsDetailActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Pushups workout details coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        
        findViewById(R.id.cardioWorkoutCard).setOnClickListener(v -> {
            // Log workout in database (30 minutes of cardio burns ~200 calories)
            dbHelper.addWorkout("Cardio", 30, 200);
            try {
                Intent intent = new Intent(MainActivity.this, CardioDetailActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Cardio workout details coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        checkAndRequestPermissions();

        // Weight entry restriction logic
        final long FIFTEEN_DAYS_MILLIS = 15L * 24 * 60 * 60 * 1000;
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long lastWeightMillis = prefs.getLong(LAST_WEIGHT_DATE_KEY, 0);
        long now = System.currentTimeMillis();

        if (lastWeightMillis > 0 && now - lastWeightMillis < FIFTEEN_DAYS_MILLIS) {
            // Less than 15 days since last entry
            weightInput.setEnabled(false);
            saveWeightBtn.setEnabled(false);
            weightInput.setHint("You can update your weight after 15 days");
            Toast.makeText(this, "You can update your weight after 15 days", Toast.LENGTH_LONG).show();
        } else {
            // Allow entry
            weightInput.setEnabled(true);
            saveWeightBtn.setEnabled(true);
            weightInput.setHint("Enter weight (kg)");
        }

        saveWeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_in));
                String input = weightInput.getText().toString();
                if (!input.isEmpty()) {
                    try {
                        lastWeight = Double.parseDouble(input);
                        // Update only the top weight text view since weightTextView no longer exists
                        weightTextTop.setText("Weight: " + lastWeight + " kg");
                        // Save the current time as the last entry
                        // Save to SharedPreferences
                        prefs.edit().putLong(LAST_WEIGHT_DATE_KEY, System.currentTimeMillis()).apply();
                        
                        // Save to database
                        dbHelper.addWeightEntry(lastWeight);
                        Toast.makeText(MainActivity.this, "Weight saved", Toast.LENGTH_SHORT).show();
                        // Disable input after saving
                        weightInput.setEnabled(false);
                        saveWeightBtn.setEnabled(false);
                        weightInput.setHint("You can update your weight after 15 days");
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Please enter a valid weight", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter your weight", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // Set up bottom navigation
        setupBottomNavigation();
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_CODE);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stepSensor != null) {
            sensorManager.unregisterListener(this, stepSensor);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = (int) event.values[0];
            int stepsToday = totalSteps - previousTotalSteps;
            stepsTextView.setText("Steps: " + stepsToday);
            caloriesTextView.setText("Calories: " + estimateCalories(stepsToday));
            
            // Save steps to SharedPreferences for Report section
            saveStepsToPrefs(stepsToday);
        }
    }
    
    private void saveStepsToPrefs(int steps) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(STEPS_COUNT_KEY, steps).apply();
    }

    private String estimateCalories(int steps) {
        // Simple estimate: 0.04 kcal per step
        double calories = steps * 0.04;
        return String.format("%.2f kcal", calories);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission denied. Some features may not work.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    private void setupBottomNavigation() {
        LinearLayout dietNavButton = findViewById(R.id.dietNavButton);
        LinearLayout workoutNavButton = findViewById(R.id.workoutNavButton);
        LinearLayout reportNavButton = findViewById(R.id.reportNavButton);
        
        // Workout button is already active
        workoutNavButton.setBackgroundResource(android.R.color.darker_gray);
        
        dietNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DietActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
        reportNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
