package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class RottenRoots extends Game{
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;

	public SpriteBatch batch;
	public gameCamera cam;
	BitmapFont font;
    public Preferences prefs;
	SoundMaster soundMaster;
	Sound snap;

	RottenRoots(){}

	@Override
	public void create () {
		soundMaster = new SoundMaster();

        prefs = Gdx.app.getPreferences("My Preferences");
        prefs.flush();

		batch = new SpriteBatch();
		cam = new gameCamera(WIDTH, HEIGHT);
		//Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new startMenu(this));
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(cam.combined());
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		cam.update(width, height);
		super.resize(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

}
