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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String PREFS_NAME = "FitnessPrefs";
    private static final String LAST_WEIGHT_DATE_KEY = "last_weight_date";
    private static final String STEPS_COUNT_KEY = "steps_count";
    private static final String CHANNEL_ID = "workout_reminder_channel";
    
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int totalSteps = 0;
    private int previousTotalSteps = 0;

    private TextView stepsTextView, caloriesTextView, weightTextTop, distanceTextView, activeMinutesTextView;
    private EditText weightInput;
    private Button saveWeightBtn;
    private Button btnRemindWorkout;
    private BottomNavigationView bottomNavigationView;

    private double lastWeight = 0.0;
    private FitnessDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            
            // Initialize database first
            dbHelper = new FitnessDbHelper(this);
            
            // Create notification channel
            createNotificationChannel();
            
            // Initialize views
            initializeViews();
            
            // Set up sensor
            setupStepSensor();
            
            // Check and request permissions
            checkAndRequestPermissions();
            
            // Set up weight entry restrictions
            setupWeightEntryRestrictions();
            
            // Set up click listeners
            setupClickListeners();
            
            // Set up bottom navigation
            setupBottomNavigation();
            
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing app: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        try {
            stepsTextView = findViewById(R.id.stepsTextView);
            caloriesTextView = findViewById(R.id.caloriesTextView);
            distanceTextView = findViewById(R.id.distanceTextView);
            activeMinutesTextView = findViewById(R.id.activeMinutesTextView);
            weightTextTop = findViewById(R.id.weightTextTop);
            weightInput = findViewById(R.id.weightInput);
            saveWeightBtn = findViewById(R.id.saveWeightButton);
            btnRemindWorkout = findViewById(R.id.btnRemindWorkout);
            bottomNavigationView = findViewById(R.id.bottomNavigationView);

            // Set initial values
            stepsTextView.setText("0");
            caloriesTextView.setText("0.00 kcal");
            distanceTextView.setText("0.00 km");
            activeMinutesTextView.setText("0 min");
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupStepSensor() {
        try {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager != null) {
                stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                if (stepSensor == null) {
                    Toast.makeText(this, "Step counter sensor not available", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up step sensor: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupWeightEntryRestrictions() {
        try {
            final long FIFTEEN_DAYS_MILLIS = 15L * 24 * 60 * 60 * 1000;
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            long lastWeightMillis = prefs.getLong(LAST_WEIGHT_DATE_KEY, 0);
            long now = System.currentTimeMillis();

            if (lastWeightMillis > 0 && now - lastWeightMillis < FIFTEEN_DAYS_MILLIS) {
                weightInput.setEnabled(false);
                saveWeightBtn.setEnabled(false);
                weightInput.setHint("You can update your weight after 15 days");
            } else {
                weightInput.setEnabled(true);
                saveWeightBtn.setEnabled(true);
                weightInput.setHint("Enter weight (kg)");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up weight restrictions: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        try {
            // Quick action buttons
            MaterialButton trackMealButton = findViewById(R.id.trackMealButton);
            MaterialButton viewProgressButton = findViewById(R.id.viewProgressButton);
            MaterialButton generatePlanButton = findViewById(R.id.generatePlanButton);

            trackMealButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DietActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });

            viewProgressButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });

            generatePlanButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LibraryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });

            // Weight tracking
            saveWeightBtn.setOnClickListener(v -> saveWeight());

            // Workout reminder
            btnRemindWorkout.setOnClickListener(v -> showTimePickerDialog());

        } catch (Exception e) {
            Toast.makeText(this, "Error setting up click listeners: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Workout Reminders",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Daily workout reminder notifications");
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error creating notification channel: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.BODY_SENSORS_BACKGROUND,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.SCHEDULE_EXACT_ALARM,
                Manifest.permission.USE_EXACT_ALARM
        };

        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
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
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                Toast.makeText(this, "Some permissions were not granted. App functionality may be limited.", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void setupBottomNavigation() {
        try {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                Intent intent = null;
                
                if (itemId == R.id.navigation_home) {
                    // Already in home, do nothing
                    return true;
                } else if (itemId == R.id.navigation_workout) {
                    intent = new Intent(MainActivity.this, WorkoutGuideActivity.class);
                } else if (itemId == R.id.navigation_diet) {
                    intent = new Intent(MainActivity.this, DietActivity.class);
                } else if (itemId == R.id.navigation_profile) {
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                } else if (itemId == R.id.navigation_library) {
                    intent = new Intent(MainActivity.this, LibraryActivity.class);
                }
                
                if (intent != null) {
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                return true;
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up bottom navigation: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // --- Workout Reminder Methods ---
    private void showTimePickerDialog() {
        java.util.Calendar now = java.util.Calendar.getInstance();
        int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = now.get(java.util.Calendar.MINUTE);
        new android.app.TimePickerDialog(this, (view, hourOfDay, minute1) -> setWorkoutReminder(hourOfDay, minute1), hour, minute, true).show();
    }

    private void setWorkoutReminder(int hour, int minute) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WorkoutReminderReceiver.class);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(this, 0, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour);
        calendar.set(java.util.Calendar.MINUTE, minute);
        calendar.set(java.util.Calendar.SECOND, 0);
        long triggerTime = calendar.getTimeInMillis();
        if (System.currentTimeMillis() > triggerTime) {
            triggerTime += 24 * 60 * 60 * 1000; // Next day
        }
        alarmManager.setRepeating(android.app.AlarmManager.RTC_WAKEUP, triggerTime, android.app.AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this, "Workout reminder set for " + String.format("%02d:%02d", hour, minute), Toast.LENGTH_LONG).show();
    }

    private void saveWeight() {
        try {
            String weightStr = weightInput.getText().toString();
            if (weightStr.isEmpty()) {
                Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight = Double.parseDouble(weightStr);
            if (weight <= 0) {
                Toast.makeText(this, "Please enter a valid weight", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save weight to database
            long result = dbHelper.addWeightEntry(weight);
            if (result == -1) {
                Toast.makeText(this, "Failed to save weight", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update UI
            weightTextTop.setText(String.format("%.1f kg", weight));
            weightInput.setText("");
            weightInput.setEnabled(false);
            saveWeightBtn.setEnabled(false);
            weightInput.setHint("You can update your weight after 15 days");

            // Save last weight update time
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putLong(LAST_WEIGHT_DATE_KEY, System.currentTimeMillis()).apply();

            Toast.makeText(this, "Weight saved successfully", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving weight: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

