package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.ScrollView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fitnesstracker.database.FitnessDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DietActivity extends AppCompatActivity {

    private CalendarView dietCalendar;
    private TextView selectedDateText;
    private FitnessDbHelper dbHelper;
    private long selectedDateMillis;
    
    // Diet plan calorie values
    private static final double KETO_DIET_CALORIES = 1800;
    private static final double PROTEIN_DIET_CALORIES = 2000;
    private static final double VEGAN_DIET_CALORIES = 1600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        
        // Initialize database helper
        dbHelper = new FitnessDbHelper(this);

        // Animate main views
        findViewById(R.id.tvDietTitle).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        findViewById(R.id.dietCalendar).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        findViewById(R.id.dietPlansTitle).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        findViewById(R.id.dietPlansScroll).startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom));
        // Animate each card
        android.view.ViewGroup cardContainer = findViewById(R.id.dietPlansScroll);
        if (cardContainer instanceof android.widget.HorizontalScrollView) {
            android.view.ViewGroup linear = (android.view.ViewGroup) ((android.widget.HorizontalScrollView) cardContainer).getChildAt(0);
            for (int i = 0; i < linear.getChildCount(); i++) {
                android.view.View card = linear.getChildAt(i);
                card.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_in));
            }
        }
        // Initialize calendar
        dietCalendar = findViewById(R.id.dietCalendar);
        selectedDateText = findViewById(R.id.selectedDateText);
        setupCalendar();

        // Set up bottom navigation
        setupBottomNavigation();
    }

    private void setupCalendar() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        selectedDateMillis = calendar.getTimeInMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        selectedDateText.setText(formattedDate);
        
        // Setup diet plan cards
        setupDietPlanCards();
        
        dietCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                selectedDateMillis = calendar.getTimeInMillis();
                String selectedDate = dateFormat.format(calendar.getTime());
                selectedDateText.setText(selectedDate);
                
                // Check if there are any diet plans for this date
                checkDietPlansForDate(selectedDateMillis);
            }
        });
    }

    private void setupDietPlanCards() {
        // Find all diet plan cards in the horizontal scroll view
        android.widget.HorizontalScrollView scrollView = findViewById(R.id.dietPlansScroll);
        LinearLayout container = (LinearLayout) scrollView.getChildAt(0);

        // Set click listeners for each diet plan card
        for (int i = 0; i < container.getChildCount(); i++) {
            View card = container.getChildAt(i);
            if (card instanceof CardView) {
                final int cardIndex = i;
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDietTypeDialog(cardIndex);
                    }
                });
            }
        }
    }

    private void showDietTypeDialog(final int cardIndex) {
        showFullDietPlanDialog(cardIndex, true); // Default to veg, user can switch in dialog
    }

    private void showFullDietPlanDialog(int cardIndex, boolean isVeg) {
        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog_diet_plan, null);

        final android.widget.TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        final android.widget.ImageView dialogClose = dialogView.findViewById(R.id.dialogClose);
        final android.widget.RadioGroup vegNonVegGroup = dialogView.findViewById(R.id.vegNonVegGroup);
        final android.widget.RadioButton vegOption = dialogView.findViewById(R.id.vegOption);
        final android.widget.RadioButton nonVegOption = dialogView.findViewById(R.id.nonVegOption);
        final android.widget.TextView menuTitle = dialogView.findViewById(R.id.menuTitle);
        final android.widget.TextView menuContent = dialogView.findViewById(R.id.menuContent);
        final com.google.android.material.button.MaterialButton okButton = dialogView.findViewById(R.id.okButton);

        // Set initial selection
        vegOption.setChecked(isVeg);
        nonVegOption.setChecked(!isVeg);

        // Helper to update menu
        java.util.function.BiConsumer<Integer, Boolean> updateMenu = (index, veg) -> {
            String title;
            StringBuilder plan = new StringBuilder();
            switch (index) {
                case 0: // Keto Diet
                    title = veg ? "Veg Keto Diet Plan" : "Non-Veg Keto Diet Plan";
                    if (veg) {
                        plan.append("Breakfast: Paneer bhurji, avocado toast\n");
                        plan.append("Lunch: Cauliflower rice, tofu curry\n");
                        plan.append("Snacks: Nuts, cheese cubes\n");
                        plan.append("Dinner: Grilled mushrooms, salad\n");
                    } else {
                        plan.append("Breakfast: Eggs & avocado\n");
                        plan.append("Lunch: Grilled chicken, spinach salad\n");
                        plan.append("Snacks: Boiled eggs, cheese\n");
                        plan.append("Dinner: Fish curry, sautéed veggies\n");
                    }
                    break;
                case 1: // Protein Diet
                    title = veg ? "Veg Protein Diet Plan" : "Non-Veg Protein Diet Plan";
                    if (veg) {
                        plan.append("Breakfast: Moong dal chilla, milk\n");
                        plan.append("Lunch: Rajma, brown rice, salad\n");
                        plan.append("Snacks: Greek yogurt, sprouts\n");
                        plan.append("Dinner: Paneer tikka, mixed veg\n");
                    } else {
                        plan.append("Breakfast: Scrambled eggs, toast\n");
                        plan.append("Lunch: Grilled fish, quinoa, salad\n");
                        plan.append("Snacks: Chicken breast, nuts\n");
                        plan.append("Dinner: Chicken curry, veggies\n");
                    }
                    break;
                case 2: // Vegan Diet
                    title = "Vegan Diet Plan";
                    plan.append("Breakfast: Oats porridge, almond milk\n");
                    plan.append("Lunch: Chickpea curry, brown rice, salad\n");
                    plan.append("Snacks: Fruits, trail mix\n");
                    plan.append("Dinner: Lentil soup, roasted veggies\n");
                    break;
                default:
                    title = "Custom Diet Plan";
                    plan.append("Breakfast: Oats, milk\n");
                    plan.append("Lunch: Dal, rice, veggies\n");
                    plan.append("Snacks: Fruits\n");
                    plan.append("Dinner: Paneer curry, roti\n");
                    break;
            }
            dialogTitle.setText(title);
            menuContent.setText(plan.toString());
        };

        // Initial menu
        updateMenu.accept(cardIndex, isVeg);

        // Listen for veg/non-veg switch
        vegNonVegGroup.setOnCheckedChangeListener((group, checkedId) -> {
            boolean veg = checkedId == R.id.vegOption;
            updateMenu.accept(cardIndex, veg);
        });

        final androidx.appcompat.app.AlertDialog dialog = new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .create();

        dialogClose.setOnClickListener(v -> dialog.dismiss());
        okButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void saveDietPlan(int cardIndex) {
        String dietType;
        double calories;

        // Determine diet type and calories based on card index
        switch (cardIndex) {
            case 0:
                dietType = "Keto Diet";
                calories = KETO_DIET_CALORIES;
                break;
            case 1:
                dietType = "Protein Diet";
                calories = PROTEIN_DIET_CALORIES;
                break;
            case 2:
                dietType = "Vegan Diet";
                calories = VEGAN_DIET_CALORIES;
                break;
            default:
                dietType = "Custom Diet";
                calories = 1500;
                break;
        }
        
        // Save to database
        long result = dbHelper.addDietPlan(dietType, calories);
        
        if (result > 0) {
            Toast.makeText(this, dietType + " selected for " + selectedDateText.getText(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save diet plan", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void checkDietPlansForDate(long date) {
        List<FitnessDbHelper.DietPlanEntry> plans = dbHelper.getDietPlansForDate(date);
        
        if (!plans.isEmpty()) {
            StringBuilder message = new StringBuilder("Diet plans for this date:\n");
            for (FitnessDbHelper.DietPlanEntry plan : plans) {
                message.append("- ").append(plan.type)
                       .append(" (").append(String.format(Locale.getDefault(), "%.0f", plan.calories))
                       .append(" calories)\n");
            }
            Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void setupBottomNavigation() {
        LinearLayout dietNavButton = findViewById(R.id.dietNavButton);
        LinearLayout workoutNavButton = findViewById(R.id.workoutNavButton);
        LinearLayout reportNavButton = findViewById(R.id.reportNavButton);

        // Diet button is already active
        dietNavButton.setBackgroundResource(android.R.color.darker_gray);

        workoutNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DietActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reportNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DietActivity.this, ReportActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
