package com.example.carlos.firstgame.Animation;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Carlos on 16/08/2015.
 */
public class Animation {
    private ArrayList<Bitmap> frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;

    public void setFrames(ArrayList<Bitmap> frames){
        this.frames = new ArrayList();
        for(Bitmap frame: frames){
            this.frames.add(frame);
        }
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setFrame(int f){
        currentFrame = f;
    }

    //Se actualiza el Frame que se muestra
    public void update(){
        long elapsed = (System.nanoTime() - startTime)/1000000;
        if(elapsed > delay){
            if(currentFrame < frames.size()){
                currentFrame ++;
            }
            else{
                currentFrame = 0;
                playedOnce = true;
            }
            startTime = System.nanoTime();
        }
    }

    public Bitmap getImage(){
        return frames.get(currentFrame);
    }

    public int getFrame(){
        return currentFrame;
    }

    public boolean playedOnce(){
        return playedOnce;
    }
}
