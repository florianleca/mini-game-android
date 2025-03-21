package com.example.minijeu;

public class Score {
    private String difficulte;
    private int score;

    public Score(String difficulte, int score) {
        this.difficulte = difficulte;
        this.score = score;
    }

    public String getDifficulte() {
        return difficulte;
    }

    public int getScore() {
        return score;
    }
}