package com.maltelenz.sensortrouble;

import java.util.ArrayList;
import java.util.Collections;

import com.maltelenz.framework.Input.TouchEvent;

public class PieCircle {

    ArrayList<Integer> pieces;
    int rotation = 0;
    private int maxRadius;
    private int minRadius;
    private int centerX;
    private int centerY;

    public PieCircle(ArrayList<Integer> colors, int minRadius, int maxRadius, int centerX, int centerY) {
        this.pieces = colors;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    private boolean inBounds(TouchEvent event) {
        int dx = centerX - event.x;
        int dy = centerY - event.y;
        double radius = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        if (radius < maxRadius && radius > minRadius)
            return true;
        else
            return false;
    }

    public void touch(TouchEvent event) {
        if (inBounds(event)) {
            Collections.rotate(this.pieces, 1);
        }
    }

    public ArrayList<Integer> getColors() {
        return pieces;
    }

    public int getMaxRadius() {
        return maxRadius;
    }
}