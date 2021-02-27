package com.inspw.cypress.game.model.serialized;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Chart {
    private String name;
    private int level;
    private float delay;
    private float endMargin;
    private float length;
    private List<ScanlineMovement> scanlineMovements;
    private List<Note> notes;

    @JsonCreator
    public Chart(
            @JsonProperty("name") String name,
            @JsonProperty("level") int level,
            @JsonProperty("delay") float delay,
            @JsonProperty("endMargin") float endMargin,
            @JsonProperty("length") float length,
            @JsonProperty("scanlineMovements") List<ScanlineMovement> scanlineMovements,
            @JsonProperty("notes") List<Note> notes
    ) {
        this.name = name;
        this.level = level;
        this.delay = delay;
        this.endMargin = endMargin;
        this.length = length;
        this.scanlineMovements = scanlineMovements;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getEndMargin() {
        return endMargin;
    }

    public void setEndMargin(float endMargin) {
        this.endMargin = endMargin;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public List<ScanlineMovement> getScanlineMovements() {
        return scanlineMovements;
    }

    public void setScanlineMovements(List<ScanlineMovement> scanlineMovements) {
        this.scanlineMovements = scanlineMovements;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
