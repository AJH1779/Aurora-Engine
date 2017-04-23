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
import com.auroraengine.utils.IterativeSupplier;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class GLModel implements GLObject {
	private static final Logger LOG = AuroraLogs
					.getLogger(GLModel.class.getName());

	public GLModel(List<Vertex> vertexes, List<Face> faces, GLMaterial material) {
		this.material = material;
		this.vertex_buffers = new GLVertexBuffers(new IterativeSupplier<>(vertexes));
		this.index_buffer = new GLIndexBuffer(new IterativeSupplier<>(faces));
	}
	private final GLIndexBuffer index_buffer;
	private final GLMaterial material;
	private final GLVertexBuffers vertex_buffers;

	@Override
	public void create()
					throws GLException {
		index_buffer.create();
		vertex_buffers.create();
		material.create(this);
	}

	@Override
	public void destroy() {
		index_buffer.destroy();
		vertex_buffers.destroy();
		material.destroy(this);
	}

	@Override
	public boolean isCreated() {
		return index_buffer.isCreated() && vertex_buffers.isCreated();
	}

	@Override
	public boolean isLoaded() {
		return index_buffer.isLoaded() && vertex_buffers.isLoaded();
	}

	@Override
	public void load()
					throws ClientException {
		index_buffer.load();
		vertex_buffers.load();
		material.load(this);
	}

	@Override
	public void unload()
					throws ClientException {
		index_buffer.unload();
		vertex_buffers.unload();
		material.unload(this);
	}

	@Override
	public void update()
					throws GLException {
		index_buffer.update();
		vertex_buffers.update();
		material.update();
	}

}
