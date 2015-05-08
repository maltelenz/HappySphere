package com.laserfountain.framework;

import com.laserfountain.framework.Graphics.ImageFormat;

public interface Image {
    int getWidth();
    int getHeight();
    ImageFormat getFormat();
    void dispose();
}
