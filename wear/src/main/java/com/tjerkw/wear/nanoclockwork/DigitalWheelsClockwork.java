package com.tjerkw.wear.nanoclockwork;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Calendar;

public class DigitalWheelsClockwork extends AbstractClockworkView {
    private static Paint BACKGROUND = new Paint();
    private static Paint WHEEL_GRADIENT = new Paint();
    private static Paint TEXT_PAINT = new Paint();

    private Matrix matrix = new Matrix();
    private Camera camera = new Camera();

    private boolean initialized = false;
    private int textSize;
    private int outerPadding = 20;
    private int textPadding = 10;


    static {
        WHEEL_GRADIENT.setStyle(Paint.Style.FILL);
        BACKGROUND.setColor(0xFF000000);

        BACKGROUND.setARGB(255, 0, 0, 0);

        BACKGROUND.setStyle(Paint.Style.FILL);
        TEXT_PAINT.setColor(0xFFFFFFFF);
        TEXT_PAINT.setSubpixelText(true);
        //TEXT_PAINT.setTextAlign(Paint.Align.CENTER);


        TEXT_PAINT.setARGB(255, 255, 255, 255);
        TEXT_PAINT.setAntiAlias(true);
        TEXT_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        TEXT_PAINT.setTextAlign(Paint.Align.CENTER);
    }

    public DigitalWheelsClockwork(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DigitalWheelsClockwork(Context context) {
        super(context);
    }

    public DigitalWheelsClockwork(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {

        // initialize shader
        int[] colors = new int[] {0xFF000000, 0x00000000, 0xFF000000};
        float[] positions = new float[] {0f, 0.5f, 1f};
        WHEEL_GRADIENT.setShader(new LinearGradient(0f, 0f, 0f, (float)getHeight(), colors, positions, Shader.TileMode.MIRROR));

        textSize = getHeight()/2;
        int textWidth = 0;
        do {
            textSize--;
            TEXT_PAINT.setTextSize(textSize);
            textWidth = (int) TEXT_PAINT.measureText("00-0000");
        } while (textWidth > getWidth() - outerPadding * 2);

    }


    protected int getAnimationDelayMillis() {
        return 50;
    }

    protected void onDraw(Canvas c) {
        //log("Drawing");

        if (!initialized) {
            init();
        }

        c.drawRect(0f, 0f, getWidth(), getHeight(), BACKGROUND);

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int millis = cal.get(Calendar.MILLISECOND);

        // Draw the hour
        String hourStr = intToStr(hour);
        Rect bounds = new Rect();
        TEXT_PAINT.getTextBounds(hourStr, 0, hourStr.length(), bounds);
        int textHeight = bounds.height();
        int textWidth = (int)TEXT_PAINT.measureText(hourStr);
        Paint.FontMetricsInt fontMetricsInt = TEXT_PAINT.getFontMetricsInt();

        // make the hours bolder
        TEXT_PAINT.setFakeBoldText(true);

        c.save();
        c.translate(
            getWidth() / 2 - textWidth/2 - textWidth/2 - textPadding,
            getHeight() / 2
        );

        drawWheel(c, hour, 24, minute / 60f, textHeight, fontMetricsInt);

        c.restore();


        TEXT_PAINT.setFakeBoldText(false);


        c.save();
        c.translate(
                getWidth() / 2,
                getHeight() / 2
        );

        drawWheel(c, minute, 60, second / 60f, textHeight, fontMetricsInt);

        c.restore();


        c.save();
        c.translate(
                getWidth() / 2 + textWidth / 2 + textWidth/2 + textPadding,
                getHeight() / 2
        );

        drawWheel(c, second, 60, millis / 1000f, textHeight, fontMetricsInt);

        c.restore();


        // draw the wheel gradient on top
        c.drawRect(0f, 0f, getWidth(), getHeight(), WHEEL_GRADIENT);

    }

    private String intToStr(int x) {

        String val = String.valueOf(x);
        if (val.length()==1) {
            return "0" + val;
        }
        return val;
    }

    private void drawWheel(Canvas c, int val, int modulo, float completed, int textHeight, Paint.FontMetricsInt fontMetricsInt) {

        int numDigits = 3;
        int verticalTextPadding = 10;
        float deltaRotation = -16f;
        float deltaTranslate = (textHeight * 1.4f + verticalTextPadding);

        // make the rounded
        for (int delta=-numDigits;delta<=numDigits;delta++) {

            c.save();
            camera.save();


            float deltaF = delta - completed;

            float rotation = deltaF * deltaRotation;

            Log.d("DigitalWheelsClock", "val: " + ((val+delta)%modulo) + ", rot: " + rotation);

            camera.rotate(rotation, 0f, 0f);
            camera.getMatrix(matrix);

            //matrix.preTranslate(-getWidth()/2, -getHeight()/2);
            //matrix.postTranslate(getWidth()/2, getHeight()/2);

            c.concat(matrix);

            c.translate(0f, deltaF * deltaTranslate);

            /*
            c.scale(1f,  1 - Math.abs(deltaF) * 0.3f);
            */
            c.drawText(
                    intToStr((val + delta) % modulo),
                    0f,
                    (textHeight / 2f) - fontMetricsInt.descent,
                    TEXT_PAINT
            );

            camera.restore();
            c.restore();
        }
    }
}
