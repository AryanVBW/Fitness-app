package com.example.fitnesstracker;

public class Exercise {
    private String name;
    private String instructions;
    private String imageUrl;

    public Exercise(String name, String instructions, String imageUrl) {
        this.name = name;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
} 