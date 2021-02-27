package com.inspw.cypress.game.rhythm;

public class RhythmDataException extends RuntimeException {
    public RhythmDataException() {
    }

    public RhythmDataException(String message) {
        super(message);
    }

    public RhythmDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public RhythmDataException(Throwable cause) {
        super(cause);
    }
}
