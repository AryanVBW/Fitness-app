package com.example.fitnesstracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExerciseStepAdapter extends RecyclerView.Adapter<ExerciseStepAdapter.ViewHolder> {
    private List<ExerciseStep> steps;

    public ExerciseStepAdapter(List<ExerciseStep> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_exercise_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciseStep step = steps.get(position);
        holder.stepName.setText(step.getName());
        holder.stepDescription.setText(step.getDescription());
        
        String details = "";
        if (step.getDuration() > 0) {
            details = holder.itemView.getContext().getString(
                R.string.workout_time_format, 
                step.getDuration()
            );
        }
        
        if (step.getSets() > 0) {
            String setsText = holder.itemView.getContext().getString(
                R.string.workout_sets_format, 
                step.getSets()
            );
            details = details.isEmpty() ? setsText : details + " • " + setsText;
            
            if (step.getReps() > 0) {
                String repsText = holder.itemView.getContext().getString(
                    R.string.workout_reps_format, 
                    step.getReps()
                );
                details += " • " + repsText;
            }
        }
        
        holder.stepDetails.setText(details);
        holder.checkBox.setChecked(step.isCompleted());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            step.setCompleted(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void updateSteps(List<ExerciseStep> newSteps) {
        this.steps = newSteps;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stepName;
        TextView stepDescription;
        TextView stepDetails;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            stepName = itemView.findViewById(R.id.stepName);
            stepDescription = itemView.findViewById(R.id.stepDescription);
            stepDetails = itemView.findViewById(R.id.stepDetails);
            checkBox = itemView.findViewById(R.id.stepCheckBox);
        }
    }
} 