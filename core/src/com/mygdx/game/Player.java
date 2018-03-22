package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;

public class Player extends MyGdxGame {
    static final int MAX_HEALTH = 3;
    static final int MAX_NUMBER_OF_AMMO = 3;
    static int numberOfAmmo = MAX_NUMBER_OF_AMMO;
    static int playerHealth = MAX_HEALTH;

    static Body player;
    public static boolean gameOver = false;

    public Player() {}

    public void playerManager() {
        // Draw the player sprite.
        if(playerHealth == 3) {
            drawSprite("toothWizard", player.getPosition().x,player.getPosition().y, 0);
        }
        else if(playerHealth == 2) {
            drawSprite("toothWizard", player.getPosition().x,player.getPosition().y, 0);
        }
        else if(playerHealth == 1){
            drawSprite("toothWizardLowHealth", player.getPosition().x,player.getPosition().y, 0);
        }
        else {
            drawSprite("deadWizard", player.getPosition().x,player.getPosition().y, 0);
        }
        // Draw the black hearts behind red hearts.
        for(int i = 0;i<MAX_HEALTH;i++) {
            drawSprite("heartSilhoutte", 2 + (i*5), 46, 0);
        }
        // Draw every full red heart, that the player has.
        for(int i = 0;i<playerHealth;i++) {
            drawSprite("heart", 2 + (i*5), 46, 0);
        }
    }

    public void createPlayer() {
        player = createBody("crate", 0,0,0);
        player.setLinearVelocity(0,0);
        player.setGravityScale(0);
    }

    public void loseHealth() {
        // Decrease player health by 1 when hit.
        if(playerHealth != 0) {
            playerHealth--;
        }
        // If the player health hits 0, start game over procedure.
        if(playerHealth == 0) {
            gameOver = true;
            soundMaster.dyingSound.play();
        }
    }

    public void fullHeal() {
        playerHealth = MAX_HEALTH;
    }

    public void gainHealth() {
        if(playerHealth < 3) {
            playerHealth++;
        }
    }
}
