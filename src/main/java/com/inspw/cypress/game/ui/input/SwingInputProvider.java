package com.inspw.cypress.game.ui.input;

import com.inspw.cypress.game.misc.unit.Vector2;

import java.awt.event.*;

public class SwingInputProvider implements InputProvider, KeyListener, MouseListener, MouseMotionListener {
    private final ArrayInputStates states;
    private final ArrayInputStates nextStates;

    public SwingInputProvider() {
        states = new ArrayInputStates();
        nextStates = new ArrayInputStates();
    }

    private synchronized void triggerKeyDown(int keyCode) {
        if (!nextStates.getKeyState(keyCode)) {
            nextStates.keyDown(keyCode);
        }
        nextStates.setKeyState(keyCode, true);
    }

    private synchronized void triggerKeyUp(int keyCode) {
        nextStates.keyUp(keyCode);
        nextStates.setKeyState(keyCode, false);
    }

    private synchronized void triggerMouseDown(int buttonIdx) {
        nextStates.mouseDown(buttonIdx);
        nextStates.setMouseState(buttonIdx, true);
    }

    private synchronized void triggerMouseUp(int buttonIdx) {
        nextStates.mouseUp(buttonIdx);
        nextStates.setMouseState(buttonIdx, false);
    }

    @Override
    public synchronized ArrayInputStates getStates() {
        return states;
    }

    @Override
    public synchronized void flush() {
        states.copyFrom(nextStates);
    }

    @Override
    public void clearUpAndDownStates() {
        nextStates.clearUpAndDownStates();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        triggerKeyDown(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        triggerKeyUp(e.getKeyCode());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        triggerMouseDown(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        triggerMouseUp(e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        nextStates.setMouseInWindow(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        nextStates.setMouseInWindow(false);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        nextStates.setMousePosition(new Vector2(e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        nextStates.setMousePosition(new Vector2(e.getX(), e.getY()));
    }
}
