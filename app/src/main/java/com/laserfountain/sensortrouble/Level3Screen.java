package com.laserfountain.sensortrouble;

import com.laserfountain.framework.Game;

public class Level3Screen extends SimonSaysLevel {

    public Level3Screen(Game game) {
        super(game);

        circleRadius = game.scale(300);

        circles.add(new TouchButton(circleRadius, gameWidth/2, 3 * gameHeight/12));
        circles.add(new TouchButton(circleRadius, gameWidth/2, 9 * gameHeight/12));
    }
}
