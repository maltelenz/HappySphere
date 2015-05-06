package com.maltelenz.framework;

import android.content.Context;

import com.maltelenz.sensortrouble.Screen;

public interface Game {

    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getInitScreen();

    public Context getContext();

    public int scaleX(int in);

    public int scaleY(int in);

    public int scale(int in);

    public float scaleX(float in);

    public float scaleY(float in);

    public float scale(float in);

    public void updateMaxLevel(int level);

    /**
     * @return the highest level finished, 1-indexed
     */
    public int getMaxLevel();

    /**
     * Unlocks the screen orientation.
     */
    public void unLockOrientation();

    /**
     * Locks the screen in portrait mode.
     */
    public void lockOrientationPortrait();

    /**
     * Locks the screen in landscape mode.
     */
    public void lockOrientationLandscape();
}
