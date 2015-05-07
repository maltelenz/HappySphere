package com.laserfountain.sensortrouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

import com.laserfountain.framework.Game;
import com.laserfountain.framework.Graphics;
import com.laserfountain.framework.Input.TouchEvent;
import com.laserfountain.sensortrouble.GridArea.LaserDirection;
import com.laserfountain.sensortrouble.GridArea.Shape;

public class LaserLevel extends LevelScreen {

    List<GridArea> grid;
    List<GridArea> touched;
    private int nrLasering = 0;
    protected int nrLasers;
    private float timeToFinish = 300;
    private float timeLeftLasering = timeToFinish;
    int nrX = 5;
    int nrY = 7;
    protected int xOffset;
    protected int yOffset;
    protected int boxWidth;
    private Paint arrowPaint;

    private boolean somethingIsSelected;

    public LaserLevel(Game game) {
        super(game);

        boxWidth = Math.min(game.getGraphics().getWidth()/nrX, game.getGraphics().getHeight()/nrY);
        xOffset = (game.getGraphics().getWidth() - nrX * boxWidth)/2;
        yOffset = (game.getGraphics().getHeight() - nrY * boxWidth)/2;

        grid = new ArrayList<GridArea>();

        touched = new ArrayList<GridArea>();

        arrowPaint = new Paint();
        arrowPaint.setAntiAlias(true);
        arrowPaint.setColor(ColorPalette.progress);
        arrowPaint.setStyle(Style.FILL_AND_STROKE);

        somethingIsSelected = false;
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        if (nrLasering >= nrLasers) {
            timeLeftLasering  -= deltaTime;
            if (timeLeftLasering < 0) {
                state = GameState.Finished;
            }
        } else {
            //Not lasering, progress goes backwards
            timeLeftLasering = Math.min(timeToFinish, timeLeftLasering + deltaTime);
        }
        // Assume not lasering unless found shown otherwise below.
        nrLasering = 0;

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            touched = new ArrayList<GridArea>();
            for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
                GridArea area = (GridArea) iterator.next();
                area.lasered = false;
                if(!somethingIsSelected && area.inBounds(event)) {
                    if (game.getInput().isTouchDown(event.pointer)) {
                        touched.add(area);
                    }
                    if (event.type == TouchEvent.TOUCH_DOWN) {
                        if (area.shape == Shape.Box) {
                            area.selected = true;
                        } else {
                            area.rotation = area.getRotation() + 90;
                        }

                    }
                } else if (event.type == TouchEvent.TOUCH_DOWN) {
                    if (area.selected) {
                        if (area.inBounds(event)) {
                            area.selected = false;
                        } else {
                            move(area, event);
                        }
                    }
                    area.selected = false;
                }
            }
        }

        // Clear lasers from last round and check if anything is selected
        somethingIsSelected = false;
        for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
            GridArea a = (GridArea) iterator.next();
            a.clearLasers();
            if (a.selected) {
                somethingIsSelected = true;
            }
        }

        for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
            GridArea area = (GridArea) iterator.next();
            if(area.shape == Shape.Laser) {
                followLaser(area, 0, 0, false);
            }
        }

        // Check how many targets are lasered
        for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
            GridArea area = (GridArea) iterator.next();
            if(area.shape == Shape.Target && area.lasered) {
                nrLasering++;
            }
        }
    }

    /**
     * Moves area in the direction of touch event by switching places with item on grid
     * @param area to move
     * @param event to move towards
     */
    private void move(GridArea area, TouchEvent event) {
        int newPositionX, newPositionY;
        int newx0, newy0, newx1, newy1;
        int dx = (area.x1 + area.x0)/2 - event.x;
        int dy = (area.y1 + area.y0)/2 - event.y;
        int adx = Math.abs(dx);
        int ady = Math.abs(dy);
        if (adx > ady) {
            // Moving in x direction
            newPositionY = area.y;
            if (dx > 0) {
                newPositionX = area.x - 1;
            } else {
                newPositionX = area.x + 1;
            }
        } else {
            // Moving in y direction
            newPositionX = area.x;
            if (dy > 0) {
                newPositionY = area.y - 1;
            } else {
                newPositionY = area.y + 1;
            }
        }
        for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
            GridArea a = (GridArea) iterator.next();
            if(a.x == newPositionX && a.y == newPositionY) {
                newx0 = a.x0;
                newy0 = a.y0;
                newx1 = a.x1;
                newy1 = a.y1;
                
                a.x = area.x;
                a.y = area.y;
                a.x0 = area.x0;
                a.y0 = area.y0;
                a.x1 = area.x1;
                a.y1 = area.y1;
                
                area.x = newPositionX;
                area.y = newPositionY;
                area.x0 = newx0;
                area.y0 = newy0;
                area.x1 = newx1;
                area.y1 = newy1;
                break;
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

        //Target lit
        if (area.shape == Shape.Target) {
            area.lasered = true;
            return;
        }

        // Box, they eat lasers
        if (area.shape == Shape.Box) {
            return;
        }

        // Empty space, laser goes in current direction
        if (area.shape == Shape.Empty) {
            area.lasered = true;
        }

        // New laser, it will shoot its own laser
        if (area.shape == Shape.Laser) {
            area.lasered = true;
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
                    return;
                case 0:
                    switch (dy) {
                    case 1:
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
                    return;
                case -1:
                    nextdy = 1;
                    break;
                case 0:
                    switch (dy) {
                    case 1:
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
                    return;
                case 0:
                    switch (dy) {
                    case 1:
                        nextdx = -1;
                        break;
                    case -1:
                        return;
                    }
                }
                break;
            default:
                // Unknown rotation, better bail out
                return;
            }
            area.lasered = true;
        }

        nextGridPointX = area.x + nextdx;
        nextGridPointY = area.y + nextdy;

        for (Iterator<GridArea> iterator = grid.iterator(); iterator.hasNext();) {
            GridArea a = (GridArea) iterator.next();
            if(a.x == nextGridPointX && a.y == nextGridPointY) {
                if (nextdx == 1) {
                    a.addInCominglaserDirection(LaserDirection.Left);
                } else if (nextdx == -1) {
                    a.addInCominglaserDirection(LaserDirection.Right);
                } else if (nextdy == 1) {
                    a.addInCominglaserDirection(LaserDirection.Top);
                } else {
                    a.addInCominglaserDirection(LaserDirection.Bottom);
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
            // Always draw a rectangle to get an unbroken grid look.
            g.drawRectNoFill(area.x0, area.y0, width, height, ColorPalette.gridLines);
            if (area.shape == Shape.Empty) {
                if (area.lasered) {
                    if (area.isInComingHorizontal()) {
                        g.drawLaserLine(area.x0, area.y0 + height/2, area.x1, area.y0 + height/2);
                    }
                    if (area.isInComingVertical()){
                        g.drawLaserLine(area.x0 + width/2, area.y0, area.x0 + width/2, area.y1);
                    }
                }
            } else if (area.shape == Shape.Box) {
                g.drawRect(area.x0, area.y0, width, height, ColorPalette.box);
                if (area.selected) {
                    // Draw small arrows
                    Path p = new Path();
                    p.moveTo(area.x0 + boxWidth/3, area.y0);
                    p.lineTo(area.x0 + boxWidth/3, area.y0);
                    p.lineTo(area.x0 + boxWidth/2, area.y0 - boxWidth/5);
                    p.lineTo(area.x0 + 2 * boxWidth/3, area.y0);
                    p.close();
                    g.drawPath(p, arrowPaint);
                    
                    Matrix transform = new Matrix();
                    transform.setRotate(90, area.x0 + boxWidth/2, area.y0 + boxWidth/2);
                    p.transform(transform);
                    g.drawPath(p, arrowPaint);
                    p.transform(transform);
                    g.drawPath(p, arrowPaint);
                    p.transform(transform);
                    g.drawPath(p, arrowPaint);
                }
            } else if (area.shape == Shape.Triangle) {
                g.drawLaserDeflectorTriangle(area.x0, area.y0, width, height, area.getRotation(), ColorPalette.button, area.lasered);
            } else if (area.shape == Shape.Laser) {
                g.drawLaser(area.x0, area.y0, width, height, area.getRotation());
            } else if (area.shape == Shape.Target) {
                g.drawTarget(area.x0, area.y0, width, height, area.getLaserDirections(), area.lasered);
            }
        }
        for (Iterator<GridArea> iterator = touched.iterator(); iterator.hasNext();) {
            GridArea area = (GridArea) iterator.next();
            g.drawRect(area.x0, area.y0, area.x1 - area.x0, area.y1 - area.y0, ColorPalette.progress);
        }
    }

}
