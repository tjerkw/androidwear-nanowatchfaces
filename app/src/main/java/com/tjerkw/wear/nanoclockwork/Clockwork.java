package com.tjerkw.wear.nanoclockwork;

import android.os.Bundle;
import android.support.wearable.activity.InsetActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class Clockwork  extends InsetActivity {
    private TextView mTextView;

    private void log(String msg) {
        Log.d("Clockwork", msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("oncCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReadyForContent() {
        log("onReadyForContent");

        setContentView(R.layout.round_clockwork);

        /*
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                log("WatchStubView inflated: " + stub.is);
                // whoo
            }
        });
        */
    }
}