package  com.tjerkw.wear.nanoclockwork;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.util.Log;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

class AbstractClockworkView extends View {
    private Timer timer;
    protected long visibleTime;

    private void log(String msg) {
        Log.d("ClockworkView", msg);
    }

    public AbstractClockworkView(Context context) {
        super(context);
        log("init");
    }

    public AbstractClockworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        log("init");
    }

    public AbstractClockworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        log("init");
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        visibleTime = System.currentTimeMillis();

        if (timer!=null) {
            timer.cancel();
            timer = null;
        }

        startTimer();

        Handler handler = new Handler(Looper.getMainLooper());

        final DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(new DisplayManager.DisplayListener() {
            @Override
            public void onDisplayAdded(int displayId) {

            }

            @Override
            public void onDisplayRemoved(int displayId) {

            }

            @Override
            public void onDisplayChanged(int displayId) {
                try {
                    if (displayManager.getDisplay(displayId).getState() == Display.STATE_DOZING) {
                        updateFaceDisplay(true);
                        Log.d(TAG, "onDisplayChanged: dozing");
                    } else {
                        updateFaceDisplay(false);
                        Log.d(TAG, "onDisplayChanged: not dozing");

                    }
                } catch (NullPointerException exception) {
                }
            }
        }, handler);
    }

    protected void onDetachedFromWindow() {

        if (timer!=null) {
            timer.cancel();
            timer = null;
        }

        super.onDetachedFromWindow();
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //log("timer invalidated");
                AbstractClockworkView.this.postInvalidate();
            }
        }, 100, 100);
    }

    /**
     * Override this to get a faster animation
     * @return
     */
    protected int getAnimationDelayMillis() {
        return 1000;
    }

}