package com.inspw.cypress.game.ui.input;

import com.inspw.cypress.game.misc.unit.Vector2;

public interface InputStates {
    boolean getKeyState(int keyCode);

    boolean getKeyDownState(int keyCode);

    boolean getKeyUpState(int keyCode);

    boolean getMouseState(int buttonIdx);

    boolean getMouseDownState(int buttonIdx);

    boolean getMouseUpState(int buttonIdx);

    boolean isMouseInWindow();

    Vector2 getMousePosition();
}
