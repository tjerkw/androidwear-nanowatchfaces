package  com.tjerkw.wear.nanoclockwork;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class ClockworkView extends AbstractClockworkView {
    private Paint backgroundPaint = new Paint();
    private Paint tickPaint = new Paint();
    private Paint tickSecondsPaint = new Paint();
    private Paint hourPaint = new Paint();
    private Paint minutePaint = new Paint();
    private Paint secondPaint = new Paint();
    private Paint hourTickPaint = new Paint();
    private Paint innerCirclePaint = new Paint();
    private Paint circleBorderPaint = new Paint();
    private Paint textPaint = new Paint();

    private static int PADDING = 7;
    private static int TICK_LENGTH = 7;
    private static int HOUR_TICK_LENGTH = 10;



    private static float MINUTE_LENGTH = 8/10f;
    private static float SECOND_LENGTH = 8/10f;
    private static float HOUR_LENGTH = 6/10f;

    private void initColors() {
        backgroundPaint.setColor(backgroundColor);

        innerCirclePaint.setColor(backgroundColor);
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setAlpha(100);
        innerCirclePaint.setShadowLayer(5f, 1f, 1f, foregroundColor);

        tickPaint.setColor(foregroundColor);
        tickPaint.setAntiAlias(true);
        tickPaint.setStrokeWidth(3f);
        tickPaint.setDither(true);
        //tickPaint.setStrokeCap(Paint.Cap.ROUND);
        //tickPaint.setPathEffect(new CornerPathEffect(0f));
        tickPaint.setStyle(Paint.Style.FILL_AND_STROKE);


        tickSecondsPaint.setColor(backgroundColor);
        tickSecondsPaint.setAntiAlias(true);
        tickSecondsPaint.setStrokeWidth(8f);
        //tickSecondsPaint.setStrokeCap(Paint.Cap.ROUND);
        //tickSecondsPaint.setPathEffect(new CornerPathEffect(4f));
        tickSecondsPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        hourPaint.setColor(foregroundColor);
        hourPaint.setAntiAlias(true);
        hourPaint.setStrokeWidth(7f);
        hourPaint.setStrokeCap(Paint.Cap.ROUND);
        //hourPaint.setPathEffect(new CornerPathEffect(2f));
        hourPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        hourPaint.setShadowLayer(10f, 2f, 2f, Color.WHITE);

        minutePaint.setColor(foregroundColor);
        minutePaint.setAntiAlias(true);
        minutePaint.setStrokeWidth(3f);
        minutePaint.setDither(true);
        minutePaint.setStrokeCap(Paint.Cap.ROUND);
        //minutePaint.setPathEffect(new CornerPathEffect(1f));
        minutePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        minutePaint.setShadowLayer(20f, 4f, 4f, Color.WHITE);


        secondPaint.setColor(foregroundColor);
        secondPaint.setAntiAlias(true);
        secondPaint.setStrokeWidth(2f);
        secondPaint.setDither(true);
        secondPaint.setStrokeCap(Paint.Cap.ROUND);
        //minutePaint.setPathEffect(new CornerPathEffect(1f));
        secondPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        hourTickPaint.setColor(foregroundColor);
        hourTickPaint.setAntiAlias(true);
        hourTickPaint.setStrokeWidth(7f);
        hourTickPaint.setDither(true);

        textPaint.setColor(foregroundColor);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(18f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
        textPaint.setShadowLayer(3f, 0f, 0f, 0xFF101010);

        circleBorderPaint.setColor(foregroundColor);
        circleBorderPaint.setAlpha(100);
        circleBorderPaint.setStyle(Paint.Style.STROKE);
        circleBorderPaint.setAntiAlias(true);
        circleBorderPaint.setStrokeWidth(2f);
    }

    private DateFormat dateFormat = new SimpleDateFormat("dd MMM");
    private int minTickColor = 50;
    private ClockCanvas clockCanvas = new ClockCanvas();

    public ClockworkView(Context context) {
        super(context);
        initColors();
    }

    public ClockworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initColors();
    }

    public ClockworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initColors();
    }

    private void loadFont() {
        try {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
            textPaint.setTypeface(tf);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected int getAnimationDelayMillis() {
        return 1000 / 120;
    }

    protected void onDraw(Canvas c) {
        //log("Drawing");

        Calendar cal = Calendar.getInstance();

        clockCanvas.c = c;
        clockCanvas.cal = cal;

        c.drawRect(0f, 0f, getWidth(), getHeight(), backgroundPaint);

        int radius = this.getWidth() / 2;
        if (this.getHeight()<this.getWidth()) {
            radius = this.getHeight() / 2;
        }

        radius -= PADDING;

        float centerX = this.getWidth() / 2f;
        float centerY = this.getHeight() / 2f;

        c.save();
        c.translate(centerX, centerY);

        int millis = cal.get(Calendar.MILLISECOND);
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);

        //log(hour + ":" + minute + ":" + second);

        double radial;
        double cos;
        double sin;

        int r;

        // loop over minutes
        for(int min =0;min<60;min++) {

            radial = 2*Math.PI / 60f * min;

            cos = Math.cos(radial);
            sin = Math.sin(radial);


            if (min % 5 == 0) {
                // indicate an hour point
                c.drawLine(
                        (float) (sin * (radius - HOUR_TICK_LENGTH)),
                        -(float) (cos * (radius - HOUR_TICK_LENGTH)),
                        (float) (sin * radius),
                        -(float) (cos * radius),
                        hourTickPaint);
            }

            r = (second == min) ? radius * 3 : radius;



            if (!isDozing) {
                tickPaint.setARGB(255, minTickColor, minTickColor, minTickColor);

                int tailLength = 10;
                int diff = ((millis%1000) * 60 / 1000) - min;
                if (diff < 0) {
                    diff += 60;
                }
                if (diff >0 && diff <= tailLength) {
                    int color = minTickColor + (int) (-diff / (float)tailLength * (255 - minTickColor));
                    tickPaint.setARGB(255, color, color, color);
                }


                c.drawLine(
                        (float) (sin * (radius - TICK_LENGTH)),
                        -(float) (cos * (radius - TICK_LENGTH)),
                        (float) (sin * r),
                        -(float) (cos * r),
                        second == min ? tickSecondsPaint : tickPaint
                );
            }

        }

        int tailSize = 20;

        clockCanvas.drawHand(hour, 12, (int)(radius*HOUR_LENGTH), 0, hourPaint);
        clockCanvas.drawHand(minute, 60, (int)(radius*MINUTE_LENGTH), tailSize, hourPaint);

        if (!isDozing) {
            clockCanvas.drawHand(second, 60, (int) (radius * SECOND_LENGTH), tailSize, secondPaint);
        }

        c.drawCircle(0f, 0f, 10, secondPaint);
        c.drawCircle(0f, 0f, 3, hourPaint);

        c.restore();

        c.translate(centerX, this.getHeight()/4 * 3);

        c.drawCircle(0f, 0f, radius / 4f, innerCirclePaint);
        c.drawCircle(0f, 0f, radius / 4f, circleBorderPaint);

        boolean drawText = false;
        if (drawText) {
            c.drawText(hour + ":" + (minute < 10 ? "0" + minute : minute), 0f, 0f, textPaint);
        }
        c.drawText(dateFormat.format(cal.getTime()), 0f, 7f, textPaint);

        c.restore();

    }
}