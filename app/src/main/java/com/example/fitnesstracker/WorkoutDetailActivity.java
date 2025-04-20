package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDetailActivity extends AppCompatActivity {
    private ImageView workoutImage;
    private TextView workoutTitle;
    private TextView workoutDescription;
    private TextView workoutDuration;
    private TextView workoutDifficulty;
    private RecyclerView exerciseList;
    private FloatingActionButton fabStartWorkout;
    private FloatingActionButton fabSpotify;
    private WorkoutType currentWorkout;
    private ExerciseStepAdapter exerciseStepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        try {
            initializeViews();
            setupRecyclerView();
            
            // Load workout details
            String workoutId = getIntent().getStringExtra("workout_id");
            if (workoutId != null) {
                loadWorkoutDetails(workoutId);
            } else {
                // Default to core workout if no ID provided
                loadWorkoutDetails("core_workout");
            }

            // Set up FABs
            setupFABs();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading workout: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        workoutImage = findViewById(R.id.workout_image);
        workoutTitle = findViewById(R.id.workout_title);
        workoutDescription = findViewById(R.id.workout_description);
        workoutDuration = findViewById(R.id.workout_duration);
        workoutDifficulty = findViewById(R.id.workout_difficulty);
        exerciseList = findViewById(R.id.exercise_list);
        fabStartWorkout = findViewById(R.id.fab_start_workout);
        fabSpotify = findViewById(R.id.fab_spotify);

        // Set up toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupRecyclerView() {
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
        exerciseStepAdapter = new ExerciseStepAdapter(new ArrayList<>());
        exerciseList.setAdapter(exerciseStepAdapter);
    }

    private void setupFABs() {
        fabStartWorkout.setOnClickListener(v -> startWorkout());
        fabSpotify.setOnClickListener(v -> openSpotifyPlaylist());
    }

    private void loadWorkoutDetails(String workoutId) {
        WorkoutType workout;
        List<ExerciseStep> steps = new ArrayList<>();

        switch (workoutId) {
            case "strength_workout":
                workout = createStrengthWorkout();
                steps = createStrengthExercises();
                break;
            case "hiit_workout":
                workout = createHIITWorkout();
                steps = createHIITExercises();
                break;
            default:
                workout = createCoreWorkout();
                steps = createCoreExercises();
                break;
        }

        currentWorkout = workout;

        // Update UI
        updateWorkoutUI(workout, steps);
    }

    private void updateWorkoutUI(WorkoutType workout, List<ExerciseStep> steps) {
        workoutTitle.setText(workout.getName());
        workoutDescription.setText(workout.getDescription());
        workoutDuration.setText(getString(R.string.workout_duration_format, workout.getDuration()));
        workoutDifficulty.setText(getString(R.string.workout_difficulty_format, workout.getDifficulty()));

        // Load workout image
        int imageResource;
        switch (workout.getId()) {
            case "strength_workout":
                imageResource = R.drawable.man;
                break;
            case "hiit_workout":
                imageResource = R.drawable.workout_hiit;
                break;
            default:
                imageResource = R.drawable.workout_core;
                break;
        }

        Glide.with(this)
            .load(imageResource)
            .placeholder(R.drawable.placeholder_workout)
            .error(R.drawable.error_workout)
            .into(workoutImage);

        // Update exercise list
        exerciseStepAdapter = new ExerciseStepAdapter(steps);
        exerciseList.setAdapter(exerciseStepAdapter);
    }

    private WorkoutType createCoreWorkout() {
        return new WorkoutType(
            "core_workout",
            "Core Strength Workout",
            "A comprehensive core workout targeting all areas of your abs and supporting muscles",
            "drawable/workout_core",
            "Intermediate",
            30,
            "https://open.spotify.com/playlist/37i9dQZF1DX0HRj9P7NxeE"
        );
    }

    private List<ExerciseStep> createCoreExercises() {
        List<ExerciseStep> steps = new ArrayList<>();
        steps.add(new ExerciseStep("Plank", "Hold your body in a straight line, supported by forearms and toes. Keep core tight and don't let hips drop.", 1, 3, 60));
        steps.add(new ExerciseStep("Russian Twists", "Sit with knees bent, feet off ground. Hold hands together and twist side to side, touching ground beside hips.", 0, 3, 20));
        steps.add(new ExerciseStep("Leg Raises", "Lie on back, legs straight. Keep lower back pressed to floor while raising legs to 90 degrees.", 0, 3, 15));
        steps.add(new ExerciseStep("Bicycle Crunches", "Lie on back, hands behind head. Alternate bringing opposite elbow to opposite knee while extending other leg.", 0, 3, 20));
        steps.add(new ExerciseStep("Mountain Climbers", "Start in plank position. Alternate bringing knees to chest in a running motion. Keep back flat.", 1, 3, 30));
        steps.add(new ExerciseStep("Dead Bug", "Lie on back, arms extended up. Lower opposite arm and leg while keeping core engaged.", 0, 3, 12));
        steps.add(new ExerciseStep("Side Plank", "Support body on forearm, keep hips lifted and body in straight line. 30 seconds each side.", 1, 2, 30));
        steps.add(new ExerciseStep("Bird Dog", "Start on hands and knees. Extend opposite arm and leg while keeping back flat and core engaged.", 0, 3, 10));
        return steps;
    }

    private WorkoutType createStrengthWorkout() {
        return new WorkoutType(
            "strength_workout",
            "Full Body Strength",
            "Build muscle and strength with this comprehensive full-body workout",
            "drawable/man",
            "Advanced",
            45,
            "https://open.spotify.com/playlist/37i9dQZF1DX70RN3TfWWJh"
        );
    }

    private List<ExerciseStep> createStrengthExercises() {
        List<ExerciseStep> steps = new ArrayList<>();
        steps.add(new ExerciseStep("Barbell Squats", "Stand with feet shoulder-width apart, barbell on upper back. Lower into squat, keep chest up.", 0, 4, 12));
        steps.add(new ExerciseStep("Deadlifts", "Stand with feet hip-width apart, bend at hips to grip barbell. Keep back straight, lift by extending hips and knees.", 0, 4, 10));
        steps.add(new ExerciseStep("Bench Press", "Lie on bench, grip barbell slightly wider than shoulders. Lower to chest and press back up.", 0, 4, 12));
        steps.add(new ExerciseStep("Bent Over Rows", "Bend at hips, back straight, pull barbell to lower chest. Focus on squeezing shoulder blades.", 0, 4, 12));
        steps.add(new ExerciseStep("Overhead Press", "Stand with feet shoulder-width apart, press barbell overhead from shoulder level.", 0, 4, 10));
        return steps;
    }

    private WorkoutType createHIITWorkout() {
        return new WorkoutType(
            "hiit_workout",
            "High Intensity Interval Training",
            "Maximize calorie burn and improve conditioning with this intense HIIT workout",
            "drawable/workout_hiit",
            "Advanced",
            25,
            "https://open.spotify.com/playlist/37i9dQZF1DX5gQonLbZD9s"
        );
    }

    private List<ExerciseStep> createHIITExercises() {
        List<ExerciseStep> steps = new ArrayList<>();
        steps.add(new ExerciseStep("Burpees", "Drop to floor, perform push-up, jump feet forward, explode into jump with arms overhead.", 1, 4, 45));
        steps.add(new ExerciseStep("Mountain Climbers", "Start in plank, alternate driving knees to chest rapidly.", 1, 4, 45));
        steps.add(new ExerciseStep("Jump Squats", "Perform regular squat, explode upward into jump, land softly back into squat.", 1, 4, 45));
        steps.add(new ExerciseStep("High Knees", "Run in place, driving knees high to chest level.", 1, 4, 45));
        steps.add(new ExerciseStep("Plank to Downward Dog", "Alternate between plank and downward dog positions.", 1, 4, 45));
        return steps;
    }

    private void startWorkout() {
        try {
            if (currentWorkout == null) {
                Toast.makeText(this, "Error: No workout selected", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String workoutId = currentWorkout.getId();
            Toast.makeText(this, "Starting workout: " + workoutId, Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(this, WorkoutSessionActivity.class);
            intent.putExtra("workout_id", workoutId);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error starting workout: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace(); // Add stack trace to logs
        }
    }

    private void openSpotifyPlaylist() {
        if (currentWorkout != null && currentWorkout.getSpotifyPlaylistUrl() != null) {
            SpotifyUtils.openSpotifyPlaylist(this, currentWorkout.getSpotifyPlaylistUrl());
        } else {
            Toast.makeText(this, "No playlist available for this workout", Toast.LENGTH_SHORT).show();
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