package com.example.minijeu;

import android.content.Context;
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
    private final Princess princess = new Princess(150, GRASS_HEIGHT, Color.rgb(255, 215, 0));
    private final Monster monster1 = new Monster(getWidth(), GRASS_HEIGHT, Color.rgb(250, 0, 0), 1000);
    private final Monster monster2 = new Monster(-500, GRASS_HEIGHT, Color.rgb(0, 0, 0), 3000);
    private final GameThread thread;

    private boolean gameOver = false;
    private MoveCaptor moveCaptor;
    private LightCaptor lightCaptor;
    private TouchCaptor touchCaptor;

    private boolean jumping = false;
    private int jumpVelocity = 0;
    private int gravity = 2;
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

        canvas.drawRect(
                princess.getX(),
                getHeight() - (princess.getY() + princess.getHeigth() + princess.getJumpHeight()),
                princess.getX() + princess.getWidth(),
                getHeight() - (princess.getY() + princess.getJumpHeight()), goldPaint);
    }

    private void drawGrass(Canvas canvas) {
        Paint greenPaint = new Paint();
        greenPaint.setColor(Color.rgb(34, 139, 34));
        canvas.drawRect(0, getHeight() - GRASS_HEIGHT, canvas.getWidth(), getHeight(), greenPaint);
    }

    public void update() {
        moveMonster(monster1);
        if (Math.abs(monster2.getX() - monster1.getX()) >= 700) {
            moveMonster(monster2);
        }
        manageJump();
        manageLight();
        manageTouch();
        manageCollisions();
    }

    private void moveMonster(Monster monster) {
        monster.setX((monster.getX() + 10) % (getWidth() + monster.getMargin()));
    }

    private void manageCollisions() {
        if (isCollided(monster1) || isCollided(monster2)) {
            gameOver = true;
            thread.setRunning(false);
        }
    }

    private boolean isCollided(Monster monster) {

        int princessLeft = princess.getX();
        int princessRight = princess.getX() + princess.getWidth();
        int princessTop = getHeight() - (princess.getHeigth() + princess.getY() + princess.getJumpHeight());
        int princessBottom = getHeight() - (princess.getY() + princess.getJumpHeight());

        int monsterLeft = getWidth() - monster.getX();
        int monsterRight = getWidth() - monster.getX() + monster.getWidth();
        int monsterTop = getHeight() - (GRASS_HEIGHT + monster.getHeigth());
        int monsterBottom = getHeight() - GRASS_HEIGHT;

        return princessRight > monsterLeft &&
                princessLeft < monsterRight &&
                princessBottom > monsterTop &&
                princessTop < monsterBottom;
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
        }

        if (jumping) {
            Log.d("GameView", "le personnage est en l'air");
            if (princess.getJumpHeight() >= 100) {
                gravity = -1;
            }

            jumpVelocity += gravity;
            princess.setJumpHeight(princess.getJumpHeight() + jumpVelocity);
            if (princess.getJumpHeight() <= 0) {
                princess.setJumpHeight(0);
                jumping = false;
                jumpVelocity = 0;
                gravity = 2;
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