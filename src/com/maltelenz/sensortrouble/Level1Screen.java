package com.maltelenz.sensortrouble;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;
import com.maltelenz.framework.Screen;

public class Level1Screen extends LevelScreen {

    int maxTouches = 5;
    int touchesLeft = maxTouches;
    
    int circleRadius = 300;

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

    @Override
    float percentDone() {
        return ((float) (maxTouches - touchesLeft))/maxTouches;
    }
    
    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        Paint circlePainter = new Paint();
        circlePainter.setColor(ColorPalette.cherry);
        circlePainter.setStyle(Style.STROKE);
        circlePainter.setStrokeWidth(5);
        g.drawCircle(g.getWidth()/2, g.getHeight()/2, circleRadius, circlePainter);

        Paint largePainter = new Paint();
        largePainter.setTextSize(150);
        largePainter.setTextAlign(Paint.Align.CENTER);
        largePainter.setAntiAlias(true);
        largePainter.setColor(Color.WHITE);
        largePainter.setTypeface(Typeface.DEFAULT_BOLD);
        g.drawStringCentered(Integer.toString(touchesLeft), largePainter);
    }

    @Override
    protected Screen nextLevel() {
        return (new MainMenuScreen(game));
    }
}
