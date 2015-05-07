package com.laserfountain.sensortrouble;

import com.laserfountain.framework.Game;
import com.laserfountain.sensortrouble.GridArea.Shape;

public class Level16Screen extends LaserLevel {
    public Level16Screen(Game game) {
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
                if (x == 2 && y == 6) {
                    shape = Shape.Box;
                }
                if (x == 3 && y == 5) {
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
