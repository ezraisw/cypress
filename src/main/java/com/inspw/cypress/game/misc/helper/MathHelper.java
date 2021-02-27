package com.inspw.cypress.game.misc.helper;

public class MathHelper {
    public static final float EPSILON = 1e-9F;

    private MathHelper() {
    }

    public static float clamp(float val, float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum is greater than maximum");
        }

        return Math.max(Math.min(val, max), min);
    }

    public static int clamp(int val, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum is greater than maximum");
        }

        return Math.max(Math.min(val, max), min);
    }

    public static boolean floatEquals(float a, float b) {
        return Math.abs(a - b) <= EPSILON * Math.max(1.0, Math.max(Math.abs(a), Math.abs(b)));
    }

    public static float revPolynomial(float x, int degree) {
        return (float) -Math.pow(x, degree) + 1;
    }
}
