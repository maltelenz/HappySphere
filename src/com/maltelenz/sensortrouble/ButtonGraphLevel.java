package com.maltelenz.sensortrouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input.TouchEvent;
import com.maltelenz.sensortrouble.ButtonLink.Type;

public class ButtonGraphLevel extends LevelScreen {

    protected int gameHeight;
    protected int gameWidth;

    int maxTouches;

    int totalTouchesLeft;

    int circleRadius;

    List<CountdownButton> circles;
    List<ButtonLink> edges;

    protected int startingTouches;

    Paint linePaint;

    private Button reStartButton;
    private int reStartButtonWidth;
    private int reStartButtonHeight;

    private Paint circlePainter;

    public ButtonGraphLevel(Game game) {
        super(game);

        gameHeight = game.getGraphics().getHeight();
        gameWidth = game.getGraphics().getWidth();

        circles = new ArrayList<CountdownButton>();
        edges = new ArrayList<ButtonLink>();

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(game.scale(10));

        reStartButtonWidth = game.scaleX(500);
        reStartButtonHeight = game.scaleY(150);

        reStartButton = new Button("Restart",
                (gameWidth - reStartButtonWidth)/2,
                gameHeight - 2 * reStartButtonHeight,
                (gameWidth + reStartButtonWidth)/2,
                gameHeight - reStartButtonHeight);

        circlePainter = new Paint();
        circlePainter.setColor(ColorPalette.button);
        circlePainter.setStyle(Style.FILL_AND_STROKE);
        circlePainter.setStrokeWidth(game.scale(5));
        circlePainter.setAntiAlias(true);
        circlePainter.setShadowLayer(game.scale(10.0f), game.scale(2.0f), game.scale(2.0f), ColorPalette.buttonShadow);

    }

    @Override
    protected void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
        totalTouchesLeft = 0;

        int len = touchEvents.size();

        for (int j = 0; j < circles.size(); j++) {
            CountdownButton button = circles.get(j);
            button.decreaseFlashTime(deltaTime);
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);
                if (event.type == TouchEvent.TOUCH_DOWN) {
                    if (button.touch(event)) {
                        for (Iterator<ButtonLink> iterator2 = edges.iterator(); iterator2.hasNext();) {
                            ButtonLink edge = (ButtonLink) iterator2.next();
                            if (edge.getFrom() == j) {
                                if (edge.getType() == Type.PlusOne) {
                                    circles.get(edge.getTo()).increaseTouchesLeft();
                                } else if (edge.getType() == Type.MinusOne) {
                                    circles.get(edge.getTo()).decreaseTouchesLeft();
                                }
                            }
                        }
                    }
                } else if (event.type == TouchEvent.TOUCH_UP) {
                    if (reStartButton.inBounds(event)) {
                        // Restart level
                        startLevel(currentLevel());
                        return;
                    }
                }
            }
            totalTouchesLeft += Math.abs(button.getTouchesLeft());
        }

        if (totalTouchesLeft <= 0) {
            state = GameState.Finished;
        }
    }

    @Override
    double percentDone() {
        return ((double) (startingTouches - totalTouchesLeft))/startingTouches;
    }

    void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.clearScreen(ColorPalette.background);

        Paint buttonTextPainter = new Paint();
        buttonTextPainter.setTextSize(circleRadius/2);
        buttonTextPainter.setTextAlign(Paint.Align.CENTER);
        buttonTextPainter.setAntiAlias(true);
        buttonTextPainter.setColor(Color.WHITE);
        buttonTextPainter.setTypeface(Typeface.DEFAULT_BOLD);

        for (Iterator<ButtonLink> iterator = edges.iterator(); iterator.hasNext();) {
            ButtonLink edge = (ButtonLink) iterator.next();
            CountdownButton fCircle = circles.get(edge.getFrom());
            CountdownButton tCircle = circles.get(edge.getTo());

            if (edge.getType() == Type.PlusOne) {
                linePaint.setColor(ColorPalette.progress);
            } else if (edge.getType() == Type.MinusOne) {
                linePaint.setColor(ColorPalette.laser);
            }
            g.drawLine(fCircle.centerX, fCircle.centerY, tCircle.centerX, tCircle.centerY, linePaint);

            Path arrowPath = new Path();
            float width = 0.5f;
            float length = 0.5f;
            arrowPath.moveTo(-width/2, 0);
            arrowPath.lineTo(-width/2, 0);
            arrowPath.lineTo(width/2, 0);
            arrowPath.lineTo(0, length);
            arrowPath.lineTo(-width/2, 0);
            arrowPath.close();

            float dx = (float) (tCircle.centerX - fCircle.centerX);
            float dy = (float) (tCircle.centerY - fCircle.centerY);
            Matrix transform = new Matrix();
            float src[] = {0, 0, 0, length};
            float dst[] = {
                    (float) (fCircle.centerX + dx * 0.5f),
                    (float) (fCircle.centerY + dy * 0.5f),
                    (float) (fCircle.centerX + dx * 0.6f),
                    (float) (fCircle.centerY + dy * 0.6f),
                };
            transform.setPolyToPoly(src, 0, dst, 0, 2);
            arrowPath.transform(transform);
            g.drawPath(arrowPath, linePaint);
        }

        for (Iterator<CountdownButton> iterator = circles.iterator(); iterator.hasNext();) {
            CountdownButton button = (CountdownButton) iterator.next();
            if (button.isFlashing()) {
                circlePainter.setColor(ColorPalette.progress);
            } else if (button.getTouchesLeft() < 0) {
                circlePainter.setColor(ColorPalette.oopsie);
            } else {
                circlePainter.setColor(ColorPalette.button);
            }
            g.drawCircle(button.centerX, button.centerY, button.getMaxRadius(), circlePainter);
            g.drawString(Integer.toString(button.getTouchesLeft()), button.centerX, button.centerY, buttonTextPainter);
        }

        g.drawButton(reStartButton);
    }

}
