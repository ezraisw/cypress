package com.inspw.cypress.game.scenery.entity.play.sub;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Play;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.util.Random;

public class BadNoteEffect extends NoteEffect {
    private static final float EFFECT_DURATION = 0.3F;
    private static final float RANDOM_SHIFT_DURATION = 0.02F;
    private static final float MAX_SHIFT = 3F;
    private static final int FONT_SIZE = 30;

    private final SimpleTimer randomShiftTimer;
    private final Random rng;
    private float shiftX;
    private float shiftY;

    public BadNoteEffect(Vector2 position, String text) {
        super(position, text, EFFECT_DURATION);
        randomShiftTimer = new SimpleTimer(RANDOM_SHIFT_DURATION, SimpleTimer.FinishType.REPEAT);
        randomShiftTimer.addFinishListener(this::randomizeShift);
        rng = new Random();
    }

    protected void finish() {
        super.finish();
        randomShiftTimer.stop();
    }

    private void randomizeShift() {
        shiftX = rng.nextFloat() * (MAX_SHIFT * 2) - MAX_SHIFT;
        shiftY = rng.nextFloat() * (MAX_SHIFT * 2) - MAX_SHIFT;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!randomShiftTimer.isStarted()) {
            randomShiftTimer.start();
        }
        randomShiftTimer.update(deltaTime);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2 p = rs().toWindowPos(getPosition(), 0, Play.MARGIN_Y);

        g.setFont(new Font("SansSerif", Font.ITALIC, FONT_SIZE));
        FontMetrics metrics = g.getFontMetrics();
        String text = getText().toUpperCase();
        float width = metrics.stringWidth(text);
        float x = (float) p.getX() - width / 2 + shiftX;
        float y = (float) p.getY() + (float) metrics.getHeight() / 3 + shiftY;
        float alpha = value() > 0.8 ? ((1 - value()) / 0.2F) : 1;
        g.setColor(new Color(1, 0, 0, alpha));
        g.drawString(text, x, y);
    }
}
