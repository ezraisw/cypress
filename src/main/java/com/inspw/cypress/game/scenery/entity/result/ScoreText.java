package com.inspw.cypress.game.scenery.entity.result;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class ScoreText extends EntryExitTransitionalEntity {
    private static final int FONT_SIZE = 80;
    private static final float SHIFT_X = 0;
    private static final float SHIFT_Y = -65;
    private static final float FADE_IN_DURATION = 0.5F;
    private static final float FADE_OUT_DURATION = 1F;

    public ScoreText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
    }

    private String getScoreText() {
        int score = Math.round(
                Game.instance().rhythmSessionManager()
                        .getSession()
                        .getRoundedScore() * fadeInValue()
        );
        return String.format("%06d", score);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2 center = rs().getCenter();

        g.setColor(new Color(1, 1, 1, value()));
        g.setFont(new Font("Consolas", Font.PLAIN, FONT_SIZE));

        String scoreText = getScoreText();
        float x = (float) center.getX() + SHIFT_X;
        float y = (float) center.getY() + SHIFT_Y;
        g.drawString(scoreText, x, y);
    }
}
