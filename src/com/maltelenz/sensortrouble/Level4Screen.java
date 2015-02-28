package com.maltelenz.sensortrouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;
import com.maltelenz.sensortrouble.GridArea.LaserDirection;
import com.maltelenz.sensortrouble.GridArea.Shape;

public class Level4Screen extends LevelScreen {

    List<GridArea> grid;
    List<GridArea> touched;
    private boolean currentlyLasering = false;
    private float timeToFinish = 300;
    private float timeLeftLasering = timeToFinish;
    int nrX = 5;
    int nrY = 7;

    public Level4Screen(Game game) {
        super(game);

        int boxWidth = Math.min(game.getGraphics().getWidth()/nrX, game.getGraphics().getHeight()/nrY);
        int xOffset = (game.getGraphics().getWidth() - nrX * boxWidth)/2;
        int yOffset = (game.getGraphics().getHeight() - nrY * boxWidth)/2;
        
        // Initialize data
        grid = new ArrayList<GridArea>();
        for (int x = 0; x < nrX; x++) {
            for (int y = 0; y < nrY; y++) {
                Shape shape = Shape.Empty;
                int rotation = 0;
                
                if (x == 2 && y == 3) {
                    shape = Shape.Box;
                }
                if (x == 1 && y == 1) {
                    shape = Shape.Triangle;
                }
                if (x == 1 && y == 6) {
                    shape = Shape.Triangle;
                }
                if (x == 0 && y == 0) {
                    shape = Shape.Triangle;
                }
                if (x == 3 && y == 0) {
                    shape = Shape.Triangle;
                    rotation = 270;
                }
                if (x == 0 && y == 1) {
                    shape = Shape.Laser;
                    rotation = 270;
                }
                if (x == 3 && y == 6) {
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

        touched = new ArrayList<GridArea>();

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
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            touched = new ArrayList<GridArea>();
            for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
                GridArea area = (GridArea) iterator.next();
                area.lasered = false;
                if(area.inBounds(event)) {
                    if (event.type == TouchEvent.TOUCH_DOWN) {
                        area.rotation = area.getRotation() + 90;
                    }
                    if (game.getInput().isTouchDown(event.pointer)) {
                        touched.add(area);
                    }
                }
            }
        }

        for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
            GridArea area = (GridArea) iterator.next();
            if(area.shape == Shape.Laser) {
                followLaser(area, 0, 0, false);
            }
        }
    }

