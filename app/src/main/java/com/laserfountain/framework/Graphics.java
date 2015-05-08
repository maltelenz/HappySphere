package com.laserfountain.framework;

import java.util.List;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.laserfountain.happysphere.Barrier;
import com.laserfountain.happysphere.Button;
import com.laserfountain.happysphere.GridArea.LaserDirection;
import com.laserfountain.happysphere.Happy;
import com.laserfountain.happysphere.PieCircle;
import com.laserfountain.happysphere.TouchPoint;

public interface Graphics {

    enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

    int scaleX(int in);

    int scaleY(int in);

    int scale(int in);

    float scaleX(float in);

    float scaleY(float in);

    float scale(float in);

    void clearScreen(int color);

    void drawLine(double x, double y, double x2, double y2, int color);

    void drawLine(double x, double y, double x2, double y2, Paint paint);

    void drawTargetLine(int x, int y, int x2, int y2, int thickness);

    void drawLaserLine(int x, int y, int x2, int y2);

    void drawLaser(int x, int y, int width, int height, int rotation);

    void drawTarget(int x, int y, int width, int height, List<LaserDirection> inComingDirections, boolean lasered);

    void drawLaserDeflectorTriangle(int x, int y, int width, int height, int rotation, int color, boolean lasered);

    void drawRect(int x, int y, int width, int height, int color);

    void drawRectNoFill(int x, int y, int width, int height, int color);

    void drawRectWithShadow(int x, int y, int width, int height, int color);

    void drawImage(Image image, int x, int y, int srcX, int srcY,
                   int srcWidth, int srcHeight);

    void drawImage(Image Image, int x, int y);

    void drawString(String text, int x, int y);

    void drawString(String text, double x, double y, Paint painter);

    void drawStringCentered(String text);

    void drawStringCentered(String string, Paint largePainter);

    void drawOopsieString();

    void drawButton(String text, int x0, int y0, int x1, int y1);

    void drawButton(Button b);

    int getWidth();

    int getHeight();

    void drawARGB(int i, int j, int k, int l);

    void drawCircle(double x, double y, float f, Paint painter);

    void drawArc(RectF rect, float percent, Paint painter);

    void drawPartialArc(RectF rect, float startPercent, float sweepPercent, Paint painter);

    void drawPoint(TouchPoint point, int pointRadius);

    void drawPoints(float[] drawingPoints, Paint paint);

    void drawPath(Path path, Paint paint);

    void drawBarrier(Barrier b, Paint paint);

    void drawShooter(int currentPoint, int height, float fractionShotsLeft);

    void drawPie(PieCircle pie);

    void drawArrow(int xmin, int ymin, int xmax, int ymax);

    void drawHappySphere(Happy happiness);
}
