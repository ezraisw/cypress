package com.inspw.cypress.game.misc.helper;

import java.util.Random;

public class StringHelper {
    private StringHelper() {
    }

    public static String random() {
        return random(32);
    }

    public static String random(int length) {
        Random rng = new Random();

        String bucket = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(bucket.charAt(
                    rng.nextInt(bucket.length())
            ));
        }

        return builder.toString();
    }
}
