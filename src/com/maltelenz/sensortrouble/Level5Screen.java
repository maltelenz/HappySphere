package com.maltelenz.sensortrouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class Level5Screen extends LevelScreen {

    private double veryLargeNumber = 10000000;
    float margin = 150;

    private float failureDrawTime = 200;
    float timeLeft = failureDrawTime;

    private boolean drawingStarted = false;

    private boolean drawFailure = false;

    int nrTouchesActive = 0;

    List<TouchPoint> path;
    private int nrTouched = 0;
    private float pointRadius;

    public Level5Screen(Game game) {
        super(game);

        int w = game.getGraphics().getWidth();
        int h = game.getGraphics().getHeight();

        // Initialize data
        path = new ArrayList<TouchPoint>();
        path.add(new TouchPoint(w/2, 0));
        path.add(new TouchPoint((3239*w)/6480, h/30));
        path.add(new TouchPoint((817*w)/1620, h/15));
        path.add(new TouchPoint((41*w)/80, h/10));
        path.add(new TouchPoint((212*w)/405, (2*h)/15));
        path.add(new TouchPoint((695*w)/1296, h/6));
        path.add(new TouchPoint((11*w)/20, h/5));
        path.add(new TouchPoint((3653*w)/6480, (7*h)/30));
        path.add(new TouchPoint((467*w)/810, (4*h)/15));
        path.add(new TouchPoint((47*w)/80, (3*h)/10));
        path.add(new TouchPoint((193*w)/324, h/3));
        path.add(new TouchPoint((3889*w)/6480, (11*h)/30));
        path.add(new TouchPoint((3*w)/5, (2*h)/5));
        path.add(new TouchPoint((1237*w)/2160, (13*h)/30));
        path.add(new TouchPoint((293*w)/540, (7*h)/15));
        path.add(new TouchPoint((41*w)/80, h/2));
        path.add(new TouchPoint((131*w)/270, (8*h)/15));
        path.add(new TouchPoint((1001*w)/2160, (17*h)/30));
        path.add(new TouchPoint((9*w)/20, (3*h)/5));
        path.add(new TouchPoint((6397*w)/12960, (19*h)/30));
        path.add(new TouchPoint((44*w)/81, (2*h)/3));
        path.add(new TouchPoint((19*w)/32, (7*h)/10));
        path.add(new TouchPoint((1037*w)/1620, (11*h)/15));
        path.add(new TouchPoint((8777*w)/12960, (23*h)/30));
        path.add(new TouchPoint((7*w)/10, (4*h)/5));
        path.add(new TouchPoint((1823*w)/2592, (5*h)/6));
        path.add(new TouchPoint((221*w)/324, (13*h)/15));
        path.add(new TouchPoint((101*w)/160, (9*h)/10));
        path.add(new TouchPoint((221*w)/405, (14*h)/15));
        path.add(new TouchPoint((5447*w)/12960, (29*h)/30));
        path.add(new TouchPoint(w/4, h));

        pointRadius = margin;
    }

    @Override
    protected void updateGameInitializing(float deltaTime) {
        for (Iterator<TouchPoint> iterator = path.iterator(); iterator.hasNext();) {
            TouchPoint point = (TouchPoint) iterator.next();
            point.touched = false;
        }
        drawFailure = false;
        drawingStarted = false;
        state = GameState.Running;
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        timeLeft -= deltaTime;
        if (drawFailure && timeLeft < 0) {
            state = GameState.Initializing;
        }

        double minDistanceFromPoints = veryLargeNumber;
        
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                nrTouchesActive++;
            } else if (event.type == TouchEvent.TOUCH_UP) {
                nrTouchesActive = Math.max(nrTouchesActive - 1, 0);
            }
            
            if (!drawFailure) {
                drawingStarted = true;
                boolean thisEventTouchedAPoint = false;
                for (Iterator<TouchPoint> iterator = path.iterator(); iterator.hasNext();) {
                    TouchPoint point = (TouchPoint) iterator.next();
                    double distance = point.distance(event);
                    minDistanceFromPoints = Math.min(minDistanceFromPoints, distance);
                    if (distance < margin) {
                        // Inside allowed touch area
                        point.touched = true;
                        thisEventTouchedAPoint = true;
                    }
                }
                if (!thisEventTouchedAPoint) {
                    drawFailure = true;
                    drawingStarted = false;
                    timeLeft = failureDrawTime;
                }
            }
        }

        if (drawingStarted && (
                (len != 0 && minDistanceFromPoints > margin) ||
                nrTouchesActive == 0)) {
            // User outside area or stopped touching
            drawFailure = true;
            drawingStarted = false;
            timeLeft = failureDrawTime;
            return;
        }

        int touchedCounter = 0;
        for (Iterator<TouchPoint> iterator = path.iterator(); iterator.hasNext();) {
            TouchPoint point = (TouchPoint) iterator.next();
            if (point.touched) {
                touchedCounter++;
            }
        }
        nrTouched = touchedCounter;
        if (nrTouched == path.size()) {
            state = GameState.Finished;
        }
    }

    @Override
    float percentDone() {
        return (float) nrTouched/path.size();
    }

    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);
        for (Iterator<TouchPoint> iterator = path.iterator(); iterator.hasNext();) {
            TouchPoint point = (TouchPoint) iterator.next();
            g.drawPoint(point, Math.round(pointRadius));
        }

        if (drawFailure) {
            g.drawOopsieString();
        }
    }

}
