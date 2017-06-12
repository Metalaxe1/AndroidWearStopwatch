package com.metalaxe.mystopwatch;

import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.widget.TextView;

import java.security.Timestamp;

/**
 * Created by Anthony Ratliff on 6/11/2017.
 */

public class Clock {
    private TextView timeDisplay;
    private final Handler timerHandler;
    private final Runnable timerRunnable;

    public Clock(final TextView timeDisplay){
        this.timeDisplay = timeDisplay;
        this.timerHandler = new Handler();
        this.timerRunnable = new Runnable() {

            @Override
            public void run() {
                String timeStamp = new SimpleDateFormat("hh.mm.ss a").format(new java.util.Date());
                timeDisplay.setText(timeStamp);
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);
    }
}
