package com.maltelenz.sensortrouble;

import java.util.List;

import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class Level9Screen extends LevelScreen {

    private int xPositionBarrier;
    private int yPositionBarrier;
    private int yPositionPhantomBarrier;
    private int xPositionPhantomBarrier;

    private Paint barrierPaint;
    private Paint barrierPaintWithShadow;
    private Paint phantomBarrierPaint;
    private int barrierRadius;

    private float xPositionBall;
    private float yPositionBall;

    private float dxBall;
    private float dyBall;

    private Paint ballPaint;
    private int ballRadius;

    private int gameHeight;
    private int gameWidth;
    private float paintChangeTime = 15;
    private float bouncePaintTimeLeft = 0;
    private float phantomPaintTimeLeft = 0;

    private int targetX1;
    private int targetX2;
    private int targetY;
    private Paint targetPaint;
    private float targetThickness;

    public Level9Screen(Game game) {
        super(game);

        gameWidth = game.getGraphics().getWidth();
        gameHeight = game.getGraphics().getHeight();

        targetX1 = gameWidth/3;
        targetX2 = 2 * gameWidth/3;
        targetY = gameHeight;
        targetThickness = game.scaleY(50);

        xPositionBarrier = gameWidth/2;
        yPositionBarrier = gameHeight/2;

        barrierRadius = game.scale(150);

        ballRadius = game.scale(50);

        xPositionBall = gameWidth/4;
        yPositionBall = gameHeight/4;

        dxBall = game.scale(6);
        dyBall = game.scale(6);

        ballPaint = new Paint();
        ballPaint.setColor(ColorPalette.laser);
        ballPaint.setStyle(Style.FILL);
        ballPaint.setAntiAlias(true);

        barrierPaint = new Paint();
        barrierPaint.set(ballPaint);
        barrierPaint.setColor(ColorPalette.progress);

        phantomBarrierPaint = new Paint();
        phantomBarrierPaint.set(barrierPaint);
        phantomBarrierPaint.setColor(ColorPalette.oopsie);

        barrierPaintWithShadow = new Paint();
        barrierPaintWithShadow.set(barrierPaint);
        barrierPaintWithShadow.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);

        targetPaint = new Paint();
        targetPaint.setStrokeWidth(targetThickness);
        targetPaint.setColor(ColorPalette.progress);

        state = GameState.Running;
    }

    private double dist2(double x1, double x2, double y1, double y2) {
        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }

    private double distanceToTargetArea() {
        double l2 = dist2(targetX1, targetX2, targetY, targetY);
        double t = ((xPositionBall - targetX1) * (targetX2 - targetX1)) / l2;
        double res;
        if (t < 0) {
            res = dist2(xPositionBall, targetX1, yPositionBall, targetY);
        } else if (t > 1) {
            res = dist2(xPositionBall, targetX2, yPositionBall, targetY);
        } else {
            res = dist2(xPositionBall, targetX1 + t * (targetX2 - targetX1), yPositionBall, targetY + t * (targetY - targetY));
        }
        return Math.sqrt(res) - ballRadius - targetThickness;
    }

    private double distanceBetweenBallAndBarrier() {
        return Math.sqrt(Math.pow(xPositionBall - xPositionBarrier, 2) + Math.pow(yPositionBall - yPositionBarrier, 2));
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        xPositionBall += dxBall;
        yPositionBall += dyBall;

        if (distanceBetweenBallAndBarrier() < barrierRadius + ballRadius) {
            bouncePaintTimeLeft = paintChangeTime;
            // Move the ball back to be collision point
            while(distanceBetweenBallAndBarrier() < barrierRadius + ballRadius) {
                xPositionBall -= dxBall * 0.0001;
                yPositionBall -= dyBall * 0.0001;
            }

            // Compute the new position and direction
            double quota = (xPositionBarrier - xPositionBall)/(yPositionBall - yPositionBarrier);
            double divExpr = 1 + Math.pow(Math.abs(quota), 2);
            float newdxBall = (float) (dxBall + (2 * quota * (dyBall - dxBall * quota))/divExpr);
            float newdyBall = (float) (dyBall + (2 * (-dyBall + dxBall * quota))/divExpr);
            dxBall = newdxBall;
            dyBall = newdyBall;
            xPositionBall += dxBall;
            yPositionBall += dyBall;
        }

        bouncePaintTimeLeft -= deltaTime;
        phantomPaintTimeLeft -= deltaTime;

        if (xPositionBall < ballRadius) {
            // Left of screen
            xPositionBall = 2 * ballRadius - xPositionBall;
            dxBall = -dxBall;
        } else if (xPositionBall > gameWidth - ballRadius) {
            // Right of screen
            xPositionBall = 2 * gameWidth - xPositionBall - 2 * ballRadius;
            dxBall = -dxBall;
        }
        if (yPositionBall < ballRadius) {
            // Above screen
            yPositionBall = 2 * ballRadius - yPositionBall;
            dyBall = -dyBall;
        } else if (yPositionBall > gameHeight - ballRadius) {
            // Below screen
            yPositionBall = 2 * gameHeight- yPositionBall - 2 * ballRadius;
            dyBall = -dyBall;
        }

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (Math.sqrt(Math.pow(xPositionBall - event.x, 2) + Math.pow(yPositionBall - event.y, 2)) > barrierRadius + ballRadius) {
                    // Does not collide, move the barrier to the touch point
                    yPositionBarrier = event.y;
                    xPositionBarrier = event.x;
                } else {
                    yPositionPhantomBarrier = event.y;
                    xPositionPhantomBarrier = event.x;
                    phantomPaintTimeLeft = paintChangeTime;
                }
            }
        }

        if (distanceToTargetArea() < 0) {
            state = GameState.Finished;
        }
    }

    @Override
    double percentDone() {
        return 1 - distanceToTargetArea() / gameHeight;
    }

    @Override
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        g.drawLine(targetX1, targetY, targetX2, targetY, targetPaint);

        // Draw some arrows pointing on the target
        g.drawArrow(targetX1/2, targetY - game.scaleY(400), targetX1, targetY - game.scaleY(150));
        g.drawArrow((gameWidth + targetX2)/2, targetY - game.scaleY(400), targetX2, targetY - game.scaleY(150));

        g.drawCircle(Math.round(xPositionBall), Math.round(yPositionBall), ballRadius, ballPaint);

        // Indicate bouncing by adding a shadow to the barrier
        if (bouncePaintTimeLeft > 0) {
            g.drawCircle(xPositionBarrier, yPositionBarrier, barrierRadius, barrierPaintWithShadow);
        } else {
            g.drawCircle(xPositionBarrier, yPositionBarrier, barrierRadius, barrierPaint);
        }

        // Draw a phantom barrier when trying to put it on top of the ball
        if (phantomPaintTimeLeft > 0) {
            g.drawCircle(xPositionPhantomBarrier, yPositionPhantomBarrier, barrierRadius, phantomBarrierPaint);
        }
    }

}
