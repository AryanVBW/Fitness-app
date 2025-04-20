package com.example.fitnesstracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class WorkoutExampleAdapter extends RecyclerView.Adapter<WorkoutExampleAdapter.WorkoutExampleViewHolder> {
    private List<WorkoutExample> workoutExamples;

    public WorkoutExampleAdapter(List<WorkoutExample> workoutExamples) {
        this.workoutExamples = workoutExamples;
    }

    @NonNull
    @Override
    public WorkoutExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_example, parent, false);
        return new WorkoutExampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutExampleViewHolder holder, int position) {
        WorkoutExample workout = workoutExamples.get(position);
        holder.workoutName.setText(workout.getName());
        holder.workoutDescription.setText(workout.getDescription());
        holder.workoutDuration.setText(workout.getDuration());

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(workout.getImageUrl())
                .centerCrop()
                .into(holder.workoutImage);
    }

    @Override
    public int getItemCount() {
        return workoutExamples.size();
    }

    static class WorkoutExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView workoutImage;
        TextView workoutName;
        TextView workoutDescription;
        TextView workoutDuration;

        WorkoutExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutImage = itemView.findViewById(R.id.workout_image);
            workoutName = itemView.findViewById(R.id.workout_name);
            workoutDescription = itemView.findViewById(R.id.workout_description);
            workoutDuration = itemView.findViewById(R.id.workout_duration);
        }
    }
} 