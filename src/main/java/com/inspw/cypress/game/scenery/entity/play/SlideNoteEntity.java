package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Play;
import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.enumerate.SlideDirection;
import com.inspw.cypress.game.model.serialized.SlideNote;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.rhythm.ScanlinePage;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class SlideNoteEntity extends FreeNoteEntity<SlideNote> {
    private static final float GUIDE_DIAMETER = 90;
    private static final float GUIDE_INNER_DIAMETER = 70;
    private static final float DIAMETER = 40;
    private static final float INNER_DIAMETER = 25;
    private static final float TRIANGLE_DISTANCE_RADIUS = 50;
    private static final float TRIANGLE_WIDTH = 25;
    private static final float TRIANGLE_HEIGHT = 12;
    private static final float TRIANGLE_PROGRESS_STOP = 0.6F;
    private static final float TRIANGLE_START_ANGLE_OFFSET = (float) (Math.PI / 2);
    private static final float ARROW_WIDTH = 60;
    private static final float ARROW_HEIGHT = 50;

    private static final Color[] GROUP_COLORS = {
            new Color(110, 113, 255),
            new Color(80, 149, 86)
    };

    private SlideNoteEntity next;
    private boolean first;
    private SlideDirection slideDirection;

    public SlideNoteEntity(SlideNote note, RhythmTimer rhythmTimer, ScanlinePage currSp) {
        super(note, rhythmTimer, currSp);
        first = true;
    }

    private static float angle(Vector2 p1, Vector2 p2) {
        Vector2 v1 = new Vector2(p1.getX(), -p1.getY());
        Vector2 v2 = new Vector2(p2.getX(), -p2.getY());
        return Vector2.subtract(v2, v1).angle();
    }

    public SlideNoteEntity getNext() {
        return next;
    }

    public void setNext(SlideNoteEntity next) {
        this.next = next;
    }

    public boolean isLast() {
        return getNext() == null;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public SlideDirection getSlideDirection() {
        return slideDirection;
    }

    public void setSlideDirection(SlideDirection slideDirection) {
        this.slideDirection = slideDirection;
    }

    public void miss() {
        spawnEffect(getPosition(), HitGrade.MISS);
    }

    public void blow(HitGrade grade) {
        spawnEffect(getPosition(), grade);
    }

    public Vector2 getGuidePosition() {
        float time = getRhythmTimer().getTime();
        float noteTime = getNote().getTime();
        Vector2 pos = getPosition();

        if (time < noteTime) {
            return isFirst() ? pos : null;
        }

        if (isLast()) {
            return pos;
        }

        float nextNoteTime = getNext().getNote().getTime();
        if (time > nextNoteTime) {
            return null;
        }

        float deltaNoteTime = nextNoteTime - noteTime;
        float timeToNoteTime = time - noteTime;

        Vector2 nextPos = getNext().getPosition();
        Vector2 deltaPos = Vector2.subtract(nextPos, pos);

        float lerpVal = timeToNoteTime / deltaNoteTime;
        return Vector2.add(pos, deltaPos.multiply((float) lerpVal));
    }

    private void renderOuterCircle(Graphics2D g, float progress) {
        renderOuterCircle(g, DIAMETER, progress);
    }

    private void renderInnerCircle(Graphics2D g, float progress) {
        int slideGroup = getNote().getSlideGroup();
        Color color = slideGroup < 0 || slideGroup >= GROUP_COLORS.length
                ? Color.WHITE
                : GROUP_COLORS[slideGroup];

        renderInnerCircle(g, color, INNER_DIAMETER, progress);
    }

    private void renderDirectionArrow(Graphics2D g, Vector2 p, float width, float height, boolean reverse) {
        float ledgeHeight = width / 4;
        float halfWidth = height / 2;

        if (reverse) {
            halfWidth *= -1;
        }

        float x1 = p.getX() - halfWidth;
        float x2 = p.getX();
        float x3 = p.getX() + halfWidth;
        float y1 = p.getY() - ledgeHeight * 2;
        float y2 = p.getY() - ledgeHeight;
        float y3 = p.getY();
        float y4 = p.getY() + ledgeHeight;
        float y5 = p.getY() + ledgeHeight * 2;

        Path2D arrowPath = new Path2D.Float();
        arrowPath.moveTo(x2, y2);
        arrowPath.lineTo(x2, y1);
        arrowPath.lineTo(x1, y3);
        arrowPath.lineTo(x2, y5);
        arrowPath.lineTo(x2, y4);
        arrowPath.lineTo(x3, y4);
        arrowPath.lineTo(x3, y2);
        arrowPath.closePath();

        g.setColor(Color.WHITE);
        g.fill(arrowPath);
    }

    private void renderDirectionTriangle(
            Graphics2D g,
            Vector2 p,
            float width,
            float height,
            float distance,
            float angle
    ) {
        float halfWidth = width / 2;

        float x1 = -halfWidth;
        float x2 = 0;
        float x3 = halfWidth;
        float y1 = -distance;
        float y2 = -distance - height;

        Path2D triPath = new Path2D.Float();
        triPath.moveTo(x2, y2);
        triPath.lineTo(x1, y1);
        triPath.lineTo(x3, y1);
        triPath.closePath();

        AffineTransform at = g.getTransform();

        g.translate(p.getX(), p.getY());
        g.rotate(angle);

        g.setColor(Color.WHITE);
        g.fill(triPath);

        g.setTransform(at);
    }

    private void renderGuide(Graphics2D g, float progress) {
        Vector2 guidePos = getGuidePosition();
        if (guidePos == null) {
            return;
        }

        float constX;
        if (isFirst()) {
            constX = progress - 1;
        } else if (isLast()) {
            constX = (1 - hitValue()) - 1;
        } else {
            constX = 0;
        }
        float constY = MathHelper.clamp(-(constX * constX) + 1, 0, 1);

        if (constY <= 0) {
            return;
        }

        Vector2 p = rs().toWindowPos(guidePos, 0, Play.MARGIN_Y);
        g.setColor(Color.WHITE);
        renderCircle(g, p, GUIDE_DIAMETER * constY);

        int slideGroup = getNote().getSlideGroup();
        Color color = slideGroup < 0 || slideGroup >= GROUP_COLORS.length
                ? Color.WHITE
                : GROUP_COLORS[slideGroup];

        g.setColor(color);
        renderCircle(g, p, GUIDE_INNER_DIAMETER * constY);

        renderDirectionArrow(
                g,
                p,
                ARROW_WIDTH * constY,
                ARROW_HEIGHT * constY,
                getSlideDirection() == SlideDirection.RIGHT
        );

        if (!isLast()) {
            Vector2 p1 = getWindowPosition();
            Vector2 p2 = rs().toWindowPos(getNext().getPosition(), 0, Play.MARGIN_Y);

            float tConstX = MathHelper.clamp(progress / TRIANGLE_PROGRESS_STOP - 1, -1, 0);
            float tConstY = -(tConstX * tConstX) + 1;

            renderDirectionTriangle(
                    g,
                    p,
                    TRIANGLE_WIDTH * constY,
                    TRIANGLE_HEIGHT * constY,
                    TRIANGLE_DISTANCE_RADIUS * constY,
                    angle(p1, p2) + (TRIANGLE_START_ANGLE_OFFSET) * (1 - tConstY)
            );
        }
    }

    @Override
    public void render(Graphics2D g) {
        float progress = animationProgress();
        if (progress > 0) {
            float scale = Math.min(progress * (1 - hitValue()), 1);
            if (scale > 0) {
                renderOuterCircle(g, scale);
                renderInnerCircle(g, scale);
            }
        }

        renderGuide(g, progress);

        super.render(g);
    }
}
