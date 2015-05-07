package com.laserfountain.sensortrouble;

import com.laserfountain.framework.Input.TouchEvent;

public class TouchPoint {

    public int x, y;
    public boolean touched;

    public TouchPoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.touched = false;
    }

    public double distance(TouchEvent event) {
        int dx = Math.abs(event.x - this.x);
        int dy = Math.abs(event.y - this.y);
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
    
    public boolean isTouched() {
        return touched;
    }
}