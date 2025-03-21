package com.example.minijeu;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_activity);

        RecyclerView recyclerFacile = findViewById(R.id.recycler_facile);
        RecyclerView recyclerDifficile = findViewById(R.id.recycler_difficile);

        List<HighScore> scoresFacile = new ArrayList<>();
        scoresFacile.add(new HighScore("facile", "AAA", 100));
        scoresFacile.add(new HighScore("facile", "BBB", 95));
        scoresFacile.add(new HighScore("facile", "CCC", 80));
        scoresFacile.add(new HighScore("facile", "DDD", 75));
        scoresFacile.add(new HighScore("facile", "EEE", 60));

        List<HighScore> scoresDifficile = new ArrayList<>();
        scoresDifficile.add(new HighScore("difficile", "FFF", 250));
        scoresDifficile.add(new HighScore("difficile", "GGG", 220));
        scoresDifficile.add(new HighScore("difficile", "HHH", 200));
        scoresDifficile.add(new HighScore("difficile", "III", 180));
        scoresDifficile.add(new HighScore("difficile", "JJJ", 150));

        // Configuration des RecyclerView
        recyclerFacile.setLayoutManager(new LinearLayoutManager(this));
        recyclerDifficile.setLayoutManager(new LinearLayoutManager(this));

        // Création et attribution des adaptateurs
        recyclerFacile.setAdapter(new HighscoreAdapter(scoresFacile));
        recyclerDifficile.setAdapter(new HighscoreAdapter(scoresDifficile));
    }
}