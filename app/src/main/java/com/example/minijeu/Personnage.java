package com.example.minijeu;


public abstract class Personnage {
    private int x;
    private int y;
    private int color;


    public Personnage(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract int getHeigth();

    public abstract int getWidth();

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
