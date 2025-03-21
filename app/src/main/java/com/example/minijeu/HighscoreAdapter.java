package com.example.minijeu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.ViewHolder> {

    private List<HighScore> highScores;

    public HighscoreAdapter(List<HighScore> highScores) {
        this.highScores = highScores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HighScore highScore = highScores.get(position);
        // Convert the int score to a String before setting it
        holder.scoreTextView.setText(String.valueOf(highScore.getScore()));
    }

    @Override
    public int getItemCount() {
        return highScores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView scoreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            scoreTextView = itemView.findViewById(R.id.score_text_view);
        }
    }
}