package com.maltelenz.sensortrouble;

import java.util.ArrayList;
import java.util.List;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class Level3Screen extends LevelScreen {

    List<TouchArea> touchAreas;

    int step = 0;

    float timeEachBlink = 50;
    float timeLeft = timeEachBlink;

    private float failureDrawTime = 100;

    private boolean drawTouch;

    private boolean drawFailure = false;

    public Level3Screen(Game game) {
        super(game);

        // Initialize data
        touchAreas = new ArrayList<TouchArea>();
        touchAreas.add(new TouchArea(
                0,
                0,
                game.getGraphics().getWidth()/2,
                game.getGraphics().getHeight()/2
        ));
        touchAreas.add(new TouchArea(
                game.getGraphics().getWidth()/2,
                game.getGraphics().getHeight()/2,
                game.getGraphics().getWidth(),
                game.getGraphics().getHeight()
        ));
        touchAreas.add(new TouchArea(
                game.getGraphics().getWidth()/2,
                0,
                game.getGraphics().getWidth(),
                game.getGraphics().getHeight()/2
        ));
        touchAreas.add(new TouchArea(
                0,
                game.getGraphics().getHeight()/2,
                game.getGraphics().getWidth()/2,
                game.getGraphics().getHeight()
        ));

    }

    @Override
    protected void updateGameInitializing(float deltaTime) {
        timeLeft -= deltaTime;

        if (timeLeft < 0) {
            step++;
            if (step == touchAreas.size()) {
                state = GameState.Running;
                step = 0;
                return;
            }
            timeLeft = timeEachBlink;
        }
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        timeLeft -= deltaTime;
        if (timeLeft < 0 && drawFailure) {
            drawFailure = false;
            state = GameState.Initializing;
            timeLeft = timeEachBlink;
            return;
        }

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            if (event.type == TouchEvent.TOUCH_DOWN) {
                if(touchAreas.get(step).inBounds(event)) {
                    step++;
                    drawTouch = true;
                } else {
                    step = 0;
                    drawFailure = true;
                    timeLeft = failureDrawTime;
                }
            }

            if (event.type == TouchEvent.TOUCH_UP) {
                drawTouch = false;
            }
        }
        if (step >= touchAreas.size()) {
            state = GameState.Finished;
        }
    }

    
    @Override
    float percentDone() {
        return ((float) step)/touchAreas.size();
    }

    private void drawGrid() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);
        
        g.drawLine(g.getWidth()/2, 0, g.getWidth()/2, g.getHeight(), ColorPalette.gridLines);
        g.drawLine(0, g.getHeight()/2, g.getWidth(), g.getHeight()/2, ColorPalette.gridLines);
    }

    @Override
    void drawInitializingUI() {
        drawGrid();
        Graphics g = game.getGraphics();
        TouchArea area = touchAreas.get(step);
        g.drawRect(area.x0, area.y0, area.x1 - area.x0, area.y1 - area.y0, ColorPalette.progress);
    }

    void drawRunningUI() {
        drawGrid();

        Graphics g = game.getGraphics();
        if (drawFailure) {
            g.drawOopsieString();
        }

        if (drawTouch) {
            TouchArea area = touchAreas.get(step - 1);
            g.drawRect(area.x0, area.y0, area.x1 - area.x0, area.y1 - area.y0, ColorPalette.progress);
        }
    }

}
