package com.inspw.cypress.game.model.serialized;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Profile {
    private int offset;
    private int lastDifficultyIndex;
    private String lastSongId;
    private Map<String, BestResult> bestResults;

    @JsonCreator
    public Profile(
            @JsonProperty("offset") int offset,
            @JsonProperty("lastDifficultyIndex") int lastDifficultyIndex,
            @JsonProperty("lastSongId") String lastSongId,
            @JsonProperty("bestResults") Map<String, BestResult> bestResults
    ) {
        this.offset = offset;
        this.lastDifficultyIndex = lastDifficultyIndex;
        this.lastSongId = lastSongId;
        this.bestResults = bestResults;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLastDifficultyIndex() {
        return lastDifficultyIndex;
    }

    public void setLastDifficultyIndex(int lastDifficultyIndex) {
        this.lastDifficultyIndex = lastDifficultyIndex;
    }

    public String getLastSongId() {
        return lastSongId;
    }

    public void setLastSongId(String lastSongId) {
        this.lastSongId = lastSongId;
    }

    public void adjustBestResult(String songId, BestResult newBestResult) {
        BestResult bestResult = getBestResults().get(songId);
        if (bestResult == null) {
            bestResult = newBestResult;
        } else {
            if (bestResult.getScore() < newBestResult.getScore()) {
                bestResult.setScore(newBestResult.getScore());
            }
            if (bestResult.getMaxCombo() < newBestResult.getMaxCombo()) {
                bestResult.setMaxCombo(newBestResult.getScore());
            }
        }
        getBestResults().put(songId, bestResult);
    }

    public Map<String, BestResult> getBestResults() {
        return bestResults;
    }

    public void setBestResults(Map<String, BestResult> bestResults) {
        this.bestResults = bestResults;
    }
}
