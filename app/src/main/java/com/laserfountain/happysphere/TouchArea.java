package com.laserfountain.happysphere;

import com.laserfountain.framework.Input.TouchEvent;

public class TouchArea {

    int x0, y0, x1, y1;

    public TouchArea(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public boolean inBounds(TouchEvent event) {
        if (event.x > x0 && event.x < x1 && event.y > y0 && event.y < y1)
            return true;
        else
            return false;
    }
}