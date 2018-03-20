package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Created by velij on 19.3.2018.
 */

class VNScreen implements Screen {
    final RottenRoots game;
    OrthographicCamera camera;
    ExtendViewport viewport;
    public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
    SpriteBatch spriteBatch;
    Texture logo;
    int screenWidth,screenHeight;

    public VNScreen(RottenRoots gam) {
        this.game = gam;
        final VNScreen VNscreen = this;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        spriteBatch = new SpriteBatch();

        //camera
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(1920, 1080, camera);

        logo = new Texture("RRPlaceHolderLogo.png");

        backgroundTexture = new Texture("teeth-background.jpg");
        backgroundSprite =new Sprite(backgroundTexture);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                VNscreen.dispose();
                game.setScreen(new gameScreen(game));
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });
    }

    public void renderBackground() {
        backgroundSprite.draw(spriteBatch);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        //renderBackground(); //In first place!!!!
        spriteBatch.draw(backgroundTexture,0,0,screenWidth,screenHeight);
        spriteBatch.end();

    }
    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
