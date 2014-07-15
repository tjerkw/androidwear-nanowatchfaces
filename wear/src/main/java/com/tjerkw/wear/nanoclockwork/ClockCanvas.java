package com.tjerkw.wear.nanoclockwork;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Calendar;

/**
 * Created by tjerkw on 08/07/14.
 */
public class ClockCanvas {
    public Canvas c;
    public Calendar cal;

    public void drawHand(int value, int maxInCircle, int frontLength, int tailLength, Paint p) {
        double radial = 2*Math.PI / (float) maxInCircle * value;
        double cos = Math.cos(radial);
        double sin = Math.sin(radial);

        c.drawLine(
                - (float) (sin*tailLength),
                (float) (cos*tailLength),
                (float) (sin*frontLength),
                - (float) (cos*frontLength), p);
    }

}
