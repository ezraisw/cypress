package com.inspw.cypress.game.scenery;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DefaultSceneManager implements SceneManager {
    private final Map<String, Class<? extends Scene>> scenes = new HashMap<>();
    private Scene activeScene;

    public Scene getActiveScene() {
        return activeScene;
    }

    public DefaultSceneManager register(String sceneName, Class<? extends Scene> sceneClass) {
        scenes.put(sceneName, sceneClass);
        return this;
    }

    public DefaultSceneManager preload(String sceneName) {
        return this;
    }

    public DefaultSceneManager changeTo(String sceneName) {
        Class<? extends Scene> sceneClass = scenes.get(sceneName);

        try {
            Scene scene = sceneClass.getConstructor().newInstance();

            if (activeScene != null) {
                activeScene.unload();
            }

            activeScene = scene;
        } catch (InstantiationException |
                IllegalAccessException |
                NoSuchMethodException |
                InvocationTargetException e
        ) {
            throw new SceneException("Unable to instantiate scene", e);
        }

        return this;
    }
}
