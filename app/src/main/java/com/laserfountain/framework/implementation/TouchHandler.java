package com.laserfountain.framework.implementation;

import java.util.List;

import android.view.View.OnTouchListener;

import com.laserfountain.framework.Input.TouchEvent;

public interface TouchHandler extends OnTouchListener {
    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    List<TouchEvent> getTouchEvents();
}
