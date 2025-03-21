package com.example.minijeu.capteurs;

public class CapteurToucher implements Capteur {

    private boolean touchDetected = false;

    public void detecterToucher() {
        touchDetected = true;
    }

    public boolean estToucherDetecte() {
        boolean detected = touchDetected;
        touchDetected = false;
        return detected;
    }

    @Override
    public void initialiserCapteur() {
        //rien
    }

    @Override
    public void arreterCapteur() {
        // rien
    }
}