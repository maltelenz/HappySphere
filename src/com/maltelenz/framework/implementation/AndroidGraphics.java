package com.maltelenz.framework.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

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
    public void drawLine(int x, int y, int x2, int y2, Paint paint) {
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawCircle(int x, int y, int radius, Paint painter) {
        canvas.drawCircle(x, y, radius, painter);
    }

    @Override
    public void drawArc(RectF rect, float percent, Paint painter) {
        canvas.drawArc(rect, -90, 360 * percent, false, painter);
    }

    @Override
    public void drawLaser(int x, int y, int width, int height, int rotation) {
        Paint laserCircle = new Paint();
        laserCircle.set(ColorPalette.laserPaint);
        laserCircle.setStyle(Style.FILL_AND_STROKE);
        drawCircle(x + width/2, y + height/2, 50, laserCircle);
        
        drawLine(x + width/2, y + height/2, x + width, y + height/2, ColorPalette.laserPaint);
    }

    @Override
    public void drawTarget(int x, int y, int width, int height, int rotation, boolean lasered) {
        if (lasered) {
            List<Point> laserPoints = new ArrayList<Point>();
            laserPoints.add(new Point(x, y + height/2));
            laserPoints.add(new Point(x + width/2, y + height));
            laserPoints.add(new Point(x + width, y + height/2));
            laserPoints.add(new Point(x + width/2, y));

            Collections.rotate(laserPoints, -rotation/90);
            drawLine(laserPoints.get(0).x, laserPoints.get(0).y, x + width/2, y + width/2, ColorPalette.laserPaint);
        }
        
        Paint laserCircle = new Paint();
        laserCircle.setColor(ColorPalette.progress);
        laserCircle.setStrokeWidth(10);
        laserCircle.setAntiAlias(true);
        if (lasered) {
            laserCircle.setStyle(Style.FILL_AND_STROKE);
        } else {
            laserCircle.setStyle(Style.STROKE);
        }
        drawCircle(x + width/2, y + height/2, 50, laserCircle);
    }

    @Override
    public void drawTriangle(int x, int y, int width, int height, int rotation, int color, boolean lasered) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(10);
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(x, y));
        points.add(new Point(x + width, y));
        points.add(new Point(x + width, y + height));
        points.add(new Point(x, y + height));

        Collections.rotate(points, rotation/90);

        Path path = new Path();
        path.moveTo(points.get(0).x, points.get(0).y);
        path.lineTo(points.get(1).x, points.get(1).y);
        path.moveTo(points.get(1).x, points.get(1).y);
        path.lineTo(points.get(2).x, points.get(2).y);
        path.moveTo(points.get(2).x, points.get(2).y);
        path.lineTo(points.get(0).x, points.get(0).y);
        path.close();

        canvas.drawPath(path, paint);
        
        if (lasered) {
            List<Point> laserPoints = new ArrayList<Point>();
            laserPoints.add(new Point(x, y + height/2));
            laserPoints.add(new Point(x + width/2, y + height));
            laserPoints.add(new Point(x + width, y + height/2));
            laserPoints.add(new Point(x + width/2, y));

            Point centerPoint = new Point(x + width/2, y + width/2);

            Collections.rotate(laserPoints, -rotation/90);

            Path laserPath = new Path();
            laserPath.moveTo(laserPoints.get(0).x, laserPoints.get(0).y);
            laserPath.lineTo(centerPoint.x, centerPoint.y);
            laserPath.moveTo(centerPoint.x, centerPoint.y);
            laserPath.lineTo(laserPoints.get(1).x, laserPoints.get(1).y);
            laserPath.moveTo(laserPoints.get(1).x, laserPoints.get(1).y);
            laserPath.close();
            canvas.drawPath(laserPath, ColorPalette.laserPaint);
        }
    }

    @Override
    public void drawRectNoFill(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
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
