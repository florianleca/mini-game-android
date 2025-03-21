package com.example.minijeu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.minijeu.capteurs.CapteurLumiere;
import com.example.minijeu.capteurs.CapteurMouvement;

public class MainActivity extends Activity {

    private CapteurMouvement capteurMouvement;
    private CapteurLumiere capteurLumiere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this));

        capteurMouvement = new CapteurMouvement(this);
        capteurMouvement.initialiserCapteur();

        capteurLumiere = new CapteurLumiere(this);
        capteurLumiere.initialiserCapteur();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int valeur_y = sharedPref.getInt("valeur_y", 0);
        valeur_y = (valeur_y + 100) % 400;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("valeur_y", valeur_y);
        editor.apply();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (capteurMouvement != null) {
            capteurMouvement.arreterCapteur();
        }
        if (capteurLumiere != null) {
            capteurLumiere.arreterCapteur();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (capteurMouvement != null) {
            capteurMouvement.initialiserCapteur();
        }
        if (capteurLumiere != null) {
            capteurLumiere.initialiserCapteur();
        }
    }
}