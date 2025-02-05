/*
 * This software is available under Apache License
 * Copyright (c) 2020
 */

package org.pixel.graphics.shader;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.pixel.commons.lifecycle.Disposable;

public class VertexArrayObject implements Disposable {

    //region private properties

    private final int id;

    //endregion

    //region constructors

    /**
     * Constructor.
     */
    public VertexArrayObject() {
        id = glGenVertexArrays();
    }

    //endregion

    //region public methods

    /**
     * Get VAO native id.
     *
     * @return The VAO native id.
     */
    public int getID() {
        return this.id;
    }

    /**
     * Bind the VAO.
     */
    public void bind() {
        glBindVertexArray(id);
    }

    /**
     * Unbind the VAO.
     */
    public void unbind() {
        glBindVertexArray(0);
    }

    @Override
    public void dispose() {
        glDeleteVertexArrays(id);
    }

    //endregion
}
