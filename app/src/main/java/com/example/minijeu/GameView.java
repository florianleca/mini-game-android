package com.example.minijeu;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.minijeu.capteurs.LightCaptor;
import com.example.minijeu.capteurs.MoveCaptor;
import com.example.minijeu.capteurs.TouchCaptor;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int GRASS_HEIGHT = 200;
    private final Princess princess = new Princess(150, GRASS_HEIGHT + 150, Color.rgb(255, 215, 0));
    private final Monster monster1 = new Monster(getWidth(), GRASS_HEIGHT, Color.rgb(250, 0, 0),1000);
    private final Monster monster2 = new Monster(-500, GRASS_HEIGHT, Color.rgb(0, 0, 0),3000);
    private final GameThread thread;

    private boolean gameOver = false;
    private MoveCaptor moveCaptor;
    private LightCaptor lightCaptor;
    private TouchCaptor touchCaptor;

    private boolean jumping = false;
    private long jumpStartTime = 0;
    private int xMonster1 = 0;
    private int xMonster2 = -500;
    private final Context context;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        this.context = context;

        miseEnPlaceDesCapteurs(context);
    }

    private void miseEnPlaceDesCapteurs(Context context) {
        moveCaptor = new MoveCaptor(context);
        moveCaptor.initCaptor();

        lightCaptor = new LightCaptor(context);
        lightCaptor.initCaptor();

        touchCaptor = new TouchCaptor();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchCaptor.detecterToucher();
                    return true;
                }
                return true;
            }
        });
    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
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
        stopCaptors();
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        initCaptors();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.rgb(135, 206, 235));

        drawGrass(canvas);
        drawPrincess(canvas);
        drawMonster(canvas);

        if (gameOver) {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(80);
            canvas.drawText("Game Over", getWidth() / 2 - 200, getHeight() / 2, textPaint);
        }
    }

    private void drawMonster(Canvas canvas) {
        Paint redPaint = new Paint();
        redPaint.setColor(Color.rgb(250, 0, 0));

        Paint blackPaint = new Paint();
        blackPaint.setColor(Color.rgb(0, 0, 0));

        canvas.drawRect(getWidth() - monster1.getX(),
                getHeight() - (GRASS_HEIGHT + monster1.getHeigth()),
                getWidth() + monster1.getWidth() - monster1.getX(),
                getHeight() - GRASS_HEIGHT, redPaint);

        canvas.drawRect(getWidth() - monster2.getX(),
                getHeight() - (GRASS_HEIGHT + monster2.getHeigth()),
                getWidth() + monster2.getWidth() - monster2.getX(),
                getHeight() - GRASS_HEIGHT, blackPaint);
    }


    private void drawPrincess(Canvas canvas) {
        Paint goldPaint = new Paint();
        goldPaint.setColor(Color.rgb(255, 215, 0));

        canvas.drawRect(princess.getX(),
                getHeight() - (GRASS_HEIGHT + princess.getHeigth()),
                princess.getX() + princess.getWidth(),
                getHeight() - GRASS_HEIGHT, goldPaint);
    }

    private void drawGrass(Canvas canvas) {
        Paint greenPaint = new Paint();
        greenPaint.setColor(Color.rgb(34, 139, 34));
        canvas.drawRect(0, getHeight() - GRASS_HEIGHT, canvas.getWidth(), getHeight(), greenPaint);
    }

    public void update() {
        int monster1ActualX = getWidth() - monster1.getX();
        int monster2ActualX = getWidth() - monster2.getX();

        boolean collisionQueenMonster1 =
                princess.getX() + princess.getWidth() > monster1ActualX &&
                        princess.getX() < monster1ActualX + monster1.getWidth() &&
                        princess.getY() + princess.getHeigth() > getHeight() - (GRASS_HEIGHT + monster1.getHeigth());

        boolean collisionQueenMonster2 =
                princess.getX() + princess.getWidth() > monster2ActualX &&
                        princess.getX() < monster2ActualX + monster2.getWidth() &&
                        princess.getY() + princess.getHeigth() > getHeight() - (GRASS_HEIGHT + monster2.getHeigth());

        if (collisionQueenMonster1 || collisionQueenMonster2) {
            gameOver = true;
            thread.setRunning(false);
        }

        monster1.setX((monster1.getX() + 10) % (getWidth() + monster1.getMargin()));

        if (Math.abs(monster2.getX() - monster1.getX()) >= 700) {
            monster2.setX((monster2.getX() + 10) % (getWidth() + monster2.getMargin()));
        }

        manageJump();
        manageLight();
        manageTouch();

        SharedPreferences sharedPref = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.apply();
    }

    public void initCaptors() {
        moveCaptor = new MoveCaptor(context);
        moveCaptor.initCaptor();

        lightCaptor = new LightCaptor(context);
        lightCaptor.initCaptor();
    }

    private void stopCaptors() {
        if (moveCaptor != null) {
            moveCaptor.stopCaptor();
        }
        if (lightCaptor != null) {
            lightCaptor.stopCaptor();
        }
    }

    private void manageJump() {
        if (moveCaptor.estSautDetecte()) {
            jumping = true;
            jumpStartTime = System.currentTimeMillis();
        }

        if (jumping) {
            if (System.currentTimeMillis() - jumpStartTime < 1000) {
                Log.d("GameView", "le personnage est en l'air");
            } else {
                jumping = false;
                Log.d("GameView", "Le personnage arrete de sauter");
            }
        }
    }

    private void manageLight() {
        float lightValue = lightCaptor.obtenirValeursCapteur();
        if (lightValue < lightCaptor.SEUIL_LUMIERE_SOMBRE) {
            Log.d("GameView", "Capteur de lumière CACHÉ ! Valeur: " + lightValue);
        }
    }

    private void manageTouch() {
        if (touchCaptor.isTouchDetected()) {
            touchCaptor.setTouchDetected(false);
            Log.d("GameView", "Toucher détecté !");
        }
    }
}