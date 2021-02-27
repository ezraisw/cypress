package com.inspw.cypress.game.constant;

public class Library {
    public static final String VLCLIB_DISCOVERY_FAILED_TITLE = "Error";
    public static final String VLCLIB_DISCOVERY_FAILED_MESSAGE =
            "Could not discover VLC native libraries.\n" +
                    "Please install VLC Media Player or libVLC.";
    public static final int VLCLIB_DISCOVERY_FAILED_EXIT_STATUS = 1;

    private Library() {
    }
}
