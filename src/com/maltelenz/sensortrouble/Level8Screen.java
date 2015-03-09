package com.maltelenz.sensortrouble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class Level8Screen extends LevelScreen {

    private int gameWidth;

    private int circleRadius;
    private ArrayList<PieCircle> circles;
    private int nCircles = 3;
    private int nPieces = 7;

    private int gameHeight;

    private int centerX;

    private int centerY;

    private HashSet<ArrayList<Integer>> allColors;

    public Level8Screen(Game game) {
        super(game);

        gameWidth = game.getGraphics().getWidth();
        gameHeight = game.getGraphics().getHeight();

        centerX = gameWidth/2;
        centerY = gameHeight/2;

        circleRadius = game.scale(gameWidth/nCircles/2);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int j = 0; j < nPieces; j++) {
            colors.add(new Random().nextInt(4));
        }
        circles =  new ArrayList<PieCircle>();
        for (int i = 0; i < nCircles; i++) {
            int rotations = new Random().nextInt(nPieces);
            Collections.rotate(colors, rotations);
            circles.add(new PieCircle(new ArrayList<Integer>(colors), i * circleRadius, (i + 1) * circleRadius, centerX, centerY));
        }
        Collections.reverse(circles);
        state = GameState.Running;
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        allColors = new HashSet<ArrayList<Integer>>();
        for (Iterator<PieCircle> iterator = circles.iterator(); iterator.hasNext();) {
            PieCircle pie = (PieCircle) iterator.next();
            allColors.add(pie.getColors());
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);
                if (event.type == TouchEvent.TOUCH_DOWN) {
                    pie.touch(event);
                }
            }
        }
        if (allColors.size() == 1) {
            state = GameState.Finished;
        }
    }

    @Override
    double percentDone() {
        return ((float) nCircles - allColors.size()) / (nCircles - 1);
    }

    @Override
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        for (Iterator<PieCircle> iterator = circles.iterator(); iterator.hasNext();) {
            PieCircle pie = (PieCircle) iterator.next();
            g.drawPie(pie);
        }
    }

}
