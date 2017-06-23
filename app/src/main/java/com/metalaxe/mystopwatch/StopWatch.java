package com.metalaxe.mystopwatch;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class StopWatch{
    // Private class fields
    private final TextView hourDisplay;
    private final TextView minuteDisplay;
    private final TextView secondDisplay;
    private final TextView milliDisplay;
    private long timer;
    private long offset;
    private boolean started;
    private final Handler timerHandler;
    private final Runnable timerRunnable;
    
    // Class constructor
    public StopWatch(TextView hour, TextView min, TextView secs, TextView millis){
        this.hourDisplay = hour;
        this.minuteDisplay = min;
        this.secondDisplay = secs;
        this.milliDisplay = millis;
        this.timer = 0;
        this.offset = 0;
        this.started = false;

        this.timerHandler = new Handler();
        this.timerRunnable = new Runnable() {

            @Override
            public void run() {
                timer = System.currentTimeMillis() - offset;
                int millis = (int)(timer%1000)/10;
                int secs = (int)((timer/1000)%60);
                int mins = (int)(((timer/1000)/60)%60);
                int hours = (int)((timer/1000)/3600);
                if (hours == 0) {
                    hourDisplay.setVisibility(View.GONE);
                } else hourDisplay.setVisibility(View.VISIBLE);
                if (mins == 0) {
                    minuteDisplay.setVisibility(View.GONE);
                } else minuteDisplay.setVisibility(View.VISIBLE);
                hourDisplay.setText(String.format("%1$02d:", hours));
                minuteDisplay.setText(String.format("%1$02d:", mins));
                secondDisplay.setText(String.format("%1$02d.", secs));
                milliDisplay.setText(String.format("%1$02d", millis));
                timerHandler.post(this);
            }
        };
    }
    
    // Private methods
    
    // Public methods
    public boolean start(){
        if (started) return false;
        this.offset = System.currentTimeMillis();
        timerHandler.post(timerRunnable);
        started = true;
        return true;
        }
    
    public boolean resume(){
        if (started || this.getTimerMilliseconds() == 0) return false;
        this.offset = System.currentTimeMillis() - this.timer;
        timerHandler.post(timerRunnable);
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
        hourDisplay.setText(String.format("00:"));
        hourDisplay.setVisibility(View.GONE);
        minuteDisplay.setText(String.format("00:"));
        minuteDisplay.setVisibility(View.GONE);
        secondDisplay.setText(String.format("00."));
        milliDisplay.setText(String.format("00"));
        return true;
    }

    public boolean isRunning(){
        return this.started;
    }
}