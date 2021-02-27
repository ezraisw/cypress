package com.inspw.cypress.game.scenery.entity.start;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.scenery.Entity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class TitleText extends Entity {
    private final Color[] colorCycles;
    private final float colorCyclePeriod;
    private Color color;
    private float periodTimer = 0;
    private int oldColorIndex = 0;
    private int targetColorIndex = 1;

    public TitleText(Color[] colorCycles, float colorCyclePeriod) {
        this.colorCycles = colorCycles;
        this.colorCyclePeriod = colorCyclePeriod;
        this.color = colorCycles[0];
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float[] oldRGBComps = colorCycles[oldColorIndex].getRGBComponents(null);
        float[] targetRGBComps = colorCycles[targetColorIndex].getRGBComponents(null);

        float deltaR = targetRGBComps[0] - oldRGBComps[0];
        float deltaG = targetRGBComps[1] - oldRGBComps[1];
        float deltaB = targetRGBComps[2] - oldRGBComps[2];

        float rateR = deltaR * deltaTime / colorCyclePeriod;
        float rateG = deltaG * deltaTime / colorCyclePeriod;
        float rateB = deltaB * deltaTime / colorCyclePeriod;

        periodTimer += deltaTime;

        if (periodTimer >= colorCyclePeriod) {
            color = colorCycles[targetColorIndex];
            oldColorIndex = targetColorIndex;
            targetColorIndex++;

            if (targetColorIndex >= colorCycles.length) {
                targetColorIndex = 0;
            }

            periodTimer = 0;
        } else {
            float[] rgbComps = color.getRGBComponents(null);
            color = new Color(
                    Math.min(Math.max(rgbComps[0] + rateR, 0), 1),
                    Math.min(Math.max(rgbComps[1] + rateG, 0), 1),
                    Math.min(Math.max(rgbComps[2] + rateB, 0), 1)
            );
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        g.setColor(color);

        float ratio = 1F / 10;
        int fontSize = Math.round((float) rs().getWidth() * ratio);

        Font font = new Font("Arial", Font.PLAIN, fontSize);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);

        float width = metrics.stringWidth("Cypress");

        Vector2 center = rs().getCenter();
        g.drawString("Cypress", (float) center.getX() - width / 2F, (float) center.getY());
    }
}
