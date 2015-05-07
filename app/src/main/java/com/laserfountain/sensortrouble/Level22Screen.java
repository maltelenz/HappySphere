package com.laserfountain.sensortrouble;

import com.laserfountain.framework.Game;

public class Level22Screen extends SimonSaysLevel {

    public Level22Screen(Game game) {
        super(game);

        circleRadius = game.scale(200);

        circles.add(new TouchButton(circleRadius, gameWidth/2, 5 * gameHeight/12));
        circles.add(new TouchButton(circleRadius, 3 * gameWidth/12, 2 * gameHeight/3));
        circles.add(new TouchButton(circleRadius, 9 * gameWidth/12, 2 * gameHeight/3));
    }
}
