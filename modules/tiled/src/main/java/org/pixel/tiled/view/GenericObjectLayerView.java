package org.pixel.tiled.view;

import org.pixel.tiled.content.*;

public interface GenericObjectLayerView extends TiledView<TiledObjectGroup> {
    default void draw(TiledTileObject object, TiledObjectGroup group) {

    }

    default void draw(TiledRectangle object, TiledObjectGroup group) {

    }

    default void draw(TiledEllipse object, TiledObjectGroup group) {

    }

    default void draw(TiledPolygon object, TiledObjectGroup group) {

    }

    default void draw(TiledPoint object, TiledObjectGroup group) {

    }
}
