package com.inspw.cypress.game.model.logic.note;

import com.inspw.cypress.game.constant.Rhythm;
import com.inspw.cypress.game.model.serialized.Note;

public abstract class NoteHitRegistration<T extends Note> {
    private final T note;
    private boolean hit;

    public NoteHitRegistration(T note) {
        this.note = note;
    }

    public T getNote() {
        return note;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    protected float diffTime(float time) {
        return time - note.getTime();
    }

    public void reset() {
        setHit(false);
    }

    public boolean shouldMiss(float time) {
        return diffTime(time) > Rhythm.MISS_DIFF_TIME;
    }
}
