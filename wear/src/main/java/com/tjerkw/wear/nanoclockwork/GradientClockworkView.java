package  com.tjerkw.wear.nanoclockwork;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import java.util.Calendar;

class GradientClockworkView extends AbstractClockworkView {
    private Paint backgroundPaint = new Paint();
    private Paint dozingBackgroundPaint = new Paint();
    private Paint hourPaint = new Paint();
    private Paint minutePaint = new Paint();
    private Paint secondPaint = new Paint();
    private Paint tickPaint = new Paint();

    private void initColors() {
        backgroundPaint.setARGB(255, 12, 12, 12);
        dozingBackgroundPaint.setARGB(255, 0, 0, 0);

        hourPaint.setColor(foregroundColor);
        hourPaint.setAntiAlias(true);
        hourPaint.setStrokeWidth(7f);
        //hourPaint.setStrokeCap(Paint.Cap.ROUND);
        //hourPaint.setPathEffect(new CornerPathEffect(2f));
        //hourPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        hourPaint.setDither(true);
        hourPaint.setShadowLayer(10f, 5f, 5f, Color.WHITE);

        minutePaint.setColor(secondaryColor);
        minutePaint.setAntiAlias(true);
        minutePaint.setStrokeWidth(3f);
        minutePaint.setDither(true);
        //minutePaint.setStrokeCap(Paint.Cap.ROUND);
        //minutePaint.setPathEffect(new CornerPathEffect(1f));
        minutePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        minutePaint.setShadowLayer(20f, 0f, 0f, Color.WHITE);


        secondPaint.setColor(secondaryColor);
        secondPaint.setAlpha(100);
        secondPaint.setAntiAlias(true);
        secondPaint.setStrokeWidth(3f);
        secondPaint.setDither(true);
        secondPaint.setStrokeCap(Paint.Cap.ROUND);
        //minutePaint.setPathEffect(new CornerPathEffect(1f));
        secondPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        tickPaint.setColor(ternaryColor);
        tickPaint.setAntiAlias(true);
        tickPaint.setStrokeWidth(4f);
        tickPaint.setDither(true);
        //tickPaint.setStrokeCap(Paint.Cap.ROUND);
        //minutePaint.setPathEffect(new CornerPathEffect(1f));
        tickPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private ClockCanvas clockCanvas = new ClockCanvas();

    public GradientClockworkView(Context context) {
        super(context);
        initColors();
    }

    public GradientClockworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initColors();
    }

    public GradientClockworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initColors();
    }


    protected void onDraw(Canvas c) {
        //log("Drawing");

        Calendar cal = Calendar.getInstance();

        clockCanvas.c = c;
        clockCanvas.cal = cal;

        int[] colors = new int[] {0xFF000000, backgroundColor};
        backgroundPaint.setShader(new RadialGradient(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, colors, null, Shader.TileMode.MIRROR));


        c.drawRect(0f, 0f, getWidth(), getHeight(), isDozing ? dozingBackgroundPaint : backgroundPaint);

        int radius = this.getWidth() / 2;
        if (this.getHeight()<this.getWidth()) {
            radius = this.getHeight() / 2;
        }

        radius -= 20;

        float centerX = this.getWidth() / 2f;
        float centerY = this.getHeight() / 2f;

        c.save();
        c.translate(centerX, centerY);

        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        int millis = cal.get(Calendar.MILLISECOND);

        clockCanvas.drawHand(minute + second / 60f, 60, radius - 27, 25, minutePaint);
        clockCanvas.drawHand(hour + minute / 60f, 12, (int)(radius/1.8f), 5, hourPaint);

        if (!isDozing) {
            clockCanvas.drawHand(second + (millis / 1000f), 60, radius - 20, 5, secondPaint);

            // draw the ticks
            c.drawLine(-3f, -radius, -3f, -radius + 6, tickPaint);
            c.drawLine(3f, -radius, 3f, -radius + 6, tickPaint);
            c.drawLine(0f, radius, 0f, radius - 6, tickPaint);


            c.drawLine(radius, 0f, radius - 6, 0f, tickPaint);
            c.drawLine(-radius, 0f, -radius + 6, 0f, tickPaint);

        }


        c.drawCircle(0f, 0f, 5, hourPaint);


        c.drawCircle(0f, 0f, 2, minutePaint);

        c.restore();

    }
}