package com.example.carlos.firstgame.Threads;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.example.carlos.firstgame.SurfaceViews.GamePanel;

/**
 * Created by Carlos on 15/08/2015.
 */
public class MainThread extends Thread {
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean corriendo;

    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    //Se sobrescribe el método run de Thread
    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        //targetTime define cada cuánto tiempo se ha de mostrar un nuevo Frame.
        long targetTime = 1000/FPS;

        while(corriendo) {
            startTime = System.nanoTime();
            canvas = null;

            //Se bloquea el canvas para la edición de píxeles
            try {
                canvas = this.surfaceHolder.lockCanvas();

                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {

            }
            //Y finalmente se desbloquea
            finally {
                if(canvas != null){
                    try{
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 100000;
            waitTime = targetTime - timeMillis;

            try{
                this.sleep(waitTime);
            }catch (Exception e){

            }

            totalTime += System.nanoTime()-startTime;
            frameCount++;

            if(frameCount == FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println(averageFPS);
            }
        }
    }

    public int getFPS() {
        return FPS;
    }

    public void setFPS(int FPS) {
        this.FPS = FPS;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void setAverageFPS(double averageFPS) {
        this.averageFPS = averageFPS;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean isCorriendo() {
        return corriendo;
    }

    public void setCorriendo(boolean corriendo) {
        this.corriendo = corriendo;
    }

    public static Canvas getCanvas() {
        return canvas;
    }

    public static void setCanvas(Canvas canvas) {
        MainThread.canvas = canvas;
    }
}
