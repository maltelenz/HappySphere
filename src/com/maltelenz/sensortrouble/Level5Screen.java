package com.maltelenz.sensortrouble;

import java.util.Arrays;
import java.util.List;

import android.graphics.Paint;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class Level5Screen extends LevelScreen {

    private int gameHeight;
    private double gameWidth;
    private int maxXDeviation = 100;
    private int maxYDeviation = 100;
    private int currentPointY = maxYDeviation;
    private boolean grabbed = false;
    private Paint finishedPixelPaint;
    private Paint unTouchedPointPaint;
    private int lineRadius = 10;
    private float[] drawingPoints;
    private int reverseSpeed;
    private Paint touchedPointPaint;
    private Paint unFinishedPixelPaint;
    private boolean landscape;

    public Level5Screen(Game game) {
        super(game);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        if (gameWidth > gameHeight) {
            landscape = true;
            gameHeight = game.getGraphics().getWidth();
            gameWidth = game.getGraphics().getHeight();
        }
        
        finishedPixelPaint = new Paint();
        finishedPixelPaint.setColor(ColorPalette.progress);
        finishedPixelPaint.setAntiAlias(true);
        finishedPixelPaint.setStrokeWidth(lineRadius);

        unFinishedPixelPaint = new Paint();
        unFinishedPixelPaint.set(finishedPixelPaint);
        unFinishedPixelPaint.setAlpha(5);

        unTouchedPointPaint = new Paint();
        unTouchedPointPaint.setColor(ColorPalette.laser);
        unTouchedPointPaint.setAntiAlias(true);
        unTouchedPointPaint.setShadowLayer(10.0f, 2.0f, 2.0f, ColorPalette.buttonShadow);

        touchedPointPaint = new Paint();
        touchedPointPaint.set(unTouchedPointPaint);
        touchedPointPaint.setColor(ColorPalette.progress);

        drawingPoints = new float[gameHeight * 2];
        for (int y = 0; y < gameHeight; y++) {
            drawingPoints[y * 2] = getXValue(y);
            drawingPoints[y * 2 + 1] = y;
        }

        reverseSpeed = (int) Math.round(gameHeight / 700.0);

        state = GameState.Running;
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            int ypos;
            int xpos;
            if (landscape) {
                ypos = event.x;
                xpos = event.y;
            } else {
                ypos = event.y;
                xpos = event.x;
            }
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (!grabbed  && Math.abs(ypos - currentPointY) < maxXDeviation) {
                    // Start touching
                    grabbed = true;
                }
            } else if (event.type == TouchEvent.TOUCH_UP) {
                grabbed = false;
            } else if (event.type == TouchEvent.TOUCH_DRAGGED) {
                double distanceX = getDistanceX(xpos, ypos);
                if (grabbed && distanceX < maxXDeviation && Math.abs(ypos - currentPointY) < maxYDeviation ) {
                    currentPointY = ypos;
                } else {
                    grabbed = false;
                }
            }
        }

        if (!grabbed) {
            currentPointY = Math.max(maxYDeviation, currentPointY - reverseSpeed);
        }

        if (currentPointY >= gameHeight - maxYDeviation) {
            state = GameState.Finished;
        }
    }

    @Override
    double percentDone() {
        // The - maxYDeviation and - 2 * maxYDeviation are because there are buffers at the top and bottom of the screen
        return (currentPointY - maxYDeviation)/((double) gameHeight - 2 * maxYDeviation);
    }

    @Override
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        int offset = 0;
        if (landscape) {
            offset = 1;
        }

        float[] finishedPoints = Arrays.copyOfRange(drawingPoints, 0 + offset, currentPointY * 2 + offset);
        float[] unFinishedPoints = Arrays.copyOfRange(drawingPoints, currentPointY * 2 + offset, drawingPoints.length - offset);
        g.drawPoints(finishedPoints, finishedPixelPaint);

        g.drawPoints(unFinishedPoints, unFinishedPixelPaint);

        Paint usedPointPaint = new Paint();
        if (grabbed) {
            usedPointPaint.set(touchedPointPaint);
        } else {
            usedPointPaint.set(unTouchedPointPaint);
        }
        int xp;
        int yp;
        if (landscape) {
            xp = currentPointY;
            yp = getXValue(currentPointY);
        } else {
            xp = getXValue(currentPointY);
            yp = currentPointY;
        }
        g.drawCircle(xp, yp, maxXDeviation, usedPointPaint);
    }
    
    private int getXValue(int y) {
        double factor = 5.0/gameHeight;
        return (int) Math.round(gameWidth * 0.5 * (1.1 + Math.sin(y * factor) * Math.cos(1.5 * y * factor)));
    }

    private double getDistanceX(int x, int y) {
        return Math.abs(getXValue(y) - x);
    }
}
