package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class startMenu implements Screen{

    private static final int EXIT_BUTTON_WIDTH = 300;
    private static final int EXIT_BUTTON_HEIGHT = 200;
    private static final int PLAY_BUTTON_WIDTH = 450;
    private static final int PLAY_BUTTON_HEIGHT = 200;
    private static final int LOGO_WIDTH = 650;
    private static final int LOGO_HEIGHT = 300;
    private static final int EXIT_BUTTON_Y = 150;
    private static final int PLAY_BUTTON_Y = 500;
    private static final int LOGO_Y = 800;

    final RottenRoots game;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture logo;
    Texture OptionsMenu;

    public startMenu(final RottenRoots gam){
        this.game = gam;
        final startMenu startMenu = this;
        playButtonActive = new Texture("badlogic.jpg");
        playButtonInactive = new Texture("badlogic.jpg");
        exitButtonActive = new Texture("badlogic.jpg");
        exitButtonInactive = new Texture("badlogic.jpg");
        logo = new Texture("UI_final/logo.png");
        OptionsMenu = new Texture("gear-button.png");

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //Exit button
                int x = RottenRoots.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
                if (game.cam.getInputInGameWorld().x < x/2 + EXIT_BUTTON_WIDTH &&
                        game.cam.getInputInGameWorld().x > x/2 &&
                        RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                        RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y)
                {
                    startMenu.dispose();
                    Gdx.app.exit();
                }

                //Options button
                int x2 = x+ x/2;
                if (game.cam.getInputInGameWorld().x < x2 + EXIT_BUTTON_WIDTH &&
                        game.cam.getInputInGameWorld().x > x2 &&
                        RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                        RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y)
                {
                    startMenu.dispose();
                    game.setScreen(new OptionsScreen(game));
                }

                //Play game button
                x = RottenRoots.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
                if (game.cam.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x &&
                        RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT &&
                        RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y > PLAY_BUTTON_Y)
                {
                    startMenu.dispose();
                    game.setScreen(new gameScreen(game));
                }

                return super.touchUp(screenX, screenY, pointer, button);
            }

        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        //game.scrollingBackground.updateAndRender(delta, game.batch);

        int exit_x = RottenRoots.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;

        //exitbutton
        if (game.cam.getInputInGameWorld().x < exit_x + EXIT_BUTTON_WIDTH &&
                game.cam.getInputInGameWorld().x > exit_x &&
                RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y)
        {
            game.batch.draw(exitButtonActive, exit_x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }
        else {
            game.batch.draw(exitButtonInactive, exit_x - exit_x/2, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        int settings_x = RottenRoots.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
        //settings button
        game.batch.draw(OptionsMenu,settings_x + settings_x/2,EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

        //playbutton
        int play_x = RottenRoots.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
        if (game.cam.getInputInGameWorld().x < play_x + PLAY_BUTTON_WIDTH &&
                game.cam.getInputInGameWorld().x > play_x &&
                RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT &&
                RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y > PLAY_BUTTON_Y)
        {
            game.batch.draw(playButtonActive, play_x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }
        else {
            game.batch.draw(playButtonInactive, play_x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        game.batch.draw(logo, RottenRoots.WIDTH / 2 - LOGO_WIDTH / 2, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT); //logo

        game.batch.end();

    }


    @Override
    public void resize(int width, int height) {

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
        playButtonActive.dispose();
        playButtonInactive.dispose();
        exitButtonActive.dispose();
        exitButtonInactive.dispose();
        logo.dispose();
        OptionsMenu.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
