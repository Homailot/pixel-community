/*
 * This software is available under Apache License
 * Copyright (c) 2020
 */

package org.pixel.content;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import org.pixel.commons.annotations.Nullable;
import org.pixel.commons.lifecycle.Disposable;
import org.pixel.commons.logger.Logger;
import org.pixel.commons.logger.LoggerFactory;
import org.pixel.commons.util.IOUtils;
import org.pixel.content.importer.ByteBufferImporter;
import org.pixel.content.importer.FontImporter;
import org.pixel.content.importer.TextImporter;
import org.pixel.content.importer.TextureImporter;
import org.pixel.content.importer.TexturePackImporter;
import org.pixel.content.importer.VorbisAudioImporter;
import org.pixel.content.importer.settings.ContentImporterSettings;
import org.pixel.pipeline.DataPipeline;

public class ContentManager implements Disposable {

    private static final Logger log = LoggerFactory.getLogger(ContentManager.class);

    private final ConcurrentHashMap<String, Object> assetCache;
    private final ConcurrentHashMap<Class<?>, ContentImporter<?>> importers;

    private DataPipeline<ByteBuffer> dataPipeline;

    /**
     * Constructor. By default, includes all internal importers.
     */
    public ContentManager() {
        this.assetCache = new ConcurrentHashMap<>();
        this.importers = new ConcurrentHashMap<>();
        this.addInternalImporters();
    }

    /**
     * Constructor. By default, it only includes the given importers.
     *
     * @param importers The importers to include.
     */
    public ContentManager(ContentImporter<?>... importers) {
        this.assetCache = new ConcurrentHashMap<>();
        this.importers = new ConcurrentHashMap<>();
        addContentImporter(importers);
    }

    /**
     * Dispose function
     */
    @Override
    public void dispose() {
        assetCache.forEach((assetName, asset) -> {
            // dispose all "disposable" assets
            if (asset instanceof Disposable) {
                ((Disposable) asset).dispose();
            }
        });

        importers.forEach((importerName, importer) -> {
            // dispose all "disposable" assets
            if (importer instanceof Disposable) {
                ((Disposable) importer).dispose();
            }
        });
    }

    /**
     * Init function
     */
    private void addInternalImporters() {
        // default importers:
        this.addContentImporter(
                new TextureImporter(),
                new VorbisAudioImporter(),
                new FontImporter(),
                new TextImporter(),
                new TexturePackImporter(),
                new ByteBufferImporter());
    }

    /**
     * Get a reference to the asset cache.
     *
     * @param filename The filename of the asset.
     * @param type     The type of the asset.
     * @return A reference to the asset cache.
     */
    private String getCacheReference(String filename, Class<?> type) {
        return type.getCanonicalName() + ":" + filename;
    }

    /**
     * Add a custom content importer to the content manager.
     *
     * @param importers The importer(s) to add.
     */
    public void addContentImporter(ContentImporter<?>... importers) {
        for (ContentImporter<?> importer : importers) {
            var importerDetails = importer.getClass().getAnnotation(ContentImporterInfo.class);
            if (importerDetails == null) {
                log.warn("Unable to add content importer without ContentImporter annotation.");
                return;
            }

            log.trace("Adding content importer '{}' to ContentManager.", importer.getClass().getSimpleName());

            this.importers.put(importerDetails.type(), importer);
        }
    }

