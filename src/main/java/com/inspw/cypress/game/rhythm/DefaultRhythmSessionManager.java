package com.inspw.cypress.game.rhythm;

import com.inspw.cypress.game.model.Song;

public class DefaultRhythmSessionManager implements RhythmSessionManager {
    private RhythmSession session;

    @Override
    public RhythmSession getSession() {
        return session;
    }

    @Override
    public void newSession(Song song, int difficultyIndex) {
        session = new RhythmSession(song, difficultyIndex);
    }
}
