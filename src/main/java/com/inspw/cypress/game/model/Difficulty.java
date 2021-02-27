package com.inspw.cypress.game.model;

import com.inspw.cypress.game.model.serialized.Chart;

import java.io.File;
import java.util.Objects;

public class Difficulty {
    private Chart chart;
    private File soundFile;

    public Difficulty(Chart chart, File soundFile) {
        this.chart = Objects.requireNonNull(chart);
        this.soundFile = Objects.requireNonNull(soundFile);
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = Objects.requireNonNull(chart);
    }

    public File getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(File soundFile) {
        this.soundFile = Objects.requireNonNull(soundFile);
    }
}
