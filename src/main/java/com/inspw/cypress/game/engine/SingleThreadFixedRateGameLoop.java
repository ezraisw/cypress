package com.inspw.cypress.game.engine;

import com.inspw.cypress.game.scenery.Director;

public class SingleThreadFixedRateGameLoop implements GameLoop {
    private final Director director;
    private final int rate;
    private boolean running;
    private Thread thread;

    public SingleThreadFixedRateGameLoop(Director director, int rate) {
        this.director = director;
        this.rate = rate;
    }

    public void start() {
        if (running) {
            throw new IllegalStateException("Trying to start a running loop");
        }
        running = true;
        thread = new Thread(this::run);
        thread.start();
    }

    public void stop() {
        if (!running) {
            throw new IllegalStateException("Trying to stop a stopped loop");
        }
        running = false;
        thread = null;
    }

    public boolean isRunning() {
        return running;
    }

    public int getRate() {
        return rate;
    }

    private void run() {
        while (isRunning()) {
            long startTime = System.nanoTime();
            director.update(getPeriod());
            long endTime = System.nanoTime();
            long delta = endTime - startTime;

            long sleepNano = Math.round(getPeriod() * 1e9 - delta);
            if (sleepNano > 0) {
                long sleepMillis = Math.floorDiv(sleepNano, 1000000);
                int sleepNanoLeftover = (int) (sleepNano % 1000000);

                try {
                    Thread.sleep(sleepMillis, sleepNanoLeftover);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
