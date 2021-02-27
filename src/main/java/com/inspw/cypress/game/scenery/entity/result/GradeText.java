package com.inspw.cypress.game.scenery.entity.result;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.rhythm.RhythmSession;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class GradeText extends EntryExitTransitionalEntity {
    private static final int FONT_SIZE = 150;
    private static final float SHIFT_X = -165;
    private static final float SHIFT_Y = -10;
    private static final float FADE_IN_DURATION = 0.5F;
    private static final float FADE_OUT_DURATION = 1F;

    public GradeText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
    }

    private String getGrade() {
        return RhythmSession.getGrade(
                Game.instance().rhythmSessionManager()
                        .getSession()
                        .getRoundedScore()
        );
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2 center = rs().getCenter();

        g.setColor(new Color(1, 1, 1, value()));
        g.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        FontMetrics metrics = g.getFontMetrics();
        String text = getGrade();
        int width = metrics.stringWidth(text);
        float x = (float) center.getX() - (float) width / 2 + SHIFT_X;
        float y = (float) center.getY() + SHIFT_Y;
        g.drawString(getGrade(), x, y);
    }
}
