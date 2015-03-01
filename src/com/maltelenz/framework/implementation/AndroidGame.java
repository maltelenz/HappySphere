package com.maltelenz.framework.implementation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.maltelenz.framework.FileIO;
import com.maltelenz.framework.Game;
import com.maltelenz.framework.Graphics;
import com.maltelenz.framework.Input;
import com.maltelenz.sensortrouble.R;
import com.maltelenz.sensortrouble.Screen;

public abstract class AndroidGame extends Activity implements Game {
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    private float xScale = 1.0F;
    private float yScale = 1.0F;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );

        Bitmap frameBuffer = getFrameBuffer();

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        input = new AndroidInput(this, renderView, xScale, yScale);
        screen = getInitScreen();
        setContentView(renderView);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onResume() {
        super.onResume();
        screen.resume();
        // Create a new frame buffer, because the screen may be rotated
        Bitmap framebuffer = getFrameBuffer();
        renderView.resume(framebuffer);
        graphics = new AndroidGraphics(getAssets(), framebuffer);
    }

    @Override
    public void onPause() {
        super.onPause();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public void onBackPressed() {
        if(screen.backButton()) {
            // If the screen says we can exit, use default implementation and exit.
            super.onBackPressed();
        }
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Context getContext() {
        return this;
    }
    
    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    public Screen getCurrentScreen() {
        return screen;
    }

    private SharedPreferences getLevelPreferences() {
        return this.getSharedPreferences(getString(R.string.level_preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public void updateMaxLevel(int level) {
        if (level > getMaxLevel()) {
            SharedPreferences preferences = getLevelPreferences();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(getString(R.string.max_level_achieved), level);
            editor.commit();
        }
    }

    @Override
    public int getMaxLevel() {
        SharedPreferences preferences = getLevelPreferences();
        return preferences.getInt(getString(R.string.max_level_achieved), 0);
    }

    @SuppressWarnings("deprecation")
    private int getFrameBufferWidth() {
        return getWindowManager().getDefaultDisplay().getWidth();
    }
    @SuppressWarnings("deprecation")
    private int getFrameBufferHeight() {
        return getWindowManager().getDefaultDisplay().getHeight();
    }

    private Bitmap getFrameBuffer() {
        Log.d("FB Width", Integer.toString(getFrameBufferWidth()));
        Log.d("FB Height", Integer.toString(getFrameBufferHeight()));
        Bitmap frameBuffer = Bitmap.createBitmap(getFrameBufferWidth(), getFrameBufferHeight(), Config.RGB_565);
        return frameBuffer;
    }
}