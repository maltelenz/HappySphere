package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Game;

public class Level18Screen extends ContinousTouchButtonLevel {

    public Level18Screen(Game game) {
        super(game);

        maxTouchTime = 500 * 3;

        circleRadius = game.scale(200);

        circles.add(new ContinuousCountdownButton(circleRadius, gameWidth/2, 5 * gameHeight/12, maxTouchTime/3));
        circles.add(new ContinuousCountdownButton(circleRadius, 3 * gameWidth/12, 2 * gameHeight/3, maxTouchTime/3));
        circles.add(new ContinuousCountdownButton(circleRadius, 9 * gameWidth/12, 2 * gameHeight/3, maxTouchTime/3));

        state = GameState.Running;
    }
}
