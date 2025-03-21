package com.example.minijeu;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_activity);

        RecyclerView recyclerFacile = findViewById(R.id.recycler_facile);
        RecyclerView recyclerDifficile = findViewById(R.id.recycler_difficile);

        List<HighScore> scoresFacile = new ArrayList<>();
        scoresFacile.add(new HighScore("facile",  100));
        scoresFacile.add(new HighScore("facile",  95));
        scoresFacile.add(new HighScore("facile",  80));
        scoresFacile.add(new HighScore("facile",  75));
        scoresFacile.add(new HighScore("facile",  60));

        List<HighScore> scoresDifficile = new ArrayList<>();
        scoresDifficile.add(new HighScore("difficile",  250));
        scoresDifficile.add(new HighScore("difficile",  220));
        scoresDifficile.add(new HighScore("difficile",  200));
        scoresDifficile.add(new HighScore("difficile",  180));
        scoresDifficile.add(new HighScore("difficile",  150));

        // Configuration des RecyclerView
        recyclerFacile.setLayoutManager(new LinearLayoutManager(this));
        recyclerDifficile.setLayoutManager(new LinearLayoutManager(this));

        // Création et attribution des adaptateurs
        recyclerFacile.setAdapter(new HighscoreAdapter(scoresFacile));
        recyclerDifficile.setAdapter(new HighscoreAdapter(scoresDifficile));
    }
}