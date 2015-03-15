package com.maltelenz.sensortrouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class Level10Screen extends LevelScreen implements SensorEventListener {

    private int gameHeight;
    private int gameWidth;

    private int pointSize;
    private PointF currentPoint;
    private Paint pointPaint;

    private float gX;
    private float gY;

    private SensorManager sensorManager;
    private Sensor gravitySensor;

    private float speed;
    private float vX;
    private float vY;

    private int targetThickness;

    private List<Barrier> barriers;
    private List<Barrier> gameSideBarriers;
    private Paint barrierPaint;
    private int barrierHeight;

    public Level10Screen(Game game) {
        super(game);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        pointSize = game.scale(50);

        speed = game.scaleY(0.1f);
        vX = 0;
        vY = 0;

        currentPoint = new PointF(gameWidth/2, pointSize * 2);

        pointPaint = new Paint();
        pointPaint.setColor(ColorPalette.laser);
        pointPaint.setAntiAlias(true);
        pointPaint.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);

        sensorManager = (SensorManager) game.getContext().getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        barrierHeight = game.scaleY(100);

        barriers = new ArrayList<Barrier>();

        barriers.add(new Barrier(gameWidth/3, gameHeight/2, 2 * gameWidth/3, gameHeight/2 + barrierHeight));
        barriers.add(new Barrier(0, gameHeight/2 + game.scaleY(300), 3 * gameWidth/9, gameHeight/2 + game.scaleY(300) + barrierHeight));
        barriers.add(new Barrier(6 * gameWidth/9, gameHeight/2 + game.scaleY(300), gameWidth, gameHeight/2 + game.scaleY(300) + barrierHeight));

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

        targetThickness = game.scaleY(50);

        state = GameState.Running;
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
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

    @Override
    double percentDone() {
        // The - pointSize and - 2 * pointSize are because there are buffers at the top and bottom of the screen
        return (currentPoint.y - pointSize)/((double) gameHeight - 2 * pointSize);
    }

    @Override
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        for (int i = 1; i <= 4; i++) {
            g.drawArrow(i * gameWidth/5, gameHeight - game.scaleY(400), i * gameWidth/5, gameHeight - game.scaleY(150));
        }

        for (Iterator<Barrier> iterator = barriers.iterator(); iterator.hasNext();) {
            Barrier b = (Barrier) iterator.next();
            g.drawBarrier(b, barrierPaint);
        }

        g.drawTargetLine(0, gameHeight, gameWidth, gameHeight, targetThickness);

        g.drawCircle(Math.round(currentPoint.x), Math.round(currentPoint.y), pointSize, pointPaint);
    }

    @Override
    public void resume() {
        super.resume();
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void pause() {
        super.pause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_GRAVITY) {
            return;
        }
        gX = event.values[0];
        gY = event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
