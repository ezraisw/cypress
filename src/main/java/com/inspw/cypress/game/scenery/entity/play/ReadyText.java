package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.scenery.Entity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class ReadyText extends Entity {
    private static final String TEXT = "READY";
    private static final int FONT_SIZE = 60;
    private SimpleTimer timer;

    public SimpleTimer getTimer() {
        return timer;
    }

    public void setTimer(SimpleTimer timer) {
        this.timer = timer;
    }

    private String displayedText() {
        float progress = getTimer() == null ? 0 : getTimer().getProgress();
        float constX = (progress * 2) - 1;
        float constY = -(constX * constX) + 1;
        int length = Math.round(MathHelper.clamp(constY, 0, 1) * TEXT.length());

        return constX > 0
                ? TEXT.substring(TEXT.length() - length)
                : TEXT.substring(0, length);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (getTimer().isDone()) {
            return;
        }

        Vector2 center = rs().getCenter();
        g.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE));
        FontMetrics metrics = g.getFontMetrics();
        String text = displayedText();
        float width = metrics.stringWidth(text);
        g.drawString(text, (float) (center.getX() - width / 2), (float) center.getY());
    }
}
