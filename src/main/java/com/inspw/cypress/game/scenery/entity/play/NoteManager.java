package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.rhythm.RhythmSession;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.scenery.Entity;

import java.util.List;

public abstract class NoteManager extends Entity {
    private RhythmSession rhythmSession;
    private RhythmTimer rhythmTimer;

    public NoteManager(RhythmSession rhythmSession, RhythmTimer rhythmTimer) {
        this.rhythmSession = rhythmSession;
        this.rhythmTimer = rhythmTimer;
    }

    public RhythmSession getRhythmSession() {
        return rhythmSession;
    }

    public void setRhythmSession(RhythmSession rhythmSession) {
        this.rhythmSession = rhythmSession;
    }

    public RhythmTimer getRhythmTimer() {
        return rhythmTimer;
    }

    public void setRhythmTimer(RhythmTimer rhythmTimer) {
        this.rhythmTimer = rhythmTimer;
    }

    public void initNoteEntities(List<NoteEntity<?>> noteEntities) {
    }
}
