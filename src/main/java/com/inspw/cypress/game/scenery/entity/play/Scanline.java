package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.model.serialized.Chart;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.scenery.Entity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.awt.geom.Path2D;

public class Scanline extends Entity {
    private static final float MARGIN_TOP = 90;
    private static final float MARGIN_BOTTOM = 90;
    private static final float DEFAULT_START_DURATION = 0.75F;
    private static final float DEFAULT_END_DURATION = 0.75F;
    private static final float STROKE_SIZE = 2F;
    private static final int Z_INDEX = Integer.MAX_VALUE;
    private final SimpleTimer startTimer;
    private final SimpleTimer endTimer;
    private Chart chart;
    private RhythmTimer rhythmTimer;
    private boolean visible;

    public Scanline() {
        startTimer = new SimpleTimer(DEFAULT_START_DURATION);
        endTimer = new SimpleTimer(DEFAULT_END_DURATION);
        endTimer.addFinishListener(this::hide);
        setZ(Z_INDEX);
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public RhythmTimer getRhythmTimer() {
        return rhythmTimer;
    }

    public void setRhythmTimer(RhythmTimer rhythmTimer) {
        this.rhythmTimer = rhythmTimer;
    }

    public void begin() {
        startTimer.setDuration(getChart().getDelay());
        startTimer.start();
        visible = true;
    }

    public void done() {
        endTimer.setDuration(getChart().getEndMargin());
        endTimer.start();
    }

    public void hide() {
        visible = false;
    }

    public boolean isFadingIn() {
        return startTimer.isRunning();
    }

    public boolean isFadingOut() {
        return endTimer.isRunning();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        startTimer.update(deltaTime);
        endTimer.update(deltaTime);
    }

    private float lineMargin() {
        return rs().getWidth() *
                MathHelper.clamp(1 - startTimer.getProgress() + endTimer.getProgress(), 0, 1);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (!visible) {
            return;
        }

        float position = getRhythmTimer().currentPosition();
        float y = rs().yRange(position, MARGIN_TOP, MARGIN_BOTTOM);

        float margin = lineMargin() / 2;

        Path2D.Float path = new Path2D.Float();
        path.moveTo(margin, y);
        path.lineTo(rs().getWidth() - margin, y);

        g.setStroke(new BasicStroke(STROKE_SIZE));
        g.setColor(Color.WHITE);
        g.draw(path);
    }
}
