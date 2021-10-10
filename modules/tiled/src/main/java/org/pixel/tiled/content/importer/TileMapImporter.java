/*
 * This software is available under Apache License
 * Copyright (c) 2020
 */

package org.pixel.tiled.content.importer;

import org.pixel.commons.logger.Logger;
import org.pixel.commons.logger.LoggerFactory;
import org.pixel.commons.util.IOUtils;
import org.pixel.commons.util.TextUtils;
import org.pixel.content.*;
import org.pixel.tiled.content.TileMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.text.AbstractDocument;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

@ContentImporterInfo(type = TileMap.class, extension = ".tmx")
public class TileMapImporter implements ContentImporter<TileMap> {
    PixelContentManager contentManager;
    private static final Logger LOG = LoggerFactory.getLogger(TileMapImporter.class);

    public TileMapImporter(PixelContentManager contentManager) {
        this.contentManager = contentManager;
    }

    public TileMapImporter() {
        this.contentManager = new ContentManager();
    }

    @Override
    public TileMap process(ImportContext ctx) {
        Document tmxDoc;

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            ByteBuffer bb = ctx.getBuffer();

            byte[] array = new byte[bb.limit()];
            bb.get(array, 0, bb.limit());
            bb.flip();

            tmxDoc = documentBuilder.parse(new ByteArrayInputStream(array, bb.position(), bb.limit()));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();

            return null;
        }

        TileMap tileMap = new TileMap();
        tmxDoc.getDocumentElement().normalize();

        Element mapElement = tmxDoc.getDocumentElement();

        tileMap.setHeight(Integer.parseInt(mapElement.getAttribute("height")));
        tileMap.setWidth(Integer.parseInt(mapElement.getAttribute("width")));

        //TexturePack texturePack = contentManager.load()

        return tileMap;
    }
}
