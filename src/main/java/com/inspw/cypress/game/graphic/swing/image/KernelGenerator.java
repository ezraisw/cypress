package com.inspw.cypress.game.graphic.swing.image;

import java.awt.image.Kernel;
import java.util.Arrays;

public class KernelGenerator {
    private KernelGenerator() {
    }

    public static Kernel boxBlur(int radius, float intensity) {
        float weight = intensity / (radius * radius);
        float[] data = new float[radius * radius];
        Arrays.fill(data, weight);
        return new Kernel(radius, radius, data);
    }
}
