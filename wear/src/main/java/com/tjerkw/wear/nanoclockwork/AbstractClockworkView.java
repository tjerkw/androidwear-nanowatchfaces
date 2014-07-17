package  com.tjerkw.wear.nanoclockwork;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.util.Log;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

class AbstractClockworkView extends View {
    private static final String NAMESPACE = "http://tjerkw.com/wear";
    private Timer timer;
    protected long visibleTime;
    protected boolean isDozing;
    protected long startDozingTime;
    protected long stopDozingTime;

    protected int backgroundColor = 0xFF000000;
    protected int foregroundColor = 0xFFFFFFFF;
    protected String font = null;

    private void log(String msg) {
        Log.d(this.getClass().getSimpleName(), msg);
    }

    public AbstractClockworkView(Context context) {
        super(context);
    }

    public AbstractClockworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AbstractClockworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        foregroundColor = attrs.getAttributeIntValue(NAMESPACE, "foreground_color", foregroundColor);
        backgroundColor = attrs.getAttributeIntValue(NAMESPACE, "background_color", backgroundColor);
        font = attrs.getAttributeValue(NAMESPACE, "font");
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

        final DisplayManager displayManager = (DisplayManager) this.getContext().getSystemService(Context.DISPLAY_SERVICE);
        try {
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

                            if (!isDozing) {
                                startDozingTime = System.currentTimeMillis();
                                isDozing = true;
                            }
                            AbstractClockworkView.this.invalidate();
                            log("onDisplayChanged: dozing");
                        } else {

                            if (isDozing) {
                                stopDozingTime = System.currentTimeMillis();
                                isDozing = false;
                            }
                            AbstractClockworkView.this.invalidate();
                            log("onDisplayChanged: not dozing");

                        }
                    } catch (NullPointerException exception) {
                    }
                }
            }, handler);
        } catch(Exception e) {
            // this fails in preview mode
        }
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


    private float interpolateAccelerateDecelerate(float input) {
        return (float)(Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
    }

    /**
     * 0f means fully fozing
     * 1f means fully active
     * it will animate between these values when dozing changes
     * @return
     */
    protected float getDozingFactor() {
        float duration = 450;

        float dozingFactor = isDozing ?
                (System.currentTimeMillis() - startDozingTime) / duration
                :
                1f - (System.currentTimeMillis() - stopDozingTime) / duration;

        if (dozingFactor > 1f) {
            dozingFactor = 1f;
        }
        if (dozingFactor < 0f) {
            dozingFactor = 0f;
        }

        return interpolateAccelerateDecelerate(dozingFactor);
    }

    /**
     * Override this to get a faster animation
     * @return
     */
    protected int getAnimationDelayMillis() {
        return 1000;
    }

}