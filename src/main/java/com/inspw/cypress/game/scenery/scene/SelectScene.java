package com.inspw.cypress.game.scenery.scene;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Select;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.model.Song;
import com.inspw.cypress.game.model.serialized.Profile;
import com.inspw.cypress.game.scenery.Entity;
import com.inspw.cypress.game.scenery.Scene;
import com.inspw.cypress.game.scenery.entity.general.BinaryTransitionalEntity;
import com.inspw.cypress.game.scenery.entity.select.*;
import com.inspw.cypress.game.ui.input.KeyCode;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.util.List;

public class SelectScene extends Scene {
    private SimpleTimer entryTransitionTimer;
    private SimpleTimer exitTransitionTimer;
    private List<Song> songs;
    private SongList songList;
    private SongInfoText songInfoText;
    private HighScoreText highScoreText;
    private DifficultySelection difficultySelection;
    private CoverImageBackground coverImageBackground;
    private MusicPreview musicPreview;

    private void initTimers() {
        entryTransitionTimer = new SimpleTimer(Select.ENTRY_TRANSITION_DURATION);
        exitTransitionTimer = new SimpleTimer(Select.EXIT_TRANSITION_DURATION);
        exitTransitionTimer.addFinishListener(this::toPlay);
    }

    private void loadSongs() {
        songs = Game.instance().songLoader().loadSongs();
    }

    private void initEntities() {
        songList = new SongList(songs);
        add(songList);

        songInfoText = new SongInfoText();
        add(songInfoText);

        difficultySelection = new DifficultySelection();
        add(difficultySelection);

        highScoreText = new HighScoreText();
        add(highScoreText);

        coverImageBackground = new CoverImageBackground();
        add(coverImageBackground);

        musicPreview = new MusicPreview();
        add(musicPreview);
    }

    @Override
    public void load() {
        super.load();

        loadSongs();
        initTimers();
        initEntities();
        loadPersistentSelection();

        updateSong();
        entryTransitionTimer.start();
    }

    private Song currentSong() {
        if (songList.getCurrentIndex() < 0 || songList.getCurrentIndex() >= songs.size()) {
            return null;
        }
        return songs.get(songList.getCurrentIndex());
    }

    private void loadPersistentSelection() {
        Profile profile = Game.instance().profileManager().getProfile();

        int currentIndex = 0;
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            if (song.getSongInfo()
                    .getId()
                    .equals(profile.getLastSongId())
            ) {
                currentIndex = i;
                break;
            }
        }

        songList.setCurrentIndex(currentIndex, false);
        difficultySelection.setDifficultyIndex(profile.getLastDifficultyIndex());
    }

    private void savePersistentSelection() {
        Profile profile = Game.instance()
                .profileManager()
                .getProfile();

        profile.setLastDifficultyIndex(difficultySelection.getDifficultyIndex());

        Song currentSong = currentSong();
        if (currentSong != null) {
            profile.setLastSongId(currentSong.getSongInfo().getId());
        }
    }

    private void toPlay() {
        savePersistentSelection();

        Game.instance()
                .sceneManager()
                .changeTo("play");
        Game.instance()
                .rhythmSessionManager()
                .newSession(currentSong(), difficultySelection.getDifficultyIndex());
    }

    private boolean isSelected() {
        return exitTransitionTimer.isStarted();
    }

    private void updateSong() {
        Song song = currentSong();

        if (song == null) {
            return;
        }

        songInfoText.newSong(song);
        difficultySelection.newSong(song);
        highScoreText.newSong(song);
        coverImageBackground.newImageFile(song.getCoverImageFile());
        musicPreview.newSong(song);
    }

    private boolean cannotSelectSong() {
        Song currentSong = currentSong();
        return isSelected() ||
                currentSong == null ||
                difficultySelection.getDifficultyIndex() < 0 ||
                difficultySelection.getDifficultyIndex() >= currentSong.getDifficulties().size() ||
                coverImageBackground.getState() != BinaryTransitionalEntity.State.ACTIVE;
    }

    private void selectSong() {
        if (cannotSelectSong()) {
            return;
        }

        exitTransitionTimer.start();

        for (Entity entity : getEntities()) {
            if (entity instanceof BinaryTransitionalEntity) {
                BinaryTransitionalEntity bte = (BinaryTransitionalEntity) entity;
                bte.setDisabledDuration(-1);
                bte.setIntoDisabledDuration(Select.EXIT_TRANSITION_DURATION);
                bte.switchToDisabled();
            }
        }

        coverImageBackground.setMinValue(Select.BACKGROUND_AS_DECORATION_ALPHA);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForKeyPresses();
        entryTransitionTimer.update(deltaTime);
        exitTransitionTimer.update(deltaTime);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (!entryTransitionTimer.isDone()) {
            g.setColor(new Color(0, 0, 0, 1 - entryTransitionTimer.getProgress()));
            g.fillRect(0, 0, rs().getWidth(), rs().getHeight());
        }
    }

    private void checkForKeyPresses() {
        checkForSongSelectKeyPress();
        checkForSongListSelectKeyPress();
        checkForDifficultySelectKeyPress();
    }

    private void checkForSongSelectKeyPress() {
        if (Game.instance().input().isKeyDown(KeyCode.VK_ENTER)) {
            selectSong();
        }
    }

    private void checkForSongListSelectKeyPress() {
        if (Game.instance().input().isKeyDown(KeyCode.VK_UP) || Game.instance().input().isKeyDown(KeyCode.VK_W)) {
            if (!isSelected() && songList.up(true)) {
                updateSong();
            }
        } else if (Game.instance().input().isKeyDown(KeyCode.VK_DOWN) || Game.instance().input().isKeyDown(KeyCode.VK_S)) {
            if (!isSelected() && songList.down(true)) {
                updateSong();
            }
        }
    }

    private void checkForDifficultySelectKeyPress() {
        if (Game.instance().input().isKeyDown(KeyCode.VK_LEFT) || Game.instance().input().isKeyDown(KeyCode.VK_A)) {
            if (!isSelected()) {
                difficultySelection.left();
            }
        } else if (Game.instance().input().isKeyDown(KeyCode.VK_RIGHT) ||Game.instance().input().isKeyDown(KeyCode.VK_D)) {
            if (!isSelected()) {
                difficultySelection.right();
            }
        }
    }
}
