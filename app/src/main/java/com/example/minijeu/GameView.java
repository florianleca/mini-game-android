package com.example.minijeu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.minijeu.capteurs.LightCaptor;
import com.example.minijeu.capteurs.MoveCaptor;
import com.example.minijeu.capteurs.TouchCaptor;
import com.example.minijeu.models.Bloc;
import com.example.minijeu.models.Fence;
import com.example.minijeu.models.Ghost;
import com.example.minijeu.models.Monster;
import com.example.minijeu.models.Princess;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int GRASS_HEIGHT = 200;
    private final Princess princess = new Princess(150, GRASS_HEIGHT, Color.rgb(255, 215, 0));

    private List<Monster> monsters = new ArrayList<>();

    private final GameThread thread;

    private float grassScrollX = 0;
    private BitmapShader shader;
    private boolean gameOver = false;
    private MoveCaptor moveCaptor;
    private LightCaptor lightCaptor;
    private TouchCaptor touchCaptor;

    private boolean jumping = false;
    private int jumpVelocity = 0;
    private int gravity = 2;
    private final Context context;

    private int score;

    private String difficulty;

    private float touchX = -1; // Coordonnée X du toucher
    private float touchY = -1; // Coordonnée Y du toucher

    public GameView(Context context, String difficulty) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        this.context = context;
        this.difficulty = difficulty;

        miseEnPlaceDesCapteurs(context);
        initGrassTile();
    }

    private void initGrassTile() {
        Bitmap originalTile = BitmapFactory.decodeResource(getResources(), R.drawable.grass_tile);
        Bitmap smallTile = Bitmap.createScaledBitmap(originalTile, 150, 150, true);
        shader = new BitmapShader(smallTile, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Paint grassPaint = new Paint();
        grassPaint.setShader(shader);
    }

    private void miseEnPlaceDesCapteurs(Context context) {
        moveCaptor = new MoveCaptor(context);
        moveCaptor.initCaptor();

        lightCaptor = new LightCaptor(context);
        lightCaptor.initCaptor();

        touchCaptor = new TouchCaptor();
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX = event.getX();
                    touchY = event.getY();

                    Log.d("GameView", "Toucher détecté à (x=" + touchX + ", y=" + touchY + ")");

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
        for (Monster monster : monsters) {
            drawSprites(canvas, monster);
        }
        drawScore(canvas); // Ajout du score

        if (gameOver) {
            goToDefeatActivity();
        }
    }

    private void drawSprites(Canvas canvas, Monster monster) {
        Bitmap spriteRaw;
        if (monster instanceof Ghost) {
            spriteRaw = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
        }
        else if (monster instanceof Bloc) {
            spriteRaw = BitmapFactory.decodeResource(getResources(), R.drawable.barrel);
        }
        else {
            spriteRaw = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        }
        Bitmap sprite = Bitmap.createScaledBitmap(spriteRaw, 100, 100, true);
        canvas.drawBitmap(sprite, monster.getX(), getHeight() - monster.getY() - monster.getHeigth(), null);
    }

    private void drawScore(Canvas canvas) {
        Paint scorePaint = new Paint();
        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(50);
        canvas.drawText("Score: " + score, getWidth() - 250, 50, scorePaint);
    }

    private void goToDefeatActivity() {
        Intent intent = new Intent(context, DefeatActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("difficulty", difficulty);
        context.startActivity(intent);
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
        Matrix matrix = new Matrix();
        matrix.setTranslate(grassScrollX, 0);
        shader.setLocalMatrix(matrix);

        Paint paint = new Paint();
        paint.setShader(shader);

        canvas.drawRect(0, getHeight() - GRASS_HEIGHT, getWidth(), getHeight(), paint);
        grassScrollX -= 10;

    }

    public void update() {

        if (monsters.isEmpty()) {
            createMonster();
        } else if (monsters.size() == 1) {
            Monster existingMonster = monsters.get(0);
            if (existingMonster.getX() <= 500) {
                createMonster();
            }
        }

        for (Monster monster : monsters) {
            moveMonster(monster);
        }

        manageJump();
        manageLight();
        manageTouch();
        manageCollisions();
    }

    private void createMonster() {
        double random = Math.random();
        if (random < 0.01) {
            monsters.add(new Bloc(getWidth(), GRASS_HEIGHT));
        } else if (random < 0.02) {
            monsters.add(new Ghost(getWidth(), GRASS_HEIGHT));
        } else if (random < 0.03) {
            monsters.add(new Fence(getWidth(), GRASS_HEIGHT));
        }
    }

    private void moveMonster(Monster monster) {
        monster.setX(monster.getX() - 10);
        if (monster.getX() + monster.getWidth() <= 0) {
            killMonster(monster);
        }
    }

    private void killMonster(Monster monster) {
        score = score + 10;
        monsters.remove(monster);
    }

    private void manageCollisions() {
        for (Monster monster : monsters) {
            if (isCollided(monster)) {
                gameOver = true;
                thread.setRunning(false);
            }
        }
    }

    private boolean isCollided(Monster monster) {

        int princessLeft = princess.getX();
        int princessRight = princess.getX() + princess.getWidth();
        int princessTop = getHeight() - (princess.getHeigth() + princess.getY() + princess.getJumpHeight());
        int princessBottom = getHeight() - (princess.getY() + princess.getJumpHeight());

        int monsterLeft = monster.getX();
        int monsterRight = monster.getX() + monster.getWidth();
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
            for (Monster monster : monsters) {
                if (monster instanceof Ghost) {
                    killMonster(monster);
                }
            }
        }
    }

    private void manageTouch() {
        if (touchX != -1 && touchY != -1) {
            isMonsterTouched(touchX, touchY);
            touchX = -1;
            touchY = -1;
        }

        if (touchCaptor.isTouchDetected()) {
            touchCaptor.setTouchDetected(false);
            Log.d("GameView", "Toucher détecté !");
            for (Monster monster : monsters) {
                if (monster instanceof Fence) {
                    killMonster(monster);
                }
            }
        }
    }

    private void isMonsterTouched(float touchX, float touchY) {
        for (Monster monster : monsters) {
            if (monster instanceof Fence) {
                int monsterLeft = monster.getX() - 20;
                int monsterRight = (monsterLeft + monster.getWidth()) + 20;
                int monsterTop = getHeight() - (GRASS_HEIGHT + monster.getHeigth());
                int monsterBottom = getHeight() - GRASS_HEIGHT;


                if (touchX >= monsterLeft && touchX <= monsterRight && touchY >= monsterTop && touchY <= monsterBottom) {
                    Log.d("GameView", "Toucher sur le monstre !");
                    killMonster(monster);
                    break;
                }
            }


        }
    }
}