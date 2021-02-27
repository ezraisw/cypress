package com.inspw.cypress.game.scenery;

public class SceneException extends RuntimeException {
    public SceneException() {
    }

    public SceneException(String message) {
        super(message);
    }

    public SceneException(String message, Throwable cause) {
        super(message, cause);
    }

    public SceneException(Throwable cause) {
        super(cause);
    }
}
