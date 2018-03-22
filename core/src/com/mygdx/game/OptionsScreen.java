package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by velij on 16.3.2018.
 */

public class OptionsScreen implements Screen {
    final RottenRoots game;
    OrthographicCamera camera;
    ExtendViewport viewport;
    SpriteBatch batch;
    Texture backToMenu;
    private static final int BACK_BUTTON_X = (int) (1920*0.04);
    private static final int BACK_BUTTON_Y = (int) (1080*0.88); // for drawing
    private static final int BACK_BUTTON_Y_TOP = (int) (1080*0.04);// for inputadapter
    private static final int BACK_BUTTON_DIAMETER = (int) (1920*0.08);
    private float magic = (float) 1.3333; // 2560*1440 to full hd magic conversion rate
    Texture logo;
    private static final int LOGO_WIDTH = 450;
    private static final int LOGO_HEIGHT = 200;
    private static final int LOGO_Y = (int) (1080*0.88);
    BitmapFont currentName,currentSound,currentdifficulty;
    String Name = "", soundvalue = "",difficultyString = "";
    //fpscounter
    BitmapFont fps;
    int fpsInt = 0;
    String hintname = "Your Name";

    private Stage stage;
    public int row_height;
    public int col_width;

    OptionsScreen(final RottenRoots gam){
        this.game = gam;
        //backToMenu = new Texture("vapenation.bmp");
        logo = new Texture("UI_final/logo.png");
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(1920, 1080, camera);
        batch = new SpriteBatch();
        final int screenWidth = Gdx.graphics.getWidth();
        final int screenHeight = Gdx.graphics.getHeight();
        row_height = Gdx.graphics.getHeight() / 12;
        col_width = Gdx.graphics.getWidth() / 12;

        fps = new BitmapFont(); //fpscounter
        fps.getData().setScale(2f, 2f);

        //skin and stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        //exitbutton
        Button exitButton = new TextButton("EXIT",mySkin,"default");
        exitButton.setSize(col_width, row_height);
        exitButton.setPosition(64,64);
        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                game.setScreen(new startMenu(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(exitButton);

        ImageButton nameEditButton = new ImageButton(mySkin);
        nameEditButton.setSize(col_width,col_width);
        nameEditButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg"))));
        nameEditButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("olvinhenki.jpg"))));
        nameEditButton.setPosition(col_width*2,row_height*5);
        nameEditButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                MyTextInputListener listener = new MyTextInputListener(game);
                Gdx.input.getTextInput(listener, "Enter your name:", "", hintname);            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(nameEditButton);
        //charName bitmapfont
        currentName = new BitmapFont(); //Name of char
        currentName.getData().setScale(5f,5f);

        ImageButton soundButton = new ImageButton(mySkin);
        soundButton.setSize(col_width,col_width);
        soundButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg"))));
        soundButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("olvinhenki.jpg"))));
        soundButton.setPosition(col_width*2,row_height*3);
        soundButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                soundOption();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(soundButton);
        //currentsound bitmapfont
        currentSound = new BitmapFont(); //sound true/false
        currentSound.getData().setScale(5f,5f);

        ImageButton diffucultyButton = new ImageButton(mySkin);
        diffucultyButton.setSize(col_width,col_width);
        diffucultyButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg"))));
        diffucultyButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("olvinhenki.jpg"))));
        diffucultyButton.setPosition(col_width*6,row_height*5);
        diffucultyButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                difficultyChange();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(diffucultyButton);
        //currentdifficulty bitmapfont
        currentdifficulty = new BitmapFont(); //sound true/false
        currentdifficulty.getData().setScale(5f,5f);

/*        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //backToMenu button
                if (  screenX > BACK_BUTTON_X * magic &&
                        screenX < BACK_BUTTON_X + BACK_BUTTON_DIAMETER * magic &&
                        screenY > BACK_BUTTON_Y_TOP * magic &&
                        screenY < BACK_BUTTON_Y_TOP + BACK_BUTTON_DIAMETER* magic)
                {
                    dispose();
                    game.setScreen(new startMenu(game));
                }

                // nameEditButton
                int x = RottenRoots.WIDTH / 2 - LOGO_WIDTH / 2;
                if (game.cam.getInputInGameWorld().x < x + LOGO_WIDTH && game.cam.getInputInGameWorld().x > x &&
                        RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y < LOGO_Y + LOGO_HEIGHT * magic &&
                        RottenRoots.HEIGHT - game.cam.getInputInGameWorld().y > LOGO_Y)
                {
                    MyTextInputListener listener = new MyTextInputListener(game);
                    //hintname = game.prefs.getString("name");
                    Gdx.input.getTextInput(listener, "Enter your name:", "", hintname);

                }
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });*/
    }

    private void difficultyChange(){
        int difficulty = game.prefs.getInteger("difficulty");
        switch (difficulty) {
            case 0: game.prefs.putInteger("difficulty", 1);
                    difficultyString = "normal";
                    break;
            case 1: game.prefs.putInteger("difficulty", 2);
                    difficultyString = "hard";
                    break;
            case 2: game.prefs.putInteger("difficulty", 0);
                    difficultyString = "easy";
                    break;
            default: difficultyString = "Invalid ";
                    break;
        }
        game.prefs.flush();
    }

    private void soundOption(){
        if (!game.prefs.getBoolean("sound")){
            game.prefs.putBoolean("sound",true);
            soundvalue = "true";
        }else
        {
            game.prefs.putBoolean("sound",false);
            soundvalue = "false";
        }
        game.prefs.flush();
    }

    private void getSettings(){
        if(game.prefs.getBoolean("sound")){
            soundvalue = "true";
        }else {
            soundvalue = "false";
        }

        int difficulty = game.prefs.getInteger("difficulty");
        switch (difficulty) {
            case 0:
                difficultyString = "easy";
                break;
            case 1:
                difficultyString = "normal";
                break;
            case 2:
                difficultyString = "hard";
                break;
            default: difficultyString = "Invalid ";
                break;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.57f, 0.95f, 0.45f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getSettings();
        batch.begin();
        //batch.draw(backToMenu,BACK_BUTTON_X,BACK_BUTTON_Y,BACK_BUTTON_DIAMETER,BACK_BUTTON_DIAMETER);   //backToMenu
        fpsInt = Gdx.graphics.getFramesPerSecond();
        fps.draw(batch, "" + fpsInt, 10, 1060); //fpscounter
        Name = game.prefs.getString("name", "no name stored");
        currentName.draw(batch,Name,col_width*3 ,row_height*5);
        currentSound.draw(batch,soundvalue,col_width*3 ,row_height*3);
        currentdifficulty.draw(batch,difficultyString,col_width*6,row_height*5);
        batch.draw(logo, col_width*4 - LOGO_WIDTH / 2, row_height*6, (int)(LOGO_WIDTH*1.5), (int)(LOGO_HEIGHT*1.5)); //logo
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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
        stage.dispose();
        batch.dispose();
        fps.dispose();
        currentSound.dispose();
        currentdifficulty.dispose();
        currentName.dispose();
    }
}
