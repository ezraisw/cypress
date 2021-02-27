package com.inspw.cypress.game.constant;

import com.inspw.cypress.game.model.serialized.Profile;

import java.io.File;
import java.util.HashMap;

public class Persistent {
    public static final File PROFILE_FILE = new File("profile.json");
    public static final Profile DEFAULT_PROFILE = new Profile(
            0,
            0,
            null,
            new HashMap<>()
    );

    private Persistent() {
    }
}
