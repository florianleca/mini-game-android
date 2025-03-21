package com.example.minijeu;

import android.graphics.Color;

public class Monster extends Personnage {

    public Monster(int x, int y, int color) {
        super(x, y, color);
    }

    public int getHeigth() {
        return 100;
    }

    public int getWidth() {
        return 100;
    }
}
