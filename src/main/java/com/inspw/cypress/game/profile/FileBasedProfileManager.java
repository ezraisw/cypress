package com.inspw.cypress.game.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspw.cypress.game.constant.Persistent;
import com.inspw.cypress.game.model.serialized.Profile;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileBasedProfileManager implements ProfileManager {
    private final ObjectMapper om = new ObjectMapper();
    private Profile profile;
    private boolean saving;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public boolean isSaving() {
        return saving;
    }

    private void writeProfileToFile() {
        saving = true;
        try (FileOutputStream fos = new FileOutputStream(Persistent.PROFILE_FILE)) {
            om.writeValue(fos, getProfile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        saving = false;
    }

    public void saveProfile() {
        Thread thread = new Thread(this::writeProfileToFile);
        thread.start();
    }

    public void loadProfile() {
        try {
            setProfile(om.readValue(Persistent.PROFILE_FILE, Profile.class));
        } catch (IOException e) {
            setProfile(Persistent.DEFAULT_PROFILE);
        }
    }
}
