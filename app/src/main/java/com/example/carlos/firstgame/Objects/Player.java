package com.example.carlos.firstgame.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.Toast;

import com.example.carlos.firstgame.Animation.Animation;
import com.example.carlos.firstgame.SurfaceViews.GamePanel;

import java.util.ArrayList;

/**
 * Created by Carlos on 16/08/2015.
 */
public class Player extends GameObject{
    private Bitmap spritesheet;
    private int score;
    private boolean playing;
    private boolean up;
    private Animation animation;
    private long startTime;

    public Player(Bitmap res, int w, int h, int numFrames){
        animation = new Animation();
        x = 100;
        y = GamePanel.HEIGHT/2;
        dy = 0;
        score = 0;
        height = h;
        width = w;
        spritesheet = res;

        ArrayList<Bitmap> image = new ArrayList();
                //new Bitmap[numFrames];

        for(int i= 0; i < numFrames; i++){
            image.add(Bitmap.createBitmap(spritesheet, i*width, 0, width, height));
        }
        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    public void setUp(Boolean up){
        this.up = up;
    }

    public void update(){
        long elapsed = (System.nanoTime() - startTime)/1000000;
        //Cada 10 segundos se consigue un punto
        if(elapsed > 10){
            score ++;
            startTime = System.nanoTime();
        }
        animation.update();

        //Aceleración
        if(up){
            dy -= 1;
        }
        else{
            dy += 1;
        }

        if(dy > 7) dy = 7;
        if(dy < -7) dy = -7;
        y += dy;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public int getScore(){
        return score;
    }
    public boolean getPlaying(){
        return playing;
    }

    public void setPlaying(boolean playing){
        this.playing = playing;
    }
    public void resetDy(){ this.dy = 0;}
    public void resetScore(){
        this.score = 0;
    }
}
