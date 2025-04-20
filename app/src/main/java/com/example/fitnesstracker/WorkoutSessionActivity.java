package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;
import java.util.ArrayList;

public class WorkoutSessionActivity extends AppCompatActivity {
    private TextView countdownText;
    private TextView exerciseNameText;
    private TextView exerciseDescriptionText;
    private TextView exerciseTimerText;
    private LinearProgressIndicator progressBar;
    private MaterialButton pauseButton;
    private MaterialButton addTimeButton;
    private MaterialButton stopButton;
    
    private List<ExerciseStep> exerciseSteps;
    private int currentExerciseIndex = 0;
    private CountDownTimer countdownTimer;
    private CountDownTimer exerciseTimer;
    private boolean isPaused = false;
    private long timeLeftInMillis = 0;
    private static final long EXERCISE_DURATION = 15000; // 15 seconds
    private static final long COUNTDOWN_DURATION = 3000; // 3 seconds
    private static final long ADD_TIME_DURATION = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);

        try {
            // Set up toolbar
            MaterialToolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle("Workout Session");
            }

            // Initialize views
            countdownText = findViewById(R.id.countdownText);
            exerciseNameText = findViewById(R.id.exerciseNameText);
            exerciseDescriptionText = findViewById(R.id.exerciseDescriptionText);
            exerciseTimerText = findViewById(R.id.exerciseTimerText);
            progressBar = findViewById(R.id.progressBar);
            pauseButton = findViewById(R.id.pauseButton);
            addTimeButton = findViewById(R.id.addTimeButton);
            stopButton = findViewById(R.id.stopButton);

            // Get workout steps from intent
            String workoutId = getIntent().getStringExtra("workout_id");
            Toast.makeText(this, "Initializing workout: " + workoutId, Toast.LENGTH_SHORT).show();

            if (workoutId != null) {
                switch (workoutId) {
                    case "cardio_workout":
                        exerciseSteps = createCardioExercises();
                        break;
                    case "strength_workout":
                        exerciseSteps = createStrengthExercises();
                        break;
                    case "hiit_workout":
                        exerciseSteps = createHIITExercises();
                        break;
                    default:
                        exerciseSteps = createCoreExercises();
                }
            } else {
                Toast.makeText(this, "Error: No workout ID provided", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            if (exerciseSteps == null || exerciseSteps.isEmpty()) {
                Toast.makeText(this, "Error: No exercises found for this workout", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Set up button listeners
            pauseButton.setOnClickListener(v -> togglePause());
            addTimeButton.setOnClickListener(v -> addExtraTime());
            stopButton.setOnClickListener(v -> stopWorkout());

            // Start the workout
            startExerciseCountdown();
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing workout: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
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

    private void startExerciseCountdown() {
        countdownText.setVisibility(View.VISIBLE);
        exerciseNameText.setVisibility(View.GONE);
        exerciseDescriptionText.setVisibility(View.GONE);
        exerciseTimerText.setVisibility(View.GONE);

        countdownTimer = new CountDownTimer(COUNTDOWN_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000) + 1;
                countdownText.setText(String.valueOf(secondsLeft));
            }

            @Override
            public void onFinish() {
                countdownText.setVisibility(View.GONE);
                startExercise();
            }
        }.start();
    }

    private void startExercise() {
        if (currentExerciseIndex >= exerciseSteps.size()) {
            finishWorkout();
            return;
        }

        ExerciseStep currentExercise = exerciseSteps.get(currentExerciseIndex);
        exerciseNameText.setText(currentExercise.getName());
        exerciseDescriptionText.setText(currentExercise.getDescription());
        
        View exerciseInfoLayout = findViewById(R.id.exerciseInfoLayout);
        exerciseInfoLayout.setVisibility(View.VISIBLE);
        exerciseNameText.setVisibility(View.VISIBLE);
        exerciseDescriptionText.setVisibility(View.VISIBLE);
        exerciseTimerText.setVisibility(View.VISIBLE);

        // Calculate total duration for progress bar
        int totalDuration = exerciseSteps.size() * 15; // 15 seconds per exercise
        progressBar.setMax(totalDuration);

        // Start exercise timer
        timeLeftInMillis = EXERCISE_DURATION;
        startExerciseTimer();
    }

    private void startExerciseTimer() {
        if (exerciseTimer != null) {
            exerciseTimer.cancel();
        }

        exerciseTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) {
                    timeLeftInMillis = millisUntilFinished;
                    int secondsLeft = (int) (millisUntilFinished / 1000);
                    exerciseTimerText.setText(String.valueOf(secondsLeft));
                    
                    // Update progress bar
                    int totalProgress = currentExerciseIndex * 15 + (15 - secondsLeft);
                    progressBar.setProgress(totalProgress);
                }
            }

            @Override
            public void onFinish() {
                if (!isPaused) {
                    currentExerciseIndex++;
                    startExerciseCountdown();
                }
            }
        }.start();
    }

    private void addExtraTime() {
        if (!isPaused) {
            timeLeftInMillis += ADD_TIME_DURATION;
            if (exerciseTimer != null) {
                exerciseTimer.cancel();
            }
            startExerciseTimer();
            Toast.makeText(this, "+5 seconds added", Toast.LENGTH_SHORT).show();
        }
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseButton.setText(isPaused ? "Resume" : "Pause");
        
        if (isPaused) {
            if (exerciseTimer != null) {
                exerciseTimer.cancel();
            }
        } else {
            startExerciseTimer();
        }
    }

    private void stopWorkout() {
        if (exerciseTimer != null) {
            exerciseTimer.cancel();
        }
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
        finish();
    }

    private void finishWorkout() {
        Toast.makeText(this, "Workout completed! Great job!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exerciseTimer != null) {
            exerciseTimer.cancel();
        }
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
    }

    private List<ExerciseStep> createCardioExercises() {
        List<ExerciseStep> steps = new ArrayList<>();
        steps.add(new ExerciseStep("Jumping Jacks", "Start with feet together and arms at sides. Jump while spreading legs and raising arms overhead.", 1, 3, 15));
        steps.add(new ExerciseStep("High Knees", "Run in place while lifting knees as high as possible. Keep core engaged.", 1, 3, 15));
        steps.add(new ExerciseStep("Mountain Climbers", "Start in plank position. Alternate bringing knees to chest in a running motion.", 1, 3, 15));
        steps.add(new ExerciseStep("Burpees", "Squat down, kick feet back, do a push-up, jump feet forward, and explode upward.", 1, 3, 15));
        steps.add(new ExerciseStep("Jump Rope", "Jump continuously while swinging rope overhead and under feet.", 1, 3, 15));
        return steps;
    }

    private List<ExerciseStep> createStrengthExercises() {
        List<ExerciseStep> steps = new ArrayList<>();
        steps.add(new ExerciseStep("Push-ups", "Start in plank position. Lower body until chest nearly touches floor, then push back up.", 0, 3, 15));
        steps.add(new ExerciseStep("Squats", "Stand with feet shoulder-width apart. Lower body as if sitting in a chair, then stand back up.", 0, 3, 15));
        steps.add(new ExerciseStep("Lunges", "Step forward with one leg, lower body until both knees are bent at 90 degrees.", 0, 3, 15));
        steps.add(new ExerciseStep("Plank", "Hold body in a straight line, supported by forearms and toes.", 1, 3, 15));
        steps.add(new ExerciseStep("Superman", "Lie face down, lift arms and legs off ground, hold for specified time.", 1, 3, 15));
        return steps;
    }

    private List<ExerciseStep> createHIITExercises() {
        List<ExerciseStep> steps = new ArrayList<>();
        steps.add(new ExerciseStep("Sprint in Place", "Run in place as fast as possible, lifting knees high.", 1, 4, 15));
        steps.add(new ExerciseStep("Rest", "Take a short break to recover", 0, 4, 15));
        steps.add(new ExerciseStep("Jump Squats", "Perform regular squat, explode upward into jump, land softly.", 1, 4, 15));
        steps.add(new ExerciseStep("Rest", "Take a short break to recover", 0, 4, 15));
        steps.add(new ExerciseStep("Mountain Climbers", "Start in plank, alternate driving knees to chest rapidly.", 1, 4, 15));
        steps.add(new ExerciseStep("Rest", "Take a short break to recover", 0, 4, 15));
        return steps;
    }

    private List<ExerciseStep> createCoreExercises() {
        List<ExerciseStep> steps = new ArrayList<>();
        steps.add(new ExerciseStep("Crunches", "Lie on back, hands behind head, lift shoulders off ground.", 0, 3, 15));
        steps.add(new ExerciseStep("Russian Twists", "Sit with knees bent, feet off ground, rotate torso side to side.", 0, 3, 15));
        steps.add(new ExerciseStep("Leg Raises", "Lie on back, keep legs straight, lift them up to 90 degrees.", 0, 3, 15));
        steps.add(new ExerciseStep("Plank", "Hold body in straight line, supported by forearms and toes.", 1, 3, 15));
        steps.add(new ExerciseStep("Side Plank", "Hold body sideways, supported by one forearm.", 1, 3, 15));
        return steps;
    }
} 