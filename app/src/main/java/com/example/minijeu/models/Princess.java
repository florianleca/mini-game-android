package com.example.minijeu.models;

public class Princess extends Personnage{

    private int jumpHeight = 0;

    public Princess(int x, int y, int color) {
        super(x, y, color);
    }

    public int getHeigth() {
        return 200;
    }

    public int getWidth() {
        return 100;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }
}
