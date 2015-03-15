package com.maltelenz.sensortrouble;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class Level6Screen extends LevelScreen implements SensorEventListener {

    private int gameHeight;
    private int gameWidth;
    private int pointSize;
    private int currentPointY;
    private int currentPointX;
    private Paint pointPaint;
    private float gX;
    private float gY;
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private float speed;
    private int targetThickness;

    public Level6Screen(Game game) {
        super(game);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        pointSize = game.scale(100);

        speed = game.scaleY(4);

        currentPointY = pointSize;
        currentPointX = gameWidth/2;

        pointPaint = new Paint();
        pointPaint.setColor(ColorPalette.laser);
        pointPaint.setAntiAlias(true);
        pointPaint.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);

        sensorManager = (SensorManager) game.getContext().getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        targetThickness = game.scaleY(50);

        state = GameState.Running;
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        currentPointY = Math.max(currentPointY + Math.round(deltaTime * gY * speed), pointSize);
        currentPointX = Math.max(Math.min(currentPointX - Math.round(deltaTime * gX * speed), gameWidth - pointSize), pointSize);
        if (currentPointY >= gameHeight - pointSize) {
            state = GameState.Finished;
        }
    }

    @Override
    double percentDone() {
        // The - pointSize and - 2 * pointSize are because there are buffers at the top and bottom of the screen
        return (currentPointY - pointSize)/((double) gameHeight - 2 * pointSize);
    }

    @Override
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

//        g.drawArrow(gameWidth/2, gameHeight/6, gameWidth/2, 5 * gameHeight/6);
        for (int i = 1; i <= 4; i++) {
            g.drawArrow(i * gameWidth/5, gameHeight - game.scaleY(400), i * gameWidth/5, gameHeight - game.scaleY(150));
        }

        g.drawTargetLine(0, gameHeight, gameWidth, gameHeight, targetThickness);

        g.drawCircle(currentPointX, currentPointY, pointSize, pointPaint);
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
        gX = gX/3 + 2 * event.values[0]/3;
        gY = gY/3 + 2 * event.values[1]/3;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
