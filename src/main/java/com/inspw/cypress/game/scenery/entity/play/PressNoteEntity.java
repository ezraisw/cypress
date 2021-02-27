package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.serialized.PressNote;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.rhythm.ScanlinePage;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class PressNoteEntity extends RailedNoteEntity<PressNote> {
    private static final float DIAMETER = 100;
    private static final float INNER_DIAMETER = 90;
    private static final float OUTLINE_STROKE_SIZE = 1;
    private static final float OUTLINE_MARGIN = 3;
    private static final Color UP_COLOR = new Color(79, 169, 127);
    private static final Color DOWN_COLOR = new Color(73, 127, 174);

    private float rotation;

    public PressNoteEntity(PressNote note, RhythmTimer rhythmTimer, ScanlinePage currSp) {
        super(note, rhythmTimer, currSp);
    }

    public void down(HitGrade grade) {
        super.down(grade);
        setHit(true);
    }

    private void renderDashedOutline(Graphics2D g, float diameter, float angle) {
        Vector2 p = getWindowPosition();

        float radius = diameter / 2;

        Ellipse2D.Float ellipse = new Ellipse2D.Float(-radius, -radius, diameter, diameter);
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(
                OUTLINE_STROKE_SIZE,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0,
                new float[]{20},
                0
        ));

        AffineTransform at = new AffineTransform();

        float x = (float) p.getX();
        float y = (float) p.getY();
        g.translate(x, y);
        g.rotate(angle);
        g.draw(ellipse);

        g.setTransform(at);
        g.setStroke(oldStroke);
    }

    private void renderSpinningOutline(Graphics2D g, float baseDiameter, float progress) {
        float constX = progress - 1;
        float constY = MathHelper.clamp(-(constX * constX) + 1, 0, 1);

        renderDashedOutline(g, baseDiameter * constY, rotation);
    }

    private void renderOuterCircle(Graphics2D g, float progress) {
        renderOuterCircle(g, DIAMETER, progress);
        renderSpinningOutline(g, DIAMETER + OUTLINE_MARGIN, progress);
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

        renderInnerCircle(g, baseColor, INNER_DIAMETER, progress);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        rotation = (float) ((rotation - deltaTime) % Math.PI);
    }

    @Override
    public void render(Graphics2D g) {
        if (isHit() && hitValue() <= 0) {
            return;
        }

        float progress = animationProgress();
        if (progress > 0) {
            float scale = Math.min(progress * (1 - hitValue()), 1);
            if (!isHit()) {
                renderTimingCircle(g, DIAMETER, progress);
            }
            if (scale > 0) {
                renderOuterCircle(g, scale);
                renderInnerCircle(g, scale);
            }
        }

        super.render(g);
    }
}
