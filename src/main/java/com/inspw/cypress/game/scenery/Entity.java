package com.inspw.cypress.game.scenery;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.input.Input;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public abstract class Entity implements Loadable, Updatable, Renderable {
    private boolean loaded;
    private boolean active = true;
    private boolean toBeRemoved;
    private int z;

    protected Input input() {
        return Game.instance().input();
    }

    protected RenderingSurface rs() {
        return Game.instance().renderingSurface();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isToBeRemoved() {
        return toBeRemoved;
    }

    void flagToBeRemoved() {
        toBeRemoved = true;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    void flagLoaded() {
        loaded = true;
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public void render(Graphics2D g) {
    }
}
