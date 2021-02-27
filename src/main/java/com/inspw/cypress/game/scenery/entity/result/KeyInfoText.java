package com.inspw.cypress.game.scenery.entity.result;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class KeyInfoText extends EntryExitTransitionalEntity {
    private static final String TEXT = "[R] Retry - [ENTER] Return to List";
    private static final int FONT_SIZE = 35;
    private static final float MARGIN_BOTTOM = 20F;
    private static final float FADE_IN_DURATION = 0.5F;
    private static final float FADE_OUT_DURATION = 1F;

    public KeyInfoText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2 center = rs().getCenter();

        g.setColor(new Color(1, 1, 1, value()));
        g.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE));
        FontMetrics metrics = g.getFontMetrics();
        String text = TEXT;
        int width = metrics.stringWidth(text);
        float x = (float) center.getX() - (float) width / 2;
        float y = rs().getHeight() - MARGIN_BOTTOM * fadeInValue();
        g.drawString(text, x, y);
    }
}
