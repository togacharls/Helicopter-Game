package com.example.carlos.firstgame.Objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.carlos.firstgame.Animation.Animation;

import java.util.ArrayList;

/**
 * Created by Carlos on 17/08/2015.
 */
public class Explosion extends GameObject{

    private Animation animation;
    private Bitmap spritesheet;

    public Explosion(Bitmap res, int x, int y, int w, int h, int numframes){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.animation = new Animation();
        ArrayList<Bitmap> imagen = new ArrayList();
        spritesheet = res;

        for(int i=0; i < numframes; i++){
            imagen.add(Bitmap.createBitmap(this.spritesheet, i*this.width, 0, this.width, this.height));
        }
        this.animation.setFrames(imagen);
        this.animation.setDelay(10);
    }

    public void draw(Canvas canvas){
        if(!animation.playedOnce()){
            canvas.drawBitmap(animation.getImage(), x, y, null);
        }
    }

    public void update(){
        if(!animation.playedOnce()){
            animation.update();
        }
    }

    public int getHeight(){
        return height;
    }

}
