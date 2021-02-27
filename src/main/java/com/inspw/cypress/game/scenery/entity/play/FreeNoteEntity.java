package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.model.serialized.FreeNote;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.rhythm.ScanlinePage;

public abstract class FreeNoteEntity<T extends FreeNote> extends NoteEntity<T> {
    public FreeNoteEntity(T note, RhythmTimer rhythmTimer, ScanlinePage currSp) {
        super(note, rhythmTimer, currSp);
    }
}
