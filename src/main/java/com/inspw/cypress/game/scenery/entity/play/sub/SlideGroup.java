package com.inspw.cypress.game.scenery.entity.play.sub;

import com.inspw.cypress.game.constant.Rhythm;
import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.enumerate.SlideDirection;
import com.inspw.cypress.game.model.logic.note.SlideNoteHitRegistration;
import com.inspw.cypress.game.model.serialized.SlideNote;
import com.inspw.cypress.game.rhythm.RhythmSession;
import com.inspw.cypress.game.scenery.entity.play.SlideNoteEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SlideGroup {
    private final RhythmSession rhythmSession;
    private final List<Mapping> mappings;
    private float leftBuffer;
    private float rightBuffer;
    public SlideGroup(RhythmSession rhythmSession, List<SlideNoteEntity> noteEntities) {
        this.rhythmSession = rhythmSession;
        this.mappings = noteEntities.stream()
                .sorted((ne1, ne2) -> Float.compare(ne1.getNote().getTime(), ne2.getNote().getTime()))
                .map(ne -> new Mapping(ne, ne.getNote().createHitRegistration()))
                .collect(Collectors.toList());
    }

    private float updateBuffer(float prevBuffer, float deltaTime) {
        float newBuffer = prevBuffer - deltaTime;
        if (newBuffer > 0) {
            return newBuffer;
        } else {
            return 0;
        }
    }

    public void update(float time, float deltaTime) {
        leftBuffer = updateBuffer(leftBuffer, deltaTime);
        rightBuffer = updateBuffer(rightBuffer, deltaTime);
        check(time);
    }

    private boolean isLeftActive() {
        return leftBuffer > 0;
    }

    private boolean isRightActive() {
        return rightBuffer > 0;
    }

    private void check(float time) {
        for (Mapping m : mappings) {
            checkForMiss(m, time);
            checkForPass(m, time);
        }
    }

    private void checkForMiss(Mapping mapping, float time) {
        if (!mapping.isHit() && mapping.getHitRegistration().shouldMiss(time)) {
            mapping.miss(rhythmSession);
        }
    }

    private void checkForPass(Mapping mapping, float time) {
        if (!mapping.isHit() && mapping.getHitRegistration().shouldBeHit(time)) {
            SlideNote n1 = mapping.getNoteEntity().getNote();
            SlideNote n2 = mapping.getNoteEntity().getNext() == null
                    ? null
                    : mapping.getNoteEntity().getNext().getNote();

            SlideDirection sd = SlideNoteHitRegistration.slideDirection(n1, n2);

            boolean neutral = sd == null && (isLeftActive() || isRightActive());
            boolean left = sd == SlideDirection.LEFT && isLeftActive();
            boolean right = sd == SlideDirection.RIGHT && isRightActive();

            if (neutral || left || right) {
                mapping.pass(rhythmSession, time);
            }
        }
    }

    public float getLeftValue() {
        return MathHelper.clamp(leftBuffer / Rhythm.SLIDE_BUFFER_DURATION, 0, 1);
    }

    public float getRightValue() {
        return MathHelper.clamp(rightBuffer / Rhythm.SLIDE_BUFFER_DURATION, 0, 1);
    }

    public void pressingLeft() {
        leftBuffer = Rhythm.SLIDE_BUFFER_DURATION;
    }

    public void pressingRight() {
        rightBuffer = Rhythm.SLIDE_BUFFER_DURATION;
    }

    private static class Mapping {
        private final SlideNoteEntity noteEntity;
        private final SlideNoteHitRegistration hitRegistration;

        public Mapping(SlideNoteEntity noteEntity, SlideNoteHitRegistration hitRegistration) {
            this.noteEntity = noteEntity;
            this.hitRegistration = hitRegistration;
        }

        public SlideNoteEntity getNoteEntity() {
            return noteEntity;
        }

        public SlideNoteHitRegistration getHitRegistration() {
            return hitRegistration;
        }

        public SlideNote getNote() {
            return getHitRegistration().getNote();
        }

        public boolean isHit() {
            return getHitRegistration().isHit();
        }

        public void setHit(boolean hit) {
            getHitRegistration().setHit(hit);
            getNoteEntity().setHit(hit);
        }

        private void pass(RhythmSession rhythmSession, float time) {
            HitGrade grade = getHitRegistration().pass(time);
            if (grade != null) {
                for (int i = 0; i < getNote().expectedCount(); i++) {
                    rhythmSession.hitNote(grade);
                }
                getNoteEntity().blow(grade);
                setHit(true);
            }
        }

        private void miss(RhythmSession rhythmSession) {
            for (int i = 0; i < getNote().expectedCount(); i++) {
                rhythmSession.hitNote(HitGrade.MISS);
            }
            getNoteEntity().miss();
            setHit(true);
        }
    }
}
