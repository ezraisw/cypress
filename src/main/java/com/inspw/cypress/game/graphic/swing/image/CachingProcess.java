package com.inspw.cypress.game.graphic.swing.image;

import java.awt.image.BufferedImage;

public interface CachingProcess {
    BufferedImage process(BufferedImage image);

    default void remember() {
    }

    default void forget() {
    }

    default boolean shouldCache() {
        return false;
    }
}
