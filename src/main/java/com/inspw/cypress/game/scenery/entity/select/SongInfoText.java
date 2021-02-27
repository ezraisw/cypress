package com.inspw.cypress.game.scenery.entity.select;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.model.Song;
import com.inspw.cypress.game.scenery.entity.general.BinaryTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class SongInfoText extends BinaryTransitionalEntity {
    private static final int TITLE_FONT_SIZE = 40;
    private static final int ARTIST_FONT_SIZE = 25;
    private static final int MARGIN = 50;
    private static final float DISPLAYING_DURATION = 0.2F;
    private static final float HIDING_DURATION = 0.1F;
    private static final float HIDDEN_DURATION = 1.0F;

    private Song newSong;
    private Song song;

    public SongInfoText() {
        super(DISPLAYING_DURATION, HIDING_DURATION, -1, HIDDEN_DURATION);
    }

    public Song getSong() {
        return song;
    }

    public void newSong(Song song) {
        this.newSong = song;
        switchToDisabled();
    }

    @Override
    protected void onIntoActive() {
        super.onIntoActive();
        song = newSong;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (getSong() == null) {
            return;
        }

        g.setColor(new Color(1, 1, 1, value()));

        g.setFont(new Font("SansSerif", Font.PLAIN, TITLE_FONT_SIZE));
        FontMetrics titleMetrics = g.getFontMetrics();

        g.drawString(getSong().getSongInfo().getTitle(), MARGIN, rs().getHeight() - MARGIN);

        g.setFont(new Font("SansSerif", Font.PLAIN, ARTIST_FONT_SIZE));
        g.drawString(getSong().getSongInfo().getArtist(), MARGIN, rs().getHeight() - MARGIN - titleMetrics.getHeight());
    }
}
