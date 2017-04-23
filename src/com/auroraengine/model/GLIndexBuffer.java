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
package com.auroraengine.model;

import com.auroraengine.client.ClientException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLObject;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

/**
 *
 * @author LittleRover
 */
public class GLIndexBuffer implements GLObject {
	private static final Logger LOG = AuroraLogs.getLogger(GLIndexBuffer.class
					.getName());

	public GLIndexBuffer(Supplier<Face> p_supplier) {
		this.face_supplier = p_supplier;
	}
	private final Supplier<Face> face_supplier;
	private int index;
	private IntBuffer index_buffer;
	final List<Face> faces = new LinkedList<>();
	final BufferUsage usage = BufferUsage.STATIC;

	@Override
	public void create()
					throws GLException {
		if (index_buffer != null && index == 0) {
			index = GL15.glGenBuffers();
			if (index == 0) {
				throw new GLException("Buffer could not be generated.");
			}
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, index);
			GL15.glBufferData(index, index_buffer, usage.gl_draw);
		}
	}

	@Override
	public void destroy() {
		if (index != 0) {
			GL15.glDeleteBuffers(index);
		}
	}

	@Override
	public boolean isCreated() {
		return index != 0;
	}

	@Override
	public boolean isLoaded() {
		return index_buffer != null;
	}

	@Override
	public void load()
					throws ClientException {
		if (!isLoaded()) {
			int[] index_count = {0};
			Stream.generate(face_supplier).peek((f) -> {
				if (f != null) {
					faces.add(f);
					index_count[0] += f.getIndexCount();
				}
			}).anyMatch((f) -> f == null);
			index_buffer = BufferUtils.createIntBuffer(index);
			if (!isLoaded()) {
				throw new ClientException("IndexBuffer failed to work properly!");
			}
		}
	}

	@Override
	public void unload()
					throws ClientException {
		index_buffer = null;
		faces.clear();
	}

	@Override
	public void update()
					throws GLException {
		faces.forEach((f) -> f.getVertexStream().forEach((v) -> index_buffer.put(
						v.index)));
		index_buffer.flip();
	}

}
