package com.metalaxe.mystopwatch;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.WearableActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mAppName;
    private TextView mHourView;
    private TextView mMinuteView;
    private TextView mSecondView;
    private TextView mMilliView;
    private TextView mTime;
    private Button mStartStopButton;
    private Button mResetButton;
    private StopWatch stopWatch;
    private Clock clock;
    private View backgroundView;
    private CanvasView customCanvas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setAmbientEnabled();
        mAppName = (TextView) findViewById(R.id.text);
        mTime = (TextView) findViewById(R.id.time);
        mHourView = (TextView) findViewById(R.id.stopwatch_display_hours);
        mMinuteView = (TextView) findViewById(R.id.stopwatch_display_minutes);
        mSecondView = (TextView) findViewById(R.id.stopwatch_display_seconds);
        mMilliView = (TextView) findViewById(R.id.stopwatch_display_millis);
        mStartStopButton = (Button) findViewById(R.id.start_stop_button);
        mResetButton = (Button) findViewById(R.id.reset_button);
        backgroundView = (View) this.findViewById(android.R.id.content);
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getY() < v.getHeight()*.5) {
                    mStartStopButton.performClick();
                } else if (mResetButton.getVisibility() == View.VISIBLE && event.getAction() == MotionEvent.ACTION_DOWN && event.getY() > v.getHeight()*.5){
                    mResetButton.performClick();
                }
                return false;
            }

        });
        //final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        /* stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mStopwatchView = (TextView) stub.findViewById(R.id.stopwatch_display);
                mStartStopButton = (Button) stub.findViewById(R.id.start_top_button);
                mResetButton = (Button) stub.findViewById(R.id.reset_button);
            }
        }); */
        mStartStopButton = (Button) findViewById(R.id.start_stop_button);
        mResetButton = (Button) findViewById(R.id.reset_button);
        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        this.stopWatch = new StopWatch(mHourView, mMinuteView, mSecondView, mMilliView);
        customCanvas.setStopWatch(this.stopWatch);
        this.clock = new Clock(mTime);
        mResetButton.setVisibility(View.GONE);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        mStartStopButton.setVisibility(View.GONE);
        mResetButton.setVisibility(View.GONE);
        mHourView.setTextColor(Color.WHITE);
        mMinuteView.setTextColor(Color.WHITE);
        mSecondView.setTextColor(Color.WHITE);
        mMilliView.setTextColor(Color.WHITE);
        mAppName.setTextColor(Color.WHITE);
        mTime.setTextColor(Color.WHITE);
        customCanvas.setSpinnerColor(Color.WHITE);
        customCanvas.showBorderCircle(false);
    }

    @Override
    public void onExitAmbient() {
        mTime.setTextColor(Color.WHITE);
        mAppName.setTextColor(Color.YELLOW);
        mStartStopButton.setVisibility(View.VISIBLE);
        customCanvas.setSpinnerColor(Color.RED);
        customCanvas.showBorderCircle(true);
        if (stopWatch.isRunning()){
            mHourView.setTextColor(Color.GREEN);
            mMinuteView.setTextColor(Color.GREEN);
            mSecondView.setTextColor(Color.GREEN);
            mMilliView.setTextColor(Color.GREEN);
        } else {
            mHourView.setTextColor(Color.RED);
            mMinuteView.setTextColor(Color.RED);
            mSecondView.setTextColor(Color.RED);
            mMilliView.setTextColor(Color.RED);
            if (stopWatch.getTimerMilliseconds() != 0){
                mResetButton.setVisibility(View.VISIBLE);
            }
        }
        super.onExitAmbient();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK){
            stopWatch.reset();
            customCanvas.resetSpinner();
            mResetButton.setVisibility(View.GONE);
        }
    }

    public void toggleStartStop(View view){
        if (mStartStopButton.getText().toString().equals("START")){
            mStartStopButton.setTextColor(Color.RED);
            mHourView.setTextColor(Color.GREEN);
            mMinuteView.setTextColor(Color.GREEN);
            mSecondView.setTextColor(Color.GREEN);
            mMilliView.setTextColor(Color.GREEN);
            mStartStopButton.setText("STOP ");

            mStartStopButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_pause, 0, 0, 0);
            mResetButton.setVisibility(View.GONE);
            if (stopWatch.getTimerMilliseconds() == 0){
                stopWatch.start();
            } else {
                stopWatch.resume();
            }
        } else {
            mStartStopButton.setText("START");
            mStartStopButton.setTextColor(Color.GREEN);
            mHourView.setTextColor(Color.RED);
            mMinuteView.setTextColor(Color.RED);
            mSecondView.setTextColor(Color.RED);
            mMilliView.setTextColor(Color.RED);
            mStartStopButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);
            mResetButton.setVisibility(View.VISIBLE);
            stopWatch.stop();
        }
        customCanvas.animation();
    }

    public void resetStopwatch(View v){
        Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
        startActivityForResult(intent, 1001);
    }
}
