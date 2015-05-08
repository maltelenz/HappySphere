package com.laserfountain.happysphere;

public class TouchPoint {

    public int x, y;
    public boolean touched;

    public TouchPoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.touched = false;
    }

    public boolean isTouched() {
        return touched;
    }
}