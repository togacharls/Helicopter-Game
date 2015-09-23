package com.example.carlos.firstgame.Objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.carlos.firstgame.SurfaceViews.GamePanel;

/**
 * Created by Carlos on 17/08/2015.
 */
public class Border extends GameObject{
    private Bitmap imagen;

    public Border(Bitmap res, int y, int height){
        this.x = 0;
        this.y = y;
        this.width = GamePanel.WIDTH;
        this.height = height;
        this.dx = GamePanel.MOVESPEED;
        this.imagen = Bitmap.createBitmap(res, 0, 0, this.width, this.height);
    }

    public void update(){
        x += dx;
        if(x < - GamePanel.WIDTH){
            x = 0;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(imagen, x, y, null);
        if(x < 0){
            canvas.drawBitmap(imagen, x + GamePanel.WIDTH, y, null);
        }

    }
}
