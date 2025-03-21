package com.example.minijeu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.ViewHolder> {

    private List<HighScore> highScores;

    public HighscoreAdapter(List<HighScore> highScores) {
        Collections.sort(highScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore h1, HighScore h2) {
                return Integer.compare(h2.getScore(), h1.getScore());
            }
        });

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