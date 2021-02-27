package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.model.Song;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class SongTitleText extends EntryExitTransitionalEntity {
    private static final int MARGIN = 35;
    private static final int FONT_SIZE = 30;
    private static final float FADE_IN_DURATION = 1.5F;
    private static final float FADE_OUT_DURATION = 1F;

    public SongTitleText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
    }

    private String displayedText() {
        Song song = Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getSong();
        String songTitle = song.getSongInfo().getTitle();
        int length = Math.round(fadeInValue() * songTitle.length());
        return songTitle.substring(0, length);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        g.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE));
        g.setColor(new Color(1, 1, 1, 1 - fadeOutValue()));
        g.drawString(displayedText(), MARGIN, rs().getHeight() - MARGIN);
    }
}
