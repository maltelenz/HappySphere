package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Input.TouchEvent;

public class GridArea {

    protected enum Shape {
        Empty, Triangle, Box, Laser, Target
    }

    protected enum LaserDirection {
        Horizontal, Vertical
    }

    int x, y, x0, y0, x1, y1;
    Shape shape;
    int rotation = 0;
    boolean lasered = false;
    LaserDirection inCominglaserDirection = LaserDirection.Horizontal;

    public GridArea(int x, int y, int x0, int y0, int x1, int y1, Shape s) {
        this.x = x;
        this.y = y;
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.shape = s;
    }

    public boolean inBounds(TouchEvent event) {
        if (event.x > x0 && event.x < x1 && event.y > y0 && event.y < y1)
            return true;
        else
            return false;
    }

    public int getRotation() {
        return this.rotation % 360;
    }
}