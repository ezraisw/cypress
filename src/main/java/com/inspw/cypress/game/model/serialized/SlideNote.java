package com.inspw.cypress.game.model.serialized;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inspw.cypress.game.model.logic.note.SlideNoteHitRegistration;

@JsonTypeName("slide")
public class SlideNote extends FreeNote {
    private String targetId;
    private int slideGroup;

    @JsonCreator
    public SlideNote(
            @JsonProperty("id") String id,
            @JsonProperty("time") float time,
            @JsonProperty("x") int x,
            @JsonProperty("targetId") String targetId,
            @JsonProperty("slideGroup") int slideGroup
    ) {
        super(id, time, x);
        this.targetId = targetId;
        this.slideGroup = slideGroup;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getSlideGroup() {
        return slideGroup;
    }

    public void setSlideGroup(int slideGroup) {
        this.slideGroup = slideGroup;
    }

    @Override
    public SlideNoteHitRegistration createHitRegistration() {
        return new SlideNoteHitRegistration(this);
    }
}
