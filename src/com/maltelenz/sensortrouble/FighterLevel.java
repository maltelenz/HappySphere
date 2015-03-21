package com.maltelenz.sensortrouble;

import java.util.Iterator;
import java.util.List;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Input.TouchEvent;

public class FighterLevel extends LabyrinthLevel {

    private float speedupFactor = 2;

    public FighterLevel(Game game) {
        super(game);
    }

    @Override
    void updateAccelerations(List<TouchEvent> touchEvents, float deltaTime) {
        gX = gX/1.2f;
        gY = gY/1.2f;
        for (Iterator<TouchEvent> iterator = touchEvents.iterator(); iterator.hasNext();) {
            TouchEvent event = (TouchEvent) iterator.next();
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
