package com.inspw.cypress.game.model.logic.note;

import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.serialized.HoldNote;

public class HoldNoteHitRegistration extends RailedNoteHitRegistration<HoldNote> {
    private static final float PERFECT_MIN_DIFF_TIME = 0.05F;
    private static final float GREAT_MIN_DIFF_TIME = 0.125F;
    private static final float GOOD_MIN_DIFF_TIME = 0.3F;
    private static final float HOLD_DIFF_TIME_TOLERANCE = GOOD_MIN_DIFF_TIME;
    private static final float PERFECT_MIN_PROGRESS = 0.99F;
    private static final float GREAT_MIN_PROGRESS = 0.95F;
    private static final float GOOD_MIN_PROGRESS = 0.50F;
    private static final float BAD_MIN_PROGRESS = 0.1F;

    private float downTime = Float.NaN;

    public HoldNoteHitRegistration(HoldNote note) {
        super(note);
    }

    @Override
    public void reset() {
        super.reset();
        downTime = Float.NaN;
    }

    @Override
    public boolean shouldRelease(float time) {
        return time >= getNote().getEndTime();
    }

    @Override
    public boolean shouldCheckForUp(float time) {
        return !Float.isNaN(downTime);
    }

    @Override
    public HitGrade down(float time) {
        float diffTime = Math.abs(diffTime(time));
        downTime = time;
        if (diffTime <= PERFECT_MIN_DIFF_TIME) {
            return HitGrade.PERFECT;
        } else if (diffTime <= GREAT_MIN_DIFF_TIME) {
            return HitGrade.GREAT;
        } else if (diffTime <= GOOD_MIN_DIFF_TIME) {
            return HitGrade.GOOD;
        } else {
            return HitGrade.BAD;
        }
    }

    @Override
    public HitGrade up(float time) {
        float downTimeBounded = Math.max(downTime - HOLD_DIFF_TIME_TOLERANCE, getNote().getTime());
        float progress = (time - downTimeBounded) / (getNote().getEndTime() - getNote().getTime());
        if (progress >= PERFECT_MIN_PROGRESS) {
            return HitGrade.PERFECT;
        } else if (progress >= GREAT_MIN_PROGRESS) {
            return HitGrade.GREAT;
        } else if (progress >= GOOD_MIN_PROGRESS) {
            return HitGrade.GOOD;
        } else if (progress >= BAD_MIN_PROGRESS) {
            return HitGrade.BAD;
        } else {
            return HitGrade.MISS;
        }
    }
}
