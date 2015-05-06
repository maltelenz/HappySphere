package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Game;

public class Level2Screen extends ContinousTouchButtonLevel {

    public Level2Screen(Game game) {
        super(game);

        maxTouchTime = 500;

        circleRadius = game.scale(300);

        circles.add(new ContinuousCountdownButton(circleRadius, gameWidth/2, gameHeight/2, maxTouchTime));

        state = GameState.Running;
    }
}
