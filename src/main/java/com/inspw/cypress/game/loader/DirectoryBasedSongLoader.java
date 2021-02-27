package com.inspw.cypress.game.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspw.cypress.game.constant.SongLoading;
import com.inspw.cypress.game.model.Difficulty;
import com.inspw.cypress.game.model.Song;
import com.inspw.cypress.game.model.serialized.Chart;
import com.inspw.cypress.game.model.serialized.SongInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DirectoryBasedSongLoader implements SongLoader {
    private final ObjectMapper om = new ObjectMapper();

    private Optional<File> findFileWithSuffix(File dir, String expectedName, String[] suffixes) {
        File[] potentialFile = dir.listFiles(
                (f, name) -> Arrays.stream(suffixes)
                        .anyMatch(suf -> (expectedName + suf).equals(name))
        );

        return potentialFile != null && potentialFile.length > 0
                ? Optional.of(potentialFile[0])
                : Optional.empty();
    }

    private String unaffixedChartFileName(File file) {
        if (!file.getName().startsWith(SongLoading.CHART_FILE_NAME_PREFIX) ||
                !file.getName().endsWith(SongLoading.CHART_FILE_NAME_SUFFIX)
        ) {
            throw new IllegalArgumentException("Invalid file name for chart");
        }

        return file.getName()
                .replace(SongLoading.CHART_FILE_NAME_PREFIX, "")
                .replace(SongLoading.CHART_FILE_NAME_SUFFIX, "");
    }

    private File getSoundFile(File songDir, String chartName) throws FileNotFoundException {
        String chartSoundFileName = SongLoading.CHART_SPECIFIC_SOUND_FILE_NAME_PREFIX + chartName;

        return findFileWithSuffix(songDir, chartSoundFileName, SongLoading.SOUND_FILE_NAME_SUFFIXES)
                .orElse(getDefaultSoundFile(songDir));
    }

    private File getDefaultSoundFile(File songDir) throws FileNotFoundException {
        return findFileWithSuffix(songDir, SongLoading.DEFAULT_SOUND_FILE_NAME, SongLoading.SOUND_FILE_NAME_SUFFIXES)
                .orElseThrow(() -> new FileNotFoundException("Missing sound file"));
    }

    private File getCoverImageFile(File songDir) throws FileNotFoundException {
        return findFileWithSuffix(songDir, SongLoading.COVER_IMAGE_FILE_NAME, SongLoading.COVER_IMAGE_FILE_NAME_SUFFIXES)
                .orElseThrow(() -> new FileNotFoundException("Missing cover image file"));
    }

    private List<Difficulty> parseDifficulties(File songDir) {
        File[] chartFiles = songDir.listFiles(
                (f, name) -> name.startsWith(SongLoading.CHART_FILE_NAME_PREFIX) && name.endsWith(SongLoading.CHART_FILE_NAME_SUFFIX)
        );

        ArrayList<Difficulty> difficulties = new ArrayList<>();
        if (chartFiles != null) {
            for (File chartFile : chartFiles) {
                try {
                    Chart chart = om.readValue(chartFile, Chart.class);
                    File soundFile = getSoundFile(songDir, unaffixedChartFileName(chartFile));
                    difficulties.add(new Difficulty(chart, soundFile));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return difficulties;
    }

    private Song parseSong(File songDir) {
        File[] potentialSongInfoFile = songDir.listFiles((f, name) -> SongLoading.SONG_INFO_FILE_NAME.equals(name));
        if (potentialSongInfoFile == null) {
            return null;
        }

        File songInfoFile = potentialSongInfoFile[0];
        try {
            SongInfo songInfo = om.readValue(songInfoFile, SongInfo.class);
            File defaultSoundFile = getDefaultSoundFile(songDir);
            File coverImageFile = getCoverImageFile(songDir);
            return new Song(songDir, defaultSoundFile, coverImageFile, songInfo, parseDifficulties(songDir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Song> loadSongs() {
        File[] files = SongLoading.SONGS_DIRECTORY.listFiles();

        if (files == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(files)
                .filter(File::isDirectory)
                .map(this::parseSong)
                .collect(Collectors.toList());
    }
}
