package com.example.fitnesstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fitnesstracker.database.FitnessDbHelper;
import com.example.fitnesstracker.database.FitnessDbHelper.WorkoutEntry;
import com.example.fitnesstracker.database.FitnessDbHelper.DietPlanEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FitnessPrefs";
    private static final String STEPS_COUNT_KEY = "steps_count";
    private static final double CALORIES_PER_STEP = 0.04;
    private static final int TARGET_CALORIES = 1000;
    
    private FitnessDbHelper dbHelper;
    private ProgressBar circularProgress;
    private TextView caloriesValue;
    private TextView dateText;
    private TextView workoutCalories;
    private TextView dailyAverage;
    private TextView stepsCalories;
    private LinearLayout workoutsListContainer;
    private TextView noWorkoutsMessage;
    private LinearLayout dietPlansListContainer;
    private TextView noDietPlansMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        
        // Initialize database helper
        dbHelper = new FitnessDbHelper(this);

        // Initialize views
        findViewById(R.id.caloriesCard).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        findViewById(R.id.workoutsCard).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        findViewById(R.id.dietCard).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        
        // Find all UI elements
        circularProgress = findViewById(R.id.circularProgress);
        caloriesValue = findViewById(R.id.caloriesValue);
        dateText = findViewById(R.id.dateText);
        workoutCalories = findViewById(R.id.workoutCalories);
        dailyAverage = findViewById(R.id.dailyAverage);
        stepsCalories = findViewById(R.id.stepsCalories);
        workoutsListContainer = findViewById(R.id.workoutsListContainer);
        noWorkoutsMessage = findViewById(R.id.noWorkoutsMessage);
        dietPlansListContainer = findViewById(R.id.dietPlansListContainer);
        noDietPlansMessage = findViewById(R.id.noDietPlansMessage);

        // Set up report data
        setupReportData();

        // Set up bottom navigation
        setupBottomNavigation();
        
        // Add sample data if database is empty (for testing)
        if (isFirstRun()) {
            addSampleData();
            setupReportData(); // Refresh display with sample data
        }
    }

    private void setupReportData() {
        // Set current date
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        dateText.setText(formattedDate);
        
        // Get workout calories from database
        double workoutCals = dbHelper.getTotalWorkoutCalories();
        workoutCalories.setText(String.format(Locale.getDefault(), "%.0f", workoutCals));
        
        // Get average daily workout calories
        double avgCals = dbHelper.getAverageDailyWorkoutCalories();
        dailyAverage.setText(String.format(Locale.getDefault(), "%.0f", avgCals));
        
        // Get steps calories from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int steps = prefs.getInt(STEPS_COUNT_KEY, 0);
        double stepsCals = steps * CALORIES_PER_STEP;
        stepsCalories.setText(String.format(Locale.getDefault(), "%.2f", stepsCals));
        
        // Calculate total calories (workout + steps)
        double totalCalories = workoutCals + stepsCals;
        caloriesValue.setText(String.format(Locale.getDefault(), "%.0f", totalCalories));
        
        // Set progress based on total calories (out of TARGET_CALORIES)
        int progressPercentage = (int) Math.min(100, (totalCalories / TARGET_CALORIES) * 100);
        
        // Animate the progress bar
        android.animation.ObjectAnimator progressAnimator = 
            android.animation.ObjectAnimator.ofInt(circularProgress, "progress", 0, progressPercentage);
        progressAnimator.setDuration(1200);
        progressAnimator.start();
        
        // Display completed workouts
        displayCompletedWorkouts();
        
        // Display diet plans
        displayDietPlans();
    }
    
    private void displayCompletedWorkouts() {
        // Clear previous entries
        workoutsListContainer.removeAllViews();
        
        // Get completed workouts from database
        List<WorkoutEntry> workouts = dbHelper.getRecentWorkouts(10);
        
        if (workouts.isEmpty()) {
            noWorkoutsMessage.setVisibility(View.VISIBLE);
            return;
        }
        
        noWorkoutsMessage.setVisibility(View.GONE);
        
        // Create a view for each workout
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        
        for (WorkoutEntry workout : workouts) {
            // Create a card for each workout
            CardView workoutCard = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 16);
            workoutCard.setLayoutParams(cardParams);
            workoutCard.setCardBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            workoutCard.setRadius(16);
            workoutCard.setCardElevation(4);
            
            // Create layout for workout details
            LinearLayout workoutLayout = new LinearLayout(this);
            workoutLayout.setOrientation(LinearLayout.HORIZONTAL);
            workoutLayout.setPadding(24, 16, 24, 16);
            
            // Create left section with workout type and date
            LinearLayout leftSection = new LinearLayout(this);
            leftSection.setOrientation(LinearLayout.VERTICAL);
            leftSection.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            ));
            
            // Workout type
            TextView workoutType = new TextView(this);
            workoutType.setText(workout.type);
            workoutType.setTextColor(getResources().getColor(android.R.color.white));
            workoutType.setTextSize(16);
            workoutType.setTypeface(null, android.graphics.Typeface.BOLD);
            
            // Workout date
            TextView workoutDate = new TextView(this);
            workoutDate.setText(dateFormat.format(new Date(workout.date)));
            workoutDate.setTextColor(getResources().getColor(android.R.color.white));
            workoutDate.setTextSize(14);
            
            leftSection.addView(workoutType);
            leftSection.addView(workoutDate);
            
            // Create right section with duration and calories
            LinearLayout rightSection = new LinearLayout(this);
            rightSection.setOrientation(LinearLayout.VERTICAL);
            rightSection.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            ));
            rightSection.setGravity(android.view.Gravity.END);
            
            // Duration
            TextView durationText = new TextView(this);
            durationText.setText(workout.duration + " min");
            durationText.setTextColor(getResources().getColor(android.R.color.white));
            durationText.setTextSize(14);
            
            // Calories
            TextView caloriesText = new TextView(this);
            caloriesText.setText(String.format(Locale.getDefault(), "%.0f cal", workout.calories));
            caloriesText.setTextColor(getResources().getColor(android.R.color.white));
            caloriesText.setTextSize(16);
            caloriesText.setTypeface(null, android.graphics.Typeface.BOLD);
            
            rightSection.addView(durationText);
            rightSection.addView(caloriesText);
            
            // Add sections to workout layout
            workoutLayout.addView(leftSection);
            workoutLayout.addView(rightSection);
            
            // Add layout to card
            workoutCard.addView(workoutLayout);
            
            // Add card to container
            workoutsListContainer.addView(workoutCard);
        }
    }
    
    private void displayDietPlans() {
        // Clear previous entries
        dietPlansListContainer.removeAllViews();
        
        // Get diet plans from database
        List<DietPlanEntry> dietPlans = dbHelper.getRecentDietPlans(5);
        
        if (dietPlans.isEmpty()) {
            noDietPlansMessage.setVisibility(View.VISIBLE);
            return;
        }
        
        noDietPlansMessage.setVisibility(View.GONE);
        
        // Create a view for each diet plan
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        
        for (DietPlanEntry dietPlan : dietPlans) {
            // Create a card for each diet plan
            CardView dietCard = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 16);
            dietCard.setLayoutParams(cardParams);
            dietCard.setCardBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            dietCard.setRadius(16);
            dietCard.setCardElevation(4);
            
            // Create layout for diet plan details
            LinearLayout dietLayout = new LinearLayout(this);
            dietLayout.setOrientation(LinearLayout.HORIZONTAL);
            dietLayout.setPadding(24, 16, 24, 16);
            
            // Create left section with diet type and date
            LinearLayout leftSection = new LinearLayout(this);
            leftSection.setOrientation(LinearLayout.VERTICAL);
            leftSection.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            ));
            
            // Diet type
            TextView dietType = new TextView(this);
            dietType.setText(dietPlan.type);
            dietType.setTextColor(getResources().getColor(android.R.color.white));
            dietType.setTextSize(16);
            dietType.setTypeface(null, android.graphics.Typeface.BOLD);
            
            // Diet date
            TextView dietDate = new TextView(this);
            dietDate.setText(dateFormat.format(new Date(dietPlan.date)));
            dietDate.setTextColor(getResources().getColor(android.R.color.white));
            dietDate.setTextSize(14);
            
            leftSection.addView(dietType);
            leftSection.addView(dietDate);
            
            // Create right section with calories
            LinearLayout rightSection = new LinearLayout(this);
            rightSection.setOrientation(LinearLayout.VERTICAL);
            rightSection.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            ));
            rightSection.setGravity(android.view.Gravity.END);
            
            // Calories
            TextView caloriesText = new TextView(this);
            caloriesText.setText(String.format(Locale.getDefault(), "%.0f cal", dietPlan.calories));
            caloriesText.setTextColor(getResources().getColor(android.R.color.white));
            caloriesText.setTextSize(16);
            caloriesText.setTypeface(null, android.graphics.Typeface.BOLD);
            
            rightSection.addView(caloriesText);
            
            // Add sections to diet layout
            dietLayout.addView(leftSection);
            dietLayout.addView(rightSection);
            
            // Add layout to card
            dietCard.addView(dietLayout);
            
            // Add card to container
            dietPlansListContainer.addView(dietCard);
        }
    }
    
    private boolean isFirstRun() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("first_run_report", true);
        if (isFirstRun) {
            prefs.edit().putBoolean("first_run_report", false).apply();
        }
        return isFirstRun;
    }
    
    private void addSampleData() {
        // Add sample workouts
        dbHelper.addWorkout("Chest Workout", 30, 120);
        dbHelper.addWorkout("Pushups", 15, 45);
        dbHelper.addWorkout("Cardio", 20, 180);
        
        // Add sample diet plans
        dbHelper.addDietPlan("Keto Diet", 1800);
        dbHelper.addDietPlan("Protein Diet", 2000);
        
        // Add sample weight
        dbHelper.addWeightEntry(70.5);
        
        // Add sample steps
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(STEPS_COUNT_KEY, 3500).apply();
        
        Toast.makeText(this, "Sample data added for demonstration", Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNavigation() {
        LinearLayout dietNavButton = findViewById(R.id.dietNavButton);
        LinearLayout workoutNavButton = findViewById(R.id.workoutNavButton);
        LinearLayout reportNavButton = findViewById(R.id.reportNavButton);

        // Report button is already active
        reportNavButton.setBackgroundResource(android.R.color.darker_gray);

        dietNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, DietActivity.class);
                startActivity(intent);
                finish();
            }
        });

        workoutNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
