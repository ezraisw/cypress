package com.inspw.cypress.game.scenery.entity.select;

import com.inspw.cypress.game.model.Song;
import com.inspw.cypress.game.scenery.entity.general.BinaryTransitionalEntity;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.awt.*;

public class MusicPreview extends BinaryTransitionalEntity {
    private static final float FADEIN_DURATION = 0.7F;
    private static final float FADEOUT_DURATION = 0.1F;
    private static final float HIDDEN_DURATION = 1.0F;
    private static final float PREVIEW_FADEOUT_LENGTH = 3.5F;
    private static final float PREVIEW_FADEIN_LENGTH = 0.7F;

    private final MediaPlayer mediaPlayer;
    private float songTime;
    private float volumeMultiplier;
    private boolean looped;
    private Song newSong;
    private Song song;

    public MusicPreview() {
        super(FADEIN_DURATION, FADEOUT_DURATION, -1, HIDDEN_DURATION);
        mediaPlayer = new MediaPlayerFactory("--no-metadata-network-access")
                .mediaPlayers().newMediaPlayer();
        volumeMultiplier = 1;
    }

    public void newSong(Song song) {
        this.newSong = song;
        switchToDisabled();
    }

    @Override
    public void unload() {
        super.unload();
        mediaPlayer.release();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (song != null && mediaPlayer.status().isPlaying()) {
            songTime += deltaTime;

            long previewStart = song.getSongInfo().getPreviewStart();
            long previewEnd = song.getSongInfo().getPreviewEnd();

            float previewLength = (float) (previewEnd - previewStart) / 1000;
            float fadeInEnd = PREVIEW_FADEIN_LENGTH;
            float fadeoutStart = previewLength - PREVIEW_FADEOUT_LENGTH;

            if (looped && songTime < fadeInEnd) {
                float songTimerToFadeinEnd = fadeInEnd - songTime;
                volumeMultiplier = (float) (1 - songTimerToFadeinEnd / PREVIEW_FADEIN_LENGTH);
            } else if (songTime > previewLength) {
                reset(true);
                mediaPlayer.controls().setTime(previewStart);
            } else if (songTime > fadeoutStart) {
                float songTimerToFadeoutStart = songTime - fadeoutStart;
                volumeMultiplier = (float) (1 - songTimerToFadeoutStart / PREVIEW_FADEOUT_LENGTH);
            }
        }

        mediaPlayer.audio().setVolume(Math.round(100 * volumeMultiplier * value()));
    }

    private void reset(boolean looped) {
        this.looped = looped;
        songTime = 0;
        volumeMultiplier = looped ? 0 : 1;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
    }

    @Override
    protected void onIntoActive() {
        super.onIntoActive();

        if (newSong != null) {
            song = newSong;

            mediaPlayer.media().prepare(song.getDefaultSoundFile().getAbsolutePath());
            mediaPlayer.audio().setVolume(0);
            mediaPlayer.controls().play();
            mediaPlayer.controls().setTime(song.getSongInfo().getPreviewStart());
        }

        reset(false);
    }

    @Override
    protected void onDisabled() {
        super.onDisabled();
        mediaPlayer.controls().stop();
    }
}
