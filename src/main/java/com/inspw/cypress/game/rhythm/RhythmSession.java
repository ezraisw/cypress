package com.inspw.cypress.game.rhythm;

import com.inspw.cypress.game.constant.Rhythm;
import com.inspw.cypress.game.model.Difficulty;
import com.inspw.cypress.game.model.Song;
import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.model.serialized.Note;

import java.util.HashMap;
import java.util.Map;

public class RhythmSession {
    private final Map<HitGrade, Integer> hitCounts;
    private Song song;
    private int difficultyIndex;
    private float score;
    private int combo, maxCombo, possibleMaxCombo;
    private int totalHitCount;

    public RhythmSession(Song song, int difficultyIndex) {
        this.song = song;
        this.difficultyIndex = difficultyIndex;
        this.hitCounts = new HashMap<>();
        initHitCounts();
        initPossibleMaxCombo();
    }

    public static String getGrade(int score) {
        if (score == Rhythm.MAX_SCORE) {
            return "MX";
        } else if (score >= Rhythm.S_GRADE_SCORE) {
            return "S";
        } else if (score >= Rhythm.A_GRADE_SCORE) {
            return "A";
        } else if (score >= Rhythm.B_GRADE_SCORE) {
            return "B";
        } else if (score >= Rhythm.C_GRADE_SCORE) {
            return "C";
        } else if (score >= Rhythm.D_GRADE_SCORE) {
            return "D";
        } else {
            return "F";
        }
    }

    private void initHitCounts() {
        for (HitGrade grade : HitGrade.values()) {
            hitCounts.put(grade, 0);
        }
    }

    private void initPossibleMaxCombo() {
        for (Note note : getDifficulty().getChart().getNotes()) {
            possibleMaxCombo += note.expectedCount();
        }
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getDifficultyIndex() {
        return difficultyIndex;
    }

    public void setDifficultyIndex(int difficultyIndex) {
        this.difficultyIndex = difficultyIndex;
    }

    public Difficulty getDifficulty() {
        return getSong().getDifficulties().get(difficultyIndex);
    }

    public int getRoundedScore() {
        return Math.round(score);
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        if (combo > maxCombo) {
            maxCombo = combo;
        }

        this.combo = combo;
    }

    public int getPossibleMaxCombo() {
        return possibleMaxCombo;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public void addCombo(int addition) {
        this.combo += addition;
        if (combo > getMaxCombo()) {
            maxCombo = combo;
        }
    }

    public void resetCombo() {
        combo = 0;
    }

    public int getHitCount(HitGrade grade) {
        return hitCounts.get(grade);
    }

    public void hitNote(HitGrade grade) {
        if (grade != HitGrade.MISS && grade != HitGrade.BAD) {
            addCombo(1);
        }

        int pmc = getPossibleMaxCombo();
        float songConst = (2F * grade.getScore()) / (pmc * (pmc + 1));
        score += songConst * getCombo();
        totalHitCount++;
        hitCounts.put(grade, hitCounts.get(grade) + 1);

        if (grade == HitGrade.MISS || grade == HitGrade.BAD) {
            resetCombo();
        }
    }

    public int getTotalHitCount() {
        return totalHitCount;
    }

    public boolean isAllPerfectOrGreat() {
        return (getHitCount(HitGrade.MISS) +
                getHitCount(HitGrade.BAD) +
                getHitCount(HitGrade.GOOD)
        ) <= 0;
    }

    public boolean isAllPerfectOrGreatOrGood() {
        return (getHitCount(HitGrade.MISS) +
                getHitCount(HitGrade.BAD)
        ) <= 0;
    }
}
