package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;

import java.awt.*;

public class ComboText extends EntryExitTransitionalEntity {
    private static final int COMBO_FONT_SIZE = 45;
    private static final int INFO_FONT_SIZE = 12;
    private static final int MARGIN_TOP = 55;
    private static final int MARGIN_LEFT = 35;
    private static final int COMBO_MARGIN_LEFT = 10;
    private static final int COMBO_THRESHOLD = 5;
    private static final float FADE_IN_DURATION = 0.5F;
    private static final float FADE_OUT_DURATION = 1F;

    public ComboText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
    }

    private int getCombo() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getCombo();
    }

    private boolean isAllPerfectOrGreat() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .isAllPerfectOrGreat();
    }

    private boolean isAllPerfectOrGreatOrGood() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .isAllPerfectOrGreatOrGood();
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        g.setColor(new Color(1, 1, 1, value()));

        Font comboFont = new Font("Consolas", Font.PLAIN, COMBO_FONT_SIZE);
        FontMetrics comboMetrics = g.getFontMetrics(comboFont);
        float comboHeight = comboMetrics.getHeight();

        g.setFont(new Font("SansSerif", Font.PLAIN, INFO_FONT_SIZE));
        FontMetrics infoMetrics = g.getFontMetrics();
        float infoX = MARGIN_LEFT;
        float infoY = MARGIN_TOP - comboHeight / 3;
        String infoText = "COMBO";
        float infoWidth = infoMetrics.stringWidth(infoText);
        g.drawString(infoText, infoX, infoY);

        if (getCombo() > COMBO_THRESHOLD) {
            g.setFont(comboFont);
            String comboText = Integer.toString(getCombo());
            float comboX = infoX + infoWidth + COMBO_MARGIN_LEFT;
            float comboY = MARGIN_TOP;
            g.drawString(comboText, comboX, comboY);
        }
    }
}
