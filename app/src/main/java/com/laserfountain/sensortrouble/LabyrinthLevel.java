package com.laserfountain.sensortrouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;

import com.laserfountain.framework.Game;
import com.laserfountain.framework.Graphics;
import com.laserfountain.framework.Input.TouchEvent;

public class LabyrinthLevel extends LevelScreen {

    protected int gameHeight;
    protected int gameWidth;

    private int pointSize;
    protected PointF currentPoint;
    private Paint pointPaint;

    protected float gX;
    protected float gY;

    private float speed;
    private float vX;
    private float vY;

    private int targetThickness;

    protected List<Barrier> barriers;
    protected List<Barrier> dangerBarriers;
    private List<Barrier> gameSideBarriers;
    private Paint barrierPaint;
    private Paint dangerBarrierPaint;
    protected int barrierHeight;
    protected int startPointX;
    protected int startPointY;
    private Paint exhaustPaint;

    public LabyrinthLevel(Game game) {
        super(game);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        pointSize = game.scale(50);

        speed = game.scaleY(0.1f);
        vX = 0;
        vY = 0;

        startPointX = gameWidth/2;
        startPointY = pointSize * 2;

        currentPoint = new PointF(startPointX, startPointY);

        pointPaint = new Paint();
        pointPaint.setColor(ColorPalette.laser);
        pointPaint.setAntiAlias(true);
        pointPaint.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);

        barrierHeight = game.scaleY(100);

        barriers = new ArrayList<Barrier>();

        dangerBarriers = new ArrayList<Barrier>();

        // Barriers around the top, left and right side of the game
        gameSideBarriers = new ArrayList<Barrier>();
        gameSideBarriers.add(new Barrier(0, -barrierHeight, gameWidth, 0));
        gameSideBarriers.add(new Barrier(-barrierHeight, 0, 0, gameHeight));
        gameSideBarriers.add(new Barrier(gameWidth, 0, gameWidth + barrierHeight, gameHeight));

        barrierPaint = new Paint();
        barrierPaint.setColor(ColorPalette.box);
        barrierPaint.setAntiAlias(true);
        barrierPaint.setStyle(Style.FILL_AND_STROKE);
        barrierPaint.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);

        dangerBarrierPaint = new Paint();
        dangerBarrierPaint.set(barrierPaint);
        dangerBarrierPaint.clearShadowLayer();
        dangerBarrierPaint.setColor(ColorPalette.oopsie);

        exhaustPaint = new Paint();
        exhaustPaint.setColor(ColorPalette.laser);
        exhaustPaint.setStrokeWidth(game.scale(10f));

        targetThickness = game.scaleY(50);
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        
        updateAccelerations(touchEvents, deltaTime);
        
        vX = vX + gX;
        vY = vY + gY;

        PointF newPoint = new PointF(
                currentPoint.x - deltaTime * vX * speed,
                currentPoint.y + deltaTime * vY * speed
            );
        PointF originalNewPoint = new PointF();
        originalNewPoint.set(newPoint);

        // Handle collisions
        for (Iterator<Barrier> iterator = barriers.iterator(); iterator.hasNext();) {
            Barrier b = (Barrier) iterator.next();
            newPoint = b.move(currentPoint, newPoint, pointSize);
        }
        for (Iterator<Barrier> iterator = gameSideBarriers.iterator(); iterator.hasNext();) {
            Barrier b = (Barrier) iterator.next();
            newPoint = b.move(currentPoint, newPoint, pointSize);
        }

        // Handle danger barriers (reset the game)
        for (Iterator<Barrier> iterator = dangerBarriers.iterator(); iterator.hasNext();) {
            Barrier b = (Barrier) iterator.next();
            if (b.inBounds(newPoint, pointSize)) {
                newPoint = new PointF(startPointX, startPointY);
            }
        }

        // Check if there was a collision
        if (originalNewPoint.x != newPoint.x) {
            vX = 0;
        }
        if (originalNewPoint.y != newPoint.y) {
            vY = 0;
        }

        currentPoint = newPoint;

        if (currentPoint.y >= gameHeight - pointSize) {
            state = GameState.Finished;
        }
    }

    
    /**
     * Updates the acceleration variables gX and gY
     */
    void updateAccelerations(List<TouchEvent> touchEvents, float deltaTime) { }

    @Override
    double percentDone() {
        // The - pointSize and - 2 * pointSize are because there are buffers at the top and bottom of the screen
        return (currentPoint.y - pointSize)/((double) gameHeight - 2 * pointSize);
    }

    @Override
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        g.drawLine(currentPoint.x, currentPoint.y, currentPoint.x + gX * game.scale(100), currentPoint.y - gY * game.scale(100), exhaustPaint);

        for (int i = 1; i <= 4; i++) {
            g.drawArrow(i * gameWidth/5, gameHeight - game.scaleY(400), i * gameWidth/5, gameHeight - game.scaleY(150));
        }

        g.drawTargetLine(0, gameHeight, gameWidth, gameHeight, targetThickness);

        for (Iterator<Barrier> iterator = barriers.iterator(); iterator.hasNext();) {
            Barrier b = (Barrier) iterator.next();
            g.drawBarrier(b, barrierPaint);
        }

        for (Iterator<Barrier> iterator = dangerBarriers.iterator(); iterator.hasNext();) {
            Barrier b = (Barrier) iterator.next();
            g.drawBarrier(b, dangerBarrierPaint);
        }

        g.drawCircle(Math.round(currentPoint.x), Math.round(currentPoint.y), pointSize, pointPaint);
    }
}
