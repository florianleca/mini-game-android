package com.example.minijeu.capteurs;

public class TouchCaptor implements Capteur {

    private boolean touchDetected = false;

    public void detecterToucher() {
        touchDetected = true;
    }

    public boolean isTouchDetected() {
        return touchDetected;
    }

    public void setTouchDetected(boolean touchDetected) {
        this.touchDetected = touchDetected;
    }

    @Override
    public void initCaptor() {
        //rien
    }

    @Override
    public void stopCaptor() {
        // rien
    }
}