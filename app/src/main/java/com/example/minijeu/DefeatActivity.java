package com.example.minijeu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DefeatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defeat);

        int score = getIntent().getIntExtra("score", 0);

        TextView scoreText = findViewById(R.id.defeat_score_text);
        scoreText.setText("Score: " + score);

        Button retryButton = findViewById(R.id.defeat_retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DefeatActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button homepageButton = findViewById(R.id.defeat_homepage_button);
        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DefeatActivity.this, HomePageActivity.class); // Remplacez MainActivity par votre activité principale
                startActivity(intent);
                finish();
            }
        });
    }
}