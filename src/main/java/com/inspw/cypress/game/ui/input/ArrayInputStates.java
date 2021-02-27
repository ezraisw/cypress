package com.inspw.cypress.game.ui.input;

import com.inspw.cypress.game.misc.unit.Vector2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArrayInputStates implements InputStates {
    private static final int KEYBOARD_KEYS = 65535;
    private static final int MOUSE_BUTTONS = 10;

    private final boolean[] key;
    private Set<Integer> keyDown;
    private Set<Integer> keyUp;
    private final boolean[] mouse;
    private final boolean[] mouseDown;
    private final boolean[] mouseUp;
    private boolean mouseInWindow;
    private Vector2 mousePosition;

    public ArrayInputStates() {
        key = new boolean[KEYBOARD_KEYS];
        keyDown = new HashSet<>();
        keyUp = new HashSet<>();
        mouse = new boolean[MOUSE_BUTTONS];
        mouseDown = new boolean[MOUSE_BUTTONS];
        mouseUp = new boolean[MOUSE_BUTTONS];
    }

    public void clearUpAndDownStates() {
        keyDown.clear();
        keyUp.clear();

        Arrays.fill(mouseDown, false);
        Arrays.fill(mouseUp, false);
    }

    public void copyFrom(ArrayInputStates states) {
        System.arraycopy(states.key, 0, key, 0, KEYBOARD_KEYS);
        keyDown = new HashSet<>(states.keyDown);
        keyUp = new HashSet<>(states.keyUp);
        System.arraycopy(states.mouse, 0, mouse, 0, MOUSE_BUTTONS);
        System.arraycopy(states.mouseDown, 0, mouseDown, 0, MOUSE_BUTTONS);
        System.arraycopy(states.mouseUp, 0, mouseUp, 0, MOUSE_BUTTONS);
        mouseInWindow = states.mouseInWindow;
        mousePosition = states.mousePosition;
    }

    public boolean getKeyState(int keyCode) {
        return key[keyCode];
    }

    public void setKeyState(int keyCode, boolean state) {
        key[keyCode] = state;
    }

    public boolean getKeyDownState(int keyCode) {
        return keyDown.contains(keyCode);
    }

    public void keyDown(int keyCode) {
        keyDown.add(keyCode);
    }

    public boolean getKeyUpState(int keyCode) {
        return keyUp.contains(keyCode);
    }

    public void keyUp(int keyCode) {
        keyUp.add(keyCode);
    }

    public boolean getMouseState(int buttonIdx) {
        return mouse[buttonIdx];
    }

    public void setMouseState(int buttonIdx, boolean state) {
        mouse[buttonIdx] = state;
    }

    public boolean getMouseDownState(int buttonIdx) {
        return mouseDown[buttonIdx];
    }

    public void mouseDown(int buttonIdx) {
        mouseDown[buttonIdx] = true;
    }

    public boolean getMouseUpState(int buttonIdx) {
        return mouseUp[buttonIdx];
    }

    public void mouseUp(int buttonIdx) {
        mouseUp[buttonIdx] = true;
    }

    public boolean isMouseInWindow() {
        return mouseInWindow;
    }

    public void setMouseInWindow(boolean mouseInWindow) {
        this.mouseInWindow = mouseInWindow;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public void setMousePosition(Vector2 mousePosition) {
        this.mousePosition = mousePosition;
    }
}
