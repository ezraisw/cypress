package com.inspw.cypress.game.scenery.scene;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.model.serialized.BestResult;
import com.inspw.cypress.game.rhythm.RhythmSession;
import com.inspw.cypress.game.scenery.Entity;
import com.inspw.cypress.game.scenery.Scene;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.scenery.entity.general.StaticCoverImageBackground;
import com.inspw.cypress.game.scenery.entity.result.*;
import com.inspw.cypress.game.ui.input.KeyCode;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class ResultScene extends Scene {
    private static final float EXIT_TRANSITION_DURATION = 1F;

    private boolean shouldFadeToBlack;
    private SimpleTimer exitTransitionTimer;

    private void initEntities() {
        RhythmSession session = Game.instance().rhythmSessionManager().getSession();
        if (session == null) {
            throw new IllegalStateException("No rhythm session set");
        }

        StaticCoverImageBackground bg = new StaticCoverImageBackground();
        bg.setImageFrom(session.getSong().getCoverImageFile());
        add(bg);

        SongInfoText songInfoText = new SongInfoText();
        add(songInfoText);

        GradeText gradeText = new GradeText();
        add(gradeText);

        HitGradeCountText hitGradeCountText = new HitGradeCountText();
        add(hitGradeCountText);

        MaxComboText maxComboText = new MaxComboText();
        add(maxComboText);

        ScoreText scoreText = new ScoreText();
        add(scoreText);

        KeyInfoText keyInfoText = new KeyInfoText();
        add(keyInfoText);
    }

    private void initTimers() {
        exitTransitionTimer = new SimpleTimer(EXIT_TRANSITION_DURATION);
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

    private void saveResult() {
        RhythmSession session = Game.instance().rhythmSessionManager().getSession();
        BestResult bestResult = new BestResult(session.getMaxCombo(), session.getRoundedScore());

        String songId = Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getSong()
                .getSongInfo()
                .getId();

        Game.instance()
                .profileManager()
                .getProfile()
                .adjustBestResult(songId, bestResult);

        Game.instance()
                .profileManager()
                .saveProfile();
    }

    @Override
    public void load() {
        super.load();

        saveResult();
        initEntities();
        initTimers();
        fadeIn();
    }

    private void toSelect() {
        Game.instance()
                .sceneManager()
                .changeTo("select");
    }

    private void toPlay() {
        Game.instance()
                .rhythmSessionManager()
                .renewSession();
        Game.instance()
                .sceneManager()
                .changeTo("play");
    }

    private void exit(SimpleTimer.FinishListener cb, boolean fadeToBlack) {
        if (!exitTransitionTimer.isStarted()) {
            exitTransitionTimer.addFinishListener(cb);
            exitTransitionTimer.start();

            if (!fadeToBlack) {
                fadeOut();
            } else {
                shouldFadeToBlack = true;
            }
        }
    }

    private void returnToList() {
        exit(this::toSelect, true);
    }

    private void retry() {
        exit(this::toPlay, false);
    }

    private void checkForReturnToListKeyPress() {
        if (Game.instance().input().isKeyDown(KeyCode.VK_ENTER)) {
            returnToList();
        }
    }

    private void checkForRetryKeyPress() {
        if (Game.instance().input().isKeyDown(KeyCode.VK_R)) {
            retry();
        }
    }

    private void checkForKeyPresses() {
        checkForReturnToListKeyPress();
        checkForRetryKeyPress();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForKeyPresses();
        exitTransitionTimer.update(deltaTime);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (shouldFadeToBlack) {
            g.setColor(new Color(0, 0, 0, exitTransitionTimer.getProgress()));
            g.fillRect(0, 0, rs().getWidth(), rs().getHeight());
        }
    }
}
