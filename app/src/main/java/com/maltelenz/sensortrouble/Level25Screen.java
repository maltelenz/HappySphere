package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Game;

public class Level25Screen extends SimonSaysLevel {

    public Level25Screen(Game game) {
        super(game);

        circleRadius = game.scale(200);

        circles.add(new TouchButton(circleRadius, gameWidth/2, 6 * gameHeight/12));
        circles.add(new TouchButton(circleRadius, 3 * gameWidth/12, 7 * gameHeight/9));
        circles.add(new TouchButton(circleRadius, 9 * gameWidth/12, 7 * gameHeight/9));
        circles.add(new TouchButton(circleRadius, 3 * gameWidth/12, 2 * gameHeight/9));
        circles.add(new TouchButton(circleRadius, 9 * gameWidth/12, 2 * gameHeight/9));
    }
}
