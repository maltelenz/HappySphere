package com.laserfountain.happysphere;

import android.graphics.PointF;

import java.util.ArrayList;

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

    public PointF move(PointF before, PointF after, float radius) {
        if (after.y >= y0 - radius && after.y <= y1 + radius && after.x >= x0 - radius && after.x <= x1 + radius) {
            // Inside the barrier
            ArrayList<PointF> alternatives = new ArrayList<PointF>();
            alternatives.add(new PointF(after.x, y0 - radius)); // Hit from top
            alternatives.add(new PointF(after.x, y1 + radius)); // Hit from bottom
            alternatives.add(new PointF(x0 - radius, after.y)); // Hit from left
            alternatives.add(new PointF(x1 + radius, after.y)); // Hit from right

            // The theory here is that if the ball is inside the barrier,
            // it should have collided with the barrier side.
            // The possible collision point closest to the original point
            // is assumed to be the correct one.
            PointF chosenPoint = alternatives.get(0);
            double shortestDistance = Math.pow(chosenPoint.x - before.x, 2) + Math.pow(chosenPoint.y - before.y, 2);
            for (PointF p : alternatives) {
                double distance = Math.pow(p.x - before.x, 2) + Math.pow(p.y - before.y, 2);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    chosenPoint = p;
                }
            }
            return chosenPoint;
        }
        return after;
    }

    public boolean inBounds(float x, float y) {
        return x >= x0 && x <= x1 && y >= y0 && y <= y1;
    }

    public boolean inBounds(PointF point, int radius) {
        return point.y >= y0 - radius && point.y <= y1 + radius && point.x >= x0 - radius && point.x <= x1 + radius;
    }
}