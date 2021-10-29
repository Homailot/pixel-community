package org.pixel.tiled.view;

import org.pixel.graphics.render.SpriteBatch;
import org.pixel.tiled.content.TileLayer;

public class LeftDownStrategy extends DrawStrategy {
    @Override
    public void draw(SpriteBatch spriteBatch, TileLayer layer, long frame) {
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = layer.getWidth() - 1; x >= 0; x--) {
                drawTile(spriteBatch, layer, x, y, frame);
            }
        }
    }
}
