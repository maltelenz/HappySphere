package com.laserfountain.happysphere;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.laserfountain.framework.Game;

public class GravityLevel extends LabyrinthLevel implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gravitySensor;

    public GravityLevel(Game game) {
        super(game);

        sensorManager = (SensorManager) game.getContext().getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
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
