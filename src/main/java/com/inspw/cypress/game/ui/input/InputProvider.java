package com.inspw.cypress.game.ui.input;

public interface InputProvider {
    InputStates getStates();

    void flush();

    void clearUpAndDownStates();
}
