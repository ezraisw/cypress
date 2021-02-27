package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Play;
import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.scenery.Entity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.awt.geom.Path2D;

public class SlideNoteGuide extends Entity {
    private static final float LINE_WIDTH = 6;
    private static final float ALPHA = 0.5F;
    private static final int Z_DIFF = -100;

    private final SlideNoteEntity noteEntity;

    public SlideNoteGuide(SlideNoteEntity noteEntity) {
        validateNoteEntity(noteEntity);
        this.noteEntity = noteEntity;
        setZ(noteEntity.getZ() + Z_DIFF);
    }

    private void validateNoteEntity(SlideNoteEntity noteEntity) {
        if (noteEntity == null) {
            throw new NullPointerException();
        }
        if (noteEntity.isLast()) {
            throw new IllegalArgumentException("Cannot assign guide to last slide note entity");
        }
    }

    public SlideNoteEntity getNoteEntity() {
        return noteEntity;
    }

    public Vector2 getEndPosition() {
        float time = getNoteEntity().getRhythmTimer().getTime();

        Vector2 pos = getNoteEntity().getPosition();
        Vector2 nextPos = getNoteEntity().getNext().getPosition();
        Vector2 deltaPos = Vector2.subtract(nextPos, pos);

        float nextNoteTime = getNoteEntity().getNext().getNote().getTime();
        float nextNoteDisplayDuration = getNoteEntity().getNext().displayDuration();
        float nextNoteStartTime = nextNoteTime - nextNoteDisplayDuration;

        float noteTime = getNoteEntity().getNote().getTime();
        float noteDisplayDuration = getNoteEntity().displayDuration();
        float noteStartTime = noteTime - noteDisplayDuration;

        float progress = MathHelper.clamp(
                (time - noteStartTime) / (nextNoteStartTime - noteStartTime),
                0,
                1
        );

        return Vector2.add(pos, deltaPos.multiply(progress));
    }

    public Vector2 getStartPosition() {
        Vector2 gp = getNoteEntity().getGuidePosition();
        return gp == null ? getNoteEntity().getPosition() : gp;
    }

    private boolean shouldHide() {
        return getNoteEntity().animationProgress() <= 0 ||
                getNoteEntity().getRhythmTimer().getTime() >= getNoteEntity().getNext().getNote().getTime();
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2 p1 = rs().toWindowPos(getStartPosition(), 0, Play.MARGIN_Y);
        Vector2 p2 = rs().toWindowPos(getEndPosition(), 0, Play.MARGIN_Y);

        if (shouldHide()) {
            return;
        }

        Path2D linePath = new Path2D.Float();
        linePath.moveTo(p1.getX(), p1.getY());
        linePath.lineTo(p2.getX(), p2.getY());

        Stroke oldStroke = g.getStroke();

        g.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0));
        g.setColor(new Color(1, 1, 1, ALPHA));
        g.draw(linePath);

        g.setStroke(oldStroke);
    }
}
