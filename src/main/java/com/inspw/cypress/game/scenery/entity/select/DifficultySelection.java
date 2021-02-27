package com.inspw.cypress.game.scenery.entity.select;

import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.model.Difficulty;
import com.inspw.cypress.game.model.Song;
import com.inspw.cypress.game.scenery.entity.general.BinaryTransitionalEntity;

import java.awt.*;
import java.awt.geom.Path2D;

public class DifficultySelection extends BinaryTransitionalEntity {
    private static final int TEXT_LENGTH_LIMIT = 15;
    private static final int MARGIN_TOP = 60;
    private static final int MARGIN_LEFT = 50;
    private static final int NAME_MARGIN_TOP = 5;
    private static final String GUIDE_TEXT = "\u25C0  DIFFICULTY  \u25B6";
    private static final int GUIDE_MARGIN_TOP = 5;
    private static final int GUIDE_FONT_SIZE = 13;
    private static final int DIFFICULTY_FONT_SIZE = 40;
    private static final int NAME_FONT_SIZE = 25;
    private static final float DISPLAYING_DURATION = 0.2F;
    private static final float HIDING_DURATION = 0.1F;
    private static final float HIDDEN_DURATION = 1.0F;
    private static final float EFFECT_TRAVEL_LENGTH = 100F;
    private static final float EFFECT_DURATION = 0.3F;
    private static final float DISABLED_ALPHA = 0.2F;
    private final SimpleTimer effectTimer;
    private Song newSong;
    private Song song;
    private int difficultyIndex = -1;

    public DifficultySelection() {
        super(DISPLAYING_DURATION, HIDING_DURATION, -1, HIDDEN_DURATION);
        effectTimer = new SimpleTimer(EFFECT_DURATION);
    }

    public int getDifficultyIndex() {
        return difficultyIndex;
    }

    public void setDifficultyIndex(int difficultyIndex) {
        this.difficultyIndex = difficultyIndex;
    }

    public Song getSong() {
        return song;
    }

    public void newSong(Song song) {
        this.newSong = song;
        switchToDisabled();
    }

    public boolean left() {
        if (newSong != song || getState() != State.ACTIVE) {
            return false;
        }

        int oldIndex = difficultyIndex;

        difficultyIndex = difficultyIndex <= 0
                ? newSong.getDifficulties().size() - 1
                : difficultyIndex - 1;

        if (oldIndex == difficultyIndex) {
            return false;
        }

        effectTimer.restart();
        return true;
    }

    public boolean right() {
        if (newSong != song || getState() != State.ACTIVE) {
            return false;
        }

        int oldIndex = difficultyIndex;

        difficultyIndex = difficultyIndex >= newSong.getDifficulties().size() - 1
                ? 0
                : difficultyIndex + 1;

        if (oldIndex == difficultyIndex) {
            return false;
        }

        effectTimer.restart();
        return true;
    }

    @Override
    public void load() {
        super.load();
        effectTimer.end();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        effectTimer.update(deltaTime);
    }

    @Override
    protected void onIntoActive() {
        super.onIntoActive();

        if (newSong != null) {
            if (difficultyIndex < 0) {
                difficultyIndex = 0;
            } else if (difficultyIndex >= newSong.getDifficulties().size()) {
                difficultyIndex = newSong.getDifficulties().size() - 1;
            }

            song = newSong;
        }
    }

    private boolean hasValidDifficulty() {
        return difficultyIndex >= 0 && difficultyIndex < getSong().getDifficulties().size();
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (getSong() == null) {
            return;
        }

        g.setColor(new Color(1, 1, 1, value()));

        Font difficultyFont = new Font("Consolas", Font.BOLD, DIFFICULTY_FONT_SIZE);
        Font nameFont = new Font("SansSerif", Font.PLAIN, NAME_FONT_SIZE);

        FontMetrics difficultyMetrics = g.getFontMetrics(difficultyFont);
        FontMetrics nameMetrics = g.getFontMetrics(nameFont);

        float difficultyY = MARGIN_TOP;
        float nameY = difficultyY + (float) difficultyMetrics.getHeight() / 2 + NAME_MARGIN_TOP;
        if (hasValidDifficulty()) {
            Difficulty difficulty = getSong().getDifficulties().get(difficultyIndex);

            g.setFont(difficultyFont);
            String difficultyText = Integer.toString(difficulty.getChart().getLevel());
            float difficultyWidth = difficultyMetrics.stringWidth(difficultyText);
            g.drawString(difficultyText, MARGIN_LEFT, MARGIN_TOP);

            g.setFont(nameFont);
            String nameText = difficulty.getChart().getName().toUpperCase();

            if (nameText.length() > TEXT_LENGTH_LIMIT) {
                nameText = nameText.substring(0, TEXT_LENGTH_LIMIT - 3).concat("...");
            }

            float nameWidth = nameMetrics.stringWidth(nameText);
            g.drawString(nameText, MARGIN_LEFT, nameY);

            float farthest = Math.max(difficultyWidth, nameWidth);

            float lineX = MARGIN_LEFT + farthest + effectTimer.getProgress() * EFFECT_TRAVEL_LENGTH;
            Path2D.Float lineEffectPath = new Path2D.Float();
            lineEffectPath.moveTo(lineX, difficultyY - (float) difficultyMetrics.getHeight() / 2);
            lineEffectPath.lineTo(lineX, nameY);

            float lineAlpha = MathHelper.clamp(1 - effectTimer.getProgress(), 0, 1);
            g.setColor(new Color(1, 1, 1, lineAlpha * value()));
            g.draw(lineEffectPath);
        }

        float guideY = nameY + (float) nameMetrics.getHeight() / 2 + GUIDE_MARGIN_TOP;
        g.setFont(new Font("SansSerif", Font.BOLD, GUIDE_FONT_SIZE));

        if (!hasValidDifficulty() || getSong().getDifficulties().size() <= 1) {
            g.setColor(new Color(1, 1, 1, DISABLED_ALPHA * value()));
        } else {
            g.setColor(new Color(1, 1, 1, value()));
        }

        String guideText = GUIDE_TEXT;
        g.drawString(guideText, MARGIN_LEFT, guideY);
    }
}
