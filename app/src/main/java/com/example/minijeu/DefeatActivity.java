package com.example.minijeu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DefeatActivity extends AppCompatActivity {

    private int score;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defeat);

        score = getIntent().getIntExtra("score", 999);
        difficulty = getIntent().getStringExtra("difficulty");
        if (difficulty == null){
            difficulty = "FACILE";
        }

        TextView scoreText = findViewById(R.id.defeat_score_text);

        TextView difficultyView = findViewById(R.id.defeat_difficulty_text);
        TextView highScoreTextView = findViewById(R.id.defeat_highscore_text);
        int highScore = findHighScore();

        highScoreTextView.setText("Highscore: " + highScore);
        scoreText.setText("Score: " + score);
        difficultyView.setText(difficulty);

        Button retryButton = findViewById(R.id.defeat_retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScoreAndNavigate(MainActivity.class);
            }
        });

        Button homepageButton = findViewById(R.id.defeat_homepage_button);
        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScoreAndNavigate(HomePageActivity.class);
            }
        });
    }

    private int findHighScore() {
        List<HighScore> highScores = HighScoreManager.getInstance(this).getMapHighScore().get(difficulty);
        int highScore = 0;
        if (highScores != null && !highScores.isEmpty()) {
            highScore = highScores.get(0).getScore();
        }
        if (score > highScore){
            highScore = score;
        }
        return highScore;
    }

    private void addScoreAndNavigate(Class<?> destinationActivity) {
        HighScoreManager.getInstance(this).addHighScore(difficulty, new HighScore(difficulty, score));
        HighScoreManager.getInstance(this).saveHighScores();
        Intent intent = new Intent(DefeatActivity.this, destinationActivity);
        startActivity(intent);
        finish();
    }
}