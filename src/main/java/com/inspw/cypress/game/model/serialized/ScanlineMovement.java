package com.inspw.cypress.game.model.serialized;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScanlineMovement {
    private float duration;
    private float position;
    private int repeat;

    @JsonCreator
    public ScanlineMovement(
            @JsonProperty("duration") float duration,
            @JsonProperty("position") float position,
            @JsonProperty("repeat") int repeat
    ) {
        validatePosition(position);

        this.duration = duration;
        this.position = position;
        this.repeat = repeat;
    }

    private void validatePosition(float position) {
        if (position > 100 || position < -100) {
            throw new IllegalArgumentException("Invalid position");
        }
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        validatePosition(position);
        this.position = position;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
