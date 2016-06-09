/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.model;

import com.auroraengine.client.ClientException;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLObject;
import java.nio.ByteBuffer;
import java.util.function.Supplier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

/**
 *
 * @author Arthur
 */
public class VertexBuffer implements GLObject {

    public VertexBuffer(Supplier<ByteBuffer> source) {
        this(source, GL15.GL_STATIC_DRAW);
    }

    public VertexBuffer(Supplier<ByteBuffer> source, int permissions) {
        this.source = source;
        this.permissions = permissions;
    }
    private Supplier<ByteBuffer> source;
    private ByteBuffer bb;

    private int index = 0;
    private int permissions;

    public void bind() {
        if (index != 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, index);
        }
    }

    public void enableClientState() {
        // Bindings should be based on the nvidia general requirements
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void disableClientState() {
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    @Override
    public void load() throws ClientException {
        if (bb != null) {
            bb = source.get();
            if (bb == null) {
                throw new ClientException("Failed to load the ByteBuffer!");
            }
        }
    }

    @Override
    public void unload() throws ClientException {
        if (index == 0) {
            bb = null; // Removes the stuff, needs to be sure it is destroyed.
        }
    }

    @Override
    public void create() throws GLException {
        if (index == 0) {
            index = GL15.glGenBuffers();
            if (index == 0) {
                throw new GLException("Buffer could not be generated.");
            }
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, index);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bb, permissions);
        }
    }

    @Override
    public void update() throws GLException {
        // Need to update the buffer if it is able to
        if (index != 0 && permissions != GL15.GL_STATIC_DRAW) {
            // Edit according to the modifications of the buffer.
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, index);
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, bb.position(), bb);
        }
    }

    @Override
    public void destroy() {
        if (index != 0) {
            GL15.glDeleteBuffers(index);
            index = 0;
        }
    }
}
