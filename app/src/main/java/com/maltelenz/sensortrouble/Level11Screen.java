package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Game;
import com.maltelenz.sensortrouble.GridArea.Shape;

public class Level11Screen extends LaserLevel {
    public Level11Screen(Game game) {
        super(game);

        // Initialize data
        for (int x = 0; x < nrX; x++) {
            for (int y = 0; y < nrY; y++) {
                Shape shape = Shape.Empty;
                int rotation = 0;

                if (x == 1 && y == 4) {
                    shape = Shape.Box;
                }
                if (x == 1 && y == 2) {
                    shape = Shape.Box;
                }
                if (x == 4 && y == 1) {
                    shape = Shape.Triangle;
                }
                if (x == 4 && y == 6) {
                    shape = Shape.Triangle;
                }
                if (x == 1 && y == 6) {
                    shape = Shape.Triangle;
                }
                if (x == 0 && y == 3) {
                    shape = Shape.Triangle;
                }
                if (x == 0 && y == 5) {
                    shape = Shape.Triangle;
                    rotation = 270;
                }
                if (x == 0 && y == 1) {
                    shape = Shape.Laser;
                    rotation = 270;
                }
                if (x == 1 && y == 5) {
                    shape = Shape.Laser;
                }
                if (x == 3 && y == 6) {
                    shape = Shape.Target;
                }
                if (x == 1 && y == 3) {
                    shape = Shape.Target;
                }

                grid.add(new GridArea(
                        x,
                        y,
                        xOffset + x * boxWidth,
                        yOffset + y * boxWidth,
                        xOffset + (x + 1) * boxWidth,
                        yOffset + (y + 1) * boxWidth,
                        shape,
                        rotation
                ));
            }
        }

        nrLasers = 2;

        state = GameState.Running;
    }

}
