package com.laserfountain.framework.implementation;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.laserfountain.framework.Graphics;
import com.laserfountain.framework.Image;
import com.laserfountain.happysphere.Barrier;
import com.laserfountain.happysphere.Button;
import com.laserfountain.happysphere.ColorPalette;
import com.laserfountain.happysphere.GridArea.LaserDirection;
import com.laserfountain.happysphere.Happy;
import com.laserfountain.happysphere.PieCircle;
import com.laserfountain.happysphere.TouchPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Paint darkTextPaint;
    Paint lightTextPaint;
    Paint laserPaint;
    Paint oopsiePaint;
    Paint shooterPaint;

    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    private float xScale;
    private float yScale;

    private Paint shooterFillPaint;
    private Paint piePaint;
    private Paint arrowPaint;
    private Paint targetPaint;
    private Paint happyPaint;
    private Paint eyePaint;
    private Paint eyeInnerPaint;
    private Paint nosePaint;
    private Paint mouthpaint;

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
        this.darkTextPaint = new Paint();

        xScale = getWidth()/1080f;
        yScale = getHeight()/1920f;

        float fontSize = scale(50);

        darkTextPaint.setTextSize(fontSize);
        darkTextPaint.setTextAlign(Paint.Align.CENTER);
        darkTextPaint.setAntiAlias(true);
        darkTextPaint.setColor(ColorPalette.darkText);
        darkTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        this.lightTextPaint = new Paint();
        lightTextPaint.set(darkTextPaint);
        lightTextPaint.setColor(ColorPalette.lightText);
        
        laserPaint = new Paint();
        laserPaint.setColor(ColorPalette.laser);
        laserPaint.setStrokeWidth(scale(10));
        laserPaint.setStyle(Style.STROKE);
        laserPaint.setStrokeJoin(Join.BEVEL);
        laserPaint.setAntiAlias(true);

        this.oopsiePaint = new Paint();

        oopsiePaint.setTextSize(scale(100));
        oopsiePaint.setTextAlign(Paint.Align.CENTER);
        oopsiePaint.setAntiAlias(true);
        oopsiePaint.setColor(ColorPalette.oopsie);
        oopsiePaint.setTypeface(Typeface.DEFAULT_BOLD);

        shooterPaint = new Paint();
        shooterPaint.setColor(ColorPalette.laser);
        shooterPaint.setAntiAlias(true);
        shooterPaint.setStyle(Style.FILL_AND_STROKE);
        shooterPaint.setStrokeWidth(scale(10));
        shooterPaint.setShadowLayer(scale(10.0f), scale(2.0f), scale(2.0f), ColorPalette.buttonShadow);

        shooterFillPaint = new Paint();
        shooterFillPaint.set(shooterPaint);
        shooterFillPaint.setColor(ColorPalette.progress);

        piePaint = new Paint();
        piePaint.setAntiAlias(true);
        piePaint.setStyle(Style.FILL);
        piePaint.setShadowLayer(scale(10.0f), scale(2.0f), scale(2.0f), ColorPalette.buttonShadow);

        arrowPaint = new Paint();
        arrowPaint.setColor(ColorPalette.inactiveProgress);
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStyle(Style.FILL);

        targetPaint = new Paint();
        targetPaint.setColor(ColorPalette.progress);

        happyPaint = new Paint();
        happyPaint.set(arrowPaint);
        happyPaint.setColor(ColorPalette.yellow);

        eyePaint = new Paint();
        eyePaint.set(happyPaint);
        eyePaint.setColor(ColorPalette.box);

        eyeInnerPaint = new Paint();
        eyeInnerPaint.set(eyePaint);
        eyeInnerPaint.setColor(ColorPalette.progress);

        nosePaint = new Paint();
        nosePaint.set(eyePaint);
        nosePaint.setColor(ColorPalette.inactiveProgress);

        mouthpaint = new Paint();
        mouthpaint.set(eyeInnerPaint);
        mouthpaint.setStrokeWidth(getWidth()/50);
    }

    @Override
    public int scaleX(int in) {
        return Math.round(in * xScale);
    }

    @Override
    public int scaleY(int in) {
        return Math.round(in * yScale);
    }

    @Override
    public int scale(int in) {
        return Math.min(scaleX(in), scaleY(in));
    }

    @Override
    public float scaleX(float in) {
        return in * xScale;
    }

    @Override
    public float scaleY(float in) {
        return in * yScale;
    }

    @Override
    public float scale(float in) {
        return Math.min(scaleX(in), scaleY(in));
    }

    @Override
    public void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawLine(double x, double y, double x2, double y2, int color) {
        paint.setColor(color);
        canvas.drawLine(Math.round(x), Math.round(y), Math.round(x2), Math.round(y2), paint);
    }

    @Override
    public void drawLine(double x, double y, double x2, double y2, Paint paint) {
        canvas.drawLine(Math.round(x), Math.round(y), Math.round(x2), Math.round(y2), paint);
    }

    @Override
    public void drawTargetLine(int x, int y, int x2, int y2, int thickness) {
        targetPaint.setStrokeWidth(thickness);
        canvas.drawLine(x, y, x2, y2, targetPaint);
    }

    @Override
    public void drawLaserLine(int x, int y, int x2, int y2) {
        canvas.drawLine(x, y, x2, y2, laserPaint);
    }

    @Override
    public void drawCircle(double x, double y, float radius, Paint painter) {
        canvas.drawCircle(Math.round(x), Math.round(y), radius, painter);
    }

    @Override
    public void drawArc(RectF rect, float percent, Paint painter) {
        canvas.drawArc(rect, -90, 360 * percent, false, painter);
    }

    @Override
    public void drawPartialArc(RectF rect, float startPercent, float sweepPercent, Paint painter) {
        canvas.drawArc(rect, -90 + 360 * startPercent, 360 * sweepPercent, false, painter);
    }

    @Override
    public void drawLaser(int x, int y, int width, int height, int rotation) {
        Paint laserCircle = new Paint();
        laserCircle.set(laserPaint);
        laserCircle.setStyle(Style.FILL_AND_STROKE);
        drawCircle(x + width/2, y + height/2, scale(50), laserCircle);

        List<Point> laserPoints = new ArrayList<Point>();
        laserPoints.add(new Point(x + width, y + height/2));
        laserPoints.add(new Point(x + width/2, y));
        laserPoints.add(new Point(x, y + height/2));
        laserPoints.add(new Point(x + width/2, y + height));

        Collections.rotate(laserPoints, rotation/90);
        drawLine(laserPoints.get(0).x, laserPoints.get(0).y, x + width/2, y + width/2, laserPaint);
    }

    @Override
    public void drawTarget(int x, int y, int width, int height, List<LaserDirection> inComingDirections, boolean lasered) {
        if (lasered) {
            Point incomingPoint;
            for (LaserDirection d : inComingDirections) {
                switch (d) {
                    case Left:
                        incomingPoint = new Point(x, y + height / 2);
                        break;
                    case Right:
                        incomingPoint = new Point(x + width, y + height / 2);
                        break;
                    case Top:
                        incomingPoint = new Point(x + width / 2, y);
                        break;
                    case Bottom:
                    default:
                        incomingPoint = new Point(x + width / 2, y + height);
                        break;
                }
                drawLine(incomingPoint.x, incomingPoint.y, x + width / 2, y + width / 2, laserPaint);
            }
        }

        Paint laserCircle = new Paint();
        laserCircle.setColor(ColorPalette.progress);
        laserCircle.setStrokeWidth(scale(10));
        laserCircle.setAntiAlias(true);
        if (lasered) {
            laserCircle.setStyle(Style.FILL_AND_STROKE);
        } else {
            laserCircle.setStyle(Style.STROKE);
        }
        drawCircle(x + width/2, y + height/2, scale(50), laserCircle);
    }

    @Override
    public void drawLaserDeflectorTriangle(int x, int y, int width, int height, int rotation, int color, boolean lasered) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(10);
        paint.setStyle(Style.FILL);
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
        path.lineTo(points.get(2).x, points.get(2).y);
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
            laserPath.lineTo(laserPoints.get(1).x, laserPoints.get(1).y);
            canvas.drawPath(laserPath, laserPaint);
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
        rectanglePainter.setShadowLayer(scale(10.0f), scale(2.0f), scale(2.0f), ColorPalette.rectangleShadow);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, rectanglePainter);
    }

    @Override
    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    @Override
    public void drawString(String text, int x, int y) {
        drawString(text, x, y, darkTextPaint);
    }

    @Override
    public void drawString(String text, double x, double y, Paint painter) {
        canvas.drawText(text, Math.round(x), Math.round(y) + painter.getFontMetrics().bottom, painter);
    }

    @Override
    public void drawStringCentered(String text) {
        drawStringCentered(text, darkTextPaint);
    }

    @Override
    public void drawOopsieString() {
        drawStringCentered("Oopsie! Try again!", oopsiePaint);
    }

    @Override
    public void drawStringCentered(String text, Paint painter) {
        drawString(text, getWidth() / 2, getHeight() / 2, painter);
    }

    @Override
    public void drawButton(String text, int x0, int y0, int x1, int y1) {
        Paint rectanglePainter = new Paint();
        rectanglePainter.setColor(ColorPalette.button);
        rectanglePainter.setShadowLayer(scale(10.0f), scale(2.0f), scale(2.0f), ColorPalette.buttonShadow);
        canvas.drawRect(x0, y0, x1, y1, rectanglePainter);
        drawString(text, x0 + (x1 - x0) / 2, y0 + (y1 - y0) / 2, lightTextPaint);
    }

    @Override
    public void drawButton(Button b) {
        drawButton(b.text, b.x0, b.y0, b.x1, b.y1);
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

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }

    @Override
    public void drawPoint(TouchPoint point, int pointRadius) {
        Paint pointPaint = new Paint();
        pointPaint.setStrokeWidth(10);
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Style.FILL_AND_STROKE);
        if (point.isTouched()) {
            pointPaint.setColor(ColorPalette.progress);
        } else {
            pointPaint.setColor(ColorPalette.inactiveProgress);
        }
        drawCircle(point.x, point.y, pointRadius, pointPaint);
    }

    @Override
    public void drawPoints(float[] drawingPoints, Paint paint) {
        canvas.drawPoints(drawingPoints, paint);
    }

    @Override
    public void drawPath(Path path, Paint paint) {
        canvas.drawPath(path, paint);
    }

    @Override
    public void drawBarrier(Barrier b, Paint paint) {
        canvas.drawRect(b.x0, b.y0, b.x1, b.y1, paint);
    }

    @Override
    public void drawShooter(int currentPoint, int height, float fractionShotsLeft) {
        RectF rect = new RectF(currentPoint - height, getHeight() - height, currentPoint + height, getHeight() + height);
        canvas.drawArc(rect, -180, 180, false, shooterPaint);
        canvas.drawArc(rect, -180, 180 * fractionShotsLeft, true, shooterFillPaint);
    }

    @Override
    public void drawPie(PieCircle pie) {
        float centerHeight = getHeight()/2;
        float centerWidth = getWidth()/2;
        float maxRadius = pie.getMaxRadius();
        RectF rectangle = new RectF(
                centerWidth - maxRadius,
                centerHeight - maxRadius,
                centerWidth + maxRadius,
                centerHeight + maxRadius);

        int pieceColor;
        ArrayList<Integer> colors = pie.getColors();
        int cSize = colors.size();
        for (int i = 0; i < cSize; i++) {
            Integer c = colors.get(i);
            switch (c) {
            case 0:
                pieceColor = ColorPalette.box;
                break;
            case 1:
                pieceColor = ColorPalette.button;
                break;
            case 2:
                pieceColor = ColorPalette.laser;
                break;
            case 3:
            default:
                pieceColor = ColorPalette.progress;
                break;
            }

            piePaint.setColor(pieceColor);
            canvas.drawArc(rectangle, i * 360f / cSize, 360f / cSize, true, piePaint);
        }
    }

    @Override
    public void drawArrow(int xmin, int ymin, int xmax, int ymax) {
        Path arrowPath = new Path();
        float tailWidth = 0.2f;
        float headWidth = 0.4f;
        float tailLength = 0.5f;
        float arrowLength = 1;
        arrowPath.moveTo(-tailWidth, 0);
        arrowPath.lineTo(-tailWidth, 0);
        arrowPath.lineTo(tailWidth, 0);
        arrowPath.lineTo(tailWidth, tailLength);
        arrowPath.lineTo(headWidth, tailLength);
        arrowPath.lineTo(0, arrowLength);
        arrowPath.lineTo(-headWidth, tailLength);
        arrowPath.lineTo(-tailWidth, tailLength);
        arrowPath.lineTo(-tailWidth, 0);
        arrowPath.close();

        Matrix transform = new Matrix();
        float src[] = {0, 0, 0, arrowLength};
        float dst[] = {xmin, ymin, xmax, ymax};
        transform.setPolyToPoly(src, 0, dst, 0, 2);
        arrowPath.transform(transform);
        drawPath(arrowPath, arrowPaint);
    }

    @Override
    public void drawHappySphere(Happy happyness) {
        canvas.drawCircle(getWidth()/2f, getHeight()/2f, getWidth()/2.5f, happyPaint);

        canvas.drawCircle(getWidth()/3f, getHeight() * 5f/14f, getWidth()/10f, eyePaint);
        canvas.drawCircle(2 * getWidth()/3f, getHeight() * 5f/14f, getWidth()/10f, eyePaint);

        canvas.drawCircle(getWidth()/3f * 1.03f, getHeight()/2.8f, getWidth()/25f, eyeInnerPaint);
        canvas.drawCircle(2 * getWidth() / 3f * 1.03f, getHeight() / 2.8f, getWidth() / 25f, eyeInnerPaint);

        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 25f, nosePaint);

        if (happyness == Happy.Ok) {
            canvas.drawLine(4 * getWidth() / 12f, getHeight() * 9f / 14f, 8 * getWidth() / 12f, getHeight() * 9f / 14f, mouthpaint);
        } else if (happyness == Happy.Happy) {
            RectF rect = new RectF(4.5f * getWidth() / 12f, getHeight() * 8f / 14f, 7.5f * getWidth() / 12f, getHeight() * 9f / 14f);
            canvas.drawArc(rect, 0f, 180f, false, mouthpaint);
        } else if (happyness == Happy.Exstatic) {
            RectF rect = new RectF(4 * getWidth() / 12f, getHeight() * 7f / 14f, 8 * getWidth() / 12f, getHeight() * 9f / 14f);
            canvas.drawArc(rect, 0f, 180f, false, mouthpaint);
        }
    }
}
