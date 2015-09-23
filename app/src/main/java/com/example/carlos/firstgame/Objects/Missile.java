package com.example.carlos.firstgame.Objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.carlos.firstgame.Animation.Animation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Carlos on 17/08/2015.
 */
public class Missile extends GameObject {

    private int score;
    private int speed;
    private Random random;
    private Animation animation;
    private Bitmap spritesheet;

    public Missile(Bitmap res, int x, int y, int width, int height, int score, int numFrames){
        random = new Random();
        animation = new Animation();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.score = score;
        this.speed = 7 + (int)(random.nextDouble()*this.score/30);

        if(this.speed > 40){
            this.speed = 40;
        }
        ArrayList<Bitmap> imagen =  new ArrayList();
        this.spritesheet = res;

        for(int i=0; i< numFrames; i++){
            imagen.add(Bitmap.createBitmap(this.spritesheet, 0, i*this.height, this.width, this.height));
        }

        this.animation.setFrames(imagen);
        this.animation.setDelay(100 - this.speed);
    }

    public void update(){
        x -= speed;
        animation.update();
    }

    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(animation.getImage(), x, y, null);
        }catch (Exception e){

        }
    }

    @Override
    public int getWidth(){
        //Se ajusta el ancho del misil para hacer una colisión más realista
        return width - 10;
    }
}
