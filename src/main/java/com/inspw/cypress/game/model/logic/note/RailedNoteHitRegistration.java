package com.inspw.cypress.game.model.logic.note;

import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.serialized.RailedNote;

public abstract class RailedNoteHitRegistration<T extends RailedNote> extends NoteHitRegistration<T> {
    public RailedNoteHitRegistration(T note) {
        super(note);
    }

    public void reset() {
        setHit(false);
    }

    public boolean shouldRelease(float time) {
        return false;
    }

    public boolean shouldCheckForUp(float time) {
        return false;
    }

    public HitGrade down(float time) {
        return null;
    }

    public HitGrade up(float time) {
        return null;
    }
}
