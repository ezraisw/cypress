package com.inspw.cypress.game.scenery.entity.play;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.rhythm.RhythmSession;
import com.inspw.cypress.game.rhythm.RhythmTimer;
import com.inspw.cypress.game.scenery.entity.play.sub.SlideGroup;
import com.inspw.cypress.game.ui.input.KeyCode;

import java.util.List;
import java.util.stream.Collectors;

public class SlideNoteManager extends NoteManager {
    private static final int[][] SLIDE_GROUPS_KEYS = {
            {KeyCode.VK_C, KeyCode.VK_V},
            {KeyCode.VK_COMMA, KeyCode.VK_PERIOD}
    };

    private final SlideGroup[] slideGroups;

    public SlideNoteManager(RhythmSession rhythmSession, RhythmTimer rhythmTimer, int slideGroupCount) {
        super(rhythmSession, rhythmTimer);
        validateSlideGroupCount(slideGroupCount);
        slideGroups = new SlideGroup[slideGroupCount];
    }

    private void validateSlideGroupCount(int slideGroupCount) {
        if (slideGroupCount <= 0 || slideGroupCount > SLIDE_GROUPS_KEYS.length) {
            throw new IllegalArgumentException("Invalid slideGroupCount");
        }
    }

    private void repositionEntities(List<SlideNoteEntity> slideNoteEntities) {
        for (SlideNoteEntity ne : slideNoteEntities) {
            float y = getRhythmTimer().getPredictor()
                    .positionAt(ne.getNote().getTime(), ne.getCurrSp());
            ne.setPosition(new Vector2(ne.getNote().getX(), y));
        }
    }

    public void initNoteEntities(List<NoteEntity<?>> noteEntities) {
        for (int i = 0; i < getSlideGroupCount(); i++) {
            int idx = i;
            List<SlideNoteEntity> slideNoteEntities = noteEntities.stream()
                    .filter(ne -> ne instanceof SlideNoteEntity)
                    .map(ne -> (SlideNoteEntity) ne)
                    .filter(ne -> ne.getNote().getSlideGroup() == idx)
                    .collect(Collectors.toList());

            repositionEntities(slideNoteEntities);
            slideGroups[i] = new SlideGroup(getRhythmSession(), slideNoteEntities);
        }
    }

    public int getSlideGroupCount() {
        return slideGroups.length;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (int i = 0; i < getSlideGroupCount(); i++) {
            SlideGroup group = slideGroups[i];
            group.update(getRhythmTimer().getTime(), deltaTime);

            // Keep players from pressing both buttons for cheese.
            if (Game.instance().input().isKeyPressed(SLIDE_GROUPS_KEYS[i][0])) {
                group.pressingLeft();
            } else if (Game.instance().input().isKeyPressed(SLIDE_GROUPS_KEYS[i][1])) {
                group.pressingRight();
            }
        }
    }
}
