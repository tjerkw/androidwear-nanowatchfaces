package com.tjerkw.wear.nanoclockwork;

import java.util.Random;

public class DigitalWheelsClockworkActivity extends AbstractClockworkActivity {

    protected int getLayoutResId() {
        Random r = new Random();
        return r.nextBoolean() ? R.layout.clockwork_digital_wheels : R.layout.clockwork_digital_wheels_light;
    }
}
