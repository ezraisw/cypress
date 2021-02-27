package com.inspw.cypress.game.ui;

import com.inspw.cypress.game.ui.input.InputProvider;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

public interface UIInfrastructure {
    /**
     * Configure the UI for use.
     */
    void configure();

    /**
     * Start and show the UI.
     */
    void start();

    /**
     * Obtain the rendering surface provided by the UI.
     *
     * @return the rendering surface
     */
    RenderingSurface renderingSurface();

    /**
     * Obtain the input instance for the UI.
     *
     * @return the input
     */
    InputProvider inputProvider();
}
