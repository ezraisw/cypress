package com.inspw.cypress.game.scenery.entity.select;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.model.Song;
import com.inspw.cypress.game.model.serialized.BestResult;
import com.inspw.cypress.game.rhythm.RhythmSession;
import com.inspw.cypress.game.scenery.entity.general.BinaryTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class HighScoreText extends BinaryTransitionalEntity {
    private static final int SCORE_FONT_SIZE = 40;
    private static final int GRADE_FONT_SIZE = 55;
    private static final int MARGIN_TOP = 60;
    private static final int GRADE_MARGIN_TOP = 5;
    private static final int NEW_FONT_SIZE = 30;
    private static final String NEW_TEXT = "NEW";
    private static final float DISPLAYING_DURATION = 0.2F;
    private static final float HIDING_DURATION = 0.1F;
    private static final float HIDDEN_DURATION = 1.0F;

    private Song newSong;
    private Song song;

    public HighScoreText() {
        super(DISPLAYING_DURATION, HIDING_DURATION, -1, HIDDEN_DURATION);
    }

    public Song getSong() {
        return song;
    }

    public void newSong(Song song) {
        this.newSong = song;
        switchToDisabled();
    }

    private int getHighScore() {
        if (getSong() == null) {
            return -1;
        }

        String songId = getSong().getSongInfo().getId();
        BestResult result = Game.instance()
                .profileManager()
                .getProfile()
                .getBestResults()
                .get(songId);

        if (result == null) {
            return -1;
        }

        return result.getScore();
    }

    @Override
    protected void onIntoActive() {
        super.onIntoActive();
        song = newSong;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (getSong() == null) {
            return;
        }

        Vector2 center = rs().getCenter();

        boolean newResult = false;
        int score = getHighScore();
        if (score == -1) {
            newResult = true;
            score = 0;
        }

        String scoreText = String.format("%06d", score);
        g.setFont(new Font("Consolas", Font.PLAIN, SCORE_FONT_SIZE));

        FontMetrics scoreMetrics = g.getFontMetrics();
        float scoreX = center.getX() - (float) scoreMetrics.stringWidth(scoreText) / 2;
        float scoreY = MARGIN_TOP;

        g.setColor(new Color(1, 1, 1, value()));
        g.drawString(scoreText, scoreX, scoreY);

        String gradeText = newResult ? NEW_TEXT : RhythmSession.getGrade(score);

        g.setFont(new Font("SansSerif", Font.BOLD, newResult ? NEW_FONT_SIZE : GRADE_FONT_SIZE));
        FontMetrics gradeMetrics = g.getFontMetrics();
        float gradeX = center.getX() - (float) gradeMetrics.stringWidth(gradeText) / 2;
        float gradeY = scoreY + (float) scoreMetrics.getHeight() + GRADE_MARGIN_TOP;

        g.drawString(gradeText, gradeX, gradeY);
    }
}
