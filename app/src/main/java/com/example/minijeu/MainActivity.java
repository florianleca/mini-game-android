package com.example.minijeu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.minijeu.capteurs.LightCaptor;
import com.example.minijeu.capteurs.MoveCaptor;

public class MainActivity extends Activity {

    private MoveCaptor moveCaptor;
    private LightCaptor lightCaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this));

        moveCaptor = new MoveCaptor(this);
        moveCaptor.initCaptor();

        lightCaptor = new LightCaptor(this);
        lightCaptor.initCaptor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (moveCaptor != null) {
            moveCaptor.stopCaptor();
        }
        if (lightCaptor != null) {
            lightCaptor.stopCaptor();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (moveCaptor != null) {
            moveCaptor.initCaptor();
        }
        if (lightCaptor != null) {
            lightCaptor.initCaptor();
        }
    }
}