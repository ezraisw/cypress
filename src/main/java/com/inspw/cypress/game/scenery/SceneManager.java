package com.inspw.cypress.game.scenery;

public interface SceneManager {
    Scene getActiveScene();

    SceneManager register(String sceneName, Class<? extends Scene> sceneClass);

    SceneManager preload(String sceneName);

    SceneManager changeTo(String sceneName);
}
