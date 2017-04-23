/*
 * Copyright (C) 2017 LittleRover
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.auroraengine.opengl.model;

import com.auroraengine.client.ClientException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLObject;
import java.nio.ByteBuffer;
import java.util.function.Supplier;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

/**
 *
 * @author LittleRover
 */
public class VertexBuffer implements GLObject {
	private static final Logger LOG = AuroraLogs.getLogger(VertexBuffer.class
					.getName());

	public VertexBuffer(Supplier<ByteBuffer> source) {
		this(source, GL15.GL_STATIC_DRAW);
	}

	public VertexBuffer(Supplier<ByteBuffer> source, int permissions) {
		this.source = source;
		this.permissions = permissions;
	}
	private ByteBuffer bb;
	private int index = 0;
	private int permissions;
	private Supplier<ByteBuffer> source;

	public void bind() {
		if (index != 0) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, index);
		}
	}

	@Override
	public void create()
					throws GLException {
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
	public void destroy() {
		if (index != 0) {
			GL15.glDeleteBuffers(index);
			index = 0;
		}
	}

	public void disableClientState() {
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	public void enableClientState() {
		// Bindings should be based on the nvidia general requirements
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	@Override
	public boolean isCreated() {
		return index > 0;
	}

	@Override
	public boolean isLoaded() {
		return bb != null;
	}

	@Override
	public void load()
					throws ClientException {
		if (bb == null) {
			bb = source.get();
			if (bb == null) {
				throw new ClientException("Failed to load the ByteBuffer!");
			}
		}
	}

	@Override
	public void unload()
					throws ClientException {
		if (index == 0) {
			bb = null; // Removes the stuff, needs to be sure it is destroyed.
		}
	}

	@Override
	public void update()
					throws GLException {
		// Need to update the buffer if it is able to
		if (index != 0 && permissions != GL15.GL_STATIC_DRAW) {
			// Edit according to the modifications of the buffer.
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, index);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, bb.position(), bb);
		}
	}
}
