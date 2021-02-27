package com.inspw.cypress.game.scenery.entity.play.sub;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Play;
import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class GoodNoteEffect extends NoteEffect {
    private static final float SPLASH_DIAMETER = 180;
    private static final int SPLASH_STROKE_SIZE = 3;
    private static final int SPLASH_STROKE_MAX_SIZE = 15;
    private static final int LINE_SPLASH_STROKE_SIZE = 2;
    private static final float LINE_SPLASH_LENGTH = 80;
    private static final float LINE_SPLASH_SHIFT_X = 80;
    private static final float EFFECT_DURATION = 0.3F;
    private static final int FONT_SIZE = 30;

    private final Color color;

    public GoodNoteEffect(Vector2 position, String text, Color color) {
        super(position, text, EFFECT_DURATION);
        this.color = color;
    }

    private void renderLineSplash(Graphics2D g, Vector2 center) {
        float val = value();
        float constX = val * 2 - 1;
        float constY = MathHelper.clamp(-(constX * constX) + 1, 0, 1);

        g.setColor(new Color(1, 1, 1, constY));
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(LINE_SPLASH_STROKE_SIZE));

        float length = constY * LINE_SPLASH_LENGTH;

        float rightX = center.getX() + val * LINE_SPLASH_SHIFT_X;
        Path2D linePathRight = new Path2D.Float();
        linePathRight.moveTo(rightX, center.getY());
        linePathRight.lineTo(rightX + length, center.getY());
        g.draw(linePathRight);

        float leftX = center.getX() - val * LINE_SPLASH_SHIFT_X;
        Path2D leftLinePath = new Path2D.Float();
        leftLinePath.moveTo(leftX, center.getY());
        leftLinePath.lineTo(leftX - length, center.getY());
        g.draw(leftLinePath);

        g.setStroke(oldStroke);
    }

    private void renderSplash(Graphics2D g, Vector2 center) {
        float constX = 1 - value();
        float constY = MathHelper.clamp(-(constX * constX) + 1, 0, 1);

        float diameter = SPLASH_DIAMETER * constY;
        float radius = diameter / 2;
        float x = center.getX() - radius;
        float y = center.getY() - radius;

        float strokeSize = (SPLASH_STROKE_MAX_SIZE - SPLASH_STROKE_SIZE) * constX + SPLASH_STROKE_SIZE;

        Ellipse2D.Float ellipse = new Ellipse2D.Float(x, y, diameter, diameter);
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(strokeSize));
        g.setColor(new Color(1, 1, 1, constX));
        g.draw(ellipse);
        g.setStroke(oldStroke);
    }

    private void renderText(Graphics2D g, Vector2 center) {
        float val = value();
        float constX = value() - 1;
        float constY = MathHelper.clamp(-(constX * constX * constX * constX) + 1, 0, 1);

        g.setFont(new Font("SansSerif", Font.ITALIC, FONT_SIZE));
        FontMetrics metrics = g.getFontMetrics();
        String text = getText().toUpperCase();
        float width = metrics.stringWidth(text);

        AffineTransform at = g.getTransform();

        g.translate(center.getX(), center.getY());
        g.scale(constY, constY);

        g.setColor(new Color(
                (float) color.getRed() / 255,
                (float) color.getGreen() / 255,
                (float) color.getBlue() / 255,
                val
        ));
        g.drawString(text, -width / 2, (float) metrics.getHeight() / 3);

        g.setTransform(at);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2 center = rs().toWindowPos(getPosition(), 0, Play.MARGIN_Y);

        renderSplash(g, center);
        renderLineSplash(g, center);
        renderText(g, center);
    }
}
