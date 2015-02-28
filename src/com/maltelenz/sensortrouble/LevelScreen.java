package com.maltelenz.sensortrouble;

import java.util.List;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public abstract class LevelScreen extends Screen {

    protected enum GameState {
            Initializing, Running, Finished
        }

    GameState state = GameState.Initializing;

    private int nextButtonWidth = 500;
    private int nextButtonHeight = 150;
    private int progressBarHeight = 30;
    private int levelIndicatorRadius = 100;
    private int levelIndicatorPadding = 50;

    private Button nextButton;

    public LevelScreen(Game game) {
        super(game);
        
        nextButton = new Button("Next",
                game.getGraphics().getWidth() - nextButtonWidth,
                game.getGraphics().getHeight() - nextButtonHeight,
                game.getGraphics().getWidth(),
                game.getGraphics().getHeight());
    }

    abstract void drawRunningUI();

    abstract protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime);

    abstract double percentDone();

    void updateGameInitializing(float deltaTime) {
        return;
    }

    void drawInitializingUI() {
        return;
    }

    @Override
    public void update(float deltaTime) {
        if (state == GameState.Initializing) {
            updateGameInitializing(deltaTime);
            return;
        }

        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        if (state == GameState.Running)
            updateGameRunning(touchEvents, deltaTime);
        if (state == GameState.Finished)
            updateGameFinished(touchEvents);
    }

    private void updateGameFinished(List<TouchEvent> touchEvents) {
        Graphics g = game.getGraphics();
        g.drawButton(nextButton);
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (nextButton.inBounds(event)) {
                    // Start next level
                    nullify();
                    nextLevel();
                    return;
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        if (state == GameState.Initializing) {
            drawInitializingUI();
        }
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
        int xmax = (int) Math.round(percentDone() * g.getWidth());
        g.drawRectWithShadow(0, 0, xmax, progressBarHeight, ColorPalette.progress);
        g.drawRect(xmax, 0, g.getWidth() - xmax, progressBarHeight, ColorPalette.inactiveProgress);
        // Draw total progress
        Paint arcPainter = new Paint();
        arcPainter.setColor(ColorPalette.progressBackground);
        arcPainter.setStyle(Style.FILL);
        arcPainter.setAntiAlias(true);
        g.drawCircle(
                g.getWidth() - levelIndicatorPadding - levelIndicatorRadius,
                levelIndicatorRadius + levelIndicatorPadding + progressBarHeight,
                levelIndicatorRadius,
                arcPainter
            );

        arcPainter.setColor(ColorPalette.progress);
        arcPainter.setStyle(Style.STROKE);
        arcPainter.setStrokeWidth(15);
        arcPainter.setShadowLayer(10.0f, 2.0f, 2.0f, ColorPalette.buttonShadow);
        RectF arcRect = new RectF(
                g.getWidth() - levelIndicatorPadding - 2 * levelIndicatorRadius,
                levelIndicatorPadding + progressBarHeight,
                g.getWidth() - levelIndicatorPadding,
                2 * levelIndicatorRadius + levelIndicatorPadding + progressBarHeight
            );
        int levelsReallyDone = currentLevel();
        if (finished) {
            levelsReallyDone++;
        }
        g.drawArc(arcRect, (float) levelsReallyDone/numberOfLevels(), arcPainter);

        arcPainter.setColor(ColorPalette.inactiveProgress);
        arcPainter.clearShadowLayer();
        g.drawArc(arcRect, 100 - (float) levelsReallyDone/numberOfLevels(), arcPainter);

        g.drawString(
                Integer.toString(levelsReallyDone),
                g.getWidth() - levelIndicatorPadding - levelIndicatorRadius,
                levelIndicatorRadius + levelIndicatorPadding + progressBarHeight
            );
    }

    private void drawGameFinishedUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);
        g.drawStringCentered("SUCCESS.");
        g.drawButton(nextButton);
        drawProgressOverlay(true);
    }

    @Override
    public boolean backButton() {
        game.setScreen(new MainMenuScreen(game));
        return false;
    }

}