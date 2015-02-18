package com.maltelenz.framework.implementation;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Image;
import com.maltelenz.sensortrouble.ColorPalette;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Paint textPaint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    private float fontSize = 50;

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
        this.textPaint = new Paint();

        textPaint.setTextSize(fontSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    public Image newImage(String fileName, ImageFormat format) {
        Config config = null;
        if (format == ImageFormat.RGB565)
            config = Config.RGB_565;
        else if (format == ImageFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = ImageFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = ImageFormat.ARGB4444;
        else
            format = ImageFormat.ARGB8888;

        return new AndroidImage(bitmap, format);
    }

    @Override
    public void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawCircle(int x, int y, int radius, Paint painter) {
        canvas.drawCircle(x, y, radius, painter);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawRectWithShadow(int x, int y, int width, int height, int color) {
        Paint rectanglePainter = new Paint();
        rectanglePainter.setColor(color);
        rectanglePainter.setStyle(Style.FILL);
        rectanglePainter.setShadowLayer(10.0f, 2.0f, 2.0f, ColorPalette.rectangleShadow);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, rectanglePainter);
    }

    @Override
    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    @Override
    public void drawString(String text, int x, int y) {
        drawString(text, x, y, textPaint);
    }

    @Override
    public void drawString(String text, int x, int y, Paint painter) {
        canvas.drawText(text, x, y + painter.getFontMetrics().bottom, painter);
    }

    @Override
    public void drawStringCentered(String text) {
        drawStringCentered(text, textPaint);
    }

    @Override
    public void drawStringCentered(String text, Paint painter) {
        drawString(text, getWidth() / 2, getHeight() / 2, painter);
    }

    public void drawNextButton(int width, int height) {
        drawButton("Next", getWidth() - width, getHeight() - height, getWidth(), getHeight());
    }

    public void drawStartButton(int x0, int y0, int x1, int y1) {
        drawButton("Start", x0, y0, x1, y1);
    }

    @Override
    public void drawButton(String text, int x0, int y0, int x1, int y1) {
        Paint rectanglePainter = new Paint();
        rectanglePainter.setColor(ColorPalette.button);
        rectanglePainter.setShadowLayer(10.0f, 2.0f, 2.0f, ColorPalette.buttonShadow);
        canvas.drawRect(x0, y0, x1, y1, rectanglePainter);
        drawString(text, x0 + (x1 - x0) / 2, y0 + (y1 - y0) / 2);
    }

    public void drawImage(Image Image, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawImage(Image Image, int x, int y) {
        canvas.drawBitmap(((AndroidImage) Image).bitmap, x, y, null);
    }

    public void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth,
            int srcHeight) {

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + width;
        dstRect.bottom = y + height;

        canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect, null);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}
