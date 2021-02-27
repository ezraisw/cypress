package com.inspw.cypress.game.scenery;

import com.inspw.cypress.game.ui.input.InputProvider;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

public class DefaultDirector implements Director {
    private final SceneManager sceneManager;
    private final RenderingSurface renderingSurface;
    private final InputProvider inputProvider;

    public DefaultDirector(SceneManager sceneManager, RenderingSurface renderingSurface, InputProvider inputProvider) {
        this.sceneManager = sceneManager;
        this.renderingSurface = renderingSurface;
        this.inputProvider = inputProvider;
    }

    @Override
    public void update(float deltaTime) {
        Scene scene = sceneManager.getActiveScene();

        if (scene != null) {
            if (!scene.isLoaded()) {
                scene.load();
                scene.flagLoaded();
            }

            // Update the currently active scene.
            scene.update(deltaTime);
            // Render the updated scene.
            renderingSurface.requestRender();

            // Flush the buffered inputs.
            inputProvider.flush();

            // Reset up and down states of input.
            inputProvider.clearUpAndDownStates();
        }
    }
}
