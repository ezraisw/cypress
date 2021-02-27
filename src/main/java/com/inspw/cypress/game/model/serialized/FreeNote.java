package com.inspw.cypress.game.model.serialized;

public abstract class FreeNote extends Note {
    private float x;

    public FreeNote(String id, float time, int x) {
        super(id, time);
        validateX(x);
        this.x = x;
    }

    private void validateX(float x) {
        if (x < -100 || x > 100) {
            throw new IllegalArgumentException("x must be between -100 and 100");
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        validateX(x);
        this.x = x;
    }
}
