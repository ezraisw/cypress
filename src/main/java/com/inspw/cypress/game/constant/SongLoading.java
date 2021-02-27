package com.inspw.cypress.game.constant;

import java.io.File;

public class SongLoading {
    public static final File SONGS_DIRECTORY = new File("song");
    public static final String SONG_INFO_FILE_NAME = "info.json";
    public static final String CHART_FILE_NAME_PREFIX = "chart--";
    public static final String CHART_FILE_NAME_SUFFIX = ".json";
    public static final String CHART_SPECIFIC_SOUND_FILE_NAME_PREFIX = "music--";
    public static final String DEFAULT_SOUND_FILE_NAME = "music";
    public static final String[] SOUND_FILE_NAME_SUFFIXES = {".mp3", ".m4a", ".wav", ".ogg"};
    public static final String COVER_IMAGE_FILE_NAME = "cover";
    public static final String[] COVER_IMAGE_FILE_NAME_SUFFIXES = {".jpeg", ".jpg", ".png", ".bmp", ".gif"};

    private SongLoading() {
    }
}
