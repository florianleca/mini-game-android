package com.example.minijeu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PageAccueilActivity extends AppCompatActivity {

    private Button buttonFacile;
    private Button buttonDifficile;
    private Button buttonHighScores;
    private EditText pseudoInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_accueil);

        buttonFacile = findViewById(R.id.button_facile);
        buttonDifficile = findViewById(R.id.button_difficile);
        buttonHighScores = findViewById(R.id.button_high_scores);
        pseudoInput = findViewById(R.id.pseudo_input);

        buttonFacile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demarrerJeu("facile", pseudoInput.getText().toString().toUpperCase());
            }
        });

        buttonDifficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demarrerJeu("difficile", pseudoInput.getText().toString().toUpperCase());
            }
        });

        buttonHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent highScoreIntent = new Intent(PageAccueilActivity.this, HighscoreActivity.class);
                startActivity(highScoreIntent);
            }
        });
    }

    private void demarrerJeu(String difficulte, String pseudo) {
        if (pseudo.length() != 3) {
            Toast.makeText(this, "Veuillez entrer un pseudo de 3 lettres", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(PageAccueilActivity.this, MainActivity.class);
        intent.putExtra("difficulte", difficulte);
        intent.putExtra("pseudo", pseudo);
        startActivity(intent);
    }
}