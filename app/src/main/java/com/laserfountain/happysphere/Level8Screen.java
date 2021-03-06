package com.laserfountain.happysphere;

import com.laserfountain.framework.Game;
import com.laserfountain.framework.Graphics;
import com.laserfountain.framework.Input.TouchEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Level8Screen extends LevelScreen {

    private ArrayList<PieCircle> circles;
    private int nCircles = 3;
    private int nPieces = 7;

    private boolean currentlyLasering = false;
    private float timeToFinish = 150;
    private float timeLeftLasering = timeToFinish;

    private HashSet<ArrayList<Integer>> allColors;

    public Level8Screen(Game game) {
        super(game);

        int gameWidth = game.getGraphics().getWidth();
        int gameHeight = game.getGraphics().getHeight();

        int centerX = gameWidth / 2;
        int centerY = gameHeight / 2;

        float circleRadius = gameWidth * 0.9f / 2 / nCircles;

        ArrayList<Integer> colors = new ArrayList<Integer>();
        Random rand = new Random();
        for (int j = 0; j < nPieces; j++) {
            colors.add(rand.nextInt(4));
        }
        circles =  new ArrayList<PieCircle>();
        for (int i = 0; i < nCircles; i++) {
            int rotations = rand.nextInt(nPieces);
            Collections.rotate(colors, rotations);
            circles.add(new PieCircle(new ArrayList<Integer>(colors), i * circleRadius, (i + 1) * circleRadius, centerX, centerY));
        }
        Collections.reverse(circles);
        state = GameState.Running;
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        if (currentlyLasering) {
            timeLeftLasering  -= deltaTime;
            if (timeLeftLasering < 0) {
                state = GameState.Finished;
            }
        } else {
            //Not lasering, progress goes backwards
            timeLeftLasering = Math.min(timeToFinish, timeLeftLasering + deltaTime);
        }
        // Assume not lasering unless found shown otherwise below.
        currentlyLasering = false;

        int len = touchEvents.size();
        allColors = new HashSet<ArrayList<Integer>>();
        for (PieCircle pie : circles) {
            allColors.add(pie.getColors());
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);
                if (event.type == TouchEvent.TOUCH_DOWN) {
                    pie.touch(event);
                }
            }
        }
        if (allColors.size() == 1) {
            currentlyLasering = true;
        }
    }

    @Override
    double percentDone() {
        return (((float) nCircles - allColors.size()) / (nCircles - 1))/2 + (1 - timeLeftLasering/timeToFinish)/2;
    }

    @Override
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        for (PieCircle pie : circles) {
            g.drawPie(pie);
        }
    }

}
