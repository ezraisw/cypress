package com.inspw.cypress.game.model.serialized;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inspw.cypress.game.model.logic.note.HoldNoteHitRegistration;

@JsonTypeName("hold")
public class HoldNote extends RailedNote {
    private float endTime;

    @JsonCreator
    public HoldNote(
            @JsonProperty("id") String id,
            @JsonProperty("time") float time,
            @JsonProperty("rail") int rail,
            @JsonProperty("shiftX") float shiftX,
            @JsonProperty("endTime") float endTime
    ) {
        super(id, time, rail, shiftX);
        this.endTime = endTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }

    @Override
    public int expectedCount() {
        return 2;
    }

    @Override
    public HoldNoteHitRegistration createHitRegistration() {
        return new HoldNoteHitRegistration(this);
    }
}
