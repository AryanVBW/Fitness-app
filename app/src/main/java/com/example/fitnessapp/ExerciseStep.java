package com.example.fitnessapp;

public class ExerciseStep {
    private String name;
    private String description;
    private int duration;
    private int sets;
    private int reps;
    private String imageUrl;
    private boolean isCompleted;

    public ExerciseStep(String name, String description, int duration, int sets, int reps) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.sets = sets;
        this.reps = reps;
        this.imageUrl = "";
        this.isCompleted = false;
    }

    public ExerciseStep(String name, String description, String imageUrl, String duration, String sets, String reps) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.duration = Integer.parseInt(duration.replaceAll("[^0-9]", ""));
        this.sets = Integer.parseInt(sets.equals("N/A") ? "0" : sets);
        this.reps = Integer.parseInt(reps.equals("N/A") ? "0" : reps);
        this.isCompleted = false;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public String getImageUrl() { return imageUrl; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
} 