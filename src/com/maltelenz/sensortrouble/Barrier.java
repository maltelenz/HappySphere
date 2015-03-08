package com.maltelenz.sensortrouble;

public class Barrier {

    public int x0;
    public int y0;
    public int x1;
    public int y1;

    public Barrier(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public boolean inBounds(int x, int y) {
        if (x >= x0 && x <= x1 && y >= y0 && y <= y1)
            return true;
        else
            return false;
    }
}