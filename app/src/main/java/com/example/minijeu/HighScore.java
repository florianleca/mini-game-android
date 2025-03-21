package com.example.minijeu;

public class HighScore {
    private String difficulte;
    private int score;

    private String pseudo;

    public HighScore(String difficulte,String pseudo, int score) {
        this.pseudo = pseudo;
        this.difficulte = difficulte;
        this.score = score;
    }

    public String getDifficulte() {
        return difficulte;
    }

    public int getScore() {
        return score;
    }

    public String getPseudo() {
        return pseudo;
    }
}