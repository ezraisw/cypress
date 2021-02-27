package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.awt.geom.Path2D;

public class ScanlineBorder extends EntryExitTransitionalEntity {
    private static final float FADE_IN_DURATION = 0.5F;
    private static final float FADE_OUT_DURATION = 1F;
    private static final float ALPHA = 0.3F;
    private static final float MARGIN_TOP = 90;
    private static final float MARGIN_BOTTOM = 90;
    private static final float STROKE_SIZE = 1F;

    public ScanlineBorder() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
    }

    private void renderBorder(Graphics2D g, float posY) {
        Path2D.Float path = new Path2D.Float();
        path.moveTo(0, posY);
        path.lineTo(rs().getWidth(), posY);

        g.setStroke(new BasicStroke(STROKE_SIZE));
        g.setColor(new Color(1, 1, 1, ALPHA * value()));
        g.draw(path);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        float top = MARGIN_TOP;
        float bottom = rs().getHeight() - MARGIN_BOTTOM;

        renderBorder(g, top);
        renderBorder(g, bottom);
    }
}
