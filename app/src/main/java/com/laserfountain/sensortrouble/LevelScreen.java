package com.laserfountain.sensortrouble;

import java.util.List;

import com.laserfountain.framework.Game;
import com.laserfountain.framework.Graphics;
import com.laserfountain.framework.Input.TouchEvent;

public abstract class LevelScreen extends Screen {

    protected enum GameState {
            Initializing, Running, Finished
        }

    GameState state = GameState.Initializing;

    private int nextButtonWidth;
    private int nextButtonHeight;

    private Button nextButton;
    private Button againButton;

    private boolean updatedLevelFinished = false;

    private float timeSpent = 0;

    public LevelScreen(Game game) {
        super(game);

        nextButtonWidth = game.scaleX(500);
        nextButtonHeight = game.scaleY(150);

        nextButton = new Button("Next",
                game.getGraphics().getWidth() - nextButtonWidth,
                game.getGraphics().getHeight() - nextButtonHeight,
                game.getGraphics().getWidth(),
                game.getGraphics().getHeight());

        againButton = new Button("Play Again",
                0,
                game.getGraphics().getHeight() - nextButtonHeight,
                nextButtonWidth,
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

    /**
     * @return time spent on level in seconds
     */
    float getTimeSpent() {
        return timeSpent / 100f;
    }

    @Override
    public void update(float deltaTime) {
        if (state == GameState.Initializing) {
            updateGameInitializing(deltaTime);
            return;
        }

        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        if (state == GameState.Running) {
            updateGameRunning(touchEvents, deltaTime);
            timeSpent += deltaTime;
        }
        if (state == GameState.Finished) {
            updateGameFinished(touchEvents);
            if (!updatedLevelFinished) {
                game.updateMaxLevel(currentLevel() + 1); // +1 because currentLevel() is 0-indexed.
                updatedLevelFinished = true;
            }
        }
    }

    private void updateGameFinished(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (nextButton.inBounds(event)) {
                    // Start next level
                    nullify();
                    nextLevel();
                    return;
                }
                if (againButton.inBounds(event)) {
                    // Start next level
                    nullify();
                    startLevel(currentLevel());
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
            drawGameProgressOverlay(true, false);
            drawLevelProgressOverlay();
        }
        if (state == GameState.Finished) {
            drawGameFinishedUI();
        }
    }

    private void nullify() {
        // Call garbage collector to clean up memory.
        System.gc();
    }

    protected void drawLevelProgressOverlay() {
        Graphics g = game.getGraphics();
        // Draw the progress for the current level
        int xmax = (int) Math.round(percentDone() * g.getWidth());
        g.drawRectWithShadow(0, 0, xmax, progressBarHeight, ColorPalette.progress);
        g.drawRect(xmax, 0, g.getWidth() - xmax, progressBarHeight, ColorPalette.inactiveProgress);
    }

    private void drawGameFinishedUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);
        g.drawHappySphere(Happy.Ok);
        g.drawString(Float.toString(getTimeSpent()), g.getWidth()/2, 7 * g.getHeight() / 8);
        g.drawButton(nextButton);
        g.drawButton(againButton);
        drawGameProgressOverlay(true, true);
        drawLevelProgressOverlay();
    }

    @Override
    public boolean backButton() {
        game.setScreen(new MainMenuScreen(game));
        return false;
    }

}