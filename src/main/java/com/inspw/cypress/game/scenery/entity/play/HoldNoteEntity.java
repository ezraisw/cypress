package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Play;
import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.serialized.HoldNote;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.rhythm.ScanlinePage;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class HoldNoteEntity extends RailedNoteEntity<HoldNote> {
    private static final float LINE_WIDTH = 2;
    private static final float LINE_LENGTH = 103;
    private static final float LINE_START_ANGLE_OFFSET = (float) (Math.PI / 2);
    private static final Color LINE_COLOR = new Color(100, 181, 246);
    private static final float HOLDING_DIAMETER_ADDITION = 40;
    private static final float HOLDING_INNER_DIAMETER_ADDITION = 20;
    private static final float HOLDING_ALPHA = 0.3F;
    private static final float DIAMETER = 100;
    private static final float INNER_DIAMETER = 80;
    private static final float HOLD_PROGRESS_WIDTH = 65;
    private static final float HOLD_PROGRESS_ALPHA = 0.75F;
    private static final float PROGRESS_BAR_PROGRESS_STOP = 0.6F;
    private static final Color EFFECT_COLOR = new Color(255, 238, 88);
    private static final float EFFECT_WIDTH = 130F;
    private static final float EFFECT_HEIGHT = 80F;
    private static final float EFFECT_ALPHA = 0.7F;
    private static final float EFFECT_GRADIENT_RADIUS = 100F;
    private static final float OUTLINE_DIAMETER_OFFSET = LINE_LENGTH - DIAMETER;
    private static final Color UP_COLOR = new Color(175, 99, 149);
    private static final Color DOWN_COLOR = new Color(176, 103, 87);

    private boolean held;
    private Vector2 endPosition;

    public HoldNoteEntity(HoldNote note, RhythmTimer rhythmTimer, ScanlinePage currSp) {
        super(note, rhythmTimer, currSp);
    }

    public Vector2 getEndPosition() {
        return endPosition;
    }

    private Vector2 scanlinePosition() {
        float time = getRhythmTimer().getTime();
        if (time >= getNote().getTime()) {
            return new Vector2(getPosition().getX(), getRhythmTimer().currentPosition());
        }
        return null;
    }

    @Override
    public void down(HitGrade grade) {
        super.down(grade);
        held = true;
    }

    @Override
    public void up(HitGrade grade) {
        super.up(grade);
        held = false;
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        endPosition = new Vector2(
                getPosition().getX(),
                getRhythmTimer().getPredictor().positionAt(getNote().getEndTime(), getCurrSp())
        );
    }

    @Override
    protected float baseAlpha() {
        return held ? HOLDING_ALPHA : 1;
    }

    private void renderHoldProgress(Graphics2D g, float animProgress, float fadeProgress) {
        Vector2 sp = getWindowPosition();
        Vector2 ep = rs().toWindowPos(getEndPosition(), 0, Play.MARGIN_Y);

        float height = Math.abs(ep.getY() - sp.getY());

        float width = HOLD_PROGRESS_WIDTH * animProgress;
        float halfWidth = width / 2;
        float x = sp.getX() - halfWidth;
        float y = getCurrSp().getDirection() == ScanlinePage.Direction.DOWN
                ? sp.getY()
                : sp.getY() - height;

        Rectangle2D rect = new Rectangle2D.Float(x, y, width, height);

        g.setColor(new Color(1, 1, 1, HOLD_PROGRESS_ALPHA * animProgress * (1 - fadeProgress)));
        g.fill(rect);
    }

    private void renderLine(Graphics2D g, float progress) {
        Vector2 p = getWindowPosition();

        float length = LINE_LENGTH * progress;
        float halfLength = length / 2;

        Path2D linePath = new Path2D.Float();
        linePath.moveTo(-halfLength, 0);
        linePath.lineTo(halfLength, 0);

        AffineTransform at = g.getTransform();

        g.translate(p.getX(), p.getY());
        g.rotate(LINE_START_ANGLE_OFFSET * (1 - progress));

        Stroke oldStroke = g.getStroke();

        g.setStroke(new BasicStroke(LINE_WIDTH));
        g.setColor(LINE_COLOR);
        g.draw(linePath);

        g.setStroke(oldStroke);
        g.setTransform(at);
    }

    private void renderOuterOutlineCircle(Graphics2D g, float diameter) {
        Vector2 p = getWindowPosition();
        renderOutlineCircle(g, p, diameter);
    }

    private void renderOuterCircle(Graphics2D g, float progress) {
        float diameter = DIAMETER;
        if (held) {
            diameter += HOLDING_DIAMETER_ADDITION;
        }
        renderOuterCircle(g, diameter, progress);
        renderOuterOutlineCircle(g, (diameter + OUTLINE_DIAMETER_OFFSET) * progress);
    }

    private void renderInnerCircle(Graphics2D g, float progress) {
        Color baseColor = Color.WHITE;
        switch (getCurrSp().getDirection()) {
            case UP:
                baseColor = UP_COLOR;
                break;
            case DOWN:
                baseColor = DOWN_COLOR;
                break;
        }

        float diameter = INNER_DIAMETER;
        if (held) {
            diameter += HOLDING_INNER_DIAMETER_ADDITION;
        }
        renderInnerCircle(g, baseColor, diameter, progress);
    }

    private RadialGradientPaint fadeOutGrad(float alpha, Point2D center, float radius) {
        Color baseColor = EFFECT_COLOR;
        return new RadialGradientPaint(
                center,
                radius,
                new float[]{0F, 1F},
                new Color[]{
                        new Color(
                                (float) baseColor.getRed() / 255,
                                (float) baseColor.getGreen() / 255,
                                (float) baseColor.getBlue() / 255,
                                alpha
                        ),
                        new Color(
                                (float) baseColor.getRed() / 255,
                                (float) baseColor.getGreen() / 255,
                                (float) baseColor.getBlue() / 255,
                                0
                        )
                }
        );
    }

    private void renderHoldEffect(Graphics2D g) {
        Vector2 scp = scanlinePosition();

        if (scp == null) {
            return;
        }

        Vector2 p = rs().toWindowPos(scp, 0, Play.MARGIN_Y);

        float halfWidth = EFFECT_WIDTH / 2;
        float halfProgressWidth = HOLD_PROGRESS_WIDTH / 2;
        float height = EFFECT_HEIGHT;

        float x1 = p.getX() - halfWidth;
        float x2 = p.getX() - halfProgressWidth;
        float x3 = p.getX() + halfProgressWidth;
        float x4 = p.getX() + halfWidth;

        float y1;
        float y2;

        if (getCurrSp().getDirection() == ScanlinePage.Direction.UP) {
            y1 = p.getY() + height;
        } else {
            y1 = p.getY() - height;
        }
        y2 = p.getY();

        Path2D effectPath = new Path2D.Float();
        effectPath.moveTo(x1, y1);
        effectPath.lineTo(x2, y2);
        effectPath.lineTo(x3, y2);
        effectPath.lineTo(x4, y1);
        effectPath.closePath();

        Paint oldP = g.getPaint();

        g.setPaint(fadeOutGrad(EFFECT_ALPHA, new Point2D.Float(p.getX(), p.getY()), EFFECT_GRADIENT_RADIUS));
        g.fill(effectPath);

        g.setPaint(oldP);
    }

    @Override
    public void render(Graphics2D g) {
        float progress = animationProgress();
        if (progress > 0) {
            renderHoldProgress(
                    g,
                    MathHelper.clamp(progress / PROGRESS_BAR_PROGRESS_STOP, 0, 1),
                    hitValue()
            );

            if (!isHit()) {
                renderTimingCircle(g, DIAMETER, progress);
            }

            float scale = Math.min(progress * (1 - hitValue()), 1);
            if (scale > 0) {
                renderOuterCircle(g, scale);
                renderLine(g, scale);
                renderInnerCircle(g, scale);
            }

            if (held) {
                renderHoldEffect(g);
            }
        }

        super.render(g);
    }
}
