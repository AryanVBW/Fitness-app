package com.example.fitnesstracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    private List<Meal> meals;

    public MealAdapter(List<Meal> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        
        holder.mealName.setText(meal.getName());
        holder.mealDescription.setText(meal.getDescription());
        holder.mealTime.setText(meal.getTime());
        holder.mealCalories.setText(meal.getCalories() + " calories");
        
        if (!meal.getImageUrl().isEmpty()) {
            Glide.with(holder.mealImage.getContext())
                .load(meal.getImageUrl())
                .centerCrop()
                .into(holder.mealImage);
        }
        
        holder.completedCheckBox.setChecked(meal.isCompleted());
        holder.completedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            meal.setCompleted(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        TextView mealDescription;
        TextView mealTime;
        TextView mealCalories;
        ImageView mealImage;
        CheckBox completedCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealNameTextView);
            mealDescription = itemView.findViewById(R.id.mealDescriptionTextView);
            mealTime = itemView.findViewById(R.id.mealTimeTextView);
            mealCalories = itemView.findViewById(R.id.mealCaloriesTextView);
            mealImage = itemView.findViewById(R.id.mealImageView);
            completedCheckBox = itemView.findViewById(R.id.mealCompletedCheckBox);
        }
    }
} 