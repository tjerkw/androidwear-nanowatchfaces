package  com.tjerkw.wear.nanoclockwork;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class ClockworkView extends AbstractClockworkView {
    private static Paint BACKGROUND_PAINT = new Paint();
    private static Paint TICK_PAINT = new Paint();
    private static Paint TICK_SECONDS_PAINT = new Paint();
    private static Paint HOUR_PAINT = new Paint();
    private static Paint MINUTE_PAINT = new Paint();
    private static Paint SECOND_PAINT = new Paint();
    private static Paint HOUR_TICK_PAINT = new Paint();
    private static Paint INNER_CIRCLE_PAINT = new Paint();
    private static Paint CIRCLE_BORDER_PAINT = new Paint();
    private static Paint TEXT_PAINT = new Paint();

    private static int PADDING = 30;
    private static int TICK_LENGTH = 7;
    private static int HOUR_TICK_LENGTH = 10;



    private static float MINUTE_LENGTH = 8/10f;
    private static float SECOND_LENGTH = 8/10f;
    private static float HOUR_LENGTH = 6/10f;

    static {
        BACKGROUND_PAINT.setARGB(255, 12, 12, 12);

        INNER_CIRCLE_PAINT.setColor(Color.BLACK);
        INNER_CIRCLE_PAINT.setAntiAlias(true);
        INNER_CIRCLE_PAINT.setAlpha(100);
        TICK_SECONDS_PAINT.setShadowLayer(8f, 1f, 1f, 0xFF303030);

        TICK_PAINT.setARGB(255, 100, 100, 100);
        TICK_PAINT.setAntiAlias(true);
        TICK_PAINT.setStrokeWidth(3f);
        TICK_PAINT.setDither(true);
        //TICK_PAINT.setStrokeCap(Paint.Cap.ROUND);
        //TICK_PAINT.setPathEffect(new CornerPathEffect(0f));
        TICK_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);


        TICK_SECONDS_PAINT.setARGB(255, 50, 80, 200);
        TICK_SECONDS_PAINT.setAntiAlias(true);
        TICK_SECONDS_PAINT.setStrokeWidth(8f);
        //TICK_SECONDS_PAINT.setStrokeCap(Paint.Cap.ROUND);
        //TICK_SECONDS_PAINT.setPathEffect(new CornerPathEffect(4f));
        TICK_SECONDS_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);

        HOUR_PAINT.setARGB(255, 50, 80, 200);
        HOUR_PAINT.setAntiAlias(true);
        HOUR_PAINT.setStrokeWidth(7f);
        HOUR_PAINT.setStrokeCap(Paint.Cap.ROUND);
        //HOUR_PAINT.setPathEffect(new CornerPathEffect(2f));
        HOUR_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        HOUR_PAINT.setShadowLayer(10f, 2f, 2f, Color.WHITE);

        MINUTE_PAINT.setARGB(255, 255, 255, 255);
        MINUTE_PAINT.setAntiAlias(true);
        MINUTE_PAINT.setStrokeWidth(3f);
        MINUTE_PAINT.setDither(true);
        MINUTE_PAINT.setStrokeCap(Paint.Cap.ROUND);
        //MINUTE_PAINT.setPathEffect(new CornerPathEffect(1f));
        MINUTE_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        MINUTE_PAINT.setShadowLayer(20f, 4f, 4f, Color.WHITE);


        SECOND_PAINT.setARGB(255, 255, 255, 255);
        SECOND_PAINT.setAntiAlias(true);
        SECOND_PAINT.setStrokeWidth(2f);
        SECOND_PAINT.setDither(true);
        SECOND_PAINT.setStrokeCap(Paint.Cap.ROUND);
        //MINUTE_PAINT.setPathEffect(new CornerPathEffect(1f));
        SECOND_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);

        HOUR_TICK_PAINT.setARGB(255, 50, 80, 200);
        HOUR_TICK_PAINT.setAntiAlias(true);
        HOUR_TICK_PAINT.setStrokeWidth(4f);
        HOUR_TICK_PAINT.setDither(true);
        //HOUR_TICK_PAINT.setStrokeCap(Paint.Cap.ROUND);
        HOUR_TICK_PAINT.setPathEffect(new CornerPathEffect(4f));

        TEXT_PAINT.setARGB(100, 255, 255, 255);
        TEXT_PAINT.setAntiAlias(true);
        TEXT_PAINT.setDither(true);
        TEXT_PAINT.setTextSize(18f);
        TEXT_PAINT.setTextAlign(Paint.Align.CENTER);
        TEXT_PAINT.setFakeBoldText(true);
        TEXT_PAINT.setShadowLayer(3f, 0f, 0f, 0xFF101010);

        CIRCLE_BORDER_PAINT.setARGB(255, 50, 50, 50);
        CIRCLE_BORDER_PAINT.setStyle(Paint.Style.STROKE);
        CIRCLE_BORDER_PAINT.setAntiAlias(true);
        CIRCLE_BORDER_PAINT.setStrokeWidth(2f);
    }

    private DateFormat dateFormat = new SimpleDateFormat("c\ndd MMM");
    private int minTickColor = 50;
    // the time at which the clock becomes visible
    private long visibleAnimationDuration = 400;
    private ClockCanvas clockCanvas = new ClockCanvas();

    public ClockworkView(Context context) {
        super(context);
    }

    public ClockworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void loadFont() {
        try {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/prototype.ttf");
            TEXT_PAINT.setTypeface(tf);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDraw(Canvas c) {
        //log("Drawing");

        Calendar cal = Calendar.getInstance();

        clockCanvas.c = c;
        clockCanvas.cal = cal;

        float f = (System.currentTimeMillis() - visibleTime) / (float)visibleAnimationDuration;
        if (f > 1) {
            f = 1f;
        }

        c.drawRect(0f, 0f, getWidth(), getHeight(), BACKGROUND_PAINT);

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
                        f * (float) (sin * (radius - HOUR_TICK_LENGTH)),
                        f * -(float) (cos * (radius - HOUR_TICK_LENGTH)),
                        f * (float) (sin * radius),
                        f * -(float) (cos * radius),
                        HOUR_TICK_PAINT);
            }

            r = (second == min) ? radius * 3 : radius;


            TICK_PAINT.setARGB(255, minTickColor, minTickColor, minTickColor);

            int tailLength = 10;
            int diff = ((millis%1000) * 60 / 1000) - min;
            if (diff < 0) {
                diff += 60;
            }
            if (diff >0 && diff <= tailLength) {
                int color = minTickColor + (int) (-diff / (float)tailLength * (255 - minTickColor));
                TICK_PAINT.setARGB(255, color, color, color);
            }

            c.drawLine(
                f * (float) (sin * (radius - TICK_LENGTH)),
                f * -(float) (cos * (radius - TICK_LENGTH)),
                f * (float) (sin * r),
                f * -(float) (cos * r),
                second == min ? TICK_SECONDS_PAINT : TICK_PAINT
            );

        }

        int tailSize = 20;

        clockCanvas.drawHand(hour, 12, (int)(f*radius*HOUR_LENGTH), 0, HOUR_PAINT);
        clockCanvas.drawHand(minute, 60, (int)(f*radius*MINUTE_LENGTH), tailSize, HOUR_PAINT);
        clockCanvas.drawHand(second, 60, (int)(f*radius*SECOND_LENGTH), tailSize, SECOND_PAINT);

        c.drawCircle(0f, 0f, 10, SECOND_PAINT);
        c.drawCircle(0f, 0f, 3, HOUR_PAINT);

        c.drawCircle(0f, 0f, radius / 2.6f, INNER_CIRCLE_PAINT);
        c.drawCircle(0f, 0f, radius / 2.6f, CIRCLE_BORDER_PAINT);

        boolean drawText = false;
        if (drawText) {
            c.drawText(hour + ":" + (minute < 10 ? "0" + minute : minute), 0f, 16f, TEXT_PAINT);
        }
        c.drawText(dateFormat.format(cal.getTime()), 0f, radius / 2 + 16, TEXT_PAINT);

        c.restore();

    }
}