package com.metalaxe.mystopwatch;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Anthony Ratliff on 6/24/2017.
 */

public class SquareCanvasView extends View {

    private double radius = 8;
    private double currentDegrees, trailingDegrees;
    private int centerX, centerY;
    private Handler handler;
    private AtomicBoolean running;
    private boolean firstStart;
    private StopWatch stopWatch;
    private int spinnerColor, borderColor;

    public SquareCanvasView(Context c, AttributeSet attrs){
        super(c, attrs);
        handler = new Handler();
        stopWatch = null;
        currentDegrees = -90;
        spinnerColor = Color.GREEN;
        borderColor = Color.WHITE;
        running = new AtomicBoolean(false);
        firstStart = true;
        invalidate();
    }

    private Runnable r = new Runnable() {

        @Override

        public void run() {
            long i = ((stopWatch.getTimerMilliseconds() % 1000));
            currentDegrees = ((double) i * .36) - 90;
            invalidate();
        }

    };
}
