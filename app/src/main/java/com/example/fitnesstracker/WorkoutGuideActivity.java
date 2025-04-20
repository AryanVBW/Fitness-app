package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class WorkoutGuideActivity extends AppCompatActivity {
    private CircularProgressIndicator loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_guide);

        // Set up toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize views
        loadingIndicator = findViewById(R.id.loading_indicator);
        MaterialButton startCardioButton = findViewById(R.id.start_cardio_button);
        MaterialButton startStrengthButton = findViewById(R.id.start_strength_button);
        MaterialButton startPushupButton = findViewById(R.id.start_pushup_button);
        
        ImageView cardioImage = findViewById(R.id.cardio_image);
        ImageView strengthImage = findViewById(R.id.strength_image);
        ImageView pushupImage = findViewById(R.id.pushup_image);

        // Set error drawables for images
        cardioImage.setImageResource(R.drawable.cardio);
        strengthImage.setImageResource(R.drawable.man_dumbal);
        pushupImage.setImageResource(R.drawable.pushup);

        // Set up click listeners with loading states
        startCardioButton.setOnClickListener(v -> startWorkoutWithLoading("cardio"));
        startStrengthButton.setOnClickListener(v -> startWorkoutWithLoading("strength"));
        startPushupButton.setOnClickListener(v -> startWorkoutWithLoading("pushup"));
    }

    private void startWorkoutWithLoading(String workoutType) {
        try {
            loadingIndicator.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Preparing " + workoutType + " workout...", Toast.LENGTH_SHORT).show();
            
            // Simulate loading workout data (remove in production)
            new android.os.Handler().postDelayed(() -> {
                loadingIndicator.setVisibility(View.GONE);
                startWorkout(workoutType);
            }, 500);
        } catch (Exception e) {
            loadingIndicator.setVisibility(View.GONE);
            Toast.makeText(this, "Error loading workout: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void startWorkout(String workoutType) {
        try {
            String workoutId = workoutType + "_workout";
            Toast.makeText(this, "Starting " + workoutType + " workout...", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(this, WorkoutSessionActivity.class);
            intent.putExtra("workout_id", workoutId);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error starting workout: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 