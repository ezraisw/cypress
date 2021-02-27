package com.inspw.cypress.game.graphic.swing.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ResizeCachingProcess implements CachingProcess {
    private ResizeStrategy strategy, oldStrategy = null;
    private int scalingHints, oldScalingHints = -1;
    private int width, oldWidth = -1;
    private int height, oldHeight = -1;

    public ResizeCachingProcess() {
        this(ResizeStrategy.STRETCH);
    }

    public ResizeCachingProcess(ResizeStrategy strategy) {
        this.strategy = strategy;
        this.scalingHints = Image.SCALE_FAST;
    }

    public ResizeCachingProcess(ResizeStrategy strategy, int scalingHints) {
        this.strategy = strategy;
        this.scalingHints = scalingHints;
    }

    public ResizeStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(ResizeStrategy strategy) {
        this.strategy = strategy;
    }

    public int getScalingHints() {
        return scalingHints;
    }

    public void setScalingHints(int scalingHints) {
        this.scalingHints = scalingHints;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        int imgX, imgY, imgWidth, imgHeight;
        if (getStrategy() == ResizeStrategy.KEEP_ASPECT_RATIO) {
            float imageAspRatio = (float) image.getWidth() / image.getHeight();
            float targetAspRatio = (float) getWidth() / getHeight();

            // Calculate image size while keeping aspect ratio
            if (imageAspRatio > targetAspRatio) {
                imgWidth = (int) (getHeight() * imageAspRatio);
                imgHeight = getHeight();
                imgX = -(imgWidth - getWidth()) / 2;
                imgY = 0;
            } else {
                imgWidth = getWidth();
                imgHeight = (int) (getWidth() / imageAspRatio);
                imgX = 0;
                imgY = -(imgHeight - getHeight()) / 2;
            }
        } else {
            imgX = 0;
            imgY = 0;
            imgWidth = getWidth();
            imgHeight = getHeight();
        }

        Image tempImage = image.getScaledInstance(imgWidth, imgHeight, getScalingHints());

        BufferedImage newImage = new BufferedImage(
                getWidth(),
                getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics g = newImage.getGraphics();
        g.drawImage(tempImage, imgX, imgY, null);
        g.dispose();

        return newImage;
    }

    @Override
    public void remember() {
        oldStrategy = getStrategy();
        oldScalingHints = getScalingHints();
        oldWidth = getWidth();
        oldHeight = getHeight();
    }

    @Override
    public void forget() {
        oldStrategy = null;
        oldScalingHints = -1;
        oldWidth = -1;
        oldHeight = -1;
    }

    @Override
    public boolean shouldCache() {
        return oldStrategy != getStrategy() ||
                oldScalingHints != getScalingHints() ||
                oldWidth != getWidth() ||
                oldHeight != getHeight();
    }

    public enum ResizeStrategy {
        KEEP_ASPECT_RATIO, STRETCH
    }
}
