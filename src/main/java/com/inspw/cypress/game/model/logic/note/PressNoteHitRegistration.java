package com.inspw.cypress.game.model.logic.note;

import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.serialized.PressNote;

public class PressNoteHitRegistration extends RailedNoteHitRegistration<PressNote> {
    private static final float PERFECT_MIN_DIFF_TIME = 0.05F;
    private static final float GREAT_MIN_DIFF_TIME = 0.125F;
    private static final float GOOD_MIN_DIFF_TIME = 0.3F;

    public PressNoteHitRegistration(PressNote note) {
        super(note);
    }

    @Override
    public HitGrade down(float time) {
        float diffTime = Math.abs(diffTime(time));
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
}
