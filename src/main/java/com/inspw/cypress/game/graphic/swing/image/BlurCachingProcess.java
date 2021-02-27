package com.inspw.cypress.game.graphic.swing.image;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class BlurCachingProcess implements CachingProcess {
    private Kernel kernel, oldKernel = null;

    public BlurCachingProcess(Kernel kernel) {
        this.kernel = kernel;
    }

    public Kernel getKernel() {
        return kernel;
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        ConvolveOp blur = new ConvolveOp(getKernel());
        newImage = blur.filter(image, newImage);

        return newImage;
    }

    @Override
    public void remember() {
        oldKernel = getKernel();
    }

    @Override
    public void forget() {
        oldKernel = null;
    }

    @Override
    public boolean shouldCache() {
        return oldKernel != getKernel();
    }
}
