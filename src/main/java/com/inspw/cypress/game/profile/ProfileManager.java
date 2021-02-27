package com.inspw.cypress.game.profile;

import com.inspw.cypress.game.model.serialized.Profile;

public interface ProfileManager {
    Profile getProfile();

    void setProfile(Profile profile);

    boolean isSaving();

    void saveProfile();

    void loadProfile();
}
