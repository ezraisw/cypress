package com.inspw.cypress.game.ui.rendering;

import com.inspw.cypress.game.misc.unit.Vector2;

public interface RenderingSurface {
    void requestRender();

    int getWidth();

    int getHeight();

    default float xRange(float x) {
        return xRange(x, 0, 0);
    }

    default float xRange(float x, float marginLeft, float marginRight) {
        float width = getWidth() - marginLeft - marginRight;
        return (width * (x + 100) / 200) + marginLeft;
    }

    default float yRange(float y) {
        return yRange(y, 0, 0);
    }

    default float yRange(float y, float marginTop, float marginBottom) {
        float height = getHeight() - marginTop - marginBottom;
        return (height * (y + 100) / 200) + marginTop;
    }

    default Vector2 toWindowPos(Vector2 v, float margin) {
        return toWindowPos(v, margin, margin);
    }

    default Vector2 toWindowPos(Vector2 v, float marginX, float marginY) {
        return toWindowPos(v, marginX, marginY, marginX);
    }

    default Vector2 toWindowPos(Vector2 v, float marginLeft, float marginY, float marginRight) {
        return toWindowPos(v, marginLeft, marginY, marginRight, marginY);
    }

    default Vector2 toWindowPos(Vector2 v, float marginLeft, float marginTop, float marginRight, float marginBottom) {
        return new Vector2(
                xRange(v.getX(), marginLeft, marginRight),
                yRange(v.getY(), marginTop, marginBottom)
        );
    }

    default Vector2 getCenter() {
        return new Vector2(xRange(0), yRange(0));
    }
}
