package com.maltelenz.sensortrouble;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Screen;
import com.maltelenz.framework.Input.TouchEvent;

public class GameScreen extends Screen {
    enum GameState {
        Running, Finished
    }

    GameState state = GameState.Running;

    // Variable Setup
    // You would create game objects here.

    int touchesLeft = 6;
    Paint paint;

    public GameScreen(Game game) {
        super(game);

        // Initialize game objects here

        // Defining a paint object
        paint = new Paint();
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        if (state == GameState.Running)
            updateGameRunning(touchEvents, deltaTime);
        if (state == GameState.Finished)
            updateGameFinished(touchEvents);
    }

    private void updateGameRunning(List<TouchEvent> touchEvents, float deltaTime) {
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

	private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
			return true;
		else
			return false;
	}
    
    private void updateGameFinished(List<TouchEvent> touchEvents) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.next, 0, 0);
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 0, 250, 250)) {
					// Restart Game
                    nullify();
                    game.setScreen(new MainMenuScreen(game));
                    return;
				}
			}
        }

    }

    @Override
    public void paint(float deltaTime) {
        // Graphics g = game.getGraphics();

        // First draw the game elements.

        // Example:
        // g.drawImage(Assets.background, 0, 0);
        // g.drawImage(Assets.character, characterX, characterY);

        // Secondly, draw the UI above the game elements.
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Finished)
            drawGameFinishedUI();
    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        paint = null;

        // Call garbage collector to clean up memory.
        System.gc();
    }


    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString(Integer.toString(touchesLeft), 640, 300, paint);
    }

    private void drawGameFinishedUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString("SUCCESS.", 640, 300, paint);
		g.drawImage(Assets.next, 0, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        pause();
    }
}
