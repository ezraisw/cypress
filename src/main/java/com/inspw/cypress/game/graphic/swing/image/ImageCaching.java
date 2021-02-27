package com.inspw.cypress.game.graphic.swing.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ImageCaching {
    private final CachingProcess[] cachingProcesses;
    private BufferedImage image, oldImage;
    private BufferedImage renderedImage;

    public ImageCaching(CachingProcess[] cachingProcesses) {
        this.cachingProcesses = cachingProcesses;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void refresh() {
        for (CachingProcess cp : cachingProcesses) {
            cp.forget();
        }
    }

    public void cache() {
        renderedImage = getImage();
        for (CachingProcess cp : cachingProcesses) {
            renderedImage = cp.process(renderedImage);
            cp.remember();
        }

        // Remember the current image.
        oldImage = getImage();
    }

    public void attemptCache() {
        if (oldImage == getImage() && !Arrays.stream(cachingProcesses).allMatch(CachingProcess::shouldCache)) {
            return;
        }
        cache();
    }

    public void render(Graphics2D g) {
        if (getImage() == null) {
            return;
        }

        attemptCache();
        g.drawImage(renderedImage, 0, 0, null);
    }
}