    /**
     * Load an image resource file with a custom importer settings - supports both absolute and relative paths (based on
     * the project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public Texture loadTexture(String filepath) {
        return load(filepath, Texture.class);
    }

    /**
     * Load an image resource file with a custom importer settings - supports both absolute and relative paths (based on
     * the project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @param settings The settings to use for the importer.
     * @param useCache Whether to use the cache or not.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public Texture loadTexture(String filepath, @Nullable ContentImporterSettings settings, boolean useCache) {
        return load(filepath, Texture.class, settings, useCache);
    }

    /**
     * Load a texture pack resource file with a custom importer settings - supports both absolute and relative paths
     * (based on the project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public TexturePack loadTexturePack(String filepath) {
        return load(filepath, TexturePack.class);
    }

    /**
     * Load a texture pack resource file with a custom importer settings - supports both absolute and relative paths
     * (based on the project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @param settings The settings to use for the importer.
     * @param useCache Whether to use the cache or not.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public TexturePack loadTexturePack(String filepath, @Nullable ContentImporterSettings settings, boolean useCache) {
        return load(filepath, TexturePack.class, settings, useCache);
    }

    /**
     * Load an audio resource file with a custom importer settings - supports both absolute and relative paths (based on
     * the project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public Sound loadSound(String filepath) {
        return load(filepath, Sound.class);
    }

    /**
     * Load a sound resource file with a custom importer settings - supports both absolute and relative paths (based on
     * the project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @param settings The settings to use for the importer.
     * @param useCache Whether to use the cache or not.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public Sound loadSound(String filepath, @Nullable ContentImporterSettings settings, boolean useCache) {
        return load(filepath, Sound.class, settings, useCache);
    }

    /**
     * Load a font resource file with a custom importer settings - supports both absolute and relative paths (based on
     * the project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public Font loadFont(String filepath) {
        return load(filepath, Font.class);
    }

    /**
     * Load a font resource file with a custom importer settings - supports both absolute and relative paths (based on
     * the project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @param settings The settings to use for the importer.
     * @param useCache Whether to use the cache or not.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public Font loadFont(String filepath, @Nullable ContentImporterSettings settings, boolean useCache) {
        return load(filepath, Font.class, settings, useCache);
    }

    /**
     * Load a resource file - supports both absolute and relative paths (based on the project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @param type     The class type of the resource.
     * @param <T>      The type of the resource.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public <T> T load(String filepath, Class<T> type) {
        return load(filepath, type, null);
    }

    /**
     * Load a resource file with a custom importer settings - supports both absolute and relative paths (based on the
     * project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @param type     The class type of the resource.
     * @param settings The settings to use for the importer.
     * @param <T>      The type of the resource.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    public <T> T load(String filepath, Class<T> type, @Nullable ContentImporterSettings settings) {
        return load(filepath, type, settings, true);
    }

    /**
     * Load a resource file with a custom importer settings - supports both absolute and relative paths (based on the
     * project resource folder).
     *
     * @param filepath The filepath of the resource - supports both relative and absolute paths.
     * @param type     The class type of the resource.
     * @param settings The settings to use for the importer.
     * @param useCache Determines whether to use the asset cache or not.
     * @param <T>      The type of the resource.
     * @return The loaded resource or null if the resource could not be loaded.
     */
    @SuppressWarnings("unchecked")
    public <T> T load(String filepath, Class<T> type, @Nullable ContentImporterSettings settings, boolean useCache) {
        String assetRef = getCacheReference(filepath, type);
        if (useCache && assetCache.containsKey(assetRef)) {
            Object o = assetCache.get(assetRef);
            if (type.isInstance(o)) {
                return (T) assetCache.get(assetRef);
            }
        }

        ContentImporter<T> fileImporter = (ContentImporter<T>) this.importers.get(type);
        if (fileImporter == null) {
            log.warn("Unable to load asset due to unavailable importer for '{}'.", type.getCanonicalName());
            return null;
        }

        ByteBuffer resourceData = IOUtils.loadFile(filepath);
        if (resourceData == null) {
            log.warn("Unable to load asset '{}'; target could not be found.", filepath);
            return null;
        }

        if (dataPipeline != null) {
            try {
                resourceData = dataPipeline.begin(resourceData).get();

            } catch (InterruptedException | ExecutionException e) {
                log.error("Unable to execute data pipeline on asset '{}'.", filepath, e);
                return null;
            }
        }

        ImportContext ctx = new ImportContext(this, resourceData, filepath, settings);
        T asset = fileImporter.process(ctx);
        if (useCache && asset != null) {
            assetCache.put(assetRef, asset);
        }

        return asset;
    }

    /**
     * Get the data pipeline configured (if any).
     *
     * @return The data pipeline.
     */
    public DataPipeline<ByteBuffer> getDataPipeline() {
        return dataPipeline;
    }

    /**
     * Set the data pipeline for this instance. If defined, this pipeline allows custom processing of any asset data
     * before it's passed to the importer.
     *
     * @param dataPipeline The data pipeline to use.
     */
    public void setDataPipeline(DataPipeline<ByteBuffer> dataPipeline) {
        this.dataPipeline = dataPipeline;
    }
}
