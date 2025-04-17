package com.example.fitnesstracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.database.FitnessDbHelper;

public class ChestWorkoutDetailActivity extends AppCompatActivity {
    
    private static final String PREFS_NAME = "FitnessPrefs";
    private static final String CHEST_WORKOUT_PROGRESS_KEY = "chest_workout_progress";
    
    private FitnessDbHelper dbHelper;
    private CheckBox step1CheckBox, step2CheckBox, step3CheckBox, step4CheckBox, step5CheckBox;
    private Button completeWorkoutButton;
    
    // Calorie values for each exercise
    private static final double BENCH_PRESS_CALORIES = 40;
    private static final double INCLINE_PRESS_CALORIES = 35;
    private static final double CHEST_DIPS_CALORIES = 30;
    private static final double CABLE_FLYES_CALORIES = 25;
    private static final double PUSHUPS_CALORIES = 20;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chest_workout_detail);
        
        // Initialize database helper
        dbHelper = new FitnessDbHelper(this);
        
        // Initialize views
        step1CheckBox = findViewById(R.id.step1CheckBox);
        step2CheckBox = findViewById(R.id.step2CheckBox);
        step3CheckBox = findViewById(R.id.step3CheckBox);
        step4CheckBox = findViewById(R.id.step4CheckBox);
        step5CheckBox = findViewById(R.id.step5CheckBox);
        completeWorkoutButton = findViewById(R.id.completeWorkoutButton);
        
        // Load saved progress
        loadWorkoutProgress();
        
        // Set up complete workout button
        completeWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkoutProgress();
                calculateAndSaveCalories();
                Toast.makeText(ChestWorkoutDetailActivity.this, "Workout completed and saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void loadWorkoutProgress() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int progress = prefs.getInt(CHEST_WORKOUT_PROGRESS_KEY, 0);
        
        // Check each bit to determine checkbox state
        step1CheckBox.setChecked((progress & 1) != 0);
        step2CheckBox.setChecked((progress & 2) != 0);
        step3CheckBox.setChecked((progress & 4) != 0);
        step4CheckBox.setChecked((progress & 8) != 0);
        step5CheckBox.setChecked((progress & 16) != 0);
    }
    
    private void saveWorkoutProgress() {
        // Use bit flags to save checkbox states
        int progress = 0;
        if (step1CheckBox.isChecked()) progress |= 1;
        if (step2CheckBox.isChecked()) progress |= 2;
        if (step3CheckBox.isChecked()) progress |= 4;
        if (step4CheckBox.isChecked()) progress |= 8;
        if (step5CheckBox.isChecked()) progress |= 16;
        
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(CHEST_WORKOUT_PROGRESS_KEY, progress).apply();
    }
    
    private void calculateAndSaveCalories() {
        double totalCalories = 0;
        int completedExercises = 0;
        int totalDuration = 0;
        
        // Add calories for each completed exercise
        if (step1CheckBox.isChecked()) {
            totalCalories += BENCH_PRESS_CALORIES;
            completedExercises++;
            totalDuration += 8; // Approx time for 4 sets
        }
        
        if (step2CheckBox.isChecked()) {
            totalCalories += INCLINE_PRESS_CALORIES;
            completedExercises++;
            totalDuration += 8;
        }
        
        if (step3CheckBox.isChecked()) {
            totalCalories += CHEST_DIPS_CALORIES;
            completedExercises++;
            totalDuration += 6; // 3 sets
        }
        
        if (step4CheckBox.isChecked()) {
            totalCalories += CABLE_FLYES_CALORIES;
            completedExercises++;
            totalDuration += 6;
        }
        
        if (step5CheckBox.isChecked()) {
            totalCalories += PUSHUPS_CALORIES;
            completedExercises++;
            totalDuration += 6;
        }
        
        // Only save to database if at least one exercise was completed
        if (completedExercises > 0) {
            // Save to database with accurate calories and duration
            dbHelper.addWorkout("Chest Workout", totalDuration, totalCalories);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Save progress when leaving the activity
        saveWorkoutProgress();
    }
}
