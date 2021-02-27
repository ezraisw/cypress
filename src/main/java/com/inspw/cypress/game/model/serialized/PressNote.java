package com.inspw.cypress.game.model.serialized;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inspw.cypress.game.model.logic.note.PressNoteHitRegistration;

@JsonTypeName("press")
public class PressNote extends RailedNote {
    @JsonCreator
    public PressNote(
            @JsonProperty("id") String id,
            @JsonProperty("time") float time,
            @JsonProperty("rail") int rail,
            @JsonProperty("shiftX") float shiftX
    ) {
        super(id, time, rail, shiftX);
    }

    @Override
    public PressNoteHitRegistration createHitRegistration() {
        return new PressNoteHitRegistration(this);
    }
}
