package com.maltelenz.sensortrouble;

import com.maltelenz.framework.implementation.AndroidGame;

public class SensorGame extends AndroidGame {
    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this);
    }
}
