package org.pixel.ext.tiled.view;

import org.pixel.ext.tiled.content.TiledMap;

import java.util.HashMap;

public class DrawStrategyFactory {
    private final static HashMap<String, DrawStrategy> renderOrderToStrategy = new HashMap<>();

    static {
        renderOrderToStrategy.put("right-down", new RightDownStrategy());
        renderOrderToStrategy.put("right-up", new RightUpStrategy());
        renderOrderToStrategy.put("left-down", new LeftDownStrategy());
        renderOrderToStrategy.put("left-up", new LeftUpStrategy());
    }

    public DrawStrategy getDrawStrategy(TiledMap tileMap) {
        return renderOrderToStrategy.getOrDefault(tileMap.getRenderOrder(), new RightDownStrategy());
    }
}
