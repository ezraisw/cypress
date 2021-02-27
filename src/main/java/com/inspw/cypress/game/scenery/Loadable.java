package com.inspw.cypress.game.scenery;

public interface Loadable {
    boolean isLoaded();

    void load();

    void unload();
}
