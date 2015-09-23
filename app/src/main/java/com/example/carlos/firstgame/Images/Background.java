package com.example.carlos.firstgame.Images;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.carlos.firstgame.SurfaceViews.GamePanel;

/**
 * Created by Carlos on 15/08/2015.
 */
public class Background {
    private Bitmap imagen;
    private int x, y;

    public Background(Bitmap res){
        imagen = res;
    }

    public void update(){
        x += GamePanel.MOVESPEED;
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
