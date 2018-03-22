package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Iterator;

/**
 * Created by velij on 19.3.2018.
 */

public class VNScreen implements Screen {
    final RottenRoots game;
    OrthographicCamera camera;
    ExtendViewport viewport;
    public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
    SpriteBatch spriteBatch;
    TextureRegion rect;
    Texture fontTex;
    BitmapFont font;
    String textString = "default_string";
    public String textStatus,string_number;
    int screenWidth,screenHeight;

    public VNScreen(RottenRoots gam) {
        this.game = gam;
        final VNScreen VNscreen = this;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        spriteBatch = new SpriteBatch();

        //?????
        fontTex = new Texture("badlogic.jpg");
        rect = new TextureRegion(fontTex, 250, 250, 1, 1);

        //Text in the box
        font = new BitmapFont();
        font.getData().setScale(5f,5f);

        //camera
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(1920, 1080, camera);

        backgroundTexture = new Texture("teeth-background.jpg");
        backgroundSprite =new Sprite(backgroundTexture);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                game.snap.play(1f);
                VNscreen.dispose();
                game.setScreen(new gameScreen(game));
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });

/*        // Warning: totally untested. This is just what I would do looking at the code from:
        // https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/utils/XmlReader.java
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse("res/values/strings.xml");
        Array<XmlReader.Element> items = root.getChildrenByName("item");
        for (XmlReader.Element child : items)
        {
            gameName = child.getChildByName("resources").getAttribute("app_name");
            //String wieldable = child.getChildByName("wieldable").getAttribute("slot");
            //String weapon = child.getChildByName("weapon").getAttribute("damage");
            // do what you wish with these
        }*/

        //FileHandle ProgressFileHandle = new FileHandle("res/values/dialogs.xml");
/*
        FileHandle ProgressFileHandle = Gdx.files.internal("res/values/dialogs.xml");
        XmlReader xml = new XmlReader();
        XmlReader.Element xml_element = xml.parse(ProgressFileHandle);
        Iterator iterator_level = xml_element.getChildrenByName("b").iterator();
        while(iterator_level.hasNext()){
            XmlReader.Element level_element = (XmlReader.Element)iterator_level.next();
            string_number = level_element.getAttribute("number");
            textStatus = level_element.getAttribute("status");
        }
*/

/*
<pack name="Cappellino" num_monete_livello="1100">
  <level number="1" status="nolocked" />
  <level number="2" status="locked" />
  <level number="3" status="locked"/>
</pack>
<pack name="Canottiera">
  <level number="4" status="locked"/>
  <level number="5" status="locked"/>
  <level number="6" status="locked"/>
  <level number="7" status="locked"/>
</pack>
<pack name="Tuta">
  <level number="8" status="locked"/>
  <level number="9" status="locked"/>
  <level number="10" status="locked"/>
 </pack>*/

    }

    void TextChange(String text){
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture,0,0,screenWidth,screenHeight);
        //spriteBatch.setColor(0x96a2a5);
        spriteBatch.draw(rect, (float) (screenWidth*0.1), 50, (float) (screenWidth*0.60), 250);
        font.draw(spriteBatch,textString,(float) (screenWidth*0.1) ,250);
        spriteBatch.end();

        //drawRect(180, 50+ 50, 50, 25, 3);
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
        fontTex.dispose();
        font.dispose();
        backgroundTexture.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
