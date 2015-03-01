package com.maltelenz.framework;

import com.maltelenz.sensortrouble.Screen;

public interface Game {

    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getInitScreen();

    public void updateMaxLevel(int level);

    /**
     * @return the highest level finished, 1-indexed
     */
    public int getMaxLevel();
}
