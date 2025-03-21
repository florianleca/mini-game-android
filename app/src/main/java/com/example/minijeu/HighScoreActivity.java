package com.example.minijeu;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HighScoreActivity extends AppCompatActivity {

    private Map<String, List<HighScore>> mapHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_activity);

        RecyclerView recyclerFacile = findViewById(R.id.recycler_facile);
        RecyclerView recyclerDifficile = findViewById(R.id.recycler_difficile);

        mapHighScore = HighScoreManager.getInstance(this).getMapHighScore();

        List<HighScore> scoresFacile = mapHighScore.get("FACILE");
        List<HighScore> scoresDifficile = mapHighScore.get("DIFFICILE");

        if (scoresFacile == null) {
            scoresFacile = new ArrayList<>();
            mapHighScore.put("FACILE", scoresFacile);
        }
        if (scoresDifficile == null) {
            scoresDifficile = new ArrayList<>();
            mapHighScore.put("DIFFICILE", scoresDifficile);
        }

        recyclerFacile.setLayoutManager(new LinearLayoutManager(this));
        recyclerDifficile.setLayoutManager(new LinearLayoutManager(this));

        recyclerFacile.setAdapter(new HighscoreAdapter(scoresFacile));
        recyclerDifficile.setAdapter(new HighscoreAdapter(scoresDifficile));
    }

    public void addHighScore(String difficulty, HighScore highScore) {
        HighScoreManager.getInstance(this).addHighScore(difficulty, highScore);
        HighScoreManager.getInstance(this).saveHighScores();
        onCreate(null);
    }

    public Map<String, List<HighScore>> getMapHighScore() {
        return mapHighScore;
    }
}