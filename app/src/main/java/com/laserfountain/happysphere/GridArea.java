package com.laserfountain.happysphere;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.laserfountain.framework.Input.TouchEvent;

public class GridArea {

    protected enum Shape {
        Empty, Triangle, Box, Laser, Target
    }

    public enum LaserDirection {
        Left, Top, Right, Bottom
    }

    int x, y, x0, y0, x1, y1;
    Shape shape;
    int rotation = 0;
    boolean lasered = false;
    List<LaserDirection> inCominglaserDirections;
    public boolean selected;

    public GridArea(int x, int y, int x0, int y0, int x1, int y1, Shape s, int rotation) {
        this.x = x;
        this.y = y;
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.shape = s;
        this.rotation = rotation;
        this.selected = false;
        this.inCominglaserDirections = new ArrayList<LaserDirection>();
        inCominglaserDirections.add(LaserDirection.Left);
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
    
    public List<LaserDirection> getLaserDirections() {
        return this.inCominglaserDirections;
    }

    public boolean isInComingHorizontal() {
        for (Iterator<LaserDirection> iterator = inCominglaserDirections.iterator(); iterator.hasNext();) {
            LaserDirection d = (LaserDirection) iterator.next();
            if (d == LaserDirection.Left || d == LaserDirection.Right) {
                return true;
            }
        }
        return false;
    }

    public boolean isInComingVertical() {
        for (Iterator<LaserDirection> iterator = inCominglaserDirections.iterator(); iterator.hasNext();) {
            LaserDirection d = (LaserDirection) iterator.next();
            if (d == LaserDirection.Top || d == LaserDirection.Bottom) {
                return true;
            }
        }
        return false;
    }

    public void addInCominglaserDirection(LaserDirection d) {
        inCominglaserDirections.add(d);
    }

    public void clearLasers() {
        inCominglaserDirections.clear();
        lasered = false;
    }
}