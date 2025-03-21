package com.example.minijeu;

import android.graphics.Color;

public class Queen extends Personnage{

    public Queen(int x, int y, int color) {
        super(x, y, color);
    }

    public int getHeigth() {
        return 200;
    }

    public int getWidth() {
        return 100;
    }
}
