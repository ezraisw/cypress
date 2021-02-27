package com.inspw.cypress.game.misc.timer;

import com.inspw.cypress.game.misc.helper.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class SimpleTimer {
    private final List<FinishListener> finishListeners = new ArrayList<>();
    private float duration;
    private float time;
    private boolean done;
    private boolean started;
    private boolean reversed;
    private FinishType finishType;
    public SimpleTimer(float duration) {
        this(duration, FinishType.STOP);
    }

    public SimpleTimer(float duration, FinishType finishType) {
        this.duration = duration;
        this.finishType = finishType;
    }

    public void addFinishListener(FinishListener listener) {
        finishListeners.add(listener);
    }

    public void removeFinishListener(FinishListener listener) {
        finishListeners.remove(listener);
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getTime() {
        return time;
    }

    public boolean isDone() {
        return done;
    }

    public float getProgress() {
        return MathHelper.clamp(time / duration, 0, 1);
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public boolean isRunning() {
        return isStarted() && !isDone();
    }

    public void start() {
        started = true;
    }

    public void stop() {
        started = false;
    }

    public void end() {
        time = isReversed() ? 0 : duration;
        done = true;

        for (FinishListener listener : finishListeners) {
            listener.onFinish();
        }

        switch (getFinishType()) {
            case REVERSE:
                setReversed(!isReversed());
            case REPEAT:
                restart();
                break;
        }
    }

    public FinishType getFinishType() {
        return finishType;
    }

    public void setFinishType(FinishType finishType) {
        this.finishType = finishType;
    }

    public void reset() {
        restart();
        started = false;
    }

    public void restart() {
        time = isReversed() ? duration : 0;
        done = false;
    }

    public void update(float deltaTime) {
        if (!isRunning()) {
            return;
        }

        if (isReversed()) {
            if (time > 0) {
                time -= deltaTime;
            } else {
                end();
            }
        } else {
            if (time < duration) {
                time += deltaTime;
            } else {
                end();
            }
        }
    }

    public enum FinishType {
        STOP, REPEAT, REVERSE
    }

    public interface FinishListener {
        void onFinish();
    }
}
