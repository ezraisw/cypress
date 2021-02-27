package com.inspw.cypress.game.model;

import com.inspw.cypress.game.model.serialized.SongInfo;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Song {
    private File directory;
    private File defaultSoundFile;
    private File coverImageFile;
    private SongInfo songInfo;
    private List<Difficulty> difficulties;

    public Song(
            File directory,
            File defaultSoundFile,
            File coverImageFile,
            SongInfo songInfo,
            List<Difficulty> difficulties
    ) {
        this.directory = Objects.requireNonNull(directory);
        this.defaultSoundFile = Objects.requireNonNull(defaultSoundFile);
        this.coverImageFile = Objects.requireNonNull(coverImageFile);
        this.songInfo = Objects.requireNonNull(songInfo);
        this.difficulties = Objects.requireNonNull(difficulties);
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = Objects.requireNonNull(directory);
    }

    public File getDefaultSoundFile() {
        return defaultSoundFile;
    }

    public void setDefaultSoundFile(File defaultSoundFile) {
        this.defaultSoundFile = Objects.requireNonNull(defaultSoundFile);
    }

    public File getCoverImageFile() {
        return coverImageFile;
    }

    public void setCoverImageFile(File coverImageFile) {
        this.coverImageFile = Objects.requireNonNull(coverImageFile);
    }

    public SongInfo getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = Objects.requireNonNull(songInfo);
    }

    public List<Difficulty> getDifficulties() {
        return difficulties;
    }

    public void setDifficulties(List<Difficulty> difficulties) {
        this.difficulties = Objects.requireNonNull(difficulties);
    }
}
