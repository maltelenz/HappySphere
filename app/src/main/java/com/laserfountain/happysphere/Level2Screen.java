package com.laserfountain.happysphere;

import com.laserfountain.framework.Game;

public class Level2Screen extends ContinousTouchButtonLevel {

    public Level2Screen(Game game) {
        super(game);

        maxTouchTime = 500;

        circleRadius = game.scale(300);

        circles.add(new ContinuousCountdownButton(circleRadius, gameWidth/2, gameHeight/2, maxTouchTime));

        state = GameState.Running;
    }
}
