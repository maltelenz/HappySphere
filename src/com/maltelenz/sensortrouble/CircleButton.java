package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Input.TouchEvent;

public class CircleButton {

    protected float maxRadius;
    protected float minRadius;
    protected int centerX;
    protected int centerY;

    public CircleButton(float minRadius, float maxRadius, int centerX, int centerY) {
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    protected boolean inBounds(TouchEvent event) {
        int dx = centerX - event.x;
        int dy = centerY - event.y;
        double radius = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        if (radius < maxRadius && radius > minRadius) {
            return true;
        } else {
            return false;
        }
    }

    public float getMaxRadius() {
        return maxRadius;
    }

}