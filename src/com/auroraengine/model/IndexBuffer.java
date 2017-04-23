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
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLObject;
import java.nio.IntBuffer;
import java.util.function.Supplier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

/**
 * The index buffer is an integer buffer which holds the primitive data in
 * reference to a vertex buffer.
 *
 * @author LittleRover
 */
public class IndexBuffer implements GLObject {
	/**
	 * Creates a new index buffer using the supplier of the buffer. A supplier is
	 * used to allow for loading from a file or generating by algorithm using
	 * another thread and maintaining thread safety.
	 *
	 * @param source
	 */
	public IndexBuffer(Supplier<IntBuffer> source) {
		this(source, GL15.GL_STATIC_DRAW);
	}

	public IndexBuffer(Supplier<IntBuffer> source, int permissions) {
		this.source = source;
		this.permissions = permissions;
	}
	private IntBuffer ib;
	private int index = 0;
	private int permissions;
	private Supplier<IntBuffer> source;

	public void bind() {
		if (index != 0) {
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, index);
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
			GLException.checkGL("Generating Buffer");
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, index);
			GLException.checkGL("Binding Buffer");
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ib, permissions);
			GLException.checkGL("Setting Buffer Data");
		}
	}

	@Override
	public void destroy() {
		if (index != 0) {
			GL15.glDeleteBuffers(index);
			index = 0;
		}
	}

	public void draw() {
		GL11.glDrawElements(GL11.GL_TRIANGLES, ib.remaining(), GL11.GL_UNSIGNED_INT,
												ib.position());
	}

	@Override
	public boolean isCreated() {
		return index != 0;
	}

	@Override
	public boolean isLoaded() {
		return ib != null;
	}

	@Override
	public void load()
					throws ClientException {
		if (ib != null) {
			ib = source.get();
			if (ib == null) {
				throw new ClientException("Failed to load the IndexBuffer!");
			}
		}
	}

	@Override
	public void unload()
					throws ClientException {
		ib = null; // Removes the stuff, needs to be sure it is destroyed.
	}

	@Override
	public void update()
					throws GLException {
		// Need to update the buffer if it is able to
		if (index != 0 && permissions != GL15.GL_STATIC_DRAW) {
			// Edit according to the modifications of the buffer.
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, index);
			GLException.checkGL("Binding Buffer");
			GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, ib.position(), ib);
			GLException.checkGL("Setting Buffer Data");
		}
	}

}
