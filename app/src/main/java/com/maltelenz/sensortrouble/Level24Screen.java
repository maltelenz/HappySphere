package com.maltelenz.sensortrouble;

import android.graphics.Point;

import com.maltelenz.framework.Game;

public class Level24Screen extends LineDragScreen {

    public Level24Screen(Game game) {
        super(game);

        int dx = gameWidth/6;
        int dy = gameHeight/5;
        drawingPoints.add(new Point(3 * dx, 0));
        drawingPoints.add(new Point(3 * dx, dy));
        drawingPoints.add(new Point(4 * dx, dy));
        drawingPoints.add(new Point(4 * dx, 2 * dy));
        drawingPoints.add(new Point(2 * dx, 2 * dy));
        drawingPoints.add(new Point(2 * dx, 3 * dy));
        drawingPoints.add(new Point(3 * dx, 3 * dy));
        drawingPoints.add(new Point(3 * dx, 4 * dy));
        drawingPoints.add(new Point(2 * dx, 4 * dy));
        drawingPoints.add(new Point(2 * dx, 5 * dy));

        currentPointY = drawingPoints.get(0).y;
        currentPointX = drawingPoints.get(0).x;

        state = GameState.Running;
    }
}
