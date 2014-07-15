package com.tjerkw.wear.nanoclockwork;

import android.support.wearable.activity.InsetActivity;
import android.util.Log;

public abstract class AbstractClockworkActivity extends InsetActivity {
    private void log(String msg) {
        Log.d("Clockwork", msg);
    }

    protected abstract int getLayoutResId();

    @Override
    public void onReadyForContent() {
        log("onReadyForContent");

        setContentView(getLayoutResId());

        /*
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                log("WatchStubView inflated: " + stub);
                // whoo
            }
        });
        */
    }
}