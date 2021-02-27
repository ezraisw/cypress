package com.inspw.cypress.game.ui.rendering;

import com.inspw.cypress.game.scenery.Scene;
import com.inspw.cypress.game.scenery.SceneManager;

import javax.swing.*;
import java.awt.*;

public class SwingRenderingSurface implements RenderingSurface {
    private final CanvasPanel panel;

    public SwingRenderingSurface(SceneManager sceneManager) {
        this.panel = new CanvasPanel(sceneManager);
    }

    public Component getComponent() {
        return panel;
    }

    @Override
    public void requestRender() {
        panel.repaint();
    }

    @Override
    public int getWidth() {
        return panel.getWidth();
    }

    @Override
    public int getHeight() {
        return panel.getHeight();
    }

    private static class CanvasPanel extends JPanel {
        private final SceneManager sceneManager;

        public CanvasPanel(SceneManager sceneManager) {
            this.sceneManager = sceneManager;
            setBackground(Color.BLACK);
        }

        @Override
        public void paint(Graphics g) {
            Scene scene = sceneManager.getActiveScene();

            if (scene != null && scene.isLoaded()) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                scene.render(g2d);
            }
        }
    }
}
