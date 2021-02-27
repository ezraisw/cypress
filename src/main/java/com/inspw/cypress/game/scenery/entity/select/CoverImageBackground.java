package com.inspw.cypress.game.scenery.entity.select;

import com.inspw.cypress.game.graphic.swing.image.CachingProcess;
import com.inspw.cypress.game.graphic.swing.image.ImageCaching;
import com.inspw.cypress.game.graphic.swing.image.ResizeCachingProcess;
import com.inspw.cypress.game.scenery.entity.general.BinaryTransitionalEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CoverImageBackground extends BinaryTransitionalEntity {
    private static final float DISPLAYING_DURATION = 0.2F;
    private static final float HIDING_DURATION = 0.1F;
    private static final float HIDDEN_DURATION = 1.0F;
    private static final float MAX_SCALE = 1.05F;
    private final ResizeCachingProcess resizeCachingProcess;
    private final ImageCaching imageCaching;
    private float sinTimer;
    private File newImageFile;

    public CoverImageBackground() {
        super(DISPLAYING_DURATION, HIDING_DURATION, -1, HIDDEN_DURATION);
        resizeCachingProcess = new ResizeCachingProcess(
                ResizeCachingProcess.ResizeStrategy.KEEP_ASPECT_RATIO,
                Image.SCALE_SMOOTH
        );
        imageCaching = new ImageCaching(new CachingProcess[]{resizeCachingProcess});
        setZ(-1);
    }

    public void newImageFile(File imageFile) {
        this.newImageFile = imageFile;
        switchToDisabled();
    }

    @Override
    protected void onIntoActive() {
        super.onIntoActive();

        sinTimer = 0;
        if (newImageFile != null) {
            try {
                BufferedImage image = ImageIO.read(newImageFile);
                imageCaching.setImage(image);
                imageCaching.refresh();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        sinTimer = (float) ((sinTimer + deltaTime) % (2 * Math.PI));
        resizeCachingProcess.setWidth(rs().getWidth());
        resizeCachingProcess.setHeight(rs().getHeight());
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        AffineTransform at = g.getTransform();

        float scale = (float) ((-Math.cos(sinTimer) + 1) / 2 * (MAX_SCALE - 1) * normalizedValue());

        g.translate(rs().getWidth() / 2, rs().getHeight() / 2);
        g.scale(1 + scale, 1 + scale);
        g.translate(-rs().getWidth() / 2, -rs().getHeight() / 2);
        imageCaching.render(g);
        g.setTransform(at);
        g.setColor(new Color(0, 0, 0, 1 - value()));
        g.fillRect(0, 0, rs().getWidth(), rs().getHeight());
    }
}
