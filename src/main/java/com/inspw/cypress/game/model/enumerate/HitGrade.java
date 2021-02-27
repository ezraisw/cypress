package com.inspw.cypress.game.model.enumerate;

public enum HitGrade {
    MISS("Miss", "Miss", 0),
    BAD("Bad", "Bad", 300000),
    GOOD("Good", "Good", 700000),
    GREAT("Perfect", "Perfect", 900000),
    PERFECT("Full Perfect", "Perfect", 1000000);

    private final String name;
    private final String inGameName;
    private final int score;

    HitGrade(String name, String inGameName, int score) {
        this.name = name;
        this.inGameName = inGameName;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getInGameName() {
        return inGameName;
    }

    @Override
    public String toString() {
        return "HitGrade{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
