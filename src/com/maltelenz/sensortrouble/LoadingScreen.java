package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Game;

public class LoadingScreen extends Screen {

    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        game.setScreen(new MainMenuScreen(game));
    }

}