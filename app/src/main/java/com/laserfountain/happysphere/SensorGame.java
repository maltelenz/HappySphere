package com.laserfountain.happysphere;

import com.laserfountain.framework.implementation.AndroidGame;

public class SensorGame extends AndroidGame {
    @Override
    public Screen getInitScreen() {
        return new MainMenuScreen(this);
    }
}
