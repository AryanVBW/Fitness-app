package com.example.fitnesstracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.ViewHolder> {
    private List<ProfileActivity.Achievement> achievements;

    public AchievementsAdapter(List<ProfileActivity.Achievement> achievements) {
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileActivity.Achievement achievement = achievements.get(position);
        holder.achievementTitle.setText(achievement.getTitle());
        holder.achievementDescription.setText(achievement.getDescription());
        holder.achievementIcon.setImageResource(achievement.isUnlocked() ? 
            R.drawable.ic_achievement_unlocked : R.drawable.ic_achievement_locked);
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView achievementTitle;
        TextView achievementDescription;
        ImageView achievementIcon;

        ViewHolder(View itemView) {
            super(itemView);
            achievementTitle = itemView.findViewById(R.id.achievement_title);
            achievementDescription = itemView.findViewById(R.id.achievement_description);
            achievementIcon = itemView.findViewById(R.id.achievement_icon);
        }
    }
} 