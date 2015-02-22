package com.maltelenz.sensortrouble;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;

public class ColorPalette {
    public static int background = Color.rgb(85, 98, 112);
    public static int button = Color.rgb(78, 205, 196);
    public static int progress = Color.rgb(199, 244, 100);
    public static int cherry = Color.rgb(255, 107, 107);
    public static int pillow = Color.rgb(196, 77, 88);

    public static int progressBackground = Color.argb(20, 199, 244, 100);

    public static int rectangleShadow = Color.BLACK;
    public static int buttonShadow = Color.BLACK;
    
    public static int laser = cherry;

    public static Paint laserPaint;

    // Initialize paint
    static {
        laserPaint = new Paint();
        laserPaint.setColor(laser);
        laserPaint.setStrokeWidth(10);
        laserPaint.setStyle(Style.STROKE);
        laserPaint.setStrokeJoin(Join.BEVEL);
        laserPaint.setAntiAlias(true);
    }
}
