package com.inspw.cypress.game.scenery.entity.general;

import com.inspw.cypress.game.graphic.swing.image.CachingProcess;
import com.inspw.cypress.game.graphic.swing.image.ImageCaching;
import com.inspw.cypress.game.graphic.swing.image.ResizeCachingProcess;
import com.inspw.cypress.game.scenery.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StaticCoverImageBackground extends Entity {
    private static final float BACKGROUND_AS_DECORATION_ALPHA = 0.12F;
    private static final int Z_INDEX = -1;

    private final ResizeCachingProcess resizeCachingProcess;
    private final ImageCaching imageCaching;

    public StaticCoverImageBackground() {
        resizeCachingProcess = new ResizeCachingProcess(
                ResizeCachingProcess.ResizeStrategy.KEEP_ASPECT_RATIO,
                Image.SCALE_SMOOTH
        );
        imageCaching = new ImageCaching(new CachingProcess[]{resizeCachingProcess});
        setZ(Z_INDEX);
    }

    public void setImageFrom(File imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            imageCaching.setImage(image);
            imageCaching.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        resizeCachingProcess.setWidth(rs().getWidth());
        resizeCachingProcess.setHeight(rs().getHeight());
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        imageCaching.render(g);
        g.setColor(new Color(0, 0, 0, 1 - BACKGROUND_AS_DECORATION_ALPHA));
        g.fillRect(0, 0, rs().getWidth(), rs().getHeight());
    }
}
