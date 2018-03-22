package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.Random;

/**
 * Created by jimbo on 20.3.2018.
 */

public class Enemy extends MyGdxGame{
    private static final float ENEMY_SPEED = 1f;
    private static final float MAX_ENEMY_RIGHT_MOVEMENT = 75;
    private static final float MIN_ENEMY_RIGHT_MOVEMENT = 0;

    // The possibility of spawning an enemy per one render cycle. 1/100
    private static final int ROLL_MULTIPLIER = 1;
    private static final int CHANCES_OUT_OF = 100;

    // Enemy starts going right by default.
    static boolean goRight = true;

    static Body enemy;
    Random rand = new Random();
    public boolean enemySpawned = false;

    public void createEnemy() {
        enemy = createBody("crate",MAX_ENEMY_RIGHT_MOVEMENT,0,0);
        enemy.setLinearVelocity(0,0);
        enemy.setActive(true);
    }

    public void enemyManager() {
        // Keep rolling until the enemy is spawned.
        if(!enemySpawned) {
            if(rand.nextInt(CHANCES_OUT_OF) == ROLL_MULTIPLIER) {
                enemySpawned = true;
                createEnemy();
            }
        }
       if(enemySpawned){
            if(enemy.isActive()) {
                drawSprite("angryTooth", enemy.getPosition().x, enemy.getPosition().y, 0);
                if(enemy.getPosition().x <= MAX_ENEMY_RIGHT_MOVEMENT && goRight) {
                    enemy.setTransform((enemy.getPosition().x+ENEMY_SPEED),enemy.getPosition().y, 0);
                }
                else if(enemy.getPosition().x == MAX_ENEMY_RIGHT_MOVEMENT && goRight) {
                    goRight = !goRight;
                }
                else if(enemy.getPosition().x >= MIN_ENEMY_RIGHT_MOVEMENT && !goRight) {
                    enemy.setTransform((enemy.getPosition().x-ENEMY_SPEED),enemy.getPosition().y, 0);
                }
                else {
                    goRight = !goRight;
                }
            }
        }
    }
}
