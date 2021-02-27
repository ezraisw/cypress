package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.model.serialized.HoldNote;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.rhythm.ScanlinePage;

public class LongHoldNoteEntity extends RailedNoteEntity<HoldNote> {
    public LongHoldNoteEntity(HoldNote note, RhythmTimer rhythmTimer, ScanlinePage currSp) {
        super(note, rhythmTimer, currSp);
    }
}
