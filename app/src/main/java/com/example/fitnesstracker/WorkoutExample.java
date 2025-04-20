package com.example.fitnesstracker;

public class WorkoutExample {
    private String name;
    private String description;
    private String duration;
    private String imageUrl;

    public WorkoutExample(String name, String description, String duration, String imageUrl) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }
} 