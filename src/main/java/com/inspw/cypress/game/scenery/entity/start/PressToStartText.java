package com.inspw.cypress.game.scenery.entity.start;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.scenery.Entity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class PressToStartText extends Entity {
    private final float fontSizeRatio;
    private final float shiftY;

    public PressToStartText(float fontSizeRatio, float shiftY) {
        this.fontSizeRatio = fontSizeRatio;
        this.shiftY = shiftY;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        g.setColor(Color.WHITE);
        int fontSize = Math.round(rs().getWidth() * fontSizeRatio);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth("Press [ENTER] to start");
        Vector2 center = rs().getCenter();
        g.drawString("Press [ENTER] to start", center.getX() - width / 2F, center.getY() + shiftY);
    }
}
