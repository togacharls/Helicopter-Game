package com.example.carlos.firstgame.SurfaceViews;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.carlos.firstgame.Images.Background;
import com.example.carlos.firstgame.Objects.Border;
import com.example.carlos.firstgame.Objects.Explosion;
import com.example.carlos.firstgame.Objects.GameObject;
import com.example.carlos.firstgame.Objects.Missile;
import com.example.carlos.firstgame.Objects.Player;
import com.example.carlos.firstgame.Objects.Smokepuff;
import com.example.carlos.firstgame.R;
import com.example.carlos.firstgame.Threads.MainThread;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Carlos on 15/08/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread mainThread;
    private Background background;

    //Las dimensiones de la imagen que hace de fondo. Hay que ponerlas Float ya que después se utilizarán para escalar la imagen
    //y, si son Int, la división se hace sobre enteros y la escala no se realiza correctamente.
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 512;

    public static final int MOVESPEED = -5;

    private Player player;

    private ArrayList<Smokepuff> smoke;
    private long smokeStartTime;

    private ArrayList<Missile> missiles;
    private long missileStartTime;

    private Random rand;

    private Border topBorder;
    private Border bottomBorder;

    private Explosion explosion;

    private boolean newGameCreated;

    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int bestScore;


    public GamePanel(Context context) {
        super(context);
        //Se añade el Callback para captar eventos (el Callback es la propia clase puesto que implementa la interfaz)
        this.getHolder().addCallback(this);
        //Se hace el panel "enfocable" para que pueda manejar eventos
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Se inicializa la variable aleatoria "rand"
        rand = new Random();
        bestScore = 0;
        //Se inicia la hebra:
        mainThread = new MainThread(getHolder(), this);

        //Se carga la imagen que será el fondo a la clase Background
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background1));
        //Se definen las propiedades del helicóptero: Ancho, alto y número de Frames.
        //width = image.width/nFrames, height = image.height
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopterminimized), 108, 30, 4);

        smoke = new ArrayList();
        smokeStartTime = System.nanoTime();

        missiles = new ArrayList();
        missileStartTime = System.nanoTime();

        //Se crean los bordes
        topBorder = new Border(BitmapFactory.decodeResource(getResources(), R.drawable.brick), 0, 20);
        bottomBorder = new Border(BitmapFactory.decodeResource(getResources(), R.drawable.brick), HEIGHT - 20, 20);

        //Se lanza la hebra de forma segura.
        mainThread.setCorriendo(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int contador = 0;
        while(retry && contador < 100){
            contador++;
            try{
                mainThread.setCorriendo(false);
                mainThread.join();
                retry = false;
                mainThread = null;

            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    //Se definen las acciones
    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!player.getPlaying() && newGameCreated && reset){
                    player.setPlaying(true);
                    player.setUp(true);
                }

                if(player.getPlaying()){
                    player.setUp(true);
                    if(!started){
                        started = true;
                    }
                    reset = false;
                }
                return true;

            case MotionEvent.ACTION_UP:
                player.setUp(false);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(){
        if(player.getPlaying()){
            //Se actualiza el jugador:
            player.update();
            //Se actualiza el fondo:
            background.update();

            //Se actualizan los bordes:
            topBorder.update();
            bottomBorder.update();

            //Se comprueba que no choque contra ninguno de los dos bordes:
            if(collision(topBorder, player) || collision(bottomBorder, player)){
                player.setPlaying(false);
            }

            //Se añaden misiles
            long missileElapsed = (System.nanoTime() - missileStartTime)/1000000;
            if(missileElapsed > (2000 - player.getScore()/4)){
                //Todos los misiles empiezan 10 unidades más a la derecha que el final de la pantalla.
                //El primer misil siempre sale justo en la mitad de la pantalla
                if(missiles.size() == 0){
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, HEIGHT/2, 50, 400/17, player.getScore(), 17));
                    //A ojo el ancho del misil es de 50 y, como la imagen mide 400 de alto y hay 17 misiles, la altura es 400/17
                }
                else{
                    //Mas misiles
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, (int)(rand.nextDouble()*HEIGHT), 50, 400/17, player.getScore(), 17));
                }
                //Se resetea el temporizador con cada uno de los misiles.
                missileStartTime = System.nanoTime();
            }

            //Se actualiza el estado de los misiles
            for(Missile missile: missiles){
                missile.update();
                if(collision(missile, player)){
                    missiles.remove(missile);
                    player.setPlaying(false);
                    break;
                }
                //Se elimina un misil si está fuera de la pantalla.
                if(missile.getX() < -50){
                    missiles.remove(missile);
                    break;
                }
            }
            //Se añaden Smokepuff
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if(elapsed > 120){
                smoke.add(new Smokepuff(player.getX(), player.getY()+10));
                smokeStartTime = System.nanoTime();
            }
            //Se actualizan las bolas de humo
            for(Smokepuff smokepuff: smoke){
                smokepuff.update();
                //Si se han salido de la pantalla, se eliminan las bolas de humo.
                if(smokepuff.getX() < - 10){
                    smoke.remove(smokepuff);
                }
            }
        }
        else {
            player.resetDy();
            if (!reset) {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX(),
                        player.getY(), 40, 40, 8);
            }

            explosion.update();
            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed > 2500 && !newGameCreated)
            {
               newGame();
            }
        }
    }

    @Override
    public void draw(Canvas canvas){
        //Estos son los factores escala necesaria que hay que hacer para que el fondo se adapte a cualquier pantalla
        //Hay que que multiplicar WIDTH y HEIGHT pot 1.f para obtener los decimales exactos.
        final float scaleX = getWidth()/(WIDTH * 1.f);
        final float scaleY = getHeight()/(HEIGHT * 1.f);

        //Se escala la imagen antes de ser mostrada y, una vez se ha renderizado, se devuelve el canvas a su estado anterior.
        if(canvas != null){
            final int savedState = canvas.save();
            canvas.scale(scaleX, scaleY);
            background.draw(canvas);

            topBorder.draw(canvas);
            bottomBorder.draw(canvas);

            if(!dissapear) {
                player.draw(canvas);
            }

            for(Smokepuff smokepuff: smoke){
                smokepuff.draw(canvas);
            }

            for(Missile missile: missiles){
                missile.draw(canvas);
            }

            if(started)
            {
                explosion.draw(canvas);
            }
            showText(canvas);

            canvas.restoreToCount(savedState);
        }
    }

    //Determina si dos objetos colisionan o no.
    public boolean collision(GameObject a, GameObject b){
        return Rect.intersects(a.getRectangle(), b.getRectangle());
    }

    //Muestra un menú inicial
    public void newGame(){
        dissapear = false;
        missiles.clear();
        smoke.clear();
        player.resetDy();
        if(player.getScore() > bestScore){
            bestScore = player.getScore();
        }
        player.setY(HEIGHT/2);
        player.resetScore();

        newGameCreated = true;
    }

    public void showText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCIA: " + player.getScore(), 10, HEIGHT - 10, paint);
        if(player.getScore()> bestScore){
            bestScore = player.getScore();
        }
        canvas.drawText("MEJOR: " + bestScore, WIDTH - 245, HEIGHT - 10, paint);

        if(!player.getPlaying()&&newGameCreated&&reset)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("START", WIDTH/2-50, HEIGHT/2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PULSA Y MANTÉN PARA SUBIR", WIDTH/2-50, HEIGHT/2 + 20, paint1);
            canvas.drawText("NO HAGAS NADA Y BAJARÁS", WIDTH/2-50, HEIGHT/2 + 40, paint1);
        }
    }

}
