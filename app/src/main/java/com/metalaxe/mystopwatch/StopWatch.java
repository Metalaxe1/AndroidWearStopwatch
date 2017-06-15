package com.metalaxe.mystopwatch;

import android.os.Handler;
import android.widget.TextView;

public class StopWatch{
    // Private class fields
    private final TextView timerDisplay;
    private long timer;
    private long offset;
    private boolean started;
    final Handler timerHandler;
    final Runnable timerRunnable;
    
    // Class constructor
    public StopWatch(TextView temp){
        this.timerDisplay = temp;
        this.timer = 0;
        this.offset = 0;
        this.started = false;

        this.timerHandler = new Handler();
        this.timerRunnable = new Runnable() {

            @Override
            public void run() {
                timer = System.currentTimeMillis() - offset;
                int millis = (int)(timer%100);
                int secs = (int)((timer/1000)%60);
                int mins = (int)(((timer/1000)/60)%60);
                int hours = (int)((timer/1000)/3600);
                timerDisplay.setText(String.format("%1$02d:%2$02d:%3$02d.%4$02d", hours, mins, secs, millis));
                timerHandler.postDelayed(this, 10);
            }
        };
    }
    
    // Private methods
    
    // Public methods
    public boolean start(){
        if (started) return false;
        this.offset = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 10);
        started = true;
        return true;
        }
    
    public boolean resume(){
        if (started || this.getTimerMilliseconds() == 0) return false;
        this.offset = System.currentTimeMillis() - this.timer;
        timerHandler.postDelayed(timerRunnable, 10);
        started = true;
        return true;
    }
    
    public boolean stop(){
        if (!started) return false;
        timerHandler.removeCallbacks(timerRunnable);
        started = false;
        return true;
    }
    
    public long getTimerMilliseconds(){
        return this.timer;
    }
    
    public boolean reset(){
        if (started || this.getTimerMilliseconds() == 0) return false;
        this.timer = 0;
        this.offset = 0;
        this.started = false;
        this.timerDisplay.setText("00:00:00.00");
        return true;
    }

    public boolean isRunning(){
        return this.started;
    }
}