package com.example.fitnesstracker;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class DietPlanActivity extends AppCompatActivity {
    private RecyclerView mealList;
    private MealAdapter adapter;
    private List<Meal> meals;
    private boolean isVegetarian = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Diet Plan");
        }

        // Initialize views
        ChipGroup dietTypeGroup = findViewById(R.id.dietTypeGroup);
        mealList = findViewById(R.id.meal_list);
        FloatingActionButton saveDietButton = findViewById(R.id.saveDietButton);

        // Setup RecyclerView
        mealList.setLayoutManager(new LinearLayoutManager(this));
        meals = new ArrayList<>();
        adapter = new MealAdapter(meals);
        mealList.setAdapter(adapter);

        // Setup diet type selection
        dietTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip selectedChip = findViewById(checkedId);
            if (selectedChip != null) {
                isVegetarian = selectedChip.getId() == R.id.vegetarianChip;
                loadMeals();
            }
        });

        // Load initial meals
        loadMeals();

        // Setup click listeners
        saveDietButton.setOnClickListener(v -> saveDietPlan());
    }

    private void loadMeals() {
        meals.clear();
        
        if (isVegetarian) {
            // Vegetarian meals
            meals.add(new Meal("Breakfast", "Oatmeal with fruits", 350, "breakfast_image", "8:00 AM"));
            meals.add(new Meal("Lunch", "Quinoa salad with vegetables", 450, "lunch_image", "1:00 PM"));
            meals.add(new Meal("Snack", "Greek yogurt with nuts", 200, "snack_image", "4:00 PM"));
            meals.add(new Meal("Dinner", "Vegetable stir-fry with tofu", 400, "dinner_image", "7:00 PM"));
        } else {
            // Non-vegetarian meals
            meals.add(new Meal("Breakfast", "Scrambled eggs with whole wheat toast", 400, "breakfast_image", "8:00 AM"));
            meals.add(new Meal("Lunch", "Grilled chicken with brown rice", 500, "lunch_image", "1:00 PM"));
            meals.add(new Meal("Snack", "Protein shake with banana", 250, "snack_image", "4:00 PM"));
            meals.add(new Meal("Dinner", "Salmon with roasted vegetables", 450, "dinner_image", "7:00 PM"));
        }

        adapter.notifyDataSetChanged();
    }

    private void saveDietPlan() {
    
        Toast.makeText(this, "Diet plan saved successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 