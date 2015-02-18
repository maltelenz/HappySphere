package com.maltelenz.sensortrouble;

import java.util.List;

import android.graphics.Color;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;
import com.maltelenz.framework.Screen;

public class Level1Screen extends Screen {
    enum GameState {
        Running, Finished
    }

    GameState state = GameState.Running;

    // Variable Setup
    // You would create game objects here.

    int touchesLeft = 6;

    public Level1Screen(Game game) {
        super(game);

        // Initialize game objects here
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        if (state == GameState.Running)
            updateGameRunning(touchEvents, deltaTime);
        if (state == GameState.Finished)
            updateGameFinished(touchEvents);
    }

    private void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            if (event.type == TouchEvent.TOUCH_DOWN) {
                touchesLeft--;
            }
        }
       
        if (touchesLeft == 0) {
            state = GameState.Finished;
        }
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }
    
    private void updateGameFinished(List<TouchEvent> touchEvents) {
        Graphics g = game.getGraphics();
        g.drawButton("Next", 500, 500);
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, 0, 0, 250, 250)) {
                    // Restart Game
                    nullify();
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        // Graphics g = game.getGraphics();

        // First draw the game elements.

        // Example:
        // g.drawImage(Assets.background, 0, 0);
        // g.drawImage(Assets.character, characterX, characterY);

        // Secondly, draw the UI above the game elements.
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Finished)
            drawGameFinishedUI();
    }

    private void nullify() {
        // Call garbage collector to clean up memory.
        System.gc();
    }


    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(Color.GRAY);
        g.drawStringCentered(Integer.toString(touchesLeft));
    }

    private void drawGameFinishedUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(Color.GRAY);
        g.drawStringCentered("SUCCESS.");
        g.drawButton("Next", 500, 500);
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
