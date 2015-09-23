package com.example.carlos.firstgame.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Carlos on 16/08/2015.
 */
public class Smokepuff extends GameObject {
    public int radio;
    public Smokepuff(int x, int y){
        super.x = x;
        super.y = y;
        radio = 4;
    }
    public void update(){
        x -= 10;
    }
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x - radio, y - radio, radio, paint);
        canvas.drawCircle(x-radio+2, y-radio-2, radio, paint);
        canvas.drawCircle(x-radio+4, y-radio+1, radio, paint);
    }
}
