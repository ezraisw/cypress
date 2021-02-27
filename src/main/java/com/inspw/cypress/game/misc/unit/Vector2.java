package com.inspw.cypress.game.misc.unit;

import java.util.Objects;

public class Vector2 implements Cloneable {
    /**
     * A vector that faces right.
     */
    public static final Vector2 RIGHT = new Vector2(1, 0);

    /**
     * A vector that faces up.
     */
    public static final Vector2 UP = new Vector2(0, 1);

    /**
     * A zero vector.
     */
    public static final Vector2 ZERO = new Vector2(0, 0);

    /**
     * The error approximation when comparing equals.
     */
    public static final float EPSILON = 1e-9F;

    /**
     * The X axis component of the vector.
     */
    private final float x;

    /**
     * The Y axis component of the vector.
     */
    private final float y;

    /**
     * Create a 2 dimensional vector.
     *
     * @param x the X axis component
     * @param y the Y axis component
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a 2 dimensional vector.
     *
     * @param vec2 the vector to copy
     */
    public Vector2(Vector2 vec2) {
        this(vec2.x, vec2.y);
    }

    /**
     * Obtain a dot product of two vectors().
     *
     * @param vec2A the first vector
     * @param vec2B the second vector
     * @return the resulting dot product
     */
    public static float dot(Vector2 vec2A, Vector2 vec2B) {
        return vec2A.x * vec2B.x + vec2A.y * vec2B.y;
    }

    /**
     * Obtain a cross product of two vectors().
     *
     * @param vec2A the first vector
     * @param vec2B the second vector
     * @return the resulting cross product in scalar value
     */
    public static float cross(Vector2 vec2A, Vector2 vec2B) {
        return vec2A.x * vec2B.y - vec2A.y * vec2B.x;
    }

    /**
     * Obtain a cross product of a vector and perpendicular scalar value.
     *
     * @param vec2  the vector
     * @param scale the scalar value perpendicular to the screen
     * @return the resulting cross product in vector value
     */
    public static Vector2 cross(Vector2 vec2, float scale) {
        return new Vector2(vec2.y * scale, -vec2.x * scale);
    }

    /**
     * Obtain a cross product of a vector and perpendicular scalar value.
     *
     * @param scale the scalar value perpendicular to the screen
     * @param vec2  the vector
     * @return the resulting cross product in vector value
     */
    public static Vector2 cross(float scale, Vector2 vec2) {
        return new Vector2(-vec2.y * scale, vec2.x * scale);
    }

    /**
     * Obtain the sum of two vectors().
     *
     * @param vec2A the first vector
     * @param vec2B the second vector
     * @return the resulting sum
     */
    public static Vector2 add(Vector2 vec2A, Vector2 vec2B) {
        return new Vector2(vec2A.x + vec2B.x, vec2A.y + vec2B.y);
    }

    /**
     * Obtain the subtraction of two vectors().
     *
     * @param vec2A the first vector
     * @param vec2B the second vector
     * @return the resulting subtraction
     */
    public static Vector2 subtract(Vector2 vec2A, Vector2 vec2B) {
        return new Vector2(vec2A.x - vec2B.x, vec2A.y - vec2B.y);
    }

    /**
     * Obtain the X axis component of the vector.
     *
     * @return the X axis component in decimal
     */
    public float getX() {
        return x;
    }

    /**
     * Obtain the Y axis component of the vector.
     *
     * @return the Y axis component in decimal
     */
    public float getY() {
        return y;
    }

    /**
     * The angle of the vector.
     *
     * @return the angle in radians
     */
    public float angle() {
        return (float) Math.atan2(x, y);
    }

    /**
     * Obtain the length of the vector's projection on the given vector.
     *
     * @param vec2 the vector as a projection surface
     * @return the length of the projection in scalar value
     */
    public float componentOn(Vector2 vec2) {
        return dot(this, vec2) / vec2.magnitude();
    }

    /**
     * Obtain the projected vector of this vector on the given vector.
     *
     * @param vec2 the vector as a projection surface
     * @return the projected vector
     */
    public Vector2 projectionOn(Vector2 vec2) {
        float magnitude = vec2.magnitude();
        float multiplier = dot(this, vec2) / (magnitude * magnitude);
        return new Vector2(multiplier * vec2.x, multiplier * vec2.y);
    }

