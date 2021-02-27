package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.util.Random;

public class ScoreText extends EntryExitTransitionalEntity {
    private static final int FONT_SIZE = 45;
    private static final int MARGIN_TOP = 55;
    private static final int MARGIN_RIGHT = 35;
    private static final float FADE_IN_DURATION = 1.5F;
    private static final float FADE_OUT_DURATION = 1F;
    private static final float RANDOM_TYPING_DURATION = 0.08F;
    private static final float DIGITS = 6;

    private final Random rng;
    private final SimpleTimer randomTypingTimer;
    private String digitText = "";

    public ScoreText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
        rng = new Random();
        randomTypingTimer = new SimpleTimer(RANDOM_TYPING_DURATION, SimpleTimer.FinishType.REPEAT);
        randomTypingTimer.addFinishListener(this::randomizeDigitText);
    }

    private int getScore() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getRoundedScore();
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
        randomTypingTimer.stop();
    }

    private void randomizeDigitText() {
        StringBuilder builder = new StringBuilder();
        int number = rng.nextInt(10);
        for (int i = 0; i < DIGITS; i++) {
            builder.append(number);
        }
        digitText = builder.toString();
        randomTypingTimer.restart();
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        g.setFont(new Font("Consolas", Font.PLAIN, FONT_SIZE));
        FontMetrics metrics = g.getFontMetrics();
        String text = isFadeInDone()
                ? String.format("%06d", getScore())
                : digitText;

        float width = metrics.stringWidth(text);
        g.setColor(new Color(1, 1, 1, (1 - fadeOutValue())));
        g.drawString(text, rs().getWidth() - MARGIN_RIGHT - width, MARGIN_TOP);
    }
}
