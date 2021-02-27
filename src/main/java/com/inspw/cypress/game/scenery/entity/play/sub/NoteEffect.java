package com.inspw.cypress.game.scenery.entity.play.sub;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NoteEffect {
    private final List<FinishListener> finishListeners = new ArrayList<>();
    private final Vector2 position;
    private final String text;
    private final SimpleTimer effectTimer;
    private int z;

    public NoteEffect(Vector2 position, String text, float duration) {
        this.position = position;
        this.text = text;
        this.effectTimer = new SimpleTimer(duration);
        this.effectTimer.addFinishListener(this::finish);
    }

    protected RenderingSurface rs() {
        return Game.instance().renderingSurface();
    }

    protected void finish() {
        for (FinishListener listener : finishListeners) {
            listener.onFinish(this);
        }
    }

    public void addFinishListener(FinishListener listener) {
        finishListeners.add(listener);
    }

    public void removeFinishListener(FinishListener listener) {
        finishListeners.remove(listener);
    }

    public Vector2 getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }

    protected float value() {
        return effectTimer.getProgress();
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void update(float deltaTime) {
        if (!effectTimer.isStarted()) {
            effectTimer.start();
        }

        effectTimer.update(deltaTime);
    }

    public void render(Graphics2D g) {
    }

    public interface FinishListener {
        void onFinish(NoteEffect noteEffect);
    }
}
