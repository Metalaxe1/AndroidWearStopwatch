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

public class RoundCanvasView extends View {

    private double radius = 8;
    private double currentDegrees, trailingDegrees;
    private int centerX, centerY;
    private Handler handler;
    private AtomicBoolean running;
    private boolean firstStart;
    private StopWatch stopWatch;
    private int spinnerColor, borderColor;
    private boolean showOuterCircle, showSpinner;
    private Paint outer_circle, spinner_fill;

    public RoundCanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        handler = new Handler();
        stopWatch = null;
        currentDegrees = -90;
        spinnerColor = Color.GREEN;
        borderColor = Color.WHITE;
        spinner_fill = new Paint();
        outer_circle = new Paint();
        running = new AtomicBoolean(false);
        firstStart = true;
        showOuterCircle = true;
        showSpinner = true;
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

    // override onDraw

    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (firstStart) {
            // Get the size of the face area.
            Rect clip = canvas.getClipBounds();
            Paint rect_paint = new Paint();
            rect_paint.setStyle(Paint.Style.FILL);
            rect_paint.setColor(Color.BLACK);
            // Set the background to black and apply canvas to watch face.
            canvas.drawRect(0, 0, clip.width(), clip.height(), rect_paint);
            // Get the center of the canvas
            centerX = clip.centerX();
            centerY = clip.centerY();
            firstStart = false;
        }
        if (showOuterCircle) {
            // Create and draw the outer circle.
            outer_circle.setAntiAlias(true);
            outer_circle.setColor(borderColor);
            outer_circle.setStyle(Paint.Style.STROKE);
            outer_circle.setStrokeJoin(Paint.Join.ROUND);
            outer_circle.setStrokeWidth(12f);
            canvas.drawCircle(centerX, centerY, (float) (centerX - radius), outer_circle);
        }

        if (showSpinner) {
            // Create and draw the spinner back fill.
            if (running.get()) {
                trailingDegrees = currentDegrees - 30;
                spinnerColor = Color.GREEN;
            } else {
                trailingDegrees = currentDegrees;
                spinnerColor = Color.RED;
            }
            spinner_fill.setAntiAlias(true);
            spinner_fill.setColor(spinnerColor);
            spinner_fill.setStyle(Paint.Style.FILL);
            spinner_fill.setStrokeJoin(Paint.Join.ROUND);

            while (currentDegrees >= trailingDegrees) {
                double radians = (Math.toRadians(currentDegrees));
                double xFill = ((centerX - radius) * Math.cos(radians) + centerX);
                double yFill = ((centerX - radius) * Math.sin(radians) + centerY);
                canvas.drawCircle((float) xFill, (float) yFill, (float) radius, spinner_fill);
                currentDegrees--;
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
        currentDegrees = -90.0;
        invalidate();
    }

    public void showSpinner(boolean show) {
        showSpinner = show;
        invalidate();
    }

    public void showBorderCircle(boolean show) {
        showOuterCircle = show;
        invalidate();
    }

}
