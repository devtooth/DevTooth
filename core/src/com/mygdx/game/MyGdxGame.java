package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
	private static final float STEP_TIME = 1f / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	private static final float SCALE = 0.065f;
	private static final int WIDTH = 50;
	private static final int HEIGHT = 50;

	private static float accumulator = 0;

	private static boolean enemyIsHit = false;
	private static boolean playerIsHit = false;

	static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	private static PhysicsShapeCache physicsBodies;
	private static TextureAtlas textureAtlas;
	private static SpriteBatch batch;
	private static Body ground;
	private static World world;

	static OrthographicCamera camera;
	static ExtendViewport viewport;

	private Projectile projectile;
	public Player playerClass;
	private Enemy enemyClass;
	public SoundMaster soundMaster;

	public float fadeTimeAlpha = 0.1f;
	Texture texture;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(WIDTH, HEIGHT, camera);

		batch = new SpriteBatch();
		textureAtlas = new TextureAtlas("sprites.txt");
		addSprites();

		Box2D.init();
		world = new World(new Vector2(0, -10), true);
		physicsBodies = new PhysicsShapeCache("physics.xml");

		createCollisionListener();
		texture = new Texture("youDied.png");

		projectile = new Projectile();
		playerClass = new Player();
		enemyClass = new Enemy();
		soundMaster = new SoundMaster();

		playerClass.createPlayer();
		projectile.createBullet();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);

		batch.setProjectionMatrix(camera.combined);
		createGround();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stepWorld();

		batch.begin();
		// If game over happens, draw gameOver-texture on screen else keep playing normally.
		if(playerClass.gameOver) {
			//damageTakenSound.dispose();
			if(fadeTimeAlpha < 1.0f) {
				fadeTimeAlpha += 0.004f;
				batch.setColor(1.0f, 1.0f, 1.0f, fadeTimeAlpha);
				batch.draw(texture, 0, 0,viewport.getWorldWidth(), viewport.getWorldHeight());
			}
			else {
				batch.setColor(1.0f, 1.0f, 1.0f, 5.0f);
				batch.draw(texture, 0, 0,viewport.getWorldWidth(), viewport.getWorldHeight());
				if(Gdx.input.isTouched()) {
					playerClass.gameOver = false;
					fadeTimeAlpha = 0.1f;
					projectile.reload();
					playerClass.fullHeal();
				}
			}
		}
		else {
			playerClass.playerManager();
			enemyClass.enemyManager();
			projectile.ammunitionManager();

			// If collision happened between the projectile and enemy, the following commands will take place.
			if(enemyIsHit) {
				enemyClass.enemy.setActive(false);
				projectile.gainAmmo();
				projectile.resetBullet();
				enemyIsHit = false;
				enemyClass.enemySpawned = false;
			}

			// If collision happened between the player and the enemy, the following commands will take place.
			if(playerIsHit) {
				soundMaster.damageTakenSound.play();
				playerClass.loseHealth();
				enemyClass.enemy.setActive(false);
				playerIsHit = false;
				enemyClass.enemySpawned = false;
			}
		}
		batch.end();
	}

	@Override
	public void dispose() {
		textureAtlas.dispose();
		world.dispose();
		sprites.clear();
	}

	public static void drawSprite(String name, float x, float y, float degrees) {
		Sprite sprite = sprites.get(name);
		sprite.setPosition(x, y);
		sprite.setRotation(degrees);
		sprite.setOrigin(0f,0f);
		sprite.draw(batch);
	}

	public static Body createBody(String name, float x, float y, float rotation) {
		Body body = physicsBodies.createBody(name, world, SCALE, SCALE);
		body.setTransform(x, y, rotation);
		return body;
	}

	private void stepWorld() {
		float delta = Gdx.graphics.getDeltaTime();

		accumulator += Math.min(delta, 0.25f);

		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;

			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}

	public void addSprites() {
		Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

		for (TextureAtlas.AtlasRegion region : regions) {
			Sprite sprite = textureAtlas.createSprite(region.name);

			float width = sprite.getWidth() * SCALE;
			float height = sprite.getHeight() * SCALE;

			sprite.setSize(width, height);

			sprites.put(region.name, sprite);
		}
	}

	public void createGround() {
		if (ground != null) world.destroyBody(ground);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;

		FixtureDef fixtureDef = new FixtureDef();

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(camera.viewportWidth, 1);

		fixtureDef.shape = shape;

		ground = world.createBody(bodyDef);
		ground.createFixture(fixtureDef);
		ground.setTransform(0, 0, 0);

		shape.dispose();
	}

	public void createCollisionListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				//Check collision between bullet and enemy.
				if((contact.getFixtureA().getBody() == projectile.bullet &&
						contact.getFixtureB().getBody() == enemyClass.enemy)
						||
						(contact.getFixtureA().getBody() == enemyClass.enemy &&
								contact.getFixtureB().getBody() == projectile.bullet)) {
					enemyIsHit = true;
				}
				//Check collision between player and enemy.
				if((contact.getFixtureA().getBody() == playerClass.player &&
						contact.getFixtureB().getBody() == enemyClass.enemy)
						||
						(contact.getFixtureA().getBody() == enemyClass.enemy &&
								contact.getFixtureB().getBody() == playerClass.player)) {
					playerIsHit = true;
				}
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

		});
	}
}