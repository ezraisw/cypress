package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.helper.StringHelper;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.model.Difficulty;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.util.Random;

public class DifficultyText extends EntryExitTransitionalEntity {
    private static final int MARGIN = 35;
    private static final int FONT_SIZE = 30;
    private static final float FADE_IN_DURATION = 1.5F;
    private static final float FADE_OUT_DURATION = 1F;
    private static final float RANDOM_TYPING_DURATION = 0.1F;

    private final Random rng;
    private final SimpleTimer randomTypingTimer;
    private String displayedText = "";

    public DifficultyText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
        rng = new Random();
        randomTypingTimer = new SimpleTimer(RANDOM_TYPING_DURATION, SimpleTimer.FinishType.REPEAT);
        randomTypingTimer.addFinishListener(this::randomizeDisplayedText);
    }

    @Override
    public void load() {
        super.load();
        randomTypingTimer.start();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        randomTypingTimer.update(deltaTime);
    }

    protected void onFadeInFinish() {
        Difficulty difficulty = Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getDifficulty();
        displayedText = String.format("%s [%d]", difficulty.getChart().getName(), difficulty.getChart().getLevel());
        randomTypingTimer.stop();
    }

    private void randomizeDisplayedText() {
        Difficulty difficulty = Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getDifficulty();
        String name = StringHelper.random(difficulty.getChart().getName().length());
        int level = rng.nextInt(difficulty.getChart().getLevel() * 2);
        displayedText = String.format("%s [%d]", name, level);
        randomTypingTimer.restart();
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        g.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE));
        FontMetrics metrics = g.getFontMetrics();

        String text = displayedText;
        float width = metrics.stringWidth(text);

        g.setColor(new Color(1, 1, 1, 1 - fadeOutValue()));
        g.drawString(text, rs().getWidth() - MARGIN - width, rs().getHeight() - MARGIN);
    }
}
