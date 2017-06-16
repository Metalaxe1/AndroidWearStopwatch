package com.metalaxe.mystopwatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends WearableActivity {

    private TextView mAppName;
    private TextView mStopwatchView;
    private TextView mTime;
    private Button mStartStopButton;
    private Button mResetButton;
    private static StopWatch stopWatch;
    private Clock clock;
    private CanvasView customCanvas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setAmbientEnabled();
        mAppName = (TextView) findViewById(R.id.text);
        mTime = (TextView) findViewById(R.id.time);
        mStopwatchView = (TextView) findViewById(R.id.stopwatch_display);
        mStartStopButton = (Button) findViewById(R.id.start_stop_button);
        mResetButton = (Button) findViewById(R.id.reset_button);
        //final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        /* stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mStopwatchView = (TextView) stub.findViewById(R.id.stopwatch_display);
                mStartStopButton = (Button) stub.findViewById(R.id.start_top_button);
                mResetButton = (Button) stub.findViewById(R.id.reset_button);
            }
        }); */
        mStopwatchView = (TextView) findViewById(R.id.stopwatch_display);
        mStartStopButton = (Button) findViewById(R.id.start_stop_button);
        mResetButton = (Button) findViewById(R.id.reset_button);
        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        this.stopWatch = new StopWatch(mStopwatchView);
        customCanvas.setStopWatch(this.stopWatch);
        this.clock = new Clock(mTime);
        mStopwatchView.setTextColor(Color.RED);
        mStartStopButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (mStartStopButton.getText().toString().equals("START")){
                    mStartStopButton.setTextColor(Color.RED);
                    mStopwatchView.setTextColor(Color.YELLOW);
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
                    mStopwatchView.setTextColor(Color.RED);
                    mStartStopButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);
                    mResetButton.setVisibility(View.VISIBLE);
                    stopWatch.stop();
                }
                customCanvas.animation();
            }
        });
        mResetButton.setVisibility(View.GONE);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
                startActivityForResult(intent, 1001);

            }
        });
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        mStartStopButton.setVisibility(View.GONE);
        mResetButton.setVisibility(View.GONE);
        mStopwatchView.setTextColor(Color.WHITE);
        mAppName.setTextColor(Color.WHITE);
        mTime.setTextColor(Color.WHITE);
        customCanvas.setSpinnerColor(Color.WHITE);
        customCanvas.showBorderCircle(false);
    }

    @Override
    public void onExitAmbient() {
        mTime.setTextColor(Color.YELLOW);
        mAppName.setTextColor(Color.GREEN);
        mStartStopButton.setVisibility(View.VISIBLE);
        customCanvas.setSpinnerColor(Color.RED);
        customCanvas.showBorderCircle(true);
        if (stopWatch.isRunning()){
            mStopwatchView.setTextColor(Color.YELLOW);
        } else {
            mStopwatchView.setTextColor(Color.RED);
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


}
