package com.inspw.cypress.game.constant;

public class Engine {
    /**
     * The update rate of the game loop.
     */
    public static final int UPDATE_RATE = 120;

    /**
     * The maximum amount of frame time variance when calculating time synchronization
     * and interpolation.
     */
    public static final float MAX_FRAMETIME_VARIANCE_CONSTANT = 0.05F;

    private Engine() {
    }
}
