package org.pixel.tiled.view;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pixel.content.ContentManager;
import org.pixel.content.ImportContext;
import org.pixel.content.Texture;
import org.pixel.graphics.Color;
import org.pixel.graphics.render.SpriteBatch;
import org.pixel.math.Rectangle;
import org.pixel.math.Vector2;
import org.pixel.tiled.content.TileMap;
import org.pixel.tiled.content.TileSet;
import org.pixel.tiled.content.importer.TileMapImporter;
import org.pixel.tiled.content.importer.TileSetImporter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.BufferUtils.createByteBuffer;

public class LayerViewTest {

    @Test
    public void drawCase2() throws IOException {
        TileMapImporter importer = new TileMapImporter();
        TileSetImporter tileSetImporter = new TileSetImporter();
        Texture texture1 = Mockito.mock(Texture.class);
        Texture texture2 = Mockito.mock(Texture.class);

        String tmxFileName = "case2.tmx";
        ImportContext ctx = Mockito.mock(ImportContext.class);
        ContentManager contentManager = Mockito.mock(ContentManager.class);

        Mockito.when(contentManager.load(Mockito.eq("Tileset.png"), Mockito.eq(Texture.class))).thenReturn(texture1);
        Mockito.when(contentManager.load(Mockito.eq("Tilese2t.png"), Mockito.eq(Texture.class))).thenReturn(texture2);
        Mockito.when(ctx.getContentManager()).thenReturn(contentManager);

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("Tileset.tsx");

        byte[] bytes = in.readAllBytes();
        ByteBuffer buffer = createByteBuffer(bytes.length);
        buffer.put(bytes).flip();

        Mockito.doReturn(buffer).when(ctx).getBuffer();

        TileSet tileSet1 = tileSetImporter.process(ctx);

        in = this.getClass().getClassLoader().getResourceAsStream("tes3.tsx");

        bytes = in.readAllBytes();
        buffer = createByteBuffer(bytes.length);
        buffer.put(bytes).flip();

        Mockito.doReturn(buffer).when(ctx).getBuffer();

        TileSet tileSet2 = tileSetImporter.process(ctx);

        in = this.getClass().getClassLoader().getResourceAsStream(tmxFileName);

        bytes = in.readAllBytes();
        buffer = createByteBuffer(bytes.length);
        buffer.put(bytes).flip();

        Mockito.doReturn(buffer).when(ctx).getBuffer();
        Mockito.when(contentManager.load(Mockito.matches("Tileset.tsx"), Mockito.eq(TileSet.class))).thenReturn(tileSet1);
        Mockito.when(contentManager.load(Mockito.matches("tes3.tsx"), Mockito.eq(TileSet.class))).thenReturn(tileSet2);
        Mockito.when(ctx.getContentManager()).thenReturn(contentManager);

        TileMap tileMap = importer.process(ctx);

        SpriteBatch spriteBatch = Mockito.mock(SpriteBatch.class);
        LayerView layerView = new LayerView();

        InOrder inOrder = Mockito.inOrder(spriteBatch);

        layerView.draw(spriteBatch, tileMap.getLayers().get(0));

        inOrder.verify(spriteBatch, Mockito.times(0)).draw(Mockito.any(Texture.class),Mockito.eq(new Vector2(0, 0)),
                Mockito.any(Rectangle.class), Mockito.any(Color.class), Mockito.any(Vector2.class), Mockito.anyFloat(), Mockito.anyFloat());
        inOrder.verify(spriteBatch, Mockito.times(0)).draw(Mockito.any(Texture.class),Mockito.eq(new Vector2(16, 0)),
                Mockito.any(Rectangle.class), Mockito.any(Color.class), Mockito.any(Vector2.class), Mockito.anyFloat(), Mockito.anyFloat());
        inOrder.verify(spriteBatch).draw(Mockito.same(texture2), Mockito.eq(new Vector2(0, 8)),
                Mockito.eq(new Rectangle(0, 0, 16, 16)), Mockito.same(Color.WHITE), Mockito.eq(Vector2.HALF),
                Mockito.eq(1f),
                Mockito.eq(0f));
        inOrder.verify(spriteBatch).draw(Mockito.same(texture2), Mockito.eq(new Vector2(16, 8)),
                Mockito.eq(new Rectangle(0, 0, 16, 16)), Mockito.same(Color.WHITE), Mockito.eq(Vector2.HALF),
                Mockito.eq(1f),
                Mockito.eq(0f));
        inOrder.verify(spriteBatch, Mockito.times(0)).draw(Mockito.any(Texture.class),Mockito.eq(new Vector2(0, 16)),
                Mockito.any(Rectangle.class), Mockito.any(Color.class), Mockito.any(Vector2.class), Mockito.anyFloat(), Mockito.anyFloat());
        inOrder.verify(spriteBatch, Mockito.times(0)).draw(Mockito.any(Texture.class),Mockito.eq(new Vector2(16, 16)),
                Mockito.any(Rectangle.class), Mockito.any(Color.class), Mockito.any(Vector2.class), Mockito.anyFloat(), Mockito.anyFloat());

        layerView.draw(spriteBatch, tileMap.getLayers().get(1));

        inOrder.verify(spriteBatch).draw(Mockito.same(texture1), Mockito.eq(new Vector2(0, 0)),
                Mockito.eq(new Rectangle(0, 16, 16, 16)), Mockito.same(Color.WHITE), Mockito.eq(Vector2.HALF),
                Mockito.eq(1f),
                Mockito.eq(0f));
        inOrder.verify(spriteBatch, Mockito.times(0)).draw(Mockito.any(Texture.class),Mockito.eq(new Vector2(16, 0)),
                Mockito.any(Rectangle.class), Mockito.any(Color.class), Mockito.any(Vector2.class), Mockito.anyFloat(), Mockito.anyFloat());
        inOrder.verify(spriteBatch, Mockito.times(0)).draw(Mockito.any(Texture.class),Mockito.eq(new Vector2(0, 8)),
                Mockito.any(Rectangle.class), Mockito.any(Color.class), Mockito.any(Vector2.class), Mockito.anyFloat(), Mockito.anyFloat());
        inOrder.verify(spriteBatch).draw(Mockito.same(texture1), Mockito.eq(new Vector2(16, 8)),
                Mockito.eq(new Rectangle(16, 0, 16, 16)), Mockito.same(Color.WHITE), Mockito.eq(Vector2.HALF),
                Mockito.eq(1f),
                Mockito.eq(0f));
        inOrder.verify(spriteBatch).draw(Mockito.same(texture1), Mockito.eq(new Vector2(0, 16)),
                Mockito.eq(new Rectangle(16, 0, 16, 16)), Mockito.same(Color.WHITE), Mockito.eq(Vector2.HALF),
                Mockito.eq(1f),
                Mockito.eq(0f));
        inOrder.verify(spriteBatch).draw(Mockito.same(texture1), Mockito.eq(new Vector2(16, 16)),
                Mockito.eq(new Rectangle(0, 16, 16, 16)), Mockito.same(Color.WHITE), Mockito.eq(Vector2.HALF),
                Mockito.eq(1f),
                Mockito.eq(0f));
    }
}