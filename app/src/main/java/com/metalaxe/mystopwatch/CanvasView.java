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

public class CanvasView extends View {

    private Paint mPaint;
    private double x = 0;
    private double y = 0;
    private double radius = 8;
    private int centerX, centerY;
    private Handler handler;
    private Context context;
    private AtomicBoolean running;
    private boolean firstStart;
    private StopWatch stopWatch;
    private int spinnerColor, borderColor;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        handler = new Handler();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        stopWatch = null;
        spinnerColor = Color.RED;
        borderColor = Color.WHITE;
        running = new AtomicBoolean(false);
        firstStart = true;
        invalidate();
    }

    private Runnable r = new Runnable() {

        @Override

        public void run() {
            long i = stopWatch.getTimerMilliseconds()% 1000;
            double radians = (Math.toRadians((double)i*.36)) - (Math.PI/2);
            x = ((centerX - 8) * Math.cos(radians) + centerX);
            y = ((centerX - 8) * Math.sin(radians) + centerY);
            invalidate();
        }

    };

    // override onDraw

    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (firstStart){
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
            x = ((centerX - 8) * Math.cos(0 - Math.PI/2) + centerX);
            y = ((centerX - 8) * Math.sin(0 - Math.PI/2) + centerY);
            firstStart = false;
        }
            // Outer circle to be removed later.
            Paint outer_circle = new Paint();
            outer_circle.setAntiAlias(true);
            outer_circle.setColor(borderColor);
            outer_circle.setStyle(Paint.Style.STROKE);
            outer_circle.setStrokeJoin(Paint.Join.ROUND);
            outer_circle.setStrokeWidth(6f);
            canvas.drawCircle((int)(centerX), (int)(centerY), (float) centerX - 8, outer_circle);
            // Create and draw the spinner circle.
            Paint spinner_circle = new Paint();
            spinner_circle.setAntiAlias(true);
            spinner_circle.setColor(spinnerColor);
            spinner_circle.setStyle(Paint.Style.FILL);
            spinner_circle.setStrokeJoin(Paint.Join.ROUND);
            canvas.drawCircle((float) x, (float) y, (float) radius, spinner_circle);
        // If the stopwatch is running, then continue to redraw with updates.
        if (running.get()) {
            handler.post(r);
        }
    }

    // Toggle animation on/off
    public void animation(){
        running.set(!running.get());
        invalidate();
    }

    public void setStopWatch(StopWatch watch){
        this.stopWatch = watch;
    }

    public void resetSpinner(){
        this.firstStart = true;
        invalidate();
    }

    public void setSpinnerColor(int color){
        spinnerColor = color;
        invalidate();
    }
    public void showBorderCircle(boolean show){
        if (show){
            borderColor = Color.WHITE;
        } else {
            borderColor = Color.BLACK;
        }
        invalidate();
    }

}
