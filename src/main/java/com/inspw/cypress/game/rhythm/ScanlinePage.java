package com.inspw.cypress.game.rhythm;

import com.inspw.cypress.game.model.serialized.Note;

import java.util.List;

public class ScanlinePage {
    private final float startPosition;
    private final float endPosition;
    private final float startTime;
    private final float endTime;
    private final List<Note> notes;
    private final ScanlinePage previous;

    public ScanlinePage(
            float startPosition,
            float endPosition,
            float startTime,
            float endTime,
            List<Note> notes,
            ScanlinePage previous
    ) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
        this.previous = previous;
    }

    public float getStartPosition() {
        return startPosition;
    }

    public float getEndPosition() {
        return endPosition;
    }

    public float getStartTime() {
        return startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public ScanlinePage getPrevious() {
        return previous;
    }

    public float getDistance() {
        return Math.abs(getEndPosition() - getStartPosition());
    }

    public float getDuration() {
        return getEndTime() - getStartTime();
    }

    public Direction getDirection() {
        return getEndPosition() > getStartPosition()
                ? Direction.DOWN
                : Direction.UP;
    }

    public float getBeatsPerSecond() {
        return 1 / getDuration();
    }

    public float getDisplayDuration() {
        return getPrevious() == null
                ? getDuration()
                : Math.min(getDuration(), getPrevious().getDuration());
    }

    public boolean hasAppeared(Note note, float time) {
        if (note.getTime() >= getEndTime() || note.getTime() < getStartTime()) {
            throw new IllegalArgumentException("Note not contained within page: " + note.getId());
        }

        return time > (note.getTime() - getDisplayDuration());
    }

    public enum Direction {
        UP, DOWN
    }
}
