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

public class RoundCanvasView extends View {

    private double radius = 8;
    private double currentDegrees, trailingDegrees;
    private int centerX, centerY;
    private Handler handler;
    private AtomicBoolean running;
    private boolean firstStart;
    private StopWatch stopWatch;
    private int spinnerColor, borderColor;

    public RoundCanvasView(Context c, AttributeSet attrs) {
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
        // Create and draw the outer circle.
        Paint outer_circle = new Paint();
        outer_circle.setAntiAlias(true);
        outer_circle.setColor(borderColor);
        outer_circle.setStyle(Paint.Style.STROKE);
        outer_circle.setStrokeJoin(Paint.Join.ROUND);
        outer_circle.setStrokeWidth(12f);
        canvas.drawCircle((int) (centerX), (int) (centerY), (float) centerX - 8, outer_circle);

        // Create and draw the spinner back fill.
        if (running.get()) {
            trailingDegrees = currentDegrees - 30;
            spinnerColor = Color.GREEN;
        }
        else {
            trailingDegrees = currentDegrees;
            spinnerColor = Color.RED;
        }
        Paint spinner_fill = new Paint();
        spinner_fill.setAntiAlias(true);
        spinner_fill.setColor(spinnerColor);
        spinner_fill.setStyle(Paint.Style.FILL);
        spinner_fill.setStrokeJoin(Paint.Join.ROUND);

        while (trailingDegrees <= currentDegrees) {
            double radians = (Math.toRadians(trailingDegrees));
            double xFill = ((centerX - 8) * Math.cos(radians) + centerX);
            double yFill = ((centerX - 8) * Math.sin(radians) + centerY);
            canvas.drawCircle((float) xFill, (float) yFill, (float) radius, spinner_fill);
            trailingDegrees++;
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

    public void setSpinnerColor(int color) {
        spinnerColor = color;
        invalidate();
    }

    public void showBorderCircle(boolean show) {
        if (show) {
            borderColor = Color.WHITE;
        } else {
            borderColor = Color.BLACK;
        }
        invalidate();
    }

}
