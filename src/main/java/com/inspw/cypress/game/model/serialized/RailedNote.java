package com.inspw.cypress.game.model.serialized;

import com.inspw.cypress.game.model.logic.note.RailedNoteHitRegistration;

public abstract class RailedNote extends Note {
    private int rail;
    private float shiftX;

    public RailedNote(String id, float time, int rail, float shiftX) {
        super(id, time);
        validateShiftX(shiftX);
        this.rail = rail;
        this.shiftX = shiftX;
    }

    private void validateShiftX(float shiftX) {
        if (shiftX < -100 || shiftX > 100) {
            throw new IllegalArgumentException("shiftX must be between -100 and 100");
        }
    }

    public int getRail() {
        return rail;
    }

    public void setRail(int rail) {
        this.rail = rail;
    }

    public float getShiftX() {
        return shiftX;
    }

    public void setShiftX(float shiftX) {
        validateShiftX(shiftX);
        this.shiftX = shiftX;
    }

    @Override
    public abstract RailedNoteHitRegistration<?> createHitRegistration();
}
