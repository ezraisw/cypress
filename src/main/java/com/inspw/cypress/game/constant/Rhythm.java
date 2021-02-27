package com.inspw.cypress.game.constant;

import com.inspw.cypress.game.ui.input.KeyCode;

public class Rhythm {
    /**
     * The key buffer duration for sliding.
     */
    public static final float SLIDE_BUFFER_DURATION = 0.2F;

    /**
     * Maximum amount of time before a note is considered missed.
     */
    public static final float MISS_DIFF_TIME = 0.5F;

    /* Grade-score threshold */
    public static final int MAX_SCORE = 1000000;
    public static final int S_GRADE_SCORE = 950000;
    public static final int A_GRADE_SCORE = 900000;
    public static final int B_GRADE_SCORE = 800000;
    public static final int C_GRADE_SCORE = 700000;
    public static final int D_GRADE_SCORE = 500000;

    public static final int[] RAILED_LEFT_KEYS = {
            KeyCode.VK_Q,
            KeyCode.VK_W,
            KeyCode.VK_E,
            KeyCode.VK_R
    };
    public static final int[] RAILED_RIGHT_KEYS = {
            KeyCode.VK_O,
            KeyCode.VK_P,
            KeyCode.VK_OPEN_BRACKET,
            KeyCode.VK_CLOSE_BRACKET
    };
    public static final int RAILED_TOTAL_KEYS = RAILED_LEFT_KEYS.length + RAILED_RIGHT_KEYS.length;
    public static final float RAIL_PADDING = 0.1F;

    public static final float POS_MIN = -100;
    public static final float POS_MAX = 100;

    private Rhythm() {
    }
}
