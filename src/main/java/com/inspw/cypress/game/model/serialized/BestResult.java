package com.inspw.cypress.game.model.serialized;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BestResult {
    private int maxCombo;
    private int score;

    @JsonCreator
    public BestResult(
            @JsonProperty("maxCombo") int maxCombo,
            @JsonProperty("score") int score
    ) {
        this.maxCombo = maxCombo;
        this.score = score;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public void setMaxCombo(int maxCombo) {
        this.maxCombo = maxCombo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
