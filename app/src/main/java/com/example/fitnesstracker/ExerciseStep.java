package com.example.fitnesstracker;

public class ExerciseStep {
    private String name;
    private String description;
    private int duration; // in minutes
    private int sets;
    private int reps;
    private boolean completed;

    public ExerciseStep(String name, String description, int duration, int sets, int reps) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.sets = sets;
        this.reps = reps;
        this.completed = false;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public boolean isCompleted() { return completed; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setSets(int sets) { this.sets = sets; }
    public void setReps(int reps) { this.reps = reps; }
    public void setCompleted(boolean completed) { this.completed = completed; }
} 