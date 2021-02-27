package com.inspw.cypress.game.scenery.scene;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Play;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.model.logic.note.SlideNoteHitRegistration;
import com.inspw.cypress.game.model.serialized.*;
import com.inspw.cypress.game.rhythm.*;
import com.inspw.cypress.game.scenery.Entity;
import com.inspw.cypress.game.scenery.Scene;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.scenery.entity.general.StaticCoverImageBackground;
import com.inspw.cypress.game.scenery.entity.play.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayScene extends Scene {
    private Music music;
    private Scanline scanline;
    private Chart chart;
    private RhythmTimer rhythmTimer;
    private long lastTime = 0;
    private long lastMusicTime = 0;
    private boolean finished;
    private SimpleTimer readyTimer;
    private SimpleTimer musicPlayDelayTimer;
    private SimpleTimer exitTransitionDelayTimer;
    private SimpleTimer exitTransitionTimer;
    private NoteManager[] noteManagers;

    private void readyDone() {
        musicPlayDelayTimer.start();
        scanline.begin();
    }

    private void playMusic() {
        music.play();
    }

    private void reset() {
        lastTime = 0;
        lastMusicTime = 0;
        readyTimer.reset();
        musicPlayDelayTimer.reset();
    }

    private void toResult() {
        Game.instance()
                .sceneManager()
                .changeTo("result");
    }

    private void initTimers() {
        readyTimer = new SimpleTimer(Play.READY_DURATION);
        readyTimer.addFinishListener(this::readyDone);

        musicPlayDelayTimer = new SimpleTimer(Play.DEFAULT_MUSIC_PLAY_DELAY_DURATION);
        musicPlayDelayTimer.addFinishListener(this::playMusic);
        musicPlayDelayTimer.setDuration(Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getDifficulty()
                .getChart()
                .getDelay()
        );

        exitTransitionDelayTimer = new SimpleTimer(Play.EXIT_TRANSITION_DELAY_DURATION);
        exitTransitionDelayTimer.addFinishListener(this::fadeOut);

        exitTransitionTimer = new SimpleTimer(Play.EXIT_TRANSITION_DURATION);
        exitTransitionTimer.addFinishListener(this::toResult);
    }

    private void loadChart() {
        chart = Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getDifficulty()
                .getChart();
        rhythmTimer = new StandardRhythmTimerFactory().create(chart);
        rhythmTimer.setTime(-chart.getDelay());
    }

    private void initNoteManagers() {
        RhythmSession rhythmSession = Game.instance()
                .rhythmSessionManager()
                .getSession();

        noteManagers = new NoteManager[]{
                new RailedNoteManager(rhythmSession, rhythmTimer, Play.RAIL_COUNT),
                new SlideNoteManager(rhythmSession, rhythmTimer, Play.SLIDE_GROUP_COUNT)
        };

        for (NoteManager nm : noteManagers) {
            add(nm);
        }
    }

    private void initSlideNoteNext(List<SlideNoteEntity> slideNoteEntities) {
        List<SlideNoteGuide> guides = new ArrayList<>();
        Map<String, SlideNoteEntity> idMapping = new HashMap<>();

        for (SlideNoteEntity ne : slideNoteEntities) {
            idMapping.put(ne.getNote().getId(), ne);
        }

        for (SlideNoteEntity ne : slideNoteEntities) {
            if (ne.getNote().getTargetId() == null) {
                continue;
            }

            SlideNoteEntity targetNe = idMapping.get(ne.getNote().getTargetId());
            if (targetNe == null) {
                throw new RhythmDataException(
                        "Target ID not found or not a slide note: " + ne.getNote().getTargetId()
                );
            }

            targetNe.setFirst(false);
            ne.setNext(targetNe);
            ne.setSlideDirection(SlideNoteHitRegistration.slideDirection(ne.getNote(), targetNe.getNote()));

            guides.add(new SlideNoteGuide(ne));
        }

        add(guides);

        for (SlideNoteEntity ne : slideNoteEntities) {
            if (ne.getNext() == null) {
                continue;
            }

            if (ne.getNext().getNext() == null) {
                ne.getNext().setSlideDirection(ne.getSlideDirection());
            }
        }
    }

    private void initNotes() {
        List<NoteEntity<?>> noteEntities = new ArrayList<>();

        int count = Play.BASE_NOTE_Z_INDEX;
        for (ScanlinePage sp : rhythmTimer.getPredictor().getPages()) {
            for (Note note : sp.getNotes()) {
                NoteEntity<?> noteEntity = null;
                if (note instanceof PressNote) {
                    noteEntity = new PressNoteEntity((PressNote) note, rhythmTimer, sp);
                } else if (note instanceof SlideNote) {
                    noteEntity = new SlideNoteEntity((SlideNote) note, rhythmTimer, sp);
                } else if (note instanceof HoldNote) {
                    HoldNote holdNote = (HoldNote) note;
                    if (holdNote.getEndTime() < sp.getEndTime()) {
                        noteEntity = new HoldNoteEntity(holdNote, rhythmTimer, sp);
                    }
                }

                if (noteEntity != null) {
                    noteEntity.setZ(count--);
                    noteEntities.add(noteEntity);
                }
            }
        }

        initSlideNoteNext(noteEntities.stream()
                .filter(ne -> ne instanceof SlideNoteEntity)
                .map(ne -> (SlideNoteEntity) ne)
                .collect(Collectors.toList())
        );

        for (NoteManager nm : noteManagers) {
            nm.initNoteEntities(noteEntities);
        }

        add(noteEntities);
    }

    private void initEntities() {
        StaticCoverImageBackground bg = new StaticCoverImageBackground();
        bg.setImageFrom(Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getSong()
                .getCoverImageFile()
        );
        add(bg);

        music = new Music();
        add(music);

        SongTitleText songTitleText = new SongTitleText();
        add(songTitleText);

        DifficultyText difficultyText = new DifficultyText();
        add(difficultyText);

        ScoreText scoreText = new ScoreText();
        add(scoreText);

        ComboText comboText = new ComboText();
        add(comboText);

        ReadyText readyText = new ReadyText();
        readyText.setTimer(readyTimer);
        add(readyText);

        ScanlineBorder scanlineBorder = new ScanlineBorder();
        add(scanlineBorder);

        scanline = new Scanline();
        scanline.setChart(chart);
        scanline.setRhythmTimer(rhythmTimer);
        add(scanline);

        initNotes();
    }

    private void fadeIn() {
        for (Entity entity : getEntities()) {
            if (entity instanceof EntryExitTransitionalEntity) {
                EntryExitTransitionalEntity fte = (EntryExitTransitionalEntity) entity;
                fte.fadeIn();
            }
        }
    }

    private void fadeOut() {
        for (Entity entity : getEntities()) {
            if (entity instanceof EntryExitTransitionalEntity) {
                EntryExitTransitionalEntity fte = (EntryExitTransitionalEntity) entity;
                fte.fadeOut();
            }
        }
    }

    @Override
    public void load() {
        super.load();

        if (Game.instance().rhythmSessionManager().getSession() == null) {
            throw new IllegalStateException("No rhythm session set");
        }

        initTimers();
        loadChart();
        initNoteManagers();
        initEntities();
        fadeIn();

        readyTimer.start();
    }

    private boolean isMusicShouldBeFinished() {
        return rhythmTimer.getTime() >= chart.getLength();
    }

    /**
     * Synchronize rhythm timer with music time.
     * VLCJ does not provide a high resolution timer.
     *
     * @see <a href="https://github.com/caprica/vlcj/issues/74">https://github.com/caprica/vlcj/issues/74</a>
     */
    private void syncMusicTime(float deltaTime) {
        if (isMusicShouldBeFinished()) {
            return;
        }

        // Use the provided deltaTime when the music is not playing.
        if (!music.isPlaying()) {
            rhythmTimer.setTime(rhythmTimer.getTime() + deltaTime);
            return;
        }

        long musicTime = music.getTime();

        if (lastMusicTime != musicTime) {
            lastTime = System.nanoTime();
            lastMusicTime = musicTime;
        } else {
            musicTime += Math.round((System.nanoTime() - lastTime) * 1e-6F);
        }

        rhythmTimer.setTime(musicTime * 0.001F);
    }

    private void checkForScanlineFadeout() {
        if (!scanline.isFadingOut() && rhythmTimer.getTime() >= (chart.getLength() - chart.getEndMargin())) {
            scanline.done();
        }
    }

    private void checkForMusicFinish() {
        if (isMusicShouldBeFinished() && !finished) {
            music.stop();
            exitTransitionDelayTimer.start();
            exitTransitionTimer.start();
            finished = true;
        }
    }

    private void syncTimeEntities(float deltaTime) {
        if (readyTimer.isDone()) {
            syncMusicTime(deltaTime);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        readyTimer.update(deltaTime);
        musicPlayDelayTimer.update(deltaTime);
        exitTransitionDelayTimer.update(deltaTime);
        exitTransitionTimer.update(deltaTime);

        syncTimeEntities(deltaTime);
        checkForScanlineFadeout();
        checkForMusicFinish();
    }
}
