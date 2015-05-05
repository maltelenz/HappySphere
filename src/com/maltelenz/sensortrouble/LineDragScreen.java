package com.maltelenz.sensortrouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Paint;
import android.graphics.Point;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class LineDragScreen extends LevelScreen {

    protected int gameHeight;
    protected int gameWidth;
    protected int maxDeviation;
    protected int currentPointY;
    protected int currentPointX;
    private boolean grabbed = false;
    private Paint finishedPixelPaint;
    private Paint unTouchedPointPaint;
    private int lineRadius;
    protected ArrayList<Point> drawingPoints;
    private float reverseSpeed;
    private Paint touchedPointPaint;
    private Paint unFinishedPixelPaint;
    private int targetThickness;

    public LineDragScreen(Game game) {
        super(game);

        maxDeviation = Math.max(game.scale(100), 50);
        lineRadius = game.scale(20);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        finishedPixelPaint = new Paint();
        finishedPixelPaint.setColor(ColorPalette.progress);
        finishedPixelPaint.setAntiAlias(true);
        finishedPixelPaint.setStrokeWidth(lineRadius);
        finishedPixelPaint.setStrokeCap(Paint.Cap.ROUND);

        unFinishedPixelPaint = new Paint();
        unFinishedPixelPaint.set(finishedPixelPaint);
        unFinishedPixelPaint.setColor(ColorPalette.oopsie);

        unTouchedPointPaint = new Paint();
        unTouchedPointPaint.setColor(ColorPalette.laser);
        unTouchedPointPaint.setAntiAlias(true);
        unTouchedPointPaint.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);

        touchedPointPaint = new Paint();
        touchedPointPaint.set(unTouchedPointPaint);
        touchedPointPaint.setColor(ColorPalette.progress);

        targetThickness = game.scaleY(50);

        reverseSpeed = game.scaleY(4.0f);

        drawingPoints = new ArrayList<Point>();
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (!grabbed  && Math.abs(event.y - currentPointY) < maxDeviation) {
                    // Start touching
                    grabbed = true;
                }
            } else if (event.type == TouchEvent.TOUCH_UP) {
                grabbed = false;
            } else if (event.type == TouchEvent.TOUCH_DRAGGED) {
                Point eventPoint = new Point(event.x, event.y);
                Point closestPoint = getClosestPoint(eventPoint);
                double distance = getDistance(closestPoint, eventPoint);
                if (grabbed && distance < maxDeviation) {
                    currentPointY = event.y;
                    currentPointX = event.x;
                } else {
                    grabbed = false;
                }
            }
        }

        if (!grabbed) {
            currentPointY = Math.max(maxDeviation, Math.round((float) currentPointY - reverseSpeed));
            Point newPoint = getClosestPointAtY(currentPointY);
            currentPointX = currentPointX + (newPoint.x - currentPointX)/20;
        }

        if (currentPointY >= gameHeight - maxDeviation) {
            state = GameState.Finished;
        }
    }

    @Override
    double percentDone() {
        // The - maxYDeviation and - 2 * maxYDeviation are because there are buffers at the top and bottom of the screen
        return (currentPointY - maxDeviation)/((double) gameHeight - 2 * maxDeviation);
    }

    @Override
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        g.drawTargetLine(0, gameHeight, gameWidth, gameHeight, targetThickness);

        g.drawArrow(4 * gameWidth/5, 2 * gameHeight/3 - game.scaleY(400), 4 * gameWidth/5, 2 * gameHeight/3 - game.scaleY(150));

        for (int i = 0; i < drawingPoints.size() - 1; i++) {
            Point from = drawingPoints.get(i);
            Point to = drawingPoints.get(i + 1);
            if (to.y <= currentPointY) {
                g.drawLine(from.x, from.y, to.x, to.y, finishedPixelPaint);
            } else {
                g.drawLine(from.x, from.y, to.x, to.y, unFinishedPixelPaint);
            }
        }

        Paint usedPointPaint = new Paint();
        if (grabbed) {
            usedPointPaint.set(touchedPointPaint);
        } else {
            usedPointPaint.set(unTouchedPointPaint);
        }
        g.drawCircle(currentPointX, currentPointY, maxDeviation, usedPointPaint);
    }

    private double getDistance(Point p, Point p2) {
        return Math.sqrt(Math.pow(p.x - p2.x, 2) + Math.pow(p.y - p2.y, 2));
    }
    
    private Point getClosestPoint(Point inPoint) {
        double minDistance = gameHeight * 2;
        double distance;
        Point minPoint = new Point();
        for (Iterator<Point> iterator = drawingPoints.iterator(); iterator.hasNext();) {
            Point point = (Point) iterator.next();
            distance = getDistance(point, inPoint);
            if (distance < minDistance) {
                minDistance = distance;
                minPoint = point;
            }
        }
        return minPoint;
    }

    private Point getClosestPointAtY(int y) {
        double minYDistance = gameHeight * 2;
        double yDistance;
        Point minPoint = new Point();
        for (Iterator<Point> iterator = drawingPoints.iterator(); iterator.hasNext();) {
            Point point = (Point) iterator.next();
            yDistance = Math.abs(point.y - y);
            if (yDistance < minYDistance) {
                minYDistance = yDistance;
                minPoint = point;
            }
        }
        return minPoint;
    }
}
