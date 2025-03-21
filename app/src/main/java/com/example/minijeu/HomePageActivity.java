package com.example.minijeu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {

    private Button buttonFacile;
    private Button buttonDifficile;
    private Button buttonHighScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_accueil);

        buttonFacile = findViewById(R.id.button_facile);
        buttonDifficile = findViewById(R.id.button_difficile);
        buttonHighScores = findViewById(R.id.button_high_scores);

        buttonFacile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demarrerJeu("facile");
            }
        });

        buttonDifficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demarrerJeu("difficile");
            }
        });

        buttonHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent highScoreIntent = new Intent(HomePageActivity.this, HighScoreActivity.class);
                startActivity(highScoreIntent);
            }
        });
    }

    private void demarrerJeu(String difficulte) {
        Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
        intent.putExtra("difficulte", difficulte);
        startActivity(intent);
    }
}