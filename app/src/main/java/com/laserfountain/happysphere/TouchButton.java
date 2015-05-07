package com.laserfountain.happysphere;

import com.laserfountain.framework.Input.TouchEvent;

public class TouchButton extends CircleButton {

    private float flashTimeLeft;
    private float maxFlashTime;

    public TouchButton(float maxRadius, double centerX, double centerY) {
        super(maxRadius, centerX, centerY);
        flashTimeLeft = 0;
        maxFlashTime = 30;
    }

    public boolean isFlashing() {
        return flashTimeLeft > 0;
    }

    public boolean touch(TouchEvent event) {
        if (inBounds(event)) {
            flashTimeLeft = maxFlashTime;
            return true;
        }
        return false;
    }

    public void decreaseFlashTime(float deltaTime) {
        flashTimeLeft -= deltaTime;
    }

    public void resetFlash() {
        flashTimeLeft = 0;
    }
}
