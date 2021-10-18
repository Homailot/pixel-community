package org.pixel.tiled.content;

public class Layer {
    private final int width;
    private final int height;
    private final double offsetX;
    private final double offsetY;
    private final TileMap tileMap;
    private final long[][] tiles;
    private TiledCustomProperties customProperties;

    public Layer(int width, int height, double offsetX, double offsetY, TileMap tileMap) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.tileMap = tileMap;

        tiles = new long[height][width];
    }

    public TiledCustomProperties getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(TiledCustomProperties customProperties) {
        this.customProperties = customProperties;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public long[][] getTiles() {
        return tiles;
    }

    public void addTile(int x, int y, long gID) {
        tiles[y][x] = gID;
    }
}
