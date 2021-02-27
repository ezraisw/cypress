package com.inspw.cypress.game.scenery.entity.select;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.misc.unit.Vector2;
import com.inspw.cypress.game.model.Song;
import com.inspw.cypress.game.scenery.entity.general.BinaryTransitionalEntity;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class SongList extends BinaryTransitionalEntity {
    private static final int SHIFT_MIN_SPEED = 25;
    private static final int SHIFT_ACCEL = 11;
    private static final String NO_SONG_TEXT = "No songs available";
    private static final int SONG_LIST_TEXT_SIZE = 50;
    private static final int SONG_LIST_TEXT_MARGIN_BOTTOM = 30;
    private static final int SONG_LIST_TEXT_MARGIN_RIGHT = 50;
    private static final int HIDDEN_HORIZONTAL_SHIFT = 70;
    private static final int ARROW_VERT_SHIFT = 30;
    private static final int ARROW_SIZE = 10;
    private static final int LINE_VERT_SHIFT = 15;
    private static final int Z_INDEX = 100;
    private static final float DISPLAYING_DURATION = 0.1F;
    private static final float HIDING_DURATION = 0.2F;
    private static final float DISPLAY_DURATION = 1.0F;

    private List<Song> songs;
    private int currentIndex;
    private float verticalShift;
    private float targetVerticalShift;

    public SongList(List<Song> songs) {
        super(DISPLAYING_DURATION, HIDING_DURATION, DISPLAY_DURATION, -1);
        this.songs = songs;
        setZ(Z_INDEX);
    }

    @Override
    public void load() {
        super.load();
        activate();
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        setCurrentIndex(currentIndex, true);
    }

    public void setCurrentIndex(int currentIndex, boolean withAnim) {
        this.currentIndex = currentIndex;

        if (withAnim) {
            adjustTargetVerticalShift(currentIndex);
        } else {
            jumpTo(currentIndex);
        }
    }

    public boolean up(boolean withAnim) {
        if (getCurrentIndex() <= 0) {
            return false;
        }

        setCurrentIndex(getCurrentIndex() - 1);

        if (withAnim) {
            switchToActive();
        }

        return true;
    }

    public boolean down(boolean withAnim) {
        if (getCurrentIndex() >= getSongs().size() - 1) {
            return false;
        }

        setCurrentIndex(getCurrentIndex() + 1);

        if (withAnim) {
            switchToActive();
        }

        return true;
    }

    private void adjustTargetVerticalShift(int index) {
        int effectiveHeight = SONG_LIST_TEXT_SIZE + SONG_LIST_TEXT_MARGIN_BOTTOM;
        int normal = index * effectiveHeight;
        int reversed = (index - getSongs().size()) * effectiveHeight;

        targetVerticalShift = Math.abs(normal - verticalShift) < Math.abs(reversed - verticalShift)
                ? normal
                : reversed;
    }

    private void jumpTo(int index) {
        adjustTargetVerticalShift(index);
        verticalShift = targetVerticalShift;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float shiftDelta = targetVerticalShift - verticalShift;
        float shiftSpeed = 0;
        if (shiftDelta > 0) {
            shiftSpeed = Math.max(shiftDelta * SHIFT_ACCEL, SHIFT_MIN_SPEED);
        } else if (shiftDelta < 0) {
            shiftSpeed = Math.min(shiftDelta * SHIFT_ACCEL, -SHIFT_MIN_SPEED);
        }
        shiftSpeed *= deltaTime;

        if (Math.abs(targetVerticalShift - verticalShift) < Math.abs(shiftSpeed)) {
            verticalShift = targetVerticalShift;
        } else {
            verticalShift += shiftSpeed;
        }
    }

    private RadialGradientPaint fadeOutGrad(float alpha, Point2D center, float radius) {
        return new RadialGradientPaint(
                center,
                radius,
                new float[]{0F, 1F},
                new Color[]{new Color(1, 1, 1, alpha), new Color(1, 1, 1, 0f)}
        );
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2 center = rs().getCenter();
        Font font = new Font("SansSerif", Font.PLAIN, SONG_LIST_TEXT_SIZE);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();

        if (getSongs() == null || getSongs().isEmpty()) {
            String naText = NO_SONG_TEXT;

            g.setColor(Color.WHITE);
            g.drawString(
                    naText,
                    center.getX() - (float) metrics.stringWidth(naText) / 2,
                    center.getY()
            );

            return;
        }

        if (getState() == State.DISABLED) {
            return;
        }

        float globalConstX = 1 - value();
        float globalConstY = MathHelper.clamp(-(globalConstX * globalConstX) + 1, 0, 1);
        float globalShift = (1 - globalConstY) * HIDDEN_HORIZONTAL_SHIFT;
        float globalAlpha = globalConstY;

        int effectiveHeight = SONG_LIST_TEXT_SIZE + SONG_LIST_TEXT_MARGIN_BOTTOM;
        int count = (int) Math.ceil((float) rs().getHeight() / effectiveHeight / 2) * 2 + 1;

        for (int i = -count / 2; i <= (count / 2); i++) {
            int middleIndex = (int) (verticalShift / effectiveHeight);
            int index = i + middleIndex;

            if (index < 0 || index >= getSongs().size()) {
                continue;
            }

            Song s = getSongs().get(index);

            String text = s.getSongInfo().getTitle();
            float modShift = verticalShift % effectiveHeight;
            float posY = (center.getY() + effectiveHeight * i) - modShift;
            float constX = posY / ((float) rs().getHeight() / 2) - 1;
            // y = -x^2 + 1
            float constY = -(constX * constX) + 1;
            float alpha = MathHelper.clamp(constY, 0, 1);
            float right = rs().getWidth() - SONG_LIST_TEXT_MARGIN_RIGHT + globalShift;
            float strWidth = metrics.stringWidth(text);

            if (index == getCurrentIndex()) {
                g.setColor(new Color(1, 1, 1, globalAlpha));
                Paint defPaint = g.getPaint();

                float leftLineX = rs().xRange(25) + globalShift;
                float lineLength = right - leftLineX;

                float bottomLineY = posY + LINE_VERT_SHIFT;
                g.setPaint(fadeOutGrad(globalAlpha, new Point2D.Float(right, bottomLineY), lineLength));

                Path2D bottomLinePath = new Path2D.Float();
                bottomLinePath.moveTo(right, bottomLineY);
                bottomLinePath.lineTo(leftLineX, bottomLineY);
                g.draw(bottomLinePath);

                float topLineY = posY - LINE_VERT_SHIFT - (float) metrics.getHeight() / 2;
                g.setPaint(fadeOutGrad(globalAlpha, new Point2D.Float(right, topLineY), lineLength));

                Path2D topLinePath = new Path2D.Float();
                topLinePath.moveTo(right, topLineY);
                topLinePath.lineTo(leftLineX, topLineY);
                g.draw(topLinePath);

                g.setPaint(defPaint);

                float arrowX = (leftLineX + lineLength / 2);
                if (getCurrentIndex() < songs.size() - 1) {
                    float bottomArrowY = posY + ARROW_VERT_SHIFT;

                    Path2D bottomArrowPath = new Path2D.Float();
                    bottomArrowPath.moveTo(arrowX, bottomArrowY);
                    bottomArrowPath.lineTo(arrowX + ARROW_SIZE, bottomArrowY - ARROW_SIZE);
                    bottomArrowPath.lineTo(arrowX - ARROW_SIZE, bottomArrowY - ARROW_SIZE);
                    bottomArrowPath.closePath();

                    g.fill(bottomArrowPath);
                }

                if (getCurrentIndex() > 0) {
                    float topArrowY = posY - ARROW_VERT_SHIFT - (float) metrics.getHeight() / 2;
                    Path2D topArrowPath = new Path2D.Float();
                    topArrowPath.moveTo(arrowX, topArrowY);
                    topArrowPath.lineTo(arrowX + ARROW_SIZE, topArrowY + ARROW_SIZE);
                    topArrowPath.lineTo(arrowX - ARROW_SIZE, topArrowY + ARROW_SIZE);
                    topArrowPath.closePath();

                    g.fill(topArrowPath);
                }
            }

            g.setColor(new Color(1, index == getCurrentIndex() ? 0 : 1, 1, globalAlpha * alpha));
            g.drawString(text, right - strWidth, posY);
        }
    }
}
