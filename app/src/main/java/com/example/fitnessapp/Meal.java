package com.example.fitnessapp;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private String name;
    private String description;
    private int calories;
    private String imageUrl;
    private String time;
    private boolean isCompleted;
    private List<String> ingredients;
    private String nutritionInfo;
    private String preparationTime;
    private String difficultyLevel;
    private String mealType;

    public Meal(String name, String description, int calories, String imageUrl, String time) {
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.imageUrl = imageUrl;
        this.time = time;
        this.isCompleted = false;
        this.ingredients = new ArrayList<>();
        this.nutritionInfo = "";
        this.preparationTime = "";
        this.difficultyLevel = "";
        this.mealType = "";
    }

    // Getters and setters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCalories() { return calories; }
    public String getImageUrl() { return imageUrl; }
    public String getTime() { return time; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public List<String> getIngredients() { return ingredients; }
    public String getNutritionInfo() { return nutritionInfo; }
    public String getPreparationTime() { return preparationTime; }
    public String getDifficultyLevel() { return difficultyLevel; }
    public String getMealType() { return mealType; }
} 