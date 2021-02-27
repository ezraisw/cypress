package com.inspw.cypress.game.scenery.entity.result;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class SongInfoText extends EntryExitTransitionalEntity {
    private static final int TITLE_FONT_SIZE = 50;
    private static final int ARTIST_FONT_SIZE = 30;
    private static final float MARGIN_TOP = 60F;
    private static final float ARTIST_MARGIN_TOP = 10F;
    private static final float FADE_IN_DURATION = 0.5F;
    private static final float FADE_OUT_DURATION = 1F;

    public SongInfoText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
    }

    private String getTitle() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getSong()
                .getSongInfo()
                .getTitle();
    }

    private String getArtist() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getSong()
                .getSongInfo()
                .getArtist();
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2 center = rs().getCenter();
        g.setColor(new Color(1, 1, 1, value()));
        g.setFont(new Font("SansSerif", Font.PLAIN, TITLE_FONT_SIZE));
        FontMetrics titleMetrics = g.getFontMetrics();

        String titleText = getTitle();
        int titleWidth = titleMetrics.stringWidth(titleText);

        float titleX = center.getX() - (float) titleWidth / 2;
        float titleY = MARGIN_TOP;
        g.drawString(titleText, titleX, titleY);

        g.setFont(new Font("SansSerif", Font.PLAIN, ARTIST_FONT_SIZE));
        FontMetrics artistMetrics = g.getFontMetrics();

        String artistText = getArtist();
        int artistWidth = artistMetrics.stringWidth(artistText);

        float artistX = center.getX() - (float) artistWidth / 2;
        float artistY = titleY + (float) titleMetrics.getHeight() / 2 + ARTIST_MARGIN_TOP;
        g.drawString(artistText, artistX, artistY);
    }
}
