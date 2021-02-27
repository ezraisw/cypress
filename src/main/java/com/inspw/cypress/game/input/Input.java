package com.inspw.cypress.game.input;

import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.ui.input.KeyCode;
import org.intellij.lang.annotations.MagicConstant;

public interface Input {
    boolean isMouseDown(int button);

    boolean isMousePressed(int button);

    boolean isMouseUp(int button);

    Vector2 getMousePosition();

    boolean isKeyDown(@MagicConstant(valuesFromClass = KeyCode.class) int key);

    boolean isKeyPressed(@MagicConstant(valuesFromClass = KeyCode.class) int key);

    boolean isKeyUp(@MagicConstant(valuesFromClass = KeyCode.class) int key);
}
