package com.laserfountain.happysphere;

import com.laserfountain.framework.Game;

public class Level1Screen extends ButtonGraphLevel {

    public Level1Screen(Game game) {
        super(game);

        maxTouches = 5;

        circleRadius = game.scale(300);

        circles.add(new CountdownButton(circleRadius, gameWidth/2, gameHeight/2, maxTouches));

        startingTouches = maxTouches;

        state = GameState.Running;
    }

}
