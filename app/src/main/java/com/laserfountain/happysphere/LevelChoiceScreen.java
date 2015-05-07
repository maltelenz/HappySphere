package com.laserfountain.happysphere;

import java.util.List;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.laserfountain.framework.Game;
import com.laserfountain.framework.Graphics;
import com.laserfountain.framework.Input.TouchEvent;

public class LevelChoiceScreen extends Screen {

    private int buttonWidth;
    private int buttonHeight;
    private int textWidth;

    private int startButtonWidth;
    private int startButtonHeight;

    private int gameWidth;
    private int gameHeight;
    private Button lowerButton;
    private Button higherButton;
    private Button startButton;

    private boolean lowerPressed = false;
    private boolean higherPressed = false;

    private float pressRepetitionTime = 15;
    private float longPressInitTime = 75;
    private float pressTimeLeft = longPressInitTime;

    private int levelChosen = 1;

    public LevelChoiceScreen(Game game) {
        super(game);
        gameWidth = game.getGraphics().getWidth();
        gameHeight = game.getGraphics().getHeight();

        buttonWidth = game.scaleX(150);
        buttonHeight = game.scaleY(150);
        textWidth = game.scale(400);

        startButtonWidth = game.scaleX(500);
        startButtonHeight = game.scaleY(150);

        lowerButton = new Button("-",
                (gameWidth - textWidth)/2 - buttonWidth,
                (gameHeight - buttonHeight)/2,
                (gameWidth - textWidth)/2,
                (gameHeight + buttonHeight)/2);
        higherButton = new Button("+",
                (gameWidth + textWidth)/2,
                (gameHeight - buttonHeight)/2,
                (gameWidth + textWidth)/2 + buttonWidth,
                (gameHeight + buttonHeight)/2);
        startButton = new Button("Start",
                (gameWidth - startButtonWidth)/2,
                (gameHeight - startButtonHeight)/2 + 3 * buttonHeight,
                (gameWidth + startButtonWidth)/2,
                (gameHeight + startButtonHeight)/2 + 3 * buttonHeight);
    }

    @Override
    public void update(float deltaTime) {
        if (pressTimeLeft < 0) {
            if (higherPressed) {
                levelChosen  = Math.min(Math.min(levelChosen + 1, game.getMaxLevel() + 1), numberOfLevels());
            } else if (lowerPressed) {
                levelChosen = Math.max(levelChosen - 1, 1);
            }
            pressTimeLeft = pressRepetitionTime;
        }

        if (lowerPressed || higherPressed) {
            pressTimeLeft -= deltaTime;
        }

        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (lowerButton.inBounds(event)) {
                    levelChosen = Math.max(levelChosen - 1, 1);
                    lowerPressed = true;
                }
                if (higherButton.inBounds(event)) {
                    levelChosen  = Math.min(Math.min(levelChosen + 1, game.getMaxLevel() + 1), numberOfLevels());
                    higherPressed = true;
                }
            }
            if (event.type == TouchEvent.TOUCH_UP) {
                lowerPressed = false;
                higherPressed = false;
                pressTimeLeft = longPressInitTime;
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
        levelPaint.setTextSize(game.scale(70));
        levelPaint.setTextAlign(Paint.Align.CENTER);
        levelPaint.setAntiAlias(true);
        levelPaint.setColor(ColorPalette.darkText);
        levelPaint.setTypeface(Typeface.DEFAULT_BOLD);

        drawGameProgressOverlay(gameWidth/2, gameHeight/2, levelChosen - 1, true);
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