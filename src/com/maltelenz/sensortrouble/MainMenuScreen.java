package com.maltelenz.sensortrouble;

import java.util.List;

import android.graphics.Color;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Screen;
import com.maltelenz.framework.Input.TouchEvent;

public class MainMenuScreen extends Screen {

    private int startButtonWidth = 500;
    private int startButtonHeight = 150;

    private int screenWidth;
    private int screenHeight;

    public MainMenuScreen(Game game) {
        super(game);
        screenWidth = game.getGraphics().getWidth();
        screenHeight = game.getGraphics().getHeight();
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
    if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
        return true;
    else
        return false;
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event,
                        (screenWidth - startButtonWidth)/2,
                        (screenHeight - startButtonHeight)/2,
                        startButtonWidth,
                        startButtonHeight)
                    ) {
                    // Start Game
                    game.setScreen(new Level1Screen(game));
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.clearScreen(Color.GRAY);
        g.drawStartButton(
                (screenWidth - startButtonWidth)/2,
                (screenHeight - startButtonHeight)/2,
                (screenWidth + startButtonWidth)/2,
                (screenHeight + startButtonHeight)/2
            );
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
    }
}