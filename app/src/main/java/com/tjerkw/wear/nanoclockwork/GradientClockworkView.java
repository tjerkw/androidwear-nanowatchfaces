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
    private static Paint BACKGROUND_PAINT = new Paint();
    private static Paint HOUR_PAINT = new Paint();
    private static Paint MINUTE_PAINT = new Paint();
    private static Paint SECOND_PAINT = new Paint();
    private static Paint TICK_PAINT = new Paint();

    static {
        BACKGROUND_PAINT.setARGB(255, 12, 12, 12);

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


        SECOND_PAINT.setARGB(60, 0, 0, 0);
        SECOND_PAINT.setAntiAlias(true);
        SECOND_PAINT.setStrokeWidth(2f);
        SECOND_PAINT.setDither(true);
        SECOND_PAINT.setStrokeCap(Paint.Cap.ROUND);
        //MINUTE_PAINT.setPathEffect(new CornerPathEffect(1f));
        SECOND_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);

        TICK_PAINT.setARGB(60, 0, 0, 0);
        TICK_PAINT.setAntiAlias(true);
        TICK_PAINT.setStrokeWidth(4f);
        TICK_PAINT.setDither(true);
        TICK_PAINT.setStrokeCap(Paint.Cap.ROUND);
        //MINUTE_PAINT.setPathEffect(new CornerPathEffect(1f));
        TICK_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private ClockCanvas clockCanvas = new ClockCanvas();

    public GradientClockworkView(Context context) {
        super(context);
    }

    public GradientClockworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientClockworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected void onDraw(Canvas c) {
        //log("Drawing");

        Calendar cal = Calendar.getInstance();

        clockCanvas.c = c;
        clockCanvas.cal = cal;

        int[] colors = new int[] {0xFF000000, 0xFF0000AA};
        BACKGROUND_PAINT.setShader(new RadialGradient(getWidth()/2f, getHeight()/2f, getWidth()/2f, colors, null, Shader.TileMode.MIRROR));


        c.drawRect(0f, 0f, getWidth(), getHeight(), BACKGROUND_PAINT);

        int radius = this.getWidth() / 2;
        if (this.getHeight()<this.getWidth()) {
            radius = this.getHeight() / 2;
        }

        radius -= 20;

        float centerX = this.getWidth() / 2f;
        float centerY = this.getHeight() / 2f;

        c.save();
        c.translate(centerX, centerY);

        clockCanvas.drawHand(cal.get(Calendar.MINUTE), 60, radius - 30, 25, MINUTE_PAINT);
        clockCanvas.drawHand(cal.get(Calendar.HOUR), 12, radius/3, 5, HOUR_PAINT);
        clockCanvas.drawHand(cal.get(Calendar.SECOND), 60, radius/3, 5, SECOND_PAINT);

        c.drawLine(-3f, -radius, -3f, -radius + 6, TICK_PAINT);
        c.drawLine(3f, -radius, 3f, -radius + 6, TICK_PAINT);

        c.drawLine(0f, radius, 0f, radius - 6, TICK_PAINT);


        c.drawLine(radius, 0f, radius - 6, 0f, TICK_PAINT);
        c.drawLine(-radius, 0f, -radius + 6, 0f, TICK_PAINT);


        c.drawCircle(0f, 0f, 3, HOUR_PAINT);

        c.restore();

    }
}