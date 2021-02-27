package com.inspw.cypress.game.scenery.entity.general;

import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.scenery.Entity;

public class BinaryTransitionalEntity extends Entity {
    private State state;
    private float intoActiveDuration, intoDisabledDuration, activeDuration, disabledDuration;
    private float minValue, maxValue;
    private float normalizedTransitionTimer, timer;
    public BinaryTransitionalEntity(float transitionDuration) {
        this(transitionDuration, transitionDuration);
    }

    public BinaryTransitionalEntity(float intoActiveDuration, float intoDisabledDuration) {
        this(intoActiveDuration, intoDisabledDuration, -1, -1);
    }

    public BinaryTransitionalEntity(float intoActiveDuration, float intoDisabledDuration, float activeDuration, float disabledDuration) {
        this.state = State.DISABLED;
        this.intoActiveDuration = intoActiveDuration;
        this.intoDisabledDuration = intoDisabledDuration;
        this.activeDuration = activeDuration;
        this.disabledDuration = disabledDuration;
        this.minValue = 0F;
        this.maxValue = 1F;
    }

    public State getState() {
        return state;
    }

    public float value() {
        float value = normalizedTransitionTimer * (maxValue - minValue) + minValue;
        return MathHelper.clamp(value, minValue, maxValue);
    }

    public float normalizedValue() {
        return normalizedTransitionTimer;
    }

    public float getIntoActiveDuration() {
        return intoActiveDuration;
    }

    public void setIntoActiveDuration(float intoActiveDuration) {
        this.intoActiveDuration = intoActiveDuration;
    }

    public float getIntoDisabledDuration() {
        return intoDisabledDuration;
    }

    public void setIntoDisabledDuration(float intoDisabledDuration) {
        this.intoDisabledDuration = intoDisabledDuration;
    }

    public float getActiveDuration() {
        return activeDuration;
    }

    public void setActiveDuration(float activeDuration) {
        this.activeDuration = activeDuration;
    }

    public float getDisabledDuration() {
        return disabledDuration;
    }

    public void setDisabledDuration(float disabledDuration) {
        this.disabledDuration = disabledDuration;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        if (minValue > getMaxValue()) {
            throw new IllegalArgumentException("Minimum value greater than maximum");
        }

        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        if (maxValue < getMinValue()) {
            throw new IllegalArgumentException("Maximum value less than maximum");
        }

        this.maxValue = maxValue;
    }

    public void activate() {
        state = State.ACTIVE;
        normalizedTransitionTimer = 1;
    }

    public void disable() {
        state = State.DISABLED;
        normalizedTransitionTimer = 0;
    }

    public void switchToActive() {
        timer = 0;

        if (state == State.ACTIVE || state == State.INTO_ACTIVE) {
            return;
        }

        state = State.INTO_ACTIVE;
        onIntoActive();
    }

    public void disabled() {
        state = State.DISABLED;
        normalizedTransitionTimer = 0;
    }

    public void switchToDisabled() {
        timer = 0;

        if (state == State.DISABLED || state == State.INTO_DISABLED) {
            return;
        }

        state = State.INTO_DISABLED;
        onIntoDisabled();
    }

    protected void onActive() {
    }

    protected void onDisabled() {
    }

    protected void onIntoActive() {
    }

    protected void onIntoDisabled() {
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (state == State.INTO_ACTIVE) {
            if (normalizedTransitionTimer < 1) {
                normalizedTransitionTimer += deltaTime / getIntoActiveDuration();
            } else {
                normalizedTransitionTimer = 1F;
                state = State.ACTIVE;
                onActive();
            }
        } else if (state == State.INTO_DISABLED) {
            if (normalizedTransitionTimer >= 0) {
                normalizedTransitionTimer -= deltaTime / getIntoDisabledDuration();
            } else {
                normalizedTransitionTimer = 0;
                state = State.DISABLED;
                onDisabled();
            }
        } else if (state == State.ACTIVE && getActiveDuration() > 0) {
            if (timer < getActiveDuration()) {
                timer += deltaTime;
            } else {
                timer = 0;
                switchToDisabled();
            }
        } else if (state == State.DISABLED && getDisabledDuration() > 0) {
            if (timer < getDisabledDuration()) {
                timer += deltaTime;
            } else {
                timer = 0;
                switchToActive();
            }
        }
    }

    public enum State {
        INTO_ACTIVE, ACTIVE, INTO_DISABLED, DISABLED
    }
}
