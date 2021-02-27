package com.inspw.cypress.game.scenery.entity.result;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.model.enumerate.HitGrade;
import com.inspw.cypress.game.scenery.entity.general.EntryExitTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;

public class HitGradeCountText extends EntryExitTransitionalEntity {
    private static final int TITLE_FONT_SIZE = 25;
    private static final int COUNT_FONT_SIZE = 35;
    private static final float SHIFT_Y = 70;
    private static final float COUNT_MARGIN_TOP = 10;
    private static final float FADE_IN_DURATION = 0.5F;
    private static final float FADE_OUT_DURATION = 1F;

    public HitGradeCountText() {
        super(FADE_IN_DURATION, FADE_OUT_DURATION);
    }

    private int getMiss() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getHitCount(HitGrade.MISS);
    }

    private int getBad() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getHitCount(HitGrade.BAD);
    }

    private int getGood() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getHitCount(HitGrade.GOOD);
    }

    private int getGreat() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getHitCount(HitGrade.GREAT);
    }

    private int getPerfect() {
        return Game.instance()
                .rhythmSessionManager()
                .getSession()
                .getHitCount(HitGrade.PERFECT);
    }

    private void renderHitText(Graphics2D g, Vector2 center, String[] titles, int[] counts) {
        if (titles.length != counts.length) {
            throw new IllegalArgumentException("Length does not match");
        }

        Font titleFont = new Font("SansSerif", Font.PLAIN, TITLE_FONT_SIZE);
        FontMetrics titleMetrics = g.getFontMetrics(titleFont);

        Font countFont = new Font("SansSerif", Font.PLAIN, COUNT_FONT_SIZE);
        FontMetrics countMetrics = g.getFontMetrics(countFont);

        int maxWidth = 0;
        for (String title : titles) {
            int width = titleMetrics.stringWidth(title);
            if (maxWidth < width) {
                maxWidth = width;
            }
        }
        float totalWidth = maxWidth * titles.length;

        float titleTop = (float) center.getY() + SHIFT_Y;
        float countTop = titleTop + titleMetrics.getHeight() + COUNT_MARGIN_TOP;
        float left = (float) center.getX() - totalWidth / 2;
        for (int i = 0; i < titles.length; i++) {
            String titleText = titles[i];
            String countText = Integer.toString(Math.round(counts[i] * fadeInValue()));

            float cx = left + (maxWidth * i) + ((float) maxWidth / 2);

            float titleWidth = titleMetrics.stringWidth(titleText);
            g.setFont(titleFont);
            g.drawString(titleText, cx - titleWidth / 2, titleTop);

            float countWidth = countMetrics.stringWidth(countText);
            g.setFont(countFont);
            g.drawString(countText, cx - countWidth / 2, countTop);
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        g.setColor(new Color(1, 1, 1, value()));

        renderHitText(g, rs().getCenter(),
                new String[]{
                        HitGrade.MISS.getName(),
                        HitGrade.BAD.getName(),
                        HitGrade.GOOD.getName(),
                        HitGrade.GREAT.getName(),
                        HitGrade.PERFECT.getName(),
                },
                new int[]{getMiss(), getBad(), getGood(), getGreat(), getPerfect()});
    }
}
