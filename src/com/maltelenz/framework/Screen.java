package com.maltelenz.framework;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.maltelenz.sensortrouble.Level1Screen;
import com.maltelenz.sensortrouble.Level2Screen;
import com.maltelenz.sensortrouble.Level3Screen;
import com.maltelenz.sensortrouble.Level4Screen;
import com.maltelenz.sensortrouble.LevelScreen;
import com.maltelenz.sensortrouble.MainMenuScreen;

public class Screen {
    protected final Game game;
    
    private ArrayList<Class<? extends LevelScreen>> levels;
    
    public Screen(Game game) {
        this.game = game;
        levels = new ArrayList<Class<? extends LevelScreen>>();
        levels.add(Level1Screen.class);
        levels.add(Level2Screen.class);
        levels.add(Level3Screen.class);
        levels.add(Level4Screen.class);
    }

    public void startLevel() {
        startLevel(0);
    }

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

    public int currentLevel() {
        return levels.indexOf(this.game.getCurrentScreen().getClass());
    }

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

    public void backButton() {
    }
}
