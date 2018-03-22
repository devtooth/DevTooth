package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by jimbo on 21.3.2018.
 */

public class SoundMaster {
    public static Sound damageTakenSound;
    public static Sound dyingSound;

    public SoundMaster() {
        damageTakenSound = Gdx.audio.newSound(Gdx.files.internal("damageTaken.mp3"));
        dyingSound = Gdx.audio.newSound(Gdx.files.internal("youDied.mp3"));
    }
}
