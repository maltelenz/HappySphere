package com.laserfountain.happysphere;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

import com.laserfountain.framework.Game;
import com.laserfountain.framework.Graphics;
import com.laserfountain.framework.Input.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class ShooterLevel extends LevelScreen {

    private int gameHeight;
    private int gameWidth;

    protected int barrierWidth;
    protected int barrierHeight;

    private int currentPoint;
    private Paint barrierPaint;
    private Paint targetPaint;
    private Paint dangerPaint;
    private Paint shotPaint;
    protected List<Barrier> barriersLeft;
    protected List<Barrier> dangerBarriers;
    protected List<Barrier> targetBarriers;
    private List<Point> shots;
    private int speed;
    private int shotSize;
    private int shotSpeed;
    private int shooterHeight;
    protected int barrierPadding;
    private int maxShots;
    protected float nrTargetBarriers;
    private boolean failed = false;
    private float failureDrawTime = 100;

    public ShooterLevel(Game game) {
        super(game);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        barrierPadding = game.scaleY(10);
        barrierWidth = gameWidth/5 - barrierPadding;
        barrierHeight = gameHeight/20;

        shooterHeight = game.scaleY(100);

        currentPoint = 0;
        
        maxShots = 5;

        shotSize = game.scaleY(20);

        speed = game.scaleX(20);
        shotSpeed = game.scaleY(15);

        barriersLeft = new ArrayList<Barrier>();

        targetBarriers = new ArrayList<Barrier>();

        dangerBarriers = new ArrayList<Barrier>();

        shots = new ArrayList<Point>();

        shotPaint = new Paint();
        shotPaint.setColor(ColorPalette.laser);
        shotPaint.setAntiAlias(true);

        barrierPaint = new Paint();
        barrierPaint.setColor(ColorPalette.box);
        barrierPaint.setAntiAlias(true);
        barrierPaint.setStyle(Style.FILL_AND_STROKE);
        barrierPaint.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);

        targetPaint = new Paint();
        targetPaint.set(barrierPaint);
        targetPaint.setColor(ColorPalette.progress);

        dangerPaint = new Paint();
        dangerPaint.set(targetPaint);
        dangerPaint.setColor(ColorPalette.oopsie);
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        if (failed) {
            failureDrawTime -= deltaTime;
            if (failureDrawTime < 0) {
                // Restart level
                startLevel(currentLevel());
            }
            return;
        }

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN && maxShots > shots.size()) {
                shots.add(new Point(currentPoint, gameHeight - game.scaleY(shooterHeight)));
            }
        }
        
        ArrayList<Point> shotsToRemove = new ArrayList<Point>();
        ArrayList<Barrier> barriersToRemove = new ArrayList<Barrier>();
        for (Point p : shots) {
            p.y -= shotSpeed;
            if (p.y < 0) {
                // Off screen
                shotsToRemove.add(p);
                continue;
            }
            for (Barrier b : barriersLeft) {
                if (b.inBounds(p.x, p.y)) {
                    shotsToRemove.add(p);
                    barriersToRemove.add(b);
                    break;
                }
            }
        }
        barriersLeft.removeAll(barriersToRemove);
        shots.removeAll(shotsToRemove);

        ArrayList<Barrier> targetsToRemove = new ArrayList<Barrier>();
        ArrayList<Point> shotsToRemove2 = new ArrayList<Point>();
        for (Point p : shots) {
            p.y -= shotSpeed;
            for (Barrier b : targetBarriers) {
                if (b.inBounds(p.x, p.y)) {
                    targetsToRemove.add(b);
                    shotsToRemove2.add(p);
                    break;
                }
            }
        }
        targetBarriers.removeAll(targetsToRemove);
        shots.removeAll(shotsToRemove2);
        if (targetBarriers.size() == 0) {
            state = GameState.Finished;
        }

        for (Point p : shots) {
            p.y -= shotSpeed;
            for (Barrier b : dangerBarriers) {
                if (b.inBounds(p.x, p.y)) {
                    failed = true;
                    break;
                }
            }
        }
        targetBarriers.removeAll(targetsToRemove);
        shots.removeAll(shotsToRemove2);
        if (targetBarriers.size() == 0) {
            state = GameState.Finished;
        }

        currentPoint = (currentPoint + speed) % gameWidth;
    }

    @Override
    double percentDone() {
        return (nrTargetBarriers - targetBarriers.size()) / nrTargetBarriers;
    }

    @Override
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        for (Barrier b : barriersLeft) {
            g.drawBarrier(b, barrierPaint);
        }

        for (Barrier b : targetBarriers) {
            g.drawBarrier(b, targetPaint);
        }

        for (Barrier b : dangerBarriers) {
            g.drawBarrier(b, dangerPaint);
        }

        for (Point p : shots) {
            g.drawCircle(p.x, p.y + shotSize, shotSize, shotPaint);
        }

        g.drawShooter(currentPoint, shooterHeight, ((float) maxShots - shots.size()) / maxShots);

        if (failed) {
            g.drawOopsieString();
        }
    }

}
