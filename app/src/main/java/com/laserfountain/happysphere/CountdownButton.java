package com.laserfountain.happysphere;

import com.laserfountain.framework.Input.TouchEvent;

public class CountdownButton extends CircleButton {

    private int touchesLeft;
    private float flashTimeLeft;
    private float maxFlashTime;

    public CountdownButton(float maxRadius, double centerX, double centerY, int maxTouches) {
        super(maxRadius, centerX, centerY);
        touchesLeft = maxTouches;
        flashTimeLeft = 0;
        maxFlashTime = 30;
    }

    public int getTouchesLeft() {
        return touchesLeft;
    }

    public void decreaseTouchesLeft() {
        touchesLeft--;
        flashTimeLeft = maxFlashTime;
    }

    public void increaseTouchesLeft() {
        touchesLeft++;
        flashTimeLeft = maxFlashTime;
    }

    public boolean isFlashing() {
        return flashTimeLeft > 0;
    }

    public boolean touch(TouchEvent event) {
        if (inBounds(event)) {
            decreaseTouchesLeft();
            return true;
        }
        return false;
    }

    public void decreaseFlashTime(float deltaTime) {
        flashTimeLeft -= deltaTime;
    }
}
