package com.inspw.cypress.game.model.serialized;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SongInfo {
    private String id;
    private String title;
    private String artist;
    private long previewStart;
    private long previewEnd;

    @JsonCreator
    public SongInfo(
            @JsonProperty("id") String id,
            @JsonProperty("title") String title,
            @JsonProperty("artist") String artist,
            @JsonProperty("previewStart") long previewStart,
            @JsonProperty("previewEnd") long previewEnd
    ) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.previewStart = previewStart;
        this.previewEnd = previewEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getPreviewStart() {
        return previewStart;
    }

    public void setPreviewStart(long previewStart) {
        this.previewStart = previewStart;
    }

    public long getPreviewEnd() {
        return previewEnd;
    }

    public void setPreviewEnd(long previewEnd) {
        this.previewEnd = previewEnd;
    }
}
