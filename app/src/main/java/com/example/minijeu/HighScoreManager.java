package com.example.minijeu;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighScoreManager {

    private static HighScoreManager instance;
    private Map<String, List<HighScore>> mapHighScore;
    private Context context;

    private HighScoreManager(Context context) {
        this.context = context;
        mapHighScore = new HashMap<>();
        loadHighScores();
    }

    public static synchronized HighScoreManager getInstance(Context context) {
        if (instance == null) {
            instance = new HighScoreManager(context.getApplicationContext());
        }
        return instance;
    }

    public void addHighScore(String difficulty, HighScore highScore) {
        if (mapHighScore.containsKey(difficulty)) {
            mapHighScore.get(difficulty).add(highScore);
        } else {
            List<HighScore> newScores = new ArrayList<>();
            newScores.add(highScore);
            mapHighScore.put(difficulty, newScores);
        }
    }

    public void saveHighScores() {
        SharedPreferences prefs = context.getSharedPreferences("highscores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mapHighScore);
        editor.putString("highscores_map", json);
        editor.apply();
    }

    public void loadHighScores() {
        SharedPreferences prefs = context.getSharedPreferences("highscores", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("highscores_map", null);
        Type type = new TypeToken<Map<String, List<HighScore>>>() {}.getType();
        mapHighScore = gson.fromJson(json, type);
        if (mapHighScore == null) {
            mapHighScore = new HashMap<>();
        }
    }

    public Map<String, List<HighScore>> getMapHighScore() {
        return mapHighScore;
    }
}