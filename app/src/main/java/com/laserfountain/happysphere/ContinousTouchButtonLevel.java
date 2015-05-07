package com.laserfountain.happysphere;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

import com.laserfountain.framework.Game;
import com.laserfountain.framework.Graphics;
import com.laserfountain.framework.Input.TouchEvent;

public class ContinousTouchButtonLevel extends LevelScreen {

    protected int gameHeight;
    protected int gameWidth;

    float maxTouchTime;

    float totalTouchTimeLeft;

    int circleRadius;

    List<ContinuousCountdownButton> circles;

    private Paint circlePainter;
    private Paint buttonTextPainter;

    public ContinousTouchButtonLevel(Game game) {
        super(game);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        circles = new ArrayList<ContinuousCountdownButton>();

        circlePainter = new Paint();
        circlePainter.setColor(ColorPalette.button);
        circlePainter.setStyle(Style.FILL_AND_STROKE);
        circlePainter.setStrokeWidth(game.scale(5));
        circlePainter.setAntiAlias(true);
        circlePainter.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);

        buttonTextPainter = new Paint();
        buttonTextPainter.setTextAlign(Paint.Align.CENTER);
        buttonTextPainter.setAntiAlias(true);
        buttonTextPainter.setColor(Color.WHITE);
        buttonTextPainter.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        totalTouchTimeLeft = 0;

        int len = touchEvents.size();

        for (int j = 0; j < circles.size(); j++) {
            ContinuousCountdownButton button = circles.get(j);
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);
                button.touch(event);
            }
        }
        
        for (int j = 0; j < circles.size(); j++) {
            ContinuousCountdownButton button = circles.get(j);
            button.updateTouchTime(deltaTime);
            totalTouchTimeLeft += button.getTouchTimeLeft();
        }

        if (totalTouchTimeLeft <= 0) {
            state = GameState.Finished;
        }
    }

    @Override
    double percentDone() {
        return ((double) (maxTouchTime - totalTouchTimeLeft))/maxTouchTime;
    }

    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        buttonTextPainter.setTextSize(circleRadius/2);

        for (Iterator<ContinuousCountdownButton> iterator = circles.iterator(); iterator.hasNext();) {
            ContinuousCountdownButton button = (ContinuousCountdownButton) iterator.next();
            if (button.isTouched()) {
                circlePainter.setColor(ColorPalette.progress);
            } else {
                circlePainter.setColor(ColorPalette.button);
            }
            g.drawCircle(button.centerX, button.centerY, button.getMaxRadius(), circlePainter);
            g.drawString(String.format("%.1f", button.getTouchTimeLeft()), button.centerX, button.centerY, buttonTextPainter);
        }
    }

}