    /**
     * Convert this vector to a unit vector.
     *
     * @return the normalized vector
     */
    public Vector2 normalize() {
        float magnitude = magnitude();
        if (magnitude == 0) {
            throw new ArithmeticException("Tried to normalize a zero vector");
        }

        return new Vector2(x / magnitude, y / magnitude);
    }

    /**
     * Rotate the vector.
     *
     * @param angle the angle in radians
     * @return the rotated vector
     */
    public Vector2 rotate(float angle) {
        return new Vector2(
                (float) (x * Math.cos(angle) - y * Math.sin(angle)),
                (float) (x * Math.sin(angle) + y * Math.cos(angle))
        );
    }

    /**
     * Rotate the vector on a given origin.
     *
     * @param angle  the angle in radians
     * @param origin the origin of the rotation
     * @return the rotated vector
     */
    public Vector2 rotate(float angle, Vector2 origin) {
        return new Vector2(
                (float) (((x - origin.x) * Math.cos(angle) - (y - origin.y) * Math.sin(angle)) + origin.x),
                (float) (((x - origin.x) * Math.sin(angle) + (y - origin.y) * Math.cos(angle)) + origin.y)
        );
    }

    /**
     * Scale the vector by a given multiplier.
     *
     * @param multiplier the scale multiplier
     * @return the scaled vector
     */
    public Vector2 multiply(float multiplier) {
        return new Vector2(x * multiplier, y * multiplier);
    }

    /**
     * Divide the vector by a given denominator.
     *
     * @param denominator the division denominator
     * @return the divided value
     */
    public Vector2 divide(float denominator) {
        return new Vector2(x / denominator, y / denominator);
    }

    /**
     * Scale the vector by a given multiplier.
     *
     * @param multiplier the scale multiplier
     * @return the scaled vector
     */
    public Vector2 scale(float multiplier) {
        return new Vector2(x * multiplier, y * multiplier);
    }

    /**
     * Scale the vector by a given multiplier on a given origin.
     *
     * @param multiplier the scale multiplier
     * @param origin     the origin of the scale
     * @return the scaled vector
     */
    public Vector2 scale(float multiplier, Vector2 origin) {
        return new Vector2((x - origin.x) * multiplier + origin.x, (y - origin.y) * multiplier + origin.y);
    }

    /**
     * Reverse the vector.
     *
     * @return the reversed vector.
     */
    public Vector2 reverse() {
        return multiply(-1);
    }

    /**
     * Orient the vector to a general direction.
     * The resulting vector will always be a vector that has a
     * a dot product with the orientator larger or equal to 1.
     *
     * @param orientator the orientator vector
     * @return the orientated vector
     */
    public Vector2 orient(Vector2 orientator) {
        return dot(this, orientator) < 0 ? reverse() : this;
    }

    /**
     * Obtain the length of the vector squared.
     *
     * @return the squared length of the vector
     */
    public float magnitudeSqr() {
        return (x * x) + (y * y);
    }

    /**
     * Obtain the length of the vector.
     *
     * @return the length of the vector
     */
    public float magnitude() {
        return (float) Math.sqrt((x * x) + (y * y));
    }

    /**
     * Whether this vector is a unit vector.
     * A unit vector is a vector that has a length of 1.
     *
     * @return true if it is a unit vector
     */
    public boolean isUnitVector() {
        return magnitudeSqr() == 1;
    }

    /**
     * Whether this vector is a zero vector.
     *
     * @return true if it is a zero vector
     */
    public boolean isZero() {
        return magnitudeSqr() == 0 || (x == 0 && y == 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2)) {
            return false;
        }

        Vector2 cast = (Vector2) obj;
        return Math.abs(x - cast.x) <= EPSILON * Math.max(1.0, Math.max(Math.abs(x), Math.abs(cast.x))) &&
                Math.abs(y - cast.y) <= EPSILON * Math.max(1.0, Math.max(Math.abs(y), Math.abs(cast.y)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Vec2D[x=" + x + ",y=" + y + "]";
    }

    @Override
    public Vector2 clone() {
        return new Vector2(this);
    }
}
