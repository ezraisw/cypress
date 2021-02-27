package com.inspw.cypress.game.scenery.entity.play.sub;

import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.logic.note.RailedNoteHitRegistration;
import com.inspw.cypress.game.model.serialized.HoldNote;
import com.inspw.cypress.game.model.serialized.RailedNote;
import com.inspw.cypress.game.rhythm.RhythmSession;
import com.inspw.cypress.game.scenery.entity.play.RailedNoteEntity;

import java.util.List;
import java.util.stream.Collectors;

public class Rail {
    private final RhythmSession rhythmSession;
    private final List<Mapping> mappings;
    private boolean pressed;

    public Rail(RhythmSession rhythmSession, List<RailedNoteEntity<?>> noteEntities) {
        this.rhythmSession = rhythmSession;
        this.mappings = noteEntities.stream()
                .sorted((ne1, ne2) -> Float.compare(ne1.getNote().getTime(), ne2.getNote().getTime()))
                .map(ne -> new Mapping(ne, ne.getNote().createHitRegistration()))
                .collect(Collectors.toList());
    }

    public void reset() {
        for (Mapping m : mappings) {
            m.reset();
        }
    }

    public void update(float time) {
        check(time);
    }

    private void check(float time) {
        for (Mapping m : mappings) {
            checkForMissAndPass(m, time);
        }
    }

    private void checkForMissAndPass(Mapping mapping, float time) {
        if (!mapping.isHit()) {
            if (mapping.getHitRegistration().shouldCheckForUp(time)) {
                if (!pressed || mapping.getHitRegistration().shouldRelease(time)) {
                    mapping.up(rhythmSession, time);
                }
            } else if (mapping.getHitRegistration().shouldMiss(time)) {
                mapping.miss(rhythmSession);
            }
        }
    }

    public void down(float time) {
        pressed = true;
        for (Mapping m : mappings) {
            if (!m.isHit() && m.canBeHit(time)) {
                m.down(rhythmSession, time);
                break;
            }
        }
    }

    public void up(float time) {
        pressed = false;
    }

    private static class Mapping {
        private final RailedNoteEntity<?> noteEntity;
        private final RailedNoteHitRegistration<?> hitRegistration;
        private int count;

        public Mapping(RailedNoteEntity<?> noteEntity, RailedNoteHitRegistration<?> hitRegistration) {
            this.noteEntity = noteEntity;
            this.hitRegistration = hitRegistration;
        }

        public RailedNoteEntity<?> getNoteEntity() {
            return noteEntity;
        }

        public RailedNoteHitRegistration<?> getHitRegistration() {
            return hitRegistration;
        }

        public RailedNote getNote() {
            return getHitRegistration().getNote();
        }

        public boolean isHit() {
            return getHitRegistration().isHit();
        }

        public void reset() {
            getHitRegistration().reset();
            getNoteEntity().reset();
        }

        public void setHit(boolean hit) {
            getHitRegistration().setHit(hit);
            getNoteEntity().setHit(hit);
        }

        public boolean canBeHit(float time) {
            return !isHit() && getNoteEntity().getCurrSp().hasAppeared(getNote(), time);
        }

        private void checkForHit() {
            if (!getHitRegistration().isHit() && count >= getNote().expectedCount()) {
                setHit(true);
            }
        }

        private void down(RhythmSession rhythmSession, float time) {
            HitGrade grade = getHitRegistration().down(time);
            if (grade != null) {
                rhythmSession.hitNote(grade);
                getNoteEntity().down(grade);
                count++;
            }
            checkForHit();
        }

        private void up(RhythmSession rhythmSession, float time) {
            HitGrade grade = getHitRegistration().up(time);
            if (grade != null) {
                rhythmSession.hitNote(grade);
                getNoteEntity().up(grade);
                count++;
            }
            checkForHit();
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
