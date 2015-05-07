package com.laserfountain.sensortrouble;

import com.laserfountain.framework.Input.TouchEvent;

public class Button {

    public int x0;
    public int y0;
    public int x1;
    public int y1;
    public String text;

    public Button(String text, int x0, int y0, int x1, int y1) {
        this.text = text;
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