    void followLaser(GridArea area, int dx, int dy, boolean passedLaser) {
        if (area.x < 0 || area.x > nrX ||
                area.y < 0 || area.y > nrY ) {
            return;
        }
        int nextGridPointX = 0;
        int nextGridPointY = 0;
        int nextdx = dx;
        int nextdy = dy;
        boolean nowHavePassedLaser = false;

        //Target lit, level soon finished
        if (area.shape == Shape.Target) {
            currentlyLasering  = true;
            return;
        }

        // Box, they eat lasers
        if (area.shape == Shape.Box) {
            return;
        }

        // Empty space, laser goes in current direction
        if (area.shape == Shape.Empty) {
        }

        // New laser, it will shoot its own laser
        if (area.shape == Shape.Laser) {
            if (passedLaser) {
                //Already passed a laser, we can stop here
                return;
            }
            nextdx = 0;
            nextdy = 0;
            switch (area.getRotation()) {
            case 0:
                nextdx = 1;
                break;
            case 90:
                nextdy = 1;
                break;
            case 180:
                nextdx = -1;
                break;
            case 270:
                nextdy = -1;
                break;
            }
            nowHavePassedLaser = true;
        }

        // Triangle, they redirect lasers
        if (area.shape == Shape.Triangle) {
            nextdx = 0;
            nextdy = 0;
            switch (area.getRotation()) {
            case 0:
                switch (dx) {
                case 1:
                    nextdy = 1;
                    break;
                case -1:
                    area.lasered = false;
                    return;
                case 0:
                    switch (dy) {
                    case 1:
                        area.lasered = false;
                        return;
                    case -1:
                        nextdx = -1;
                        break;
                    }
                }
                break;
            case 90:
                switch (dx) {
                case 1:
                    area.lasered = false;
                    return;
                case -1:
                    nextdy = 1;
                    break;
                case 0:
                    switch (dy) {
                    case 1:
                        area.lasered = false;
                        return;
                    case -1:
                        nextdx = 1;
                        break;
                    }
                }
                break;
            case 180:
                switch (dx) {
                case 1:
                    area.lasered = false;
                    return;
                case -1:
                    nextdy = -1;
                    break;
                case 0:
                    switch (dy) {
                    case 1:
                        nextdx = 1;
                        break;
                    case -1:
                        area.lasered = false;
                        return;
                    }
                }
                break;
            case 270:
                switch (dx) {
                case 1:
                    nextdy = -1;
                    break;
                case -1:
                    area.lasered = false;
                    return;
                case 0:
                    switch (dy) {
                    case 1:
                        nextdx = -1;
                        break;
                    case -1:
                        area.lasered = false;
                        return;
                    }
                }
                break;
            default:
                // Unknown rotation, better bail out
                area.lasered = false;
                return;
            }
        }

        nextGridPointX = area.x + nextdx;
        nextGridPointY = area.y + nextdy;

        for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
            GridArea a = (GridArea) iterator.next();
            if(a.x == nextGridPointX && a.y == nextGridPointY) {
                a.lasered = true;
                if (nextdx == 1) {
                    a.inCominglaserDirection = LaserDirection.Left;
                } else if (nextdx == -1) {
                    a.inCominglaserDirection = LaserDirection.Right;
                } else if (nextdy == 1) {
                    a.inCominglaserDirection = LaserDirection.Top;
                } else {
                    a.inCominglaserDirection = LaserDirection.Bottom;
                }

                followLaser(a, nextdx, nextdy, nowHavePassedLaser);
                return;
            }
        }
    }

    @Override
    double percentDone() {
        return 1 - timeLeftLasering/timeToFinish;
    }

    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);
        for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
            GridArea area = (GridArea) iterator.next();
            int width = area.x1 - area.x0;
            int height = area.y1 - area.y0;
            if (area.shape == Shape.Empty) {
                g.drawRectNoFill(area.x0, area.y0, width, height, ColorPalette.gridLines);
                if (area.lasered) {
                    if (area.isInComingHorizontal()) {
                        g.drawLaserLine(area.x0, area.y0 + height/2, area.x1, area.y0 + height/2);
                    } else {
                        g.drawLaserLine(area.x0 + width/2, area.y0, area.x0 + width/2, area.y1);
                    }
                }
            } else if (area.shape == Shape.Box) {
                g.drawRect(area.x0, area.y0, width, height, ColorPalette.box);
            } else if (area.shape == Shape.Triangle) {
                g.drawTriangle(area.x0, area.y0, width, height, area.getRotation(), ColorPalette.button, area.lasered);
            } else if (area.shape == Shape.Laser) {
                g.drawRectNoFill(area.x0, area.y0, width, height, ColorPalette.gridLines);
                g.drawLaser(area.x0, area.y0, width, height, area.getRotation());
            } else if (area.shape == Shape.Target) {
                g.drawRectNoFill(area.x0, area.y0, width, height, ColorPalette.gridLines);
                g.drawTarget(area.x0, area.y0, width, height, area.getLaserDirection(), area.lasered);
            }
        }
        for (Iterator<GridArea> iterator = touched.iterator(); iterator.hasNext();) {
            GridArea area = (GridArea) iterator.next();
            g.drawRect(area.x0, area.y0, area.x1 - area.x0, area.y1 - area.y0, ColorPalette.progress);
        }
    }

}
