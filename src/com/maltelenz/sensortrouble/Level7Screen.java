package com.maltelenz.sensortrouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class Level7Screen extends LevelScreen {

    private int gameHeight;
    private int gameWidth;

    private int barrierWidth;
    private int barrierHeight;

    private int currentPoint;
    private Paint barrierPaint;
    private Paint targetPaint;
    private Paint shotPaint;
    private List<Barrier> barriersLeft;
    private List<Barrier> targetBarriers;
    private List<Point> shots;
    private float shotWidth;
    private int speed;
    private int shotSize;
    private int shotSpeed;
    private int shooterHeight;
    private int barrierPadding;
    private int maxShots;
    private float nrTargetBarriers;

    public Level7Screen(Game game) {
        super(game);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        barrierPadding = game.scaleY(10);
        barrierWidth = gameWidth/5 - barrierPadding;
        barrierHeight = gameHeight/20;

        shooterHeight = game.scaleY(100);

        currentPoint = 0;
        
        maxShots = 5;

        shotWidth = game.scaleX(15);
        shotSize = game.scaleY(20);

        speed = game.scaleX(20);
        shotSpeed = game.scaleY(15);

        barriersLeft = new ArrayList<Barrier>();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                barriersLeft.add(new Barrier(barrierWidth * x + barrierPadding * (x + 1), barrierHeight * (2 + y) + barrierPadding * (y + 1), barrierWidth * (x + 1) + barrierPadding * x, barrierHeight * (3 + y) + barrierPadding * y));
            }
        }

        targetBarriers = new ArrayList<Barrier>();
        for (int x = 0; x < 5; x++) {
            targetBarriers.add(new Barrier(barrierWidth * x + barrierPadding * (x + 1), progressBarHeight, barrierWidth * (x + 1) + barrierPadding * x, progressBarHeight + barrierHeight));
        }
        nrTargetBarriers = targetBarriers.size();

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

        state = GameState.Running;
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN && maxShots > shots.size()) {
                shots.add(new Point(currentPoint, gameHeight - game.scaleY(shooterHeight)));
            }
        }
        
        ArrayList<Point> shotsToRemove = new ArrayList<Point>();
        ArrayList<Barrier> barriersToRemove = new ArrayList<Barrier>();
        for (Iterator<Point> iterator = shots.iterator(); iterator.hasNext();) {
            Point p = (Point) iterator.next();
            p.y -= shotSpeed;
            if (p.y < 0) {
                // Off screen
                shotsToRemove.add(p);
                continue;
            }
            for (Iterator<Barrier> iterator2 = barriersLeft.iterator(); iterator2.hasNext();) {
                Barrier b = (Barrier) iterator2.next();
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
        for (Iterator<Point> iterator = shots.iterator(); iterator.hasNext();) {
            Point p = (Point) iterator.next();
            p.y -= shotSpeed;
            for (Iterator<Barrier> iterator2 = targetBarriers.iterator(); iterator2.hasNext();) {
                Barrier b = (Barrier) iterator2.next();
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
        
        for (Iterator<Barrier> iterator = barriersLeft.iterator(); iterator.hasNext();) {
            Barrier b = (Barrier) iterator.next();
            g.drawBarrier(b, barrierPaint);
        }

        for (Iterator<Barrier> iterator = targetBarriers.iterator(); iterator.hasNext();) {
            Barrier b = (Barrier) iterator.next();
            g.drawBarrier(b, targetPaint);
        }

        for (Iterator<Point> iterator = shots.iterator(); iterator.hasNext();) {
            Point p = (Point) iterator.next();
            g.drawCircle(p.x, p.y + shotSize, shotSize, shotPaint);
        }

        g.drawShooter(currentPoint, shooterHeight, ((float) maxShots - shots.size()) / maxShots);
    }

}
