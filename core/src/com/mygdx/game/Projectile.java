package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

public class Projectile extends Player {
    private static final int BULLET_SPEED = 5000;
    private static Vector3 vec3 = new Vector3();

    static Body bullet;
    //Test

    public Projectile() {}

    public void createBullet() {
        bullet = createBody("orange",player.getPosition().x + 10,player.getPosition().y  + 7,0);
        bullet.setLinearVelocity(0,0);
        bullet.setGravityScale(0);
        bullet.setActive(false);
    }

    public void resetBullet() {
        bullet.setTransform(10,7,0);
        bullet.setLinearVelocity(0,0);
        bullet.setAngularVelocity(0);
        bullet.setActive(false);
    }

    public void ammunitionManager() {
        // Draw the empty ammo shells behind full ammo shells.
        for(int i = 0;i<MAX_NUMBER_OF_AMMO;i++) {
            drawSprite("ammoSilhoutte", 2 + (5*i), 36, 0);
        }
        // Draw every full ammo shell, that the player has.
        for(int i = 0;i<numberOfAmmo;i++) {
            drawSprite("ammo", 2 + (5*i), 36, 0);
        }

        // Draw the bullet when user taps on screen and has ammo.
        if(bullet.isActive()) {
            drawSprite("bullet", bullet.getPosition().x, bullet.getPosition().y, 0);
        }

        if (Gdx.input.isTouched()) {
            if(bullet.getLinearVelocity().epsilonEquals(0,0)) {
                if(numberOfAmmo != 0) {
                    bullet.setActive(true);
                    vec3.set(Gdx.input.getX(),Gdx.input.getY(), 0);
                    camera.unproject(vec3);
                    bullet.applyLinearImpulse(vec3.x*BULLET_SPEED,vec3.y*BULLET_SPEED,bullet.getWorldCenter().x,bullet.getWorldCenter().y,true);
                    numberOfAmmo--;
                }
            }
        }
        // Reset bullet if it goes beyond game screen
        if(bullet.getPosition().x >= viewport.getWorldWidth() || bullet.getPosition().y >= viewport.getWorldHeight()) {
            resetBullet();
        }
    }

    public void reload() {
        // Reload when ammo hits 0.
        numberOfAmmo = MAX_NUMBER_OF_AMMO;
    }

    public void gainAmmo() {
        if(numberOfAmmo < 3) {
            numberOfAmmo++;
        }
    }
}
