package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.HashMap;
import java.util.Random;

public class gameScreen implements Screen {

    static final float SCALE = 1f;
    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;
    float accumulator = 0;
    static final int COUNT = 10;
    Body[] fruitBodies = new Body[COUNT];
    String[] names = new String[COUNT];
    Body ground;
    Box2DDebugRenderer debugRenderer;
    PhysicsShapeCache physicsBodies;
    World world;
    OrthographicCamera camera;
    ExtendViewport viewport;
    SpriteBatch batch;
    TextureAtlas textureAtlas;
    Body banana;
    final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    final RottenRoots game;
    //button
    Texture backToMenu;
    private static final int BACK_BUTTON_X = (int) (1920*0.04);
    private static final int BACK_BUTTON_Y = (int) (1080*0.88);
    private static final int BACK_BUTTON_Y_TOP = (int) (1080*0.04);
    private static final int BACK_BUTTON_DIAMETER = (int) (1920*0.08);
    float magic = (float) 1.3333; // 2560*1440 to full hd magic conversion rate

    //fpscounter
    BitmapFont fps;
    int fpsInt = 0;

    public gameScreen(RottenRoots gam){

        this.game = gam;

        final int screenWidth = Gdx.graphics.getWidth();
        final int screenHeight = Gdx.graphics.getHeight();

        backToMenu = new Texture("vapenation.bmp");

        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();
        physicsBodies = new PhysicsShapeCache("physics.xml");
        world = new World(new Vector2(0, -80), true);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(1920, 1080, camera);

        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas("texturePack.txt");
        //banana = textureAtlas.createSprite("banana");
        //banana = createBody("banana", 10, 10, 0);

        //fpscounter
        fps = new BitmapFont();
        fps.getData().setScale(2f, 2f);

        generateFruit();

        addSprites();
/*
    World is this size, (not your screen size)
	public static final int WIDTH = 720;
	public static final int HEIGHT = 480;
 */
        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //backToMenu button
                //coordinates are on your phones resolution, starting from top left corner
                //int x = (int) (screenWidth * 0.02); // 2560 *0.02 = 51.2
                //int y = (int) (screenHeight * 0.02); //1440 *0.02 = 28.8

                if (  screenX > BACK_BUTTON_X * magic &&
                        screenX < BACK_BUTTON_X + BACK_BUTTON_DIAMETER * magic &&
                        screenY > BACK_BUTTON_Y_TOP * magic &&
                        screenY < BACK_BUTTON_Y_TOP + BACK_BUTTON_DIAMETER* magic)
                {
                    dispose();
                    game.setScreen(new startMenu(game));
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

        Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stepWorld();
        batch.begin();
        //button
        batch.draw(backToMenu,BACK_BUTTON_X,BACK_BUTTON_Y,BACK_BUTTON_DIAMETER,BACK_BUTTON_DIAMETER);

        //fpscounter
        fpsInt = Gdx.graphics.getFramesPerSecond();
        fps.draw(batch, "" + fpsInt, 10, 1060);

        for (int i = 0; i < fruitBodies.length; i++) {
            Body body = fruitBodies[i];
            String name = names[i];

            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSprite(name, position.x, position.y, degrees);
        }
        batch.end();
        // uncomment to show the polygons
        debugRenderer.render(world, camera.combined);
    }

    private void generateFruit() {
        String[] fruitNames = new String[]{"banana", "cherries", "orange"};

        Random random = new Random();

        for (int i = 0; i < fruitBodies.length; i++) {
            String name = fruitNames[random.nextInt(fruitNames.length)];

            float x = random.nextFloat() * 500;
            float y = random.nextFloat() * 500 + 50;

            names[i] = name;
            fruitBodies[i] = createBody(name, x, y, 0);
        }
    }

    private void createGround() {

        if (ground != null) world.destroyBody(ground);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(camera.viewportWidth, 1);
        fixtureDef.shape = shape;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);
        ground.setTransform(0, 0, 0);

        shape.dispose();
    }

    private Body createBody(String name, float x, float y, float rotation) {
        Body body = physicsBodies.createBody(name, world, SCALE, SCALE);
        body.setTransform(x, y, rotation);

        return body;
    }

    private void addSprites() {
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);

            float width = sprite.getWidth() * SCALE;
            float height = sprite.getHeight() * SCALE;

            sprite.setSize(width, height);
            sprite.setOrigin(0, 0);

            sprites.put(region.name, sprite);
        }
    }

    private void drawSprite(String name, float x, float y, float degrees) {
        Sprite sprite = sprites.get(name);
        sprite.setPosition(x, y);
        sprite.setRotation(degrees);
        sprite.draw(batch);
    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
        createGround();
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
        textureAtlas.dispose();
        sprites.clear();
        world.dispose();
        debugRenderer.dispose();
    }
}
