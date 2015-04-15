package com.maltelenz.sensortrouble;

import java.util.HashSet;

import android.util.Log;

import com.maltelenz.framework.Input.TouchEvent;

public class ContinuousCountdownButton extends CircleButton {

    private float touchTimeLeft;
    private float maxTouchTime;
    
    private HashSet<Integer> touching;

    public ContinuousCountdownButton(float maxRadius, double centerX, double centerY, float maxTouchTimeIn) {
        super(maxRadius, centerX, centerY);
        maxTouchTime = maxTouchTimeIn;
        touchTimeLeft = maxTouchTime;
        touching = new HashSet<Integer>();
    }

    public float getTouchTimeLeft() {
        return touchTimeLeft;
    }

    public boolean isTouched() {
        return touching.size() > 0;
    }

    public void decreaseTouchTimeLeft(float time) {
        touchTimeLeft = Math.max(touchTimeLeft - time, 0);
    }

    public void increaseTouchTimeLeft(float time) {
        touchTimeLeft = Math.min(touchTimeLeft + time, maxTouchTime);
    }

    public void touch(TouchEvent event) {
        if (event.type == TouchEvent.TOUCH_DOWN) {
            if (inBounds(event)) {
                touching.add(event.pointer);
            }
        } else if (touching.contains(event.pointer)) {
            if ((!inBounds(event)) || event.type == TouchEvent.TOUCH_UP) {
                // Moved outside or released
                touching.remove(event.pointer);
            }
        }
    }

    public void updateTouchTime(float deltaTime) {
        if (isTouched()) {
            decreaseTouchTimeLeft(deltaTime * touching.size());
        } else {
            increaseTouchTimeLeft(deltaTime);
        }
    }
}
