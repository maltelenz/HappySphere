package com.laserfountain.happysphere;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.laserfountain.framework.Game;
import com.laserfountain.framework.Graphics;
import com.laserfountain.framework.Input.TouchEvent;

public class SimonSaysLevel extends LevelScreen {

    protected int gameHeight;
    protected int gameWidth;

    int step = 0;

    int circleRadius;

    List<TouchButton> circles;

    private boolean drawFailure = false;

    private float failureDrawTime = 100;

    float timeEachBlink = 50;
    float timeLeft = timeEachBlink;

    private Paint circlePainter;

    public SimonSaysLevel(Game game) {
        super(game);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        circles = new ArrayList<TouchButton>();

        circlePainter = new Paint();
        circlePainter.setColor(ColorPalette.button);
        circlePainter.setStyle(Style.FILL_AND_STROKE);
        circlePainter.setStrokeWidth(game.scale(5));
        circlePainter.setAntiAlias(true);
        circlePainter.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);
    }

    @Override
    protected void updateGameInitializing(float deltaTime) {
        timeLeft -= deltaTime;

        if (timeLeft < 0) {
            step++;
            if (step == circles.size()) {
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

        if (state == GameState.Initializing) {
            return;
        }

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
                if(circles.get(step).touch(event)) {
                    step++;
                    break;
                } else {
                    step = 0;
                    drawFailure = true;
                    timeLeft = failureDrawTime;
                }
            }
        }

        for (TouchButton button : circles) {
            if (drawFailure) {
                button.resetFlash();
            } else {
                button.decreaseFlashTime(deltaTime);
            }
        }

        if (step >= circles.size()) {
            state = GameState.Finished;
        }
    }

    @Override
    double percentDone() {
        return ((double) step)/circles.size();
    }

    @Override
    void drawInitializingUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        for (int i = 0; i < circles.size(); i++) {
            TouchButton button = circles.get(i);
            if (step == i) {
                circlePainter.setColor(ColorPalette.progress);
            } else {
                circlePainter.setColor(ColorPalette.button);
            }
            g.drawCircle(button.centerX, button.centerY, button.getMaxRadius(), circlePainter);
        }
    }

    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        for (TouchButton button : circles) {
            if (drawFailure) {
                circlePainter.setColor(ColorPalette.button);
            } else if (button.isFlashing()) {
                circlePainter.setColor(ColorPalette.progress);
            } else {
                circlePainter.setColor(ColorPalette.button);
            }
            g.drawCircle(button.centerX, button.centerY, button.getMaxRadius(), circlePainter);
        }

        if (drawFailure) {
            g.drawOopsieString();
        }
    }

}
