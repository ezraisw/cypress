package com.inspw.cypress.game.engine;

public interface GameLoop {
    void start();

    void stop();

    boolean isRunning();

    int getRate();

    default float getPeriod() {
        return 1F / getRate();
    }

    default long getNanoPeriod() {
        return 1000000000L / getRate();
    }
}
