package com.inspw.cypress.game.rhythm;

import com.inspw.cypress.game.model.Song;

public interface RhythmSessionManager {
    RhythmSession getSession();

    void newSession(Song song, int difficultyIndex);

    default void renewSession() {
        newSession(getSession().getSong(), getSession().getDifficultyIndex());
    }
}
