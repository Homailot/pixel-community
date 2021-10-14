package org.pixel.tiled.content;

import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TileMapTest {
    @Test
    public void disposeTest() {
        TileMap tileMap = new TileMap();
        TileSet tileSet1 = Mockito.mock(TileSet.class);
        TileSet tileSet2 = Mockito.mock(TileSet.class);

        tileMap.addTileSet(tileSet1);
        tileMap.addTileSet(tileSet2);

        InOrder inOrder = Mockito.inOrder(tileSet1, tileSet2);

        tileMap.dispose();

        inOrder.verify(tileSet1).dispose();
        inOrder.verify(tileSet2).dispose();
    }
}