package com.maltelenz.framework;

import com.maltelenz.sensortrouble.Button;

import android.graphics.Paint;
import android.graphics.RectF;

public interface Graphics {
    public static enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Image newImage(String fileName, ImageFormat format);

    public void clearScreen(int color);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawLine(int x, int y, int x2, int y2, Paint paint);

    public void drawLaser(int x, int y, int width, int height, int rotation);

    public void drawTarget(int x, int y, int width, int height, int rotation, boolean lasered);

    public void drawTriangle(int x, int y, int width, int height, int rotation, int color, boolean lasered);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawRectNoFill(int x, int y, int width, int height, int color);

    public void drawRectWithShadow(int x, int y, int width, int height, int color);

    public void drawImage(Image image, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);

    public void drawImage(Image Image, int x, int y);

    public void drawString(String text, int x, int y);

    public void drawString(String text, int x, int y, Paint painter);

    public void drawStringCentered(String text);

    public void drawStringCentered(String string, Paint largePainter);

    public void drawButton(String text, int x0, int y0, int x1, int y1);

    public void drawButton(Button b);

    public int getWidth();

    public int getHeight();

    public void drawARGB(int i, int j, int k, int l);

    void drawCircle(int x, int y, int radius, Paint painter);

    public void drawArc(RectF rect, float percent, Paint painter);

}
