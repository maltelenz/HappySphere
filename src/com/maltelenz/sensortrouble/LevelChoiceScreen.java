package com.maltelenz.sensortrouble;

import java.util.List;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;

public class LevelChoiceScreen extends Screen {

    private int buttonWidth = 150;
    private int buttonHeight = 150;
    private int textWidth = 200;

    private int startButtonWidth = 500;
    private int startButtonHeight = 150;

    private int screenWidth;
    private int screenHeight;
    private Button lowerButton;
    private Button higherButton;
    private Button startButton;
    
    private int levelChosen = 1;

    public LevelChoiceScreen(Game game) {
        super(game);
        screenWidth = game.getGraphics().getWidth();
        screenHeight = game.getGraphics().getHeight();
        
        lowerButton = new Button("-",
                (screenWidth - textWidth)/2 - buttonWidth,
                (screenHeight - buttonHeight)/2,
                (screenWidth - textWidth)/2,
                (screenHeight + buttonHeight)/2);
        higherButton = new Button("+",
                (screenWidth + textWidth)/2,
                (screenHeight - buttonHeight)/2,
                (screenWidth + textWidth)/2 + buttonWidth,
                (screenHeight + buttonHeight)/2);
        startButton = new Button("Start",
                (screenWidth - startButtonWidth)/2,
                (screenHeight - startButtonHeight)/2 + 2 * buttonHeight,
                (screenWidth + startButtonWidth)/2,
                (screenHeight + startButtonHeight)/2 + 2 * buttonHeight);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (lowerButton.inBounds(event)) {
                    levelChosen = Math.max(levelChosen - 1, 1);
                }
                if (higherButton.inBounds(event)) {
                    levelChosen  = Math.min(levelChosen + 1, numberOfLevels());
                }
                if (startButton.inBounds(event)) {
                    // Start Game
                    startLevel(levelChosen - 1);
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);
        g.drawButton(lowerButton);
        g.drawButton(higherButton);
        g.drawButton(startButton);
        Paint levelPaint = new Paint();
        levelPaint.setTextSize(70);
        levelPaint.setTextAlign(Paint.Align.CENTER);
        levelPaint.setAntiAlias(true);
        levelPaint.setColor(ColorPalette.darkText);
        levelPaint.setTypeface(Typeface.DEFAULT_BOLD);
        g.drawStringCentered(Integer.toString(levelChosen), levelPaint);
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
        // Back is to the main screen
        game.setScreen(new MainMenuScreen(game));
        return false;
    }
}