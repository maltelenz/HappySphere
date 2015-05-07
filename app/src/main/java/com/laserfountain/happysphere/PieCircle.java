package com.laserfountain.happysphere;

import java.util.ArrayList;
import java.util.Collections;

import com.laserfountain.framework.Input.TouchEvent;

public class PieCircle extends CircleButton {

    ArrayList<Integer> pieces;
    int rotation = 0;

    public PieCircle(ArrayList<Integer> colors, float minRadius, float maxRadius, int centerX, int centerY) {
        super(minRadius, maxRadius, centerX, centerY);

        this.pieces = colors;
    }

    public void touch(TouchEvent event) {
        if (inBounds(event)) {
            Collections.rotate(this.pieces, 1);
        }
    }

    public ArrayList<Integer> getColors() {
        return pieces;
    }
}