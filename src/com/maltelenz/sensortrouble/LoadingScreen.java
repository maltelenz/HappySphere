package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Screen;

public class LoadingScreen extends Screen {

    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void paint(float deltaTime) {
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