package com.example.fitnesstracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {
    private List<ProfileActivity.StatItem> stats;

    public StatsAdapter(List<ProfileActivity.StatItem> stats) {
        this.stats = stats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileActivity.StatItem stat = stats.get(position);
        holder.statLabel.setText(stat.getLabel());
        holder.statValue.setText(stat.getValue());
        holder.statIcon.setImageResource(stat.getIconRes());
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView statLabel;
        TextView statValue;
        ImageView statIcon;

        ViewHolder(View itemView) {
            super(itemView);
            statLabel = itemView.findViewById(R.id.stat_label);
            statValue = itemView.findViewById(R.id.stat_value);
            statIcon = itemView.findViewById(R.id.stat_icon);
        }
    }
} 