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

}