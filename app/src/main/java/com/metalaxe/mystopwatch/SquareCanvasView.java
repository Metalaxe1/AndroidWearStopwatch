package com.metalaxe.mystopwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private int width, height;
    private Handler handler;
    private AtomicBoolean running;
    private boolean firstStart;
    private StopWatch stopWatch;
    private int spinnerColor, borderColor;
    private boolean showOuterSquare, showSpinner;


    public SquareCanvasView(Context c, AttributeSet attrs){
        super(c, attrs);
        handler = new Handler();
        stopWatch = null;
        spinnerColor = Color.GREEN;
        borderColor = Color.WHITE;
        running = new AtomicBoolean(false);
        currentDegrees = -29.0;
        trailingDegrees = 0.0;
        showOuterSquare = true;
        showSpinner = true;
        firstStart = true;
        invalidate();
        System.out.println("current Position");
    }

    private Runnable r = new Runnable() {

        @Override

        public void run() {
            long i = ((stopWatch.getTimerMilliseconds() % 1000));
            double constant = (double) (((2*width) + (2*height)) / 1000.0);
            //currentPosition = ((double) i * constant) + (width/2.0);
            //double degrees = (i*constant) %360;
            //double degrees = ((double) i * constant) - 90;
            currentDegrees = ((double) i * .232) - 29;
            //currentDegrees = (currentDegrees +1)%230;
            System.out.println(currentDegrees);
            invalidate();
        }

    };

    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Rect clip = canvas.getClipBounds();
        if (firstStart) {
            // Get the size of the face area.
            width = clip.width();
            height = clip.height();
            //currentPosition = width/2;
            // Create paint object to set background color of canvas.
            Paint rect_paint = new Paint();
            rect_paint.setStyle(Paint.Style.FILL);
            rect_paint.setColor(Color.BLACK);
            // Set the background to black and apply canvas to watch face.
            canvas.drawRect(0, 0, width, height, rect_paint);
            // Get the center of the canvas
            centerX = clip.centerX();
            centerY = clip.centerY();
            firstStart = false;
        }
        if (showOuterSquare) {
            // Create and draw the outer circle.
            Paint outer_square = new Paint();
            outer_square.setAntiAlias(true);
            outer_square.setColor(borderColor);
            outer_square.setStyle(Paint.Style.STROKE);
            outer_square.setStrokeJoin(Paint.Join.ROUND);
            outer_square.setStrokeWidth(32f);
            canvas.drawRect(0,0,width, height, outer_square);
        }

        if (showSpinner) {
            if (running.get()) {
                trailingDegrees = currentDegrees - 30;
                spinnerColor = Color.GREEN;
            } else {
                trailingDegrees = currentDegrees;
                spinnerColor = Color.RED;
            }
            Paint spinner_fill = new Paint();
            spinner_fill.setAntiAlias(true);
            spinner_fill.setColor(spinnerColor);
            spinner_fill.setStyle(Paint.Style.FILL);
            spinner_fill.setStrokeJoin(Paint.Join.ROUND);

                //canvas.drawCircle((float) radius, (float) radius, (float) radius, spinner_fill);
            float startX = 0f;
            float startY = 0f;
            float fillX = 16f;
            float fillY = 16f;
            while (trailingDegrees <= currentDegrees) {
                double radians = (Math.toRadians(trailingDegrees));
                startX = (float) (((centerX - 8)) *((Math.cos((Math.PI/2)*Math.floor(radians))) - ((2*radians - 2*Math.floor(radians) - 1)*Math.sin((Math.PI/2)*Math.floor(radians)))) + (centerX - 8));
                startY = (float) (((centerY - 8)) *((Math.sin((Math.PI/2)*Math.floor(radians))) + ((2*radians - 2*Math.floor(radians) - 1)*Math.cos((Math.PI/2)*Math.floor(radians)))) + (centerY) - 8);
                canvas.drawRect(startX, startY, startX + 16f, startY + 16f, spinner_fill);
                trailingDegrees++;
            }
            }
        // If the stopwatch is running, then continue to redraw with updates.
        if (running.get()) {
            handler.post(r);
        }
    }

    // Toggle animation on/off
    public void animation() {
        running.set(!running.get());
        invalidate();
    }

    public void setStopWatch(StopWatch watch) {
        this.stopWatch = watch;
    }

    public void resetSpinner() {
        this.firstStart = true;
        //currentPosition = width/2.0;
        currentDegrees = -29.0;
        invalidate();
    }

    public void showSpinner(boolean show) {
        showSpinner = show;
        invalidate();
    }

    public void showBorderSquare(boolean show) {
        showOuterSquare = show;
        invalidate();
    }
}
