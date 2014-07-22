package com.tjerkw.wear.nanoclockwork;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DigitalWheelsClockwork extends AbstractClockworkView {
    private Paint background = new Paint();
    private Paint wheelGradient = new Paint();
    private Paint textPaint = new Paint();
    private Paint textPaintSeconds = new Paint();
    private Paint textPaintActive = new Paint();
    private Paint textDatePaint = new Paint();
    private int textDateAlpha;

    private Matrix matrix = new Matrix();
    private Camera camera = new Camera();

    private boolean initialized = false;
    private int textSize;
    private int smallerTextSize;
    private int outerPadding = 3;
    private int textPadding = 5;
    private float dozingFactor;

    private DateFormat dateFormat = new SimpleDateFormat("EEE dd MMM");

    public DigitalWheelsClockwork(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DigitalWheelsClockwork(Context context) {
      super(context);
    }

    public DigitalWheelsClockwork(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private void initColors() {
        wheelGradient.setStyle(Paint.Style.FILL);

        background.setColor(backgroundColor);
        background.setStyle(Paint.Style.FILL);

        //textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(foregroundColor);
        textPaint.setAlpha(220);
        textPaint.setSubpixelText(true);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setShadowLayer(10f, 0f, 0f, 0xFF000000);

        textPaintActive.setColor(foregroundColor);
        textPaintActive.setAntiAlias(true);
        textPaintActive.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaintActive.setStrokeWidth(1f);
        textPaintActive.setTextAlign(Paint.Align.CENTER);
        textPaintActive.setFakeBoldText(true);
        textPaintActive.setShadowLayer(10f, 0f, 0f, 0xFF000000);

        textPaintSeconds.setColor(ternaryColor);
        textPaintSeconds.setAntiAlias(true);
        textPaintSeconds.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaintSeconds.setTextAlign(Paint.Align.CENTER);
        textPaintSeconds.setShadowLayer(10f, 0f, 0f, 0xCF000000);

        textDatePaint.setTextAlign(Paint.Align.CENTER);
        textDatePaint.setColor(secondaryColor);
        textDateAlpha = textDatePaint.getAlpha();
        textDatePaint.setSubpixelText(true);
        textDatePaint.setAntiAlias(true);
        textDatePaint.setStyle(Paint.Style.FILL);
        textDatePaint.setTextAlign(Paint.Align.CENTER);
        textDatePaint.setTextSize(16f);
        textDatePaint.setFakeBoldText(true);
        textDatePaint.setShadowLayer(5f, 0f, 0f, ternaryColor);

    }

    private void init() {

        initColors();

        if (font != null) {
            try {
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
                textPaint.setTypeface(tf);
                textPaintActive.setTypeface(tf);
                textPaintSeconds.setTypeface(tf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // initialize shader
        int[] colors = new int[] {0xFF000000, 0x00000000, 0xFF000000};
        float[] positions = new float[] {0f, 0.5f, 1f};
        wheelGradient.setShader(new LinearGradient(0f, 0f, 0f, (float) getHeight(), colors, positions, Shader.TileMode.MIRROR));

        textSize = getHeight()/2;
        int textWidth = 0;
        do {
            textSize--;
            textPaint.setTextSize(textSize);
            textWidth = (int) textPaint.measureText("0000");
        } while (textWidth > getWidth() - outerPadding * 2);


        smallerTextSize = getHeight()/2;
        textWidth = 0;
        do {
            smallerTextSize--;
            textPaintSeconds.setTextSize(smallerTextSize);
            textWidth = (int) textPaintSeconds.measureText("00-0000");
        } while (textWidth > getWidth() - outerPadding * 2);

        textPaintActive.setTextSize(textSize);
        textPaintSeconds.setTextSize(smallerTextSize);

        initialized = true;
    }


    protected int getAnimationDelayMillis() {
        return isDozing ? 200 : 40;
    }

    protected void onDraw(Canvas c) {
        //log("Drawing");

        float duration = 500;


        dozingFactor = getDozingFactor();

        if (!initialized || true) {
            init();
        }
        float factorTextSize = dozeInterpolate(smallerTextSize, textSize);
        textPaint.setTextSize(factorTextSize);
        textPaintActive.setTextSize(factorTextSize);
        textPaintSeconds.setTextSize(factorTextSize);


        c.drawRect(0f, 0f, getWidth(), getHeight(), background);

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int millis = cal.get(Calendar.MILLISECOND);

        // Draw the hour
        String hourStr = intToStr(hour);
        Rect bounds = new Rect();
        textPaint.getTextBounds(hourStr, 0, hourStr.length(), bounds);
        int textHeight = bounds.height();
        int textWidth = (int) textPaint.measureText(hourStr);

        // make the hours bolder
        textPaint.setFakeBoldText(true);

        c.save();

        c.translate(
                dozeInterpolate(
                        (getWidth() / 2 - textWidth / 2 - textWidth / 2 - textPadding),
                        dozingFactor * (getWidth() / 4)
                )
                ,
                getHeight() / 2
        );

        drawWheel(c, hour, 24, 0.30f, textHeight, textPaint, textPaintActive);

        c.restore();


        textPaint.setFakeBoldText(false);


        c.save();
        c.translate(
                dozeInterpolate(
                    (getWidth() / 2),
                    3 * getWidth() / 4
                ),
                getHeight() / 2
        );

        drawWheel(c, minute, 60, second / 60f, textHeight, textPaint, textPaintActive);

        c.restore();


        if (!isDozing) {

            c.save();
            c.translate(
                    dozeInterpolate(

                        getWidth() / 2 + textWidth / 2 + textWidth / 2 + textPadding,
                        getWidth() + textWidth / 2
                    ),
                    getHeight() / 2
            );

            drawWheel(c, second, 60, millis / 1000f, textHeight, textPaintSeconds, textPaintSeconds);
            c.restore();
        }


        // draw the wheel gradient on top
        c.drawRect(0f, 0f, getWidth(), getHeight(), wheelGradient);

        textDatePaint.setAlpha((int) dozeInterpolate(textDateAlpha, 0f));
        c.drawText(dateFormat.format(cal.getTime()).toUpperCase(), getWidth()/2, getHeight() - 13, textDatePaint);


    }

    private float dozeInterpolate(float nonDozingValue, float dozingValue) {
        return (1f - dozingFactor) * nonDozingValue + dozingFactor * dozingValue;
    }

    private String intToStr(int x) {

        String val = String.valueOf(x);
        if (val.length()==1) {
            return "0" + val;
        }
        return val;
    }

    private void drawWheel(Canvas c, int val, int modulo, float completed, int textHeight, Paint paint, Paint activePaint) {

        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int numDigits = 3;
        int verticalTextPadding = 9;
        float deltaRotation = -18f;
        float deltaTranslate = (textHeight * 1.5f + verticalTextPadding);

        // make the rounded
        for (int delta=-numDigits;delta<=numDigits;delta++) {
            float deltaF = delta - completed + 0.5f;

            float rotation = deltaF * deltaRotation;

            if (Math.abs(rotation) > 45) {
                continue;
            }

            c.save();
            camera.save();

            Log.d("DigitalWheelsClock", "val: " + ((val+delta)%modulo) + ", rot: " + rotation);

            camera.rotate(rotation, 0f, 0f);
            camera.getMatrix(matrix);

            c.concat(matrix);

            c.translate(0f, deltaF * deltaTranslate);

            c.drawText(
                    intToStr((delta < 0 ? (modulo + val + delta) : (val + delta)) % modulo),
                    0f,
                    (textHeight / 2f) - fontMetricsInt.descent,
                    delta == 0 && !isDozing ? activePaint : paint
            );

            camera.restore();
            c.restore();
        }
    }
}
