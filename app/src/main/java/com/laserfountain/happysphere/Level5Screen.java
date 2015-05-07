package com.laserfountain.happysphere;

import android.graphics.Point;

import com.laserfountain.framework.Game;

public class Level5Screen extends LineDragScreen {

    public Level5Screen(Game game) {
        super(game);

        int dy = gameHeight/100;
        for (int y = maxDeviation; y < gameHeight; y+=dy) {
            drawingPoints.add(new Point(getXValue(y), y));
        }

        currentPointY = drawingPoints.get(0).y;
        currentPointX = drawingPoints.get(0).x;

        state = GameState.Running;
    }

    protected int getXValue(int y) {
        double factor = 5.0/gameHeight;
        return (int) Math.round(gameWidth * 0.5 * (1.1 + Math.sin(y * factor) * Math.cos(1.5 * y * factor)));
    }

}
