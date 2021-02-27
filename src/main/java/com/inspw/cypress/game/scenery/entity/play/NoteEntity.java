package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Play;
import com.inspw.cypress.game.misc.cache.ValueCache;
import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.serialized.Note;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.rhythm.ScanlinePage;
import com.inspw.cypress.game.scenery.Entity;
import com.inspw.cypress.game.scenery.entity.play.sub.BadNoteEffect;
import com.inspw.cypress.game.scenery.entity.play.sub.GoodNoteEffect;
import com.inspw.cypress.game.scenery.entity.play.sub.NoteEffect;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class NoteEntity<T extends Note> extends Entity {
    private static final float HIT_DURATION = 0.05F;
    private static final float BLINK_DURATION = 0.01F;
    private static final float BLINK_ANIMATION_PROGRESS_THRESHOLD = 1.1F;
    private static final float BLINK_ALPHA = 0.5F;

    private final T note;
    private final RhythmTimer rhythmTimer;
    private final ScanlinePage currSp;
    private final SimpleTimer blinkTimer;
    private final SimpleTimer hitTimer;
    private final List<NoteEffect> noteEffects;
    private final List<NoteEffect> toBeRemovedNoteEffects;
    private boolean blinking;
    private boolean hit;
    private Vector2 oldPosition;
    private Vector2 position;
    private ValueCache<Vector2> windowPosition;

    public NoteEntity(T note, RhythmTimer rhythmTimer, ScanlinePage currSp) {
        this.note = note;
        this.rhythmTimer = rhythmTimer;
        this.currSp = currSp;
        this.blinkTimer = new SimpleTimer(BLINK_DURATION, SimpleTimer.FinishType.REPEAT);
        this.blinkTimer.addFinishListener(this::blinkToggle);
        this.hitTimer = new SimpleTimer(HIT_DURATION);
        this.noteEffects = Collections.synchronizedList(new ArrayList<>());
        this.toBeRemovedNoteEffects = new ArrayList<>();
        this.position = new Vector2(0, 0);
        this.windowPosition = new ValueCache<>(this::cacheWindowPosition, 3);
    }

    public T getNote() {
        return note;
    }

    public RhythmTimer getRhythmTimer() {
        return rhythmTimer;
    }

    public ScanlinePage getCurrSp() {
        return currSp;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getWindowPosition() {
        return windowPosition.getValue(new Object[]{getPosition(), rs().getWidth(), rs().getHeight()});
    }

    private Vector2 cacheWindowPosition(Object[] deps) {
        Vector2 position = (Vector2) deps[0];
        return Game.instance().renderingSurface().toWindowPos(position, 0, Play.MARGIN_Y);
    }

    public boolean isBlinking() {
        return blinking;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        if (!hit) {
            hitTimer.reset();
        } else {
            hitTimer.start();
        }
        this.hit = hit;
    }

    private void blinkToggle() {
        blinking = !blinking;
    }

    protected float displayDuration() {
        return getCurrSp().getDisplayDuration();
    }

    protected float animationProgress() {
        float startTime = getNote().getTime() - displayDuration();
        float timeToStartTime = getRhythmTimer().getTime() - startTime;
        float progress = timeToStartTime / displayDuration();
        return Math.max(progress, 0);
    }

    public void reset() {
        resetEffects();
        setHit(false);
    }

    public void resetEffects() {
        noteEffects.clear();
    }

    protected void spawnEffect(Vector2 position, HitGrade grade) {
        NoteEffect noteEffect = null;
        switch (grade) {
            case PERFECT:
                noteEffect = new GoodNoteEffect(position, grade.getInGameName(), Color.YELLOW);
                break;
            case GREAT:
                noteEffect = new GoodNoteEffect(position, grade.getInGameName(), Color.WHITE);
                break;
            case GOOD:
                noteEffect = new GoodNoteEffect(position, grade.getInGameName(), Color.CYAN);
                break;
            case BAD:
            case MISS:
                noteEffect = new BadNoteEffect(position, grade.getInGameName());
                break;
        }

        if (noteEffect != null) {
            noteEffect.addFinishListener(this::onEffectFinish);
            noteEffects.add(noteEffect);
        }
    }

    private void onEffectFinish(NoteEffect noteEffect) {
        toBeRemovedNoteEffects.add(noteEffect);
    }

    protected float hitValue() {
        return hitTimer.getProgress();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (NoteEffect ne : noteEffects) {
            ne.update(deltaTime);
        }
        synchronized (noteEffects) {
            noteEffects.removeAll(toBeRemovedNoteEffects);
        }
        toBeRemovedNoteEffects.clear();

        if (!blinkTimer.isStarted() && animationProgress() > BLINK_ANIMATION_PROGRESS_THRESHOLD) {
            blinkTimer.start();
        }
        blinkTimer.update(deltaTime);
        hitTimer.update(deltaTime);
    }

    protected float baseAlpha() {
        return 1;
    }

    protected void renderOutlineCircle(Graphics2D g, Vector2 center, float diameter) {
        float radius = diameter / 2;
        float x = (float) center.getX() - radius;
        float y = (float) center.getY() - radius;

        Ellipse2D.Float ellipse = new Ellipse2D.Float(x, y, diameter, diameter);
        g.draw(ellipse);
    }

    protected void renderCircle(Graphics2D g, Vector2 center, float diameter) {
        float radius = diameter / 2;
        float x = (float) center.getX() - radius;
        float y = (float) center.getY() - radius;

        Ellipse2D.Float ellipse = new Ellipse2D.Float(x, y, diameter, diameter);
        g.fill(ellipse);
    }

    protected void renderOuterCircle(Graphics2D g, float diameter, float progress) {
        Vector2 p = getWindowPosition();

        float constX = progress - 1;
        float constY = MathHelper.clamp(-(constX * constX) + 1, 0, 1);

        g.setColor(new Color(1, 1, 1, (!isBlinking() ? 1 : BLINK_ALPHA) * baseAlpha()));
        renderCircle(g, p, diameter * constY);
    }

    protected void renderInnerCircle(Graphics2D g, Color color, float diameter, float progress) {
        Vector2 p = getWindowPosition();

        float constX = progress;
        float constY = MathHelper.clamp(constX * constX, 0, 1);

        g.setColor(new Color(
                (float) color.getRed() / 255,
                (float) color.getGreen() / 255,
                (float) color.getBlue() / 255,
                (!isBlinking() ? 1 : BLINK_ALPHA) * baseAlpha()
        ));
        renderCircle(g, p, diameter * constY);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        synchronized (noteEffects) {
            for (NoteEffect ne : noteEffects) {
                ne.render(g);
            }
        }
    }
}
