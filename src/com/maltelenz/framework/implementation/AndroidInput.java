package com.maltelenz.framework.implementation;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.maltelenz.framework.Input;

public class AndroidInput implements Input {
    TouchHandler touchHandler;

    public AndroidInput(Context context, View view) {
        touchHandler = new MultiTouchHandler(view, 1.0F, 1.0F);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
