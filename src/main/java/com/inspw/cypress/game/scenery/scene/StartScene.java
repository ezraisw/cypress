package com.inspw.cypress.game.scenery.scene;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Start;
import com.inspw.cypress.game.misc.timer.SimpleTimer;
import com.inspw.cypress.game.scenery.Scene;
import com.inspw.cypress.game.scenery.entity.start.PressToStartText;
import com.inspw.cypress.game.scenery.entity.start.TitleText;
import com.inspw.cypress.game.ui.input.KeyCode;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class StartScene extends Scene {
    private SimpleTimer exitTransitionTimer;

    private void initTimers() {
        exitTransitionTimer = new SimpleTimer(Start.EXIT_TRANSITION_DURATION);
        exitTransitionTimer.addFinishListener(this::toSelect);
    }

    private void initEntities() {
        TitleText titleText = new TitleText(
                Start.TITLE_COLOR_CYCLE,
                Start.TITLE_COLOR_CYCLE_PERIOD
        );
        add(titleText);

        PressToStartText pressToStartText = new PressToStartText(
                Start.PRESS_TO_START_TEXT_FONT_SIZE_RATIO,
                Start.PRESS_TO_START_TEXT_SHIFT_Y
        );
        add(pressToStartText);
    }

    private void toSelect() {
        Game.instance()
                .sceneManager()
                .changeTo("select");
    }

    private void checkStartKeyPress() {
        if (Game.instance().input().isKeyDown(KeyCode.VK_ENTER)) {
            exitTransitionTimer.start();
        }
    }

    @Override
    public void load() {
        super.load();
        initEntities();
        initTimers();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkStartKeyPress();
        exitTransitionTimer.update(deltaTime);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        if (exitTransitionTimer.isStarted()) {
            g.setColor(new Color(0, 0, 0, exitTransitionTimer.getProgress()));
            g.fillRect(0, 0, rs().getWidth(), rs().getHeight());
        }
    }
}
