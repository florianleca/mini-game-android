package com.example.minijeu.capteurs;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class CapteurLumiere implements Capteur, SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor lightSensor;
    private float lastLightValue = -1;
    private static final float SEUIL_CHANGEMENT_LUMIERE = 25.0f;
    public final float SEUIL_LUMIERE_SOMBRE = 20.0f; // Un peu sensible à la main qui passe sur l'écran mais acceptable dans l'état

    public CapteurLumiere(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            if (lastLightValue != -1) {
                float difference = Math.abs(lightValue - lastLightValue); // On calcule la différence entre la
                if (difference > SEUIL_CHANGEMENT_LUMIERE) {
                    Log.d("CapteurLumiere", "Changement de lumière détecté ! Valeur: " + lightValue);
                }
            }
            lastLightValue = lightValue;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void initialiserCapteur() {
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void arreterCapteur() {
        sensorManager.unregisterListener(this);
    }

    public float obtenirValeursCapteur() {
        return lastLightValue;
    }
}