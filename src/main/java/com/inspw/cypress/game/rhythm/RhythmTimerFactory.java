package com.inspw.cypress.game.rhythm;

import com.inspw.cypress.game.model.serialized.Chart;

public interface RhythmTimerFactory {
    RhythmTimer create(Chart chart);
}
