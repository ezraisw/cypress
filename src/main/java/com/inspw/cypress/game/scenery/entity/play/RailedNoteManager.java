package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Rhythm;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.rhythm.RhythmSession;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.scenery.entity.play.sub.Rail;
import com.inspw.cypress.game.ui.input.KeyCode;
import org.intellij.lang.annotations.MagicConstant;

import java.util.List;
import java.util.stream.Collectors;

public class RailedNoteManager extends NoteManager {
    private final Rail[] rails;
    private int lRailCount;
    private int rRailCount;

    public RailedNoteManager(RhythmSession rhythmSession, RhythmTimer rhythmTimer, int railCount) {
        super(rhythmSession, rhythmTimer);
        validateRailCount(railCount);
        rails = new Rail[railCount];
        calculateCountBounds();
    }

    private void validateRailCount(int railCount) {
        if (railCount <= 0 || railCount > Rhythm.RAILED_TOTAL_KEYS) {
            throw new IllegalArgumentException("Invalid railCount");
        }
    }

    private void repositionEntities(List<RailedNoteEntity<?>> railedNoteEntities, int idx) {
        float width = (Rhythm.POS_MAX - Rhythm.POS_MIN) / getRailCount();
        float baseX = (idx * width + width / 2) - 100;
        float paddedWidth = width - width * Rhythm.RAIL_PADDING;
        for (RailedNoteEntity<?> ne : railedNoteEntities) {
            float x = baseX + (ne.getNote().getShiftX() / 100) * paddedWidth;
            float y = getRhythmTimer().getPredictor()
                    .positionAt(ne.getNote().getTime(), ne.getCurrSp());
            ne.setPosition(new Vector2(x, y));
        }
    }

    public void initNoteEntities(List<NoteEntity<?>> noteEntities) {
        for (int i = 0; i < getRailCount(); i++) {
            int idx = i;
            List<RailedNoteEntity<?>> railedNoteEntities = noteEntities.stream()
                    .filter(ne -> ne instanceof RailedNoteEntity<?>)
                    .map(ne -> (RailedNoteEntity<?>) ne)
                    .filter(ne -> ne.getNote().getRail() == idx)
                    .collect(Collectors.toList());

            repositionEntities(railedNoteEntities, i);
            rails[i] = new Rail(getRhythmSession(), railedNoteEntities);
        }
    }

    private void calculateCountBounds() {
        float halfRailCount = (float) getRailCount() / 2;
        lRailCount = getRailCount() - (int) Math.ceil(halfRailCount);
        rRailCount = getRailCount() - (int) Math.floor(halfRailCount);
    }

    public int getRailCount() {
        return rails.length;
    }

    @MagicConstant(valuesFromClass = KeyCode.class)
    public int indexToKey(int index) {
        if (index < 0 || index > getRailCount()) {
            throw new IllegalArgumentException("Invalid index");
        }

        if (index < lRailCount) {
            return Rhythm.RAILED_LEFT_KEYS[index];
        } else {
            return Rhythm.RAILED_RIGHT_KEYS[index - lRailCount + (Rhythm.RAILED_RIGHT_KEYS.length - rRailCount)];
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float time = getRhythmTimer().getTime();
        for (int i = 0; i < rails.length; i++) {
            Rail rail = rails[i];
            rail.update(time);

            int key = indexToKey(i);
            if (Game.instance().input().isKeyDown(key)) {
                rail.down(time);
            }

            if (Game.instance().input().isKeyUp(key)) {
                rail.up(time);
            }
        }
    }
}
