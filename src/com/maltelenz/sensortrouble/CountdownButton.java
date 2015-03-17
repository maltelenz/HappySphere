package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Input.TouchEvent;

public class CountdownButton extends CircleButton {

    private int touchesLeft;

    public CountdownButton(float maxRadius, double centerX, double centerY, int maxTouches) {
        super(maxRadius, centerX, centerY);
        touchesLeft = maxTouches;
    }

    public int getTouchesLeft() {
        return touchesLeft;
    }

    public void decreaseTouchesLeft() {
        touchesLeft--;
    }

    public void increaseTouchesLeft() {
        touchesLeft++;
    }

    public boolean touch(TouchEvent event) {
        if (inBounds(event)) {
            decreaseTouchesLeft();
            return true;
        }
        return false;
    }
}
