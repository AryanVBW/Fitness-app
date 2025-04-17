package com.example.fitnesstracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.database.FitnessDbHelper;

public class PushupsDetailActivity extends AppCompatActivity {
    
    private static final String PREFS_NAME = "FitnessPrefs";
    private static final String PUSHUPS_WORKOUT_PROGRESS_KEY = "pushups_workout_progress";
    
    private FitnessDbHelper dbHelper;
    private CheckBox step1CheckBox, step2CheckBox, step3CheckBox, step4CheckBox, step5CheckBox;
    private Button completeWorkoutButton;
    
    // Calorie values for each exercise
    private static final double STANDARD_PUSHUPS_CALORIES = 25;
    private static final double WIDE_PUSHUPS_CALORIES = 20;
    private static final double DIAMOND_PUSHUPS_CALORIES = 15;
    private static final double DECLINE_PUSHUPS_CALORIES = 18;
    private static final double CLAP_PUSHUPS_CALORIES = 12;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushups_detail);
        
        // Initialize database helper
        dbHelper = new FitnessDbHelper(this);
        
        // Initialize views
        step1CheckBox = findViewById(R.id.pushupStep1CheckBox);
        step2CheckBox = findViewById(R.id.pushupStep2CheckBox);
        step3CheckBox = findViewById(R.id.pushupStep3CheckBox);
        step4CheckBox = findViewById(R.id.pushupStep4CheckBox);
        step5CheckBox = findViewById(R.id.pushupStep5CheckBox);
        completeWorkoutButton = findViewById(R.id.completePushupWorkoutButton);
        
        // Load saved progress
        loadWorkoutProgress();
        
        // Set up complete workout button
        completeWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkoutProgress();
                calculateAndSaveCalories();
                Toast.makeText(PushupsDetailActivity.this, "Pushups workout completed and saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void loadWorkoutProgress() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int progress = prefs.getInt(PUSHUPS_WORKOUT_PROGRESS_KEY, 0);
        
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
        prefs.edit().putInt(PUSHUPS_WORKOUT_PROGRESS_KEY, progress).apply();
    }
    
    private void calculateAndSaveCalories() {
        double totalCalories = 0;
        int completedExercises = 0;
        int totalDuration = 0;
        
        // Add calories for each completed exercise
        if (step1CheckBox.isChecked()) {
            totalCalories += STANDARD_PUSHUPS_CALORIES;
            completedExercises++;
            totalDuration += 8; // Approx time for 4 sets
        }
        
        if (step2CheckBox.isChecked()) {
            totalCalories += WIDE_PUSHUPS_CALORIES;
            completedExercises++;
            totalDuration += 6; // 3 sets
        }
        
        if (step3CheckBox.isChecked()) {
            totalCalories += DIAMOND_PUSHUPS_CALORIES;
            completedExercises++;
            totalDuration += 6; // 3 sets
        }
        
        if (step4CheckBox.isChecked()) {
            totalCalories += DECLINE_PUSHUPS_CALORIES;
            completedExercises++;
            totalDuration += 6; // 3 sets
        }
        
        if (step5CheckBox.isChecked()) {
            totalCalories += CLAP_PUSHUPS_CALORIES;
            completedExercises++;
            totalDuration += 4; // 2 sets
        }
        
        // Only save to database if at least one exercise was completed
        if (completedExercises > 0) {
            // Save to database with accurate calories and duration
            dbHelper.addWorkout("Pushups Workout", totalDuration, totalCalories);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Save progress when leaving the activity
        saveWorkoutProgress();
    }
}
