package com.example.minijeu;


public class Monster extends Personnage {
private int margin;
    public Monster(int x, int y, int color, int margin) {
        super(x, y, color);
        this.margin = margin;
    }

    public int getHeigth() {
        return 100;
    }

    public int getWidth() {
        return 100;
    }

    public int getMargin() {
        return margin;
    }

}
