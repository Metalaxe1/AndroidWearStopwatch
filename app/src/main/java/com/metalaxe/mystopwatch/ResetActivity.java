package com.metalaxe.mystopwatch;

/**
 * Created by Anthony Ratliff on 6/11/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResetActivity extends WearableActivity implements DelayedConfirmationView.DelayedConfirmationListener {
    DelayedConfirmationView delayedConfirmationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        delayedConfirmationView = (DelayedConfirmationView) findViewById(R.id.delayed_confirm);
        delayedConfirmationView.setListener(this);
        delayedConfirmationView.setTotalTimeMs(2000);
        delayedConfirmationView.start();
    }

    @Override
    public void onTimerFinished(View view) {
        displayConfirmation("Stopwatch Reset", this);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onTimerSelected(View view) {
        displayConfirmation("Cancelled Reset", this);
        delayedConfirmationView.reset();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public static void displayConfirmation(String message, Context context){
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }

}