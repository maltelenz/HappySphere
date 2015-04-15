package com.maltelenz.sensortrouble;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;

public class Screen {
    protected final Game game;
    
    private ArrayList<Class<? extends LevelScreen>> levels;

    private int levelIndicatorRadius;
    private int levelIndicatorPadding;
    protected int progressBarHeight;

    public Screen(Game game) {
        this.game = game;

        levelIndicatorRadius = game.scale(100);
        levelIndicatorPadding = game.scale(50);
        progressBarHeight = game.scaleY(30);

        
        levels = new ArrayList<Class<? extends LevelScreen>>();
        levels.add(Level1Screen.class);
        levels.add(Level2Screen.class);
        levels.add(Level3Screen.class);
        levels.add(Level4Screen.class);
        levels.add(Level5Screen.class);
        levels.add(Level6Screen.class);
        levels.add(Level7Screen.class);
        levels.add(Level8Screen.class);
        levels.add(Level9Screen.class);
        levels.add(Level10Screen.class);
        levels.add(Level11Screen.class);
        levels.add(Level12Screen.class);
        levels.add(Level13Screen.class);
        levels.add(Level14Screen.class);
        levels.add(Level15Screen.class);
        levels.add(Level16Screen.class);
        levels.add(Level17Screen.class);
        levels.add(Level18Screen.class);
    }

    /**
     * @return the number of levels available in the game
     */
    public int numberOfLevels() {
        return levels.size();
    }

    /**
     * Starts the first level not yet completed
     */
    public void startLevel() {
        startLevel(Math.min(game.getMaxLevel(), numberOfLevels() - 1));
    }

    /**
     * Starts the given level
     * @param level The level to start, 0-indexed
     */
    public void startLevel(int level) {
        try {
            Constructor<?> c;
            Class<? extends LevelScreen> nextLevelClass = levels.get(level);
            c = nextLevelClass.getConstructor(Game.class);
            LevelScreen newLevel = (LevelScreen) c.newInstance(game);
            game.setScreen(newLevel);
        } catch (Exception e) {
            // Will never happen as long as the levels list is correct
            e.printStackTrace();
        }
    }

    /**
     * @return the level currently played, 0-indexed
     */
    public int currentLevel() {
        int level = levels.indexOf(this.game.getCurrentScreen().getClass());
        if(level == -1) {
            level = game.getMaxLevel();
        }
        return level;
    }

    /**
     * Start the level after the currently played
     */
    public void nextLevel() {
        int nr = currentLevel() + 1;
        if (nr > levels.size() - 1) {
            game.setScreen(new MainMenuScreen(game));
            return;
        }
        startLevel(nr);
    }

    public void update(float deltaTime) {
    }

    public void paint(float deltaTime) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }

    public boolean backButton() {
        return true;
    }

    protected void drawGameProgressOverlay(boolean playing, boolean finished) {
        int xPosition = game.getGraphics().getWidth() - levelIndicatorPadding - levelIndicatorRadius;
        int yPosition = levelIndicatorRadius + levelIndicatorPadding + progressBarHeight;

        int currentLevel = currentLevel();

        if (finished) {
            currentLevel++;
        }

        drawGameProgressOverlay(xPosition, yPosition, currentLevel, playing);
    }

    /**
     * Draws the progress overlay for the complete game (number of levels finished)
     * @param drawIndicator if currently playing a level
     */
    protected void drawGameProgressOverlay(int xPosition, int yPosition, int currentLevel, boolean drawIndicator) {
        Graphics g = game.getGraphics();
        // Draw total progress
        Paint arcPainter = new Paint();
        arcPainter.setColor(ColorPalette.progressBackground);
        arcPainter.setStyle(Style.FILL);
        arcPainter.setAntiAlias(true);
        g.drawCircle(
                xPosition,
                yPosition,
                levelIndicatorRadius,
                arcPainter
            );

        arcPainter.setColor(ColorPalette.progress);
        arcPainter.setStyle(Style.STROKE);
        arcPainter.setStrokeWidth(game.scale(15));
        arcPainter.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);
        RectF arcRect = new RectF(
                xPosition - levelIndicatorRadius,
                yPosition - levelIndicatorRadius,
                xPosition + levelIndicatorRadius,
                yPosition + levelIndicatorRadius
            );
        int maxLevelAchieved = game.getMaxLevel();
        float percentDone = (float) maxLevelAchieved/numberOfLevels();
        g.drawArc(arcRect, percentDone, arcPainter);

        arcPainter.setColor(ColorPalette.inactiveProgress);
        arcPainter.clearShadowLayer();
        g.drawArc(arcRect, 100 - (float) maxLevelAchieved/numberOfLevels(), arcPainter);

        if (drawIndicator) {
            // Draw small indicator at current level
            float currentPercent = (float) currentLevel/numberOfLevels();

            arcPainter.setColor(ColorPalette.laser);
            g.drawPartialArc(arcRect, currentPercent, 0.01f, arcPainter);
        }

        g.drawString(
                Integer.toString(Math.min(currentLevel + 1, numberOfLevels())),
                xPosition,
                yPosition
            );
    }

}
