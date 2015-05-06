package com.maltelenz.sensortrouble;

import com.maltelenz.framework.Game;

public class Level20Screen extends FighterLevel {

    public Level20Screen(Game game) {
        super(game);

        barriers.add(new Barrier(
                gameWidth/3,
                gameHeight/2 - game.scaleY(600),
                2 * gameWidth/3,
                gameHeight/2 - game.scaleY(600) + barrierHeight));

        barriers.add(new Barrier(
                0,
                gameHeight/2 - game.scaleY(300),
                3 * gameWidth/9,
                gameHeight/2 - game.scaleY(300) + barrierHeight));
        barriers.add(new Barrier(
                6 * gameWidth/9,
                gameHeight/2 - game.scaleY(300),
                gameWidth,
                gameHeight/2 - game.scaleY(300) + barrierHeight));

        barriers.add(new Barrier(
                gameWidth/3,
                gameHeight/2,
                2 * gameWidth/3,
                gameHeight/2 + barrierHeight));

        barriers.add(new Barrier(0,
                gameHeight/2 + game.scaleY(300),
                3 * gameWidth/9,
                gameHeight/2 + game.scaleY(300) + barrierHeight));
        barriers.add(new Barrier(
                6 * gameWidth/9,
                gameHeight/2 + game.scaleY(300),
                gameWidth,
                gameHeight/2 + game.scaleY(300) + barrierHeight));

        dangerBarriers.add(new Barrier(
                0,
                gameHeight/2 - game.scaleY(300) + barrierHeight,
                barrierHeight,
                gameHeight/2 + game.scaleY(300)));
        dangerBarriers.add(new Barrier(
                gameWidth - barrierHeight,
                gameHeight/2 - game.scaleY(300) + barrierHeight,
                gameWidth,
                gameHeight/2 + game.scaleY(300)));

        state = GameState.Running;
    }
}
