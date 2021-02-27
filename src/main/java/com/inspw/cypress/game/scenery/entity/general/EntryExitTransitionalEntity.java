package com.inspw.cypress.game.scenery.entity.general;

import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.scenery.Entity;

public class EntryExitTransitionalEntity extends Entity {
    private final SimpleTimer fadeInTimer;
    private final SimpleTimer fadeOutTimer;

    public EntryExitTransitionalEntity(float fadeInDuration, float fadeOutDuration) {
        fadeInTimer = new SimpleTimer(fadeInDuration);
        fadeInTimer.addFinishListener(this::onFadeInFinish);
        fadeOutTimer = new SimpleTimer(fadeOutDuration);
        fadeOutTimer.addFinishListener(this::onFadeOutFinish);
    }

    public void fadeIn() {
        fadeInTimer.start();
    }

    public void fadeOut() {
        fadeOutTimer.start();
    }

    protected float value() {
        return fadeInValue() * (1 - fadeOutValue());
    }

    protected float fadeInValue() {
        return fadeInTimer.getProgress();
    }

    protected float fadeOutValue() {
        return fadeOutTimer.getProgress();
    }

    public boolean isFadeInDone() {
        return fadeInTimer.isDone();
    }

    public boolean isFadeOutDone() {
        return fadeOutTimer.isDone();
    }

    protected void onFadeInFinish() {
    }

    protected void onFadeOutFinish() {
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        fadeInTimer.update(deltaTime);
        fadeOutTimer.update(deltaTime);
    }
}
