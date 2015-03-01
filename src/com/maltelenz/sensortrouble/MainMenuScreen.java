package com.maltelenz.sensortrouble;

import java.util.List;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class MainMenuScreen extends Screen {

    private int buttonWidth = 500;
    private int buttonHeight = 150;

    private int screenWidth;
    private int screenHeight;
    private Button startButton;
    private Button levelChoiceButton;

    public MainMenuScreen(Game game) {
        super(game);
        screenWidth = game.getGraphics().getWidth();
        screenHeight = game.getGraphics().getHeight();
        
        startButton = new Button("Start",
                (screenWidth - buttonWidth)/2,
                (screenHeight - buttonHeight)/2,
                (screenWidth + buttonWidth)/2,
                (screenHeight + buttonHeight)/2);
        levelChoiceButton = new Button("Choose Level",
                (screenWidth - buttonWidth)/2,
                (screenHeight - buttonHeight)/2 + 2 * buttonHeight,
                (screenWidth + buttonWidth)/2,
                (screenHeight + buttonHeight)/2 + 2 * buttonHeight);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (startButton.inBounds(event)) {
                    // Start Game
                    startLevel();
                } else if (levelChoiceButton.inBounds(event)) {
                    game.setScreen(new LevelChoiceScreen(game));
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);
        g.drawButton(startButton);
        g.drawButton(levelChoiceButton);

        drawGameProgressOverlay(false, false);
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
    public boolean backButton() {
        return true;
    }
}