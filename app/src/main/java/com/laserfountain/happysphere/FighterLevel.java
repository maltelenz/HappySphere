package com.laserfountain.happysphere;

import com.laserfountain.framework.Game;
import com.laserfountain.framework.Input.TouchEvent;

import java.util.List;

public class FighterLevel extends LabyrinthLevel {

    private float speedupFactor = 2;

    public FighterLevel(Game game) {
        super(game);
    }

    @Override
    void updateAccelerations(List<TouchEvent> touchEvents, float deltaTime) {
        gX = gX/1.2f;
        gY = gY/1.2f;
        for (TouchEvent event : touchEvents) {
            if (event.type == TouchEvent.TOUCH_DOWN) {
                gX = -(currentPoint.x - event.x);
                gY = (currentPoint.y - event.y);

                float maxForce = Math.max(Math.abs(gX), Math.abs(gY));

                gX = speedupFactor * gX / maxForce;
                gY = speedupFactor * gY / maxForce;
            }
        }
    }
}
