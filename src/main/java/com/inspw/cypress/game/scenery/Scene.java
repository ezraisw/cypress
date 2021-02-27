package com.inspw.cypress.game.scenery;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.input.Input;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class Scene {
    private final List<Entity> entities = Collections.synchronizedList(new ArrayList<>());
    private final List<Entity> renderableEntities = Collections.synchronizedList(new ArrayList<>());
    private boolean loaded;

    protected Input input() {
        return Game.instance().input();
    }

    protected RenderingSurface rs() {
        return Game.instance().renderingSurface();
    }

    protected List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    protected void add(Entity entity) {
        if (entity.isLoaded()) {
            throw new IllegalStateException("Entity already loaded");
        }
        entities.add(entity);
        renderableEntities.add(entity);
        reorderZ();
    }

    protected void add(Collection<? extends Entity> additionalEntities) {
        entities.addAll(additionalEntities);
        renderableEntities.addAll(additionalEntities);
        reorderZ();
    }

    protected void remove(Entity entity) {
        entity.flagToBeRemoved();
    }

    protected void removeImmediate(Entity entity) {
        removeFromList(entity);
        reorderZ();
    }

    private void removeFromList(Entity entity) {
        entities.remove(entity);
        renderableEntities.remove(entity);
    }

    private void clearEntities() {
        entities.clear();
        renderableEntities.clear();
    }

    private void loadEntity(Entity entity) {
        if (!entity.isLoaded()) {
            entity.load();
            entity.flagLoaded();
        }
    }

    private void reorderZ() {
        renderableEntities.sort(Comparator.comparingInt(Entity::getZ));
    }

    private void removeToBeRemovedEntities() {
        List<Entity> toBeRemovedEntities = new ArrayList<>();
        entities.stream()
                .filter(Entity::isToBeRemoved)
                .forEach(toBeRemovedEntities::add);

        if (!toBeRemovedEntities.isEmpty()) {
            for (Entity entity : toBeRemovedEntities) {
                removeFromList(entity);
            }
            reorderZ();
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    void flagLoaded() {
        loaded = true;
    }

    public void load() {
        for (Entity entity : entities) {
            loadEntity(entity);
        }
    }

    public void unload() {
        for (Entity entity : entities) {
            entity.unload();
        }
        clearEntities();
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            loadEntity(entity);

            if (entity.isLoaded() && entity.isActive()) {
                entity.update(deltaTime);
            }
        }

        removeToBeRemovedEntities();
    }

    public void render(Graphics2D g) {
        for (Entity entity : renderableEntities) {
            if (entity.isLoaded() && entity.isActive()) {
                entity.render(g);
            }
        }
    }
}
