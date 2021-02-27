package com.inspw.cypress.game.model.logic.note;

import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.enumerate.SlideDirection;
import com.inspw.cypress.game.model.serialized.SlideNote;

public class SlideNoteHitRegistration extends NoteHitRegistration<SlideNote> {
    private static final float PERFECT_MIN_DIFF_TIME = 0.05F;
    private static final float SLIDE_MISS_MAX_DIFF_TIME = 0.2F;

    public SlideNoteHitRegistration(SlideNote note) {
        super(note);
    }

    public static SlideDirection slideDirection(SlideNote n1, SlideNote n2) {
        if (n1 == null || n2 == null) {
            return null;
        }

        float x = n1.getX();
        float nextX = n2.getX();

        if (MathHelper.floatEquals(x, nextX)) {
            return null;
        }

        return x < nextX ? SlideDirection.RIGHT : SlideDirection.LEFT;
    }

    public HitGrade pass(float time) {
        float diffTime = Math.abs(diffTime(time));
        if (diffTime <= PERFECT_MIN_DIFF_TIME) {
            return HitGrade.PERFECT;
        } else {
            return HitGrade.GREAT;
        }
    }

    public boolean shouldBeHit(float time) {
        return time >= getNote().getTime();
    }

    @Override
    public boolean shouldMiss(float time) {
        return diffTime(time) > SLIDE_MISS_MAX_DIFF_TIME;
    }
}
