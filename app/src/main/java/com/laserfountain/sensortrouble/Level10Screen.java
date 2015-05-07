package com.laserfountain.sensortrouble;

import com.laserfountain.framework.Game;

public class Level10Screen extends GravityLevel {

    public Level10Screen(Game game) {
        super(game);

        barriers.add(new Barrier(gameWidth/3, gameHeight/2, 2 * gameWidth/3, gameHeight/2 + barrierHeight));
        barriers.add(new Barrier(0, gameHeight/2 + game.scaleY(300), 3 * gameWidth/9, gameHeight/2 + game.scaleY(300) + barrierHeight));
        barriers.add(new Barrier(6 * gameWidth/9, gameHeight/2 + game.scaleY(300), gameWidth, gameHeight/2 + game.scaleY(300) + barrierHeight));

        state = GameState.Running;
    }
}
