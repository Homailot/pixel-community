package org.pixel.tiled.content;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pixel.graphics.render.SpriteBatch;
import org.pixel.tiled.view.TileMapView;

class TiledObjectGroupTest {
    @Test
    void draw() {
        TileMap map = Mockito.mock(TileMap.class);
        TiledObjectGroup tiledObjectGroup = new TiledObjectGroup(map);
        SpriteBatch spriteBatch = Mockito.mock(SpriteBatch.class);
        TileMapView view = Mockito.mock(TileMapView.class);

        tiledObjectGroup.draw(view);

        Mockito.verify(view).draw(Mockito.same(tiledObjectGroup));
    }
}