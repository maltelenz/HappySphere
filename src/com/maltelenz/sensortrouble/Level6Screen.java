package com.maltelenz.sensortrouble;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
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
    private int pointSize = 100;
    private int currentPointY;
    private int currentPointX;
    private Paint pointPaint;
    private float gX;
    private float gY;
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private float speed = 2;
    private Path arrowPath;
    private Paint arrowPaint;

    public Level6Screen(Game game) {
        super(game);

        game.lockOrientationPortrait();

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        currentPointY = pointSize;
        currentPointX = gameWidth/2;

        pointPaint = new Paint();
        pointPaint.setColor(ColorPalette.laser);
        pointPaint.setAntiAlias(true);
        pointPaint.setShadowLayer(10.0f, 2.0f, 2.0f, ColorPalette.buttonShadow);

        arrowPaint = new Paint();
        arrowPaint.setColor(ColorPalette.inactiveProgress);
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStyle(Style.FILL);
//        arrowPaint.setShadowLayer(10.0f, 2.0f, 2.0f, ColorPalette.buttonShadow);

        arrowPath = new Path();
        arrowPath.moveTo(Math.round(2.0 * gameWidth/5), Math.round(1.0 * gameHeight/5));
        arrowPath.lineTo(Math.round(2.0 * gameWidth/5), Math.round(1.0 * gameHeight/5));
        arrowPath.lineTo(Math.round(3.0 * gameWidth/5), Math.round(1.0 * gameHeight/5));
        arrowPath.lineTo(Math.round(3.0 * gameWidth/5), Math.round(3.0 * gameHeight/5));
        arrowPath.lineTo(Math.round(4.0 * gameWidth/5), Math.round(3.0 * gameHeight/5));
        arrowPath.lineTo(Math.round(2.5 * gameWidth/5), Math.round(4.0 * gameHeight/5));
        arrowPath.lineTo(Math.round(1.0 * gameWidth/5), Math.round(3.0 * gameHeight/5));
        arrowPath.lineTo(Math.round(2.0 * gameWidth/5), Math.round(3.0 * gameHeight/5));
        arrowPath.lineTo(Math.round(2.0 * gameWidth/5), Math.round(1.0 * gameHeight/5));
        arrowPath.close();

        sensorManager = (SensorManager) game.getContext().getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

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
        
        g.drawPath(arrowPath, arrowPaint);

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
        game.unLockOrientation();
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
