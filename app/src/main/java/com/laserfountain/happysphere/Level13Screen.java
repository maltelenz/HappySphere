package com.laserfountain.happysphere;

import com.laserfountain.framework.Game;
import com.laserfountain.happysphere.ButtonLink.Type;

public class Level13Screen extends ButtonGraphLevel {

    public Level13Screen(Game game) {
        super(game);

        maxTouches = 3;

        circleRadius = game.scale(100);

        // 0
        circles.add(new CountdownButton(circleRadius, gameWidth/2, gameHeight/2, maxTouches));
        // 1
        circles.add(new CountdownButton(circleRadius, gameWidth/2, gameHeight/2 - gameWidth/3, maxTouches));
        // 2
        circles.add(new CountdownButton(circleRadius, gameWidth/2, gameHeight/2 + gameWidth/3, maxTouches));
        // 3
        circles.add(new CountdownButton(circleRadius, gameWidth/2 - gameWidth/3, gameHeight/2, maxTouches));
        // 4
        circles.add(new CountdownButton(circleRadius, gameWidth/2 + gameWidth/3, gameHeight/2, maxTouches));
        // 5
        circles.add(new CountdownButton(
                circleRadius,
                gameWidth/2 + gameWidth/(3 * Math.sqrt(2)),
                gameHeight/2 + gameWidth/(3 * Math.sqrt(2)),
                maxTouches));
        // 6
        circles.add(new CountdownButton(
                circleRadius,
                gameWidth/2 - gameWidth/(3 * Math.sqrt(2)),
                gameHeight/2 + gameWidth/(3 * Math.sqrt(2)),
                maxTouches));
        // 7
        circles.add(new CountdownButton(
                circleRadius,
                gameWidth/2 - gameWidth/(3 * Math.sqrt(2)),
                gameHeight/2 - gameWidth/(3 * Math.sqrt(2)),
                maxTouches));
        // 8
        circles.add(new CountdownButton(
                circleRadius,
                gameWidth/2 + gameWidth/(3 * Math.sqrt(2)),
                gameHeight/2 - gameWidth/(3 * Math.sqrt(2)),
                maxTouches));

        edges.add(new ButtonLink(0, 1, Type.PlusOne));
        edges.add(new ButtonLink(0, 2, Type.PlusOne));
        edges.add(new ButtonLink(0, 3, Type.PlusOne));
        edges.add(new ButtonLink(0, 4, Type.PlusOne));
        edges.add(new ButtonLink(8, 1, Type.MinusOne));
        edges.add(new ButtonLink(5, 4, Type.MinusOne));
        edges.add(new ButtonLink(6, 2, Type.MinusOne));
        edges.add(new ButtonLink(7, 3, Type.MinusOne));

        startingTouches = circles.size() * maxTouches;

        state = GameState.Running;
    }
}
