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
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.TextView;

public class DelayedConfirmationActivity extends WearableActivity implements DelayedConfirmationView.DelayedConfirmationListener {
    private DelayedConfirmationView delayedConfirmationView;
    private String completeMessage, cancelMessage;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed_confirmation);
        Intent incoming= getIntent();
        Bundle bundle = incoming.getExtras();
        title = (TextView) findViewById(R.id.delayed_confirmation_title);
        if(bundle!=null) {
            title.setText ((String)bundle.get("TITLE_MESSAGE"));
            completeMessage = (String) bundle.get("SUCCESS_MESSAGE");
            cancelMessage = (String) bundle.get("CANCEL_MESSAGE");
        }

        delayedConfirmationView = (DelayedConfirmationView) findViewById(R.id.delayed_confirm);
        delayedConfirmationView.setListener(this);
        delayedConfirmationView.setTotalTimeMs(2000);
        delayedConfirmationView.start();
    }

    @Override
    public void onTimerFinished(View view) {
        displayConfirmation(completeMessage, this);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onTimerSelected(View view) {
        displayConfirmation(cancelMessage, this);
        delayedConfirmationView.reset();
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