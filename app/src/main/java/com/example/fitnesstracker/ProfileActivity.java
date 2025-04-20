package com.example.fitnesstracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "FitnessPrefs";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_AGE = "user_age";
    private static final String KEY_HEIGHT = "user_height";
    private static final String KEY_WEIGHT = "user_weight";
    private static final String KEY_GOAL = "user_goal";

    private ImageView profileImage;
    private EditText nameInput;
    private EditText ageInput;
    private EditText heightInput;
    private EditText weightInput;
    private EditText goalInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        initializeViews();

        // Load saved profile data
        loadProfileData();

        // Set up save button
        saveButton.setOnClickListener(v -> saveProfileData());
    }

    private void initializeViews() {
        profileImage = findViewById(R.id.profileImage);
        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        goalInput = findViewById(R.id.goalInput);
        saveButton = findViewById(R.id.saveButton);

        // Load default profile image
        Glide.with(this)
            .load(R.drawable.default_profile)
            .circleCrop()
            .into(profileImage);
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        nameInput.setText(prefs.getString(KEY_NAME, ""));
        ageInput.setText(prefs.getString(KEY_AGE, ""));
        heightInput.setText(prefs.getString(KEY_HEIGHT, ""));
        weightInput.setText(prefs.getString(KEY_WEIGHT, ""));
        goalInput.setText(prefs.getString(KEY_GOAL, ""));
    }

    private void saveProfileData() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        // Save to SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_NAME, nameInput.getText().toString().trim());
        editor.putString(KEY_AGE, ageInput.getText().toString().trim());
        editor.putString(KEY_HEIGHT, heightInput.getText().toString().trim());
        editor.putString(KEY_WEIGHT, weightInput.getText().toString().trim());
        editor.putString(KEY_GOAL, goalInput.getText().toString().trim());
        editor.apply();

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Validate name
        if (nameInput.getText().toString().trim().isEmpty()) {
            nameInput.setError("Name is required");
            isValid = false;
        }

        // Validate age
        try {
            int age = Integer.parseInt(ageInput.getText().toString().trim());
            if (age < 1 || age > 120) {
                ageInput.setError("Please enter a valid age");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            ageInput.setError("Please enter a valid age");
            isValid = false;
        }

        // Validate height
        try {
            float height = Float.parseFloat(heightInput.getText().toString().trim());
            if (height < 1 || height > 300) {
                heightInput.setError("Please enter a valid height in cm");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            heightInput.setError("Please enter a valid height");
            isValid = false;
        }

        // Validate weight
        try {
            float weight = Float.parseFloat(weightInput.getText().toString().trim());
            if (weight < 1 || weight > 500) {
                weightInput.setError("Please enter a valid weight in kg");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            weightInput.setError("Please enter a valid weight");
            isValid = false;
        }

        return isValid;
    }

    // Inner class for stats items
    public static class StatItem {
        private String label;
        private String value;
        private int iconRes;

        public StatItem(String label, String value, int iconRes) {
            this.label = label;
            this.value = value;
            this.iconRes = iconRes;
        }

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }

        public int getIconRes() {
            return iconRes;
        }
    }

    // Inner class for achievements
    public static class Achievement {
        private String title;
        private String description;
        private boolean isUnlocked;

        public Achievement(String title, String description, boolean isUnlocked) {
            this.title = title;
            this.description = description;
            this.isUnlocked = isUnlocked;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public boolean isUnlocked() {
            return isUnlocked;
        }
    }
} 