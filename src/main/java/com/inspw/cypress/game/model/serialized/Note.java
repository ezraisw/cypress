package com.inspw.cypress.game.model.serialized;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inspw.cypress.game.model.logic.note.NoteHitRegistration;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PressNote.class, name = "press"),
        @JsonSubTypes.Type(value = HoldNote.class, name = "hold"),
        @JsonSubTypes.Type(value = SlideNote.class, name = "slide")
})
public abstract class Note {
    private String id;
    private float time;

    public Note(String id, float time) {
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int expectedCount() {
        return 1;
    }

    public abstract NoteHitRegistration<?> createHitRegistration();
}
