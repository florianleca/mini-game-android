package com.example.minijeu.capteurs;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MoveCaptor implements Capteur, SensorEventListener {
    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private float lastAcceleration;
    private boolean sautDetecte;
    private static final float SEUIL_SAUT = 18.0f;
    public MoveCaptor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float acceleration = event.values[2];
            sautDetecte = acceleration - lastAcceleration > SEUIL_SAUT;
            lastAcceleration = acceleration;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //rien
    }

    @Override
    public void initCaptor() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void stopCaptor() {
        sensorManager.unregisterListener(this);
    }

    public boolean estSautDetecte() {
        return sautDetecte;
    }
}