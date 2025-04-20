package com.example.fitnesstracker;

import java.util.List;

public class WorkoutType {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String difficulty;
    private int duration; // in minutes
    private String spotifyPlaylistUrl;
    private List<ExerciseStep> steps;
    private boolean isCompleted;

    public WorkoutType(String id, String name, String description, String imageUrl, 
                      String difficulty, int duration, String spotifyPlaylistUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.difficulty = difficulty;
        this.duration = duration;
        this.spotifyPlaylistUrl = spotifyPlaylistUrl;
        this.isCompleted = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getDuration() {
        return duration;
    }

    public String getSpotifyPlaylistUrl() {
        return spotifyPlaylistUrl;
    }

    public List<ExerciseStep> getSteps() {
        return steps;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setSpotifyPlaylistUrl(String url) {
        this.spotifyPlaylistUrl = url;
    }
} 