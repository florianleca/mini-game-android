package com.example.minijeu;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int GRASS_HEIGHT = 200;
    private final int monsterWidth = 100;
    private final int monsterHeight = 100;


    private final GameThread thread;
    private int xMonster1 = 0;
    private int xMonster2 = -500;
    private final Context context;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        this.context = context;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        SharedPreferences sharedPref = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        int valeur_y = sharedPref.getInt("valeur_y", 0);
        Log.d("valeur_y", String.valueOf(valeur_y));

        canvas.drawColor(Color.rgb(135, 206, 235));

        drawGrass(canvas);
        drawPrincess(canvas);
        drawMonster(canvas);
    }

    private void drawMonster(Canvas canvas) {
        Paint redPaint = new Paint();
        redPaint.setColor(Color.rgb(250, 0, 0));

        Paint blackPaint = new Paint();
        blackPaint.setColor(Color.rgb(0, 0, 0));



        canvas.drawRect(getWidth() - xMonster1, getHeight() - (GRASS_HEIGHT + monsterHeight), getWidth() + monsterWidth - xMonster1, getHeight() - GRASS_HEIGHT, redPaint);
        canvas.drawRect(getWidth() - xMonster2, getHeight() - (GRASS_HEIGHT + monsterHeight), getWidth() + monsterWidth - xMonster2, getHeight() - GRASS_HEIGHT, blackPaint);
    }

    private void drawPrincess(Canvas canvas) {
        Paint goldPaint = new Paint();
        goldPaint.setColor(Color.rgb(255, 215, 0));

        int princessX = 150;
        int princessWidth = 100;
        int princessHeight = 200;
        // left top right bottom
        canvas.drawRect(princessX, getHeight() - (GRASS_HEIGHT + princessHeight), princessX + princessWidth, getHeight() - GRASS_HEIGHT, goldPaint);
    }

    private void drawGrass(Canvas canvas) {
        Paint greenPaint = new Paint();
        greenPaint.setColor(Color.rgb(34, 139, 34));
        canvas.drawRect(0, getHeight() - GRASS_HEIGHT, canvas.getWidth(), getHeight(), greenPaint);
    }

    public void update() {
        int marginMonster1 = 1000;
        int marginMonster2 = 3000;
        xMonster1 = (xMonster1 + 10) % (getWidth() + marginMonster1);
        if (Math.abs(xMonster2 - xMonster1) >= 700) {
            xMonster2 = (xMonster2 + 10) % (getWidth() + marginMonster2);
        }
    }

}
