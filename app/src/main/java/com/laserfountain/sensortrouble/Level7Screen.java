package com.laserfountain.sensortrouble;

import com.laserfountain.framework.Game;

public class Level7Screen extends ShooterLevel {

    public Level7Screen(Game game) {
        super(game);

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 5; x++) {
                barriersLeft.add(new Barrier(barrierWidth * x + barrierPadding * (x + 1), barrierHeight * (2 + y) + barrierPadding * (y + 1), barrierWidth * (x + 1) + barrierPadding * x, barrierHeight * (3 + y) + barrierPadding * y));
            }
        }

        for (int x = 0; x < 5; x++) {
            targetBarriers.add(new Barrier(barrierWidth * x + barrierPadding * (x + 1), progressBarHeight, barrierWidth * (x + 1) + barrierPadding * x, progressBarHeight + barrierHeight));
        }
        nrTargetBarriers = targetBarriers.size();

        state = GameState.Running;
    }

}
