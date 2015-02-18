package com.maltelenz.sensortrouble;

import java.util.List;

import android.graphics.Color;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;
import com.maltelenz.framework.Screen;

public class Level1Screen extends LevelScreen {

    int touchesLeft = 6;

    public Level1Screen(Game game) {
        super(game);
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            if (event.type == TouchEvent.TOUCH_DOWN) {
                touchesLeft--;
            }
        }
        if (touchesLeft == 0) {
            state = GameState.Finished;
        }
    }

    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(Color.GRAY);
        g.drawStringCentered(Integer.toString(touchesLeft));
    }

    @Override
    protected Screen nextLevel() {
        return (new MainMenuScreen(game));
    }
}
