package com.laserfountain.sensortrouble;

import com.laserfountain.framework.Game;

public class Level15Screen extends GravityLevel {

    public Level15Screen(Game game) {
        super(game);

        startPointX = gameWidth/4;
        currentPoint.x = startPointX;

        barriers.add(new Barrier(
                0,
                gameHeight/4 - 2 * barrierHeight,
                3 * gameWidth/4 - barrierHeight,
                gameHeight/4 - barrierHeight));

        dangerBarriers.add(new Barrier(
                gameWidth - barrierHeight,
                0,
                gameWidth,
                gameHeight/4 - barrierHeight));

        barriers.add(new Barrier(
                3 * gameWidth/4 - 2 * barrierHeight,
                gameHeight/4 - barrierHeight,
                3 * gameWidth/4 - barrierHeight,
                2 * gameHeight/4 - 3 * barrierHeight));

        barriers.add(new Barrier(
                3 * gameWidth/4 - barrierHeight,
                2 * gameHeight/4 - 4 * barrierHeight,
                3 * gameWidth/4 + barrierHeight,
                2 * gameHeight/4 - 3 * barrierHeight));

        barriers.add(new Barrier(
                gameWidth/4,
                gameHeight/2,
                gameWidth,
                gameHeight/2 + barrierHeight));

        barriers.add(new Barrier(
                gameWidth/4 - barrierHeight,
                gameHeight/2 - 4 * barrierHeight,
                gameWidth/4,
                gameHeight/2 + barrierHeight));

        dangerBarriers.add(new Barrier(
                gameWidth/4 + 2 * barrierHeight,
                gameHeight/4 - barrierHeight,
                3 * gameWidth/4 - 2 * barrierHeight,
                2 * gameHeight/4 - 3 * barrierHeight));

        dangerBarriers.add(new Barrier(
                0,
                3 * gameHeight/4 - 2 * barrierHeight,
                gameWidth/2,
                3 * gameHeight/4 - barrierHeight));

        barriers.add(new Barrier(
                3 * gameWidth/4 - barrierHeight,
                3 * gameHeight/4 - 2 * barrierHeight,
                3 * gameWidth/4,
                3 * gameHeight/4 - barrierHeight));

        dangerBarriers.add(new Barrier(
                gameWidth - barrierHeight,
                gameHeight/2 + barrierHeight,
                gameWidth,
                3 * gameHeight/4 + barrierHeight));

        barriers.add(new Barrier(
                3 * gameWidth/4 - 2 * barrierHeight,
                3 * gameHeight/4 + barrierHeight,
                gameWidth,
                3 * gameHeight/4 + 2 * barrierHeight));

        barriers.add(new Barrier(
                0,
                gameHeight - barrierHeight,
                3 * gameWidth/4,
                gameHeight));

        state = GameState.Running;
    }
}
