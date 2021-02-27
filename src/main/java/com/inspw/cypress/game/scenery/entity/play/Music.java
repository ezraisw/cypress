package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.scenery.Entity;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.io.File;

public class Music extends Entity {
    private static final int VOLUME = 100;

    private final MediaPlayer mediaPlayer;

    public Music() {
        this.mediaPlayer = new MediaPlayerFactory("--no-metadata-network-access")
                .mediaPlayers().newMediaPlayer();
    }

    public void play() {
        mediaPlayer.controls().play();
    }

    public void stop() {
        mediaPlayer.controls().stop();
    }

    public long getLength() {
        return mediaPlayer.status().length();
    }

    public boolean isPlaying() {
        return mediaPlayer.status().isPlaying();
    }

    public long getTime() {
        return mediaPlayer.status().time();
    }

    @Override
    public void unload() {
        super.unload();
        mediaPlayer.release();
    }

    @Override
    public void load() {
        super.load();

        File file = Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getDifficulty()
                .getSoundFile();

        mediaPlayer.media().startPaused(file.getAbsolutePath());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isPlaying() && mediaPlayer.audio().volume() != VOLUME) {
            mediaPlayer.audio().setVolume(VOLUME);
        }
    }
}
