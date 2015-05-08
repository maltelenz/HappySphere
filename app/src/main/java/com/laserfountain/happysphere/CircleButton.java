package com.laserfountain.happysphere;

import com.laserfountain.framework.Input.TouchEvent;

public class CircleButton {

    protected float maxRadius;
    protected float minRadius;
    protected double centerX;
    protected double centerY;

    public CircleButton(float minRadius, float maxRadius, double centerX, double centerY) {
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public CircleButton(float maxRadius, double centerX, double centerY) {
        this(0, maxRadius, centerX, centerY);
    }

    protected boolean inBounds(TouchEvent event) {
        double dx = centerX - event.x;
        double dy = centerY - event.y;
        double radius = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        return radius < maxRadius && radius > minRadius;
    }

    public float getMaxRadius() {
        return maxRadius;
    }

}