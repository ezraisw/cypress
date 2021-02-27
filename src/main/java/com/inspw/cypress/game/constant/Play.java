package com.inspw.cypress.game.constant;

public class Play {
    /**
     * The y margin for note placement in window.
     */
    public static final float MARGIN_Y = 90;

    /**
     * The duration of ready text.
     */
    public static final float READY_DURATION = 3.0F;

    /**
     * Default music play delay when no data has been loaded.
     */
    public static final float DEFAULT_MUSIC_PLAY_DELAY_DURATION = 0.75F;

    /**
     * Fade out delay after finishing.
     */
    public static final float EXIT_TRANSITION_DELAY_DURATION = 1F;

    /**
     * Fade out duration after finishing.
     */
    public static final float EXIT_TRANSITION_DURATION = 2F;

    /**
     * The total rail count for {@link com.inspw.cypress.game.scenery.entity.play.RailedNoteManager}.
     */
    public static final int RAIL_COUNT = 6;

    /**
     * The total slide group count for {@link com.inspw.cypress.game.scenery.entity.play.SlideNoteManager}.
     */
    public static final int SLIDE_GROUP_COUNT = 2;

    /**
     * The base z-index for notes. Decreased by 1 for each note ordered by time.
     */
    public static final int BASE_NOTE_Z_INDEX = 1000000;

    private Play() {
    }
}
