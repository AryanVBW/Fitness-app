package com.example.fitnesstracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class ChestWorkoutActivity extends AppCompatActivity {

    private int benchPressCompletedSets = 0;
    private int inclinePressCompletedSets = 0;
    private int chestFlyCompletedSets = 0;
    private int pushupsCompletedSets = 0;
    private int cableCrossoverCompletedSets = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chest_workout);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Set up exercise tracking buttons
        setupTrackingButton(R.id.trackBenchPressButton, "Bench Press", 4);
        setupTrackingButton(R.id.trackInclinePressButton, "Incline Press", 3);
        setupTrackingButton(R.id.trackChestFlyButton, "Chest Fly", 3);
        setupTrackingButton(R.id.trackPushupsButton, "Push-ups", 3);
        setupTrackingButton(R.id.trackCableCrossoverButton, "Cable Crossover", 3);

        // Set up complete workout button
        MaterialButton completeWorkoutButton = findViewById(R.id.completeWorkoutButton);
        completeWorkoutButton.setOnClickListener(v -> {
            int totalSets = benchPressCompletedSets + inclinePressCompletedSets + 
                            chestFlyCompletedSets + pushupsCompletedSets + cableCrossoverCompletedSets;
            int totalRequiredSets = 4 + 3 + 3 + 3 + 3; // 16 sets total
            
            if (totalSets >= totalRequiredSets) {
                Toast.makeText(this, "Congratulations! Workout completed!", Toast.LENGTH_LONG).show();
                // You could add code here to save the workout to a history database
                finish();
            } else {
                Toast.makeText(this, "Please complete all sets before finishing the workout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTrackingButton(int buttonId, String exerciseName, int totalSets) {
        MaterialButton button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            if (buttonId == R.id.trackBenchPressButton) {
                if (benchPressCompletedSets < totalSets) {
                    benchPressCompletedSets++;
                    updateButtonText(button, benchPressCompletedSets, totalSets);
                }
            } else if (buttonId == R.id.trackInclinePressButton) {
                if (inclinePressCompletedSets < totalSets) {
                    inclinePressCompletedSets++;
                    updateButtonText(button, inclinePressCompletedSets, totalSets);
                }
            } else if (buttonId == R.id.trackChestFlyButton) {
                if (chestFlyCompletedSets < totalSets) {
                    chestFlyCompletedSets++;
                    updateButtonText(button, chestFlyCompletedSets, totalSets);
                }
            } else if (buttonId == R.id.trackPushupsButton) {
                if (pushupsCompletedSets < totalSets) {
                    pushupsCompletedSets++;
                    updateButtonText(button, pushupsCompletedSets, totalSets);
                }
            } else if (buttonId == R.id.trackCableCrossoverButton) {
                if (cableCrossoverCompletedSets < totalSets) {
                    cableCrossoverCompletedSets++;
                    updateButtonText(button, cableCrossoverCompletedSets, totalSets);
                }
            }
            
            Toast.makeText(this, exerciseName + " set " + 
                    getCompletedSetsForExercise(buttonId) + "/" + totalSets + " completed!", 
                    Toast.LENGTH_SHORT).show();
        });
    }

    private int getCompletedSetsForExercise(int buttonId) {
        if (buttonId == R.id.trackBenchPressButton) {
            return benchPressCompletedSets;
        } else if (buttonId == R.id.trackInclinePressButton) {
            return inclinePressCompletedSets;
        } else if (buttonId == R.id.trackChestFlyButton) {
            return chestFlyCompletedSets;
        } else if (buttonId == R.id.trackPushupsButton) {
            return pushupsCompletedSets;
        } else if (buttonId == R.id.trackCableCrossoverButton) {
            return cableCrossoverCompletedSets;
        } else {
            return 0;
        }
    }

    private void updateButtonText(MaterialButton button, int completedSets, int totalSets) {
        if (completedSets == totalSets) {
            button.setText("COMPLETED");
            button.setBackgroundTintList(getColorStateList(R.color.green_500));
        } else {
            button.setText("TRACK SET (" + completedSets + "/" + totalSets + ")");
        }
    }
}
