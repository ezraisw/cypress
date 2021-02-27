package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.serialized.RailedNote;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.rhythm.ScanlinePage;

import java.awt.*;

public abstract class RailedNoteEntity<T extends RailedNote> extends NoteEntity<T> {
    private static final float TIMING_GUIDE_START = 0.6F;
    private static final float TIMING_CIRCLE_DIAMETER = 200;
    private static final float TIMING_CIRCLE_ALPHA = 0.25F;

    public RailedNoteEntity(T note, RhythmTimer rhythmTimer, ScanlinePage currSp) {
        super(note, rhythmTimer, currSp);
    }

    public void miss() {
        spawnEffect(getPosition(), HitGrade.MISS);
    }

    public void down(HitGrade grade) {
        spawnEffect(getPosition(), grade);
    }

    public void up(HitGrade grade) {
        float time = getRhythmTimer().getTime();
        float x = getPosition().getX();
        float y = getRhythmTimer().getPredictor().positionAt(time, getCurrSp());
        spawnEffect(new Vector2(x, y), grade);
    }

    protected void renderTimingCircle(Graphics2D g, float baseDiameter, float progress) {
        Vector2 p = getWindowPosition();

        float constX = ((progress - TIMING_GUIDE_START) / (1 - TIMING_GUIDE_START) * 2) - 1;

        if (constX >= -1 && constX <= 1) {
            float constY = MathHelper.clamp(
                    constX >= 1
                            ? -(constX * constX * constX * constX) + 1
                            : -(constX * constX) + 1,
                    0,
                    1
            );
            float diameter = (TIMING_CIRCLE_DIAMETER - baseDiameter) * constY + baseDiameter;
            g.setColor(new Color(1, 1, 1, TIMING_CIRCLE_ALPHA));
            renderOutlineCircle(g, p, diameter);
        }
    }
}
