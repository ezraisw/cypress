package com.inspw.cypress.game.input;

import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.ui.input.InputProvider;

public class UIProvidedInput implements Input {
    private final InputProvider inputProvider;

    public UIProvidedInput(InputProvider inputProvider) {
        this.inputProvider = inputProvider;
    }

    @Override
    public boolean isMouseDown(int button) {
        return inputProvider.getStates().getMouseDownState(button);
    }

    @Override
    public boolean isMousePressed(int button) {
        return inputProvider.getStates().getMouseState(button);
    }

    @Override
    public boolean isMouseUp(int button) {
        return inputProvider.getStates().getMouseUpState(button);
    }

    @Override
    public Vector2 getMousePosition() {
        return inputProvider.getStates().getMousePosition();
    }

    @Override
    public boolean isKeyDown(int key) {
        return inputProvider.getStates().getKeyDownState(key);
    }

    @Override
    public boolean isKeyPressed(int key) {
        return inputProvider.getStates().getKeyState(key);
    }

    @Override
    public boolean isKeyUp(int key) {
        return inputProvider.getStates().getKeyUpState(key);
    }
}
