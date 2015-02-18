package com.maltelenz.sensortrouble;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;
import com.maltelenz.framework.Screen;

public abstract class LevelScreen extends Screen {

    protected enum GameState {
            Running, Finished
        }

    GameState state = GameState.Running;
    
    private float numberOfLevels = 1;
    
    private int nextButtonWidth = 500;
    private int nextButtonHeight = 150;
    private int progressBarHeight = 20;
    private int levelIndicatorRadius = 100;
    private int levelIndicatorPadding = 50;

    public LevelScreen(Game game) {
        super(game);
    }

    abstract void drawRunningUI();

    abstract protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime);

    abstract Screen nextLevel();

    abstract float percentDone();

    abstract int levelsDone();

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
    
        if (state == GameState.Running)
            updateGameRunning(touchEvents, deltaTime);
        if (state == GameState.Finished)
            updateGameFinished(touchEvents);
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    private void updateGameFinished(List<TouchEvent> touchEvents) {
        Graphics g = game.getGraphics();
        g.drawNextButton(nextButtonWidth, nextButtonHeight);
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event,
                            game.getGraphics().getWidth() - nextButtonWidth,
                            game.getGraphics().getHeight() - nextButtonHeight,
                            nextButtonWidth,
                            nextButtonHeight)
                        ) {
                    // Restart Game
                    nullify();
                    game.setScreen(nextLevel());
                    return;
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        if (state == GameState.Running) {
            drawRunningUI();
            drawProgressOverlay(false);
        }
        if (state == GameState.Finished) {
            drawGameFinishedUI();
        }
    }

    private void nullify() {
        // Call garbage collector to clean up memory.
        System.gc();
    }

    protected void drawProgressOverlay(boolean finished) {
        Graphics g = game.getGraphics();
        // Draw the progress for the current level
        g.drawRectWithShadow(0, 0, Math.round(percentDone() * g.getWidth()), progressBarHeight, ColorPalette.progress);
        // Draw total progress
        Paint arcPainter = new Paint();
        arcPainter.setColor(ColorPalette.progressBackground);
        arcPainter.setStyle(Style.FILL);
        g.drawCircle(
                g.getWidth() - levelIndicatorPadding - levelIndicatorRadius,
                levelIndicatorRadius + levelIndicatorPadding,
                levelIndicatorRadius,
                arcPainter
            );

        arcPainter.setColor(ColorPalette.progress);
        arcPainter.setStyle(Style.STROKE);
        arcPainter.setStrokeWidth(15);
        RectF arcRect = new RectF(
                g.getWidth() - levelIndicatorPadding - 2 * levelIndicatorRadius,
                levelIndicatorPadding,
                g.getWidth() - levelIndicatorPadding,
                2 * levelIndicatorRadius + levelIndicatorPadding
            );
        int levelsReallyDone = levelsDone();
        if (finished) {
            levelsReallyDone++;
        }
        g.drawArc(arcRect, levelsReallyDone/numberOfLevels, arcPainter);

        g.drawString(
                Integer.toString(levelsReallyDone),
                g.getWidth() - levelIndicatorPadding - levelIndicatorRadius,
                levelIndicatorRadius + levelIndicatorPadding
            );
    }

    private void drawGameFinishedUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);
        g.drawStringCentered("SUCCESS.");
        g.drawNextButton(nextButtonWidth, nextButtonHeight);
        drawProgressOverlay(true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void backButton() {
        pause();
    }

}