package com.inspw.cypress.game.scenery;

import java.awt.*;

public interface Renderable {
    int getZ();

    void setZ(int z);

    void render(Graphics2D g);
}
