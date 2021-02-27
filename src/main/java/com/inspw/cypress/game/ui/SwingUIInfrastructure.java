package com.inspw.cypress.game.ui;

import com.inspw.cypress.game.constant.Window;
import com.inspw.cypress.game.scenery.SceneManager;
import com.inspw.cypress.game.ui.input.InputProvider;
import com.inspw.cypress.game.ui.input.SwingInputProvider;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;
import com.inspw.cypress.game.ui.rendering.SwingRenderingSurface;

import javax.swing.*;
import java.awt.*;

public class SwingUIInfrastructure implements UIInfrastructure {
    private final JFrame frame;
    private final SwingRenderingSurface renderingSurface;
    private final SwingInputProvider inputProvider;

    public SwingUIInfrastructure(SceneManager sceneManager) {
        frame = new JFrame();
        renderingSurface = new SwingRenderingSurface(sceneManager);
        inputProvider = new SwingInputProvider();
    }

    private void attachRenderingComponent() {
        frame.add(renderingSurface.getComponent());
    }

    private void attachInput() {
        frame.addKeyListener(inputProvider);
        frame.addMouseListener(inputProvider);
        frame.addMouseMotionListener(inputProvider);
    }

    @Override
    public void configure() {
        attachRenderingComponent();
        attachInput();

        frame.setTitle(Window.TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(Window.DEFAULT_WIDTH, Window.DEFAULT_HEIGHT));
        frame.setMinimumSize(new Dimension(Window.MIN_WIDTH, Window.MIN_HEIGHT));
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void start() {
        frame.setVisible(true);
    }

    @Override
    public RenderingSurface renderingSurface() {
        return renderingSurface;
    }

    @Override
    public InputProvider inputProvider() {
        return inputProvider;
    }
}
