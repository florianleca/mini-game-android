package com.example.minijeu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PageAccueilActivity extends AppCompatActivity {

    private Button buttonFacile;
    private Button buttonDifficile;
    private EditText pseudoInput;
    private RecyclerView scoresFacileRecyclerView;
    private RecyclerView scoresDifficileRecyclerView;
    private ScoreAdapter scoreFacileAdapter;
    private ScoreAdapter scoreDifficileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_accueil);

        buttonFacile = findViewById(R.id.button_facile);
        buttonDifficile = findViewById(R.id.button_difficile);
        scoresFacileRecyclerView = findViewById(R.id.scores_facile_recycler_view);
        scoresDifficileRecyclerView = findViewById(R.id.scores_difficile_recycler_view);

        pseudoInput = findViewById(R.id.pseudo_input);



        buttonFacile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demarrerJeu("facile", pseudoInput.getText().toString().toUpperCase()); // Ajout du pseudo
            }
        });

        buttonDifficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demarrerJeu("difficile", pseudoInput.getText().toString().toUpperCase()); // Ajout du pseudo
            }
        });

        simulerEtAfficherMeilleursScores(); // Utilisation de la nouvelle méthode
    }

    private void demarrerJeu(String difficulte, String pseudo) {
        if (pseudo.length() != 3) {
            Toast.makeText(this, "Veuillez entrer un pseudo de 3 lettres", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(PageAccueilActivity.this, MainActivity.class);
        intent.putExtra("difficulte", difficulte);
        intent.putExtra("pseudo", pseudo); // Passage du pseudo à MainActivity
        startActivity(intent);
    }

    private void simulerEtAfficherMeilleursScores() {
        SharedPreferences sharedPref = getSharedPreferences("meilleurs_scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Random random = new Random();
        String[] pseudosFacile = {"ABC", "DRM", "AAA", "BBB", "CCC"};
        String[] pseudosDifficile = {"FFF", "GGG", "HHH", "JJJ", "LLL"};

        // Simulation de 5 scores avec pseudos pour chaque difficulté
        for (int i = 0; i < 5; i++) { // Changement de 1 à 5 en 0 à 4 pour indexer les tableaux
            int scoreFacile = random.nextInt(1000);
            int scoreDifficile = random.nextInt(2000);

            editor.putString("score_facile_" + (i + 1), pseudosFacile[i] + ": " + scoreFacile);
            editor.putString("score_difficile_" + (i + 1), pseudosDifficile[i] + ": " + scoreDifficile);
        }
        editor.apply();

        afficherMeilleursScores();
    }

    private void afficherMeilleursScores() {
        SharedPreferences sharedPref = getSharedPreferences("meilleurs_scores", Context.MODE_PRIVATE);

        List<String> scoreFacileList = new ArrayList<>();
        List<String> scoreDifficileList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            scoreFacileList.add(sharedPref.getString("score_facile_" + i, ""));
            scoreDifficileList.add(sharedPref.getString("score_difficile_" + i, ""));
        }

        // Tri des scores (chaînes de caractères) - À adapter si besoin
        Collections.sort(scoreFacileList, Collections.reverseOrder());
        Collections.sort(scoreDifficileList, Collections.reverseOrder());

        scoresFacileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoresDifficileRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        scoreFacileAdapter = new ScoreAdapter(scoreFacileList);
        scoreDifficileAdapter = new ScoreAdapter(scoreDifficileList);

        scoresFacileRecyclerView.setAdapter(scoreFacileAdapter);
        scoresDifficileRecyclerView.setAdapter(scoreDifficileAdapter);
    }
}