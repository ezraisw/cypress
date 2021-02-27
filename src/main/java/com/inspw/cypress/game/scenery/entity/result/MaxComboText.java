package com.inspw.cypress.game.scenery.entity.result;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class MaxComboText extends EntryExitTransitionalEntity {
    private static final String TITLE_TEXT = "Max Combo";
    private static final int TITLE_FONT_SIZE = 20;
    private static final int COMBO_FONT_SIZE = 50;
    private static final float SHIFT_X = 0;
    private static final float SHIFT_Y = -10;
    private static final float COMBO_MARGIN_LEFT = 20;
    private static final float FADE_IN_DURATION = 0.5F;
    private static final float FADE_OUT_DURATION = 1F;

    public MaxComboText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
    }

    private String getMaxComboText() {
        int combo = Math.round(
                Game.instance().rhythmSessionManager()
                        .getSession()
                        .getMaxCombo() * fadeInValue()
        );
        return Integer.toString(combo);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2 center = rs().getCenter();

        g.setColor(new Color(1, 1, 1, value()));
        g.setFont(new Font("SansSerif", Font.PLAIN, TITLE_FONT_SIZE));
        FontMetrics titleMetrics = g.getFontMetrics();
        String titleText = TITLE_TEXT.toUpperCase();
        int titleWidth = titleMetrics.stringWidth(titleText);

        float titleX = (float) center.getX() + SHIFT_X;
        float titleY = (float) center.getY() + SHIFT_Y;
        g.drawString(titleText, titleX, titleY);

        g.setFont(new Font("Consolas", Font.PLAIN, COMBO_FONT_SIZE));
        FontMetrics comboMetrics = g.getFontMetrics();
        String comboText = getMaxComboText();
        float comboX = titleX + titleWidth + COMBO_MARGIN_LEFT;
        float comboY = titleY + (float) comboMetrics.getHeight() / 3;
        g.drawString(comboText, comboX, comboY);
    }
}
