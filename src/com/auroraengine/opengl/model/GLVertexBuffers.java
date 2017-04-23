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
public class GLVertexBuffers implements GLObject {
	private static final Logger LOG = AuroraLogs.getLogger(GLVertexBuffers.class
					.getName());

	public GLVertexBuffers(Supplier<Vertex> p_supplier) {
		this.vertex_supplier = p_supplier;
	}
	private ByteBuffer dynamic_bb;
	private int dynamic_index = 0;
	private VertexFormat format;
	private ByteBuffer static_bb;
	private int static_index = 0;
	private ByteBuffer stream_bb;
	private int stream_index = 0;
	private boolean update_dynamic;
	private boolean update_static;
	private final Supplier<Vertex> vertex_supplier;
	private final List<Vertex> vertexes = new LinkedList<>();

	private void updateDynamic() {
		if (dynamic_bb != null) {
			format.putDynamicData(vertexes.stream(), dynamic_bb);
			dynamic_bb.flip();
			update_dynamic = false;
		}
	}

	private void updateStatic() {
		if (static_bb != null) {
			format.putStaticData(vertexes.stream(), static_bb);
			static_bb.flip();
			update_static = false;
		}
	}

	private void updateStream() {
		if (stream_bb != null) {
			format.putStreamData(vertexes.stream(), stream_bb);
			stream_bb.flip();
		}
	}

	@Override
	public void create()
					throws GLException {
		if (dynamic_bb != null && dynamic_index == 0) {
			dynamic_index = GL15.glGenBuffers();
			if (dynamic_index == 0) {
				throw new GLException("Buffer could not be generated.");
			}
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, dynamic_index);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dynamic_bb, GL15.GL_DYNAMIC_DRAW);
		}
		if (stream_bb != null && stream_index == 0) {
			stream_index = GL15.glGenBuffers();
			if (stream_index == 0) {
				throw new GLException("Buffer could not be generated.");
			}
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, stream_index);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, stream_bb, GL15.GL_STREAM_DRAW);
		}
		if (static_bb != null && static_index == 0) {
			static_index = GL15.glGenBuffers();
			if (static_index == 0) {
				throw new GLException("Buffer could not be generated.");
			}
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, static_index);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, static_bb, GL15.GL_STATIC_DRAW);
		}
	}

	@Override
	public void destroy() {
		if (static_index != 0) {
			GL15.glDeleteBuffers(static_index);
		}
		if (stream_index != 0) {
			GL15.glDeleteBuffers(stream_index);
		}
		if (dynamic_index != 0) {
			GL15.glDeleteBuffers(dynamic_index);
		}
	}

	@Override
	public boolean isCreated() {
		return static_index != 0 || dynamic_index != 0 || stream_index != 0;
	}

	@Override
	public boolean isLoaded() {
		return stream_bb != null || dynamic_bb != null || static_bb != null;
	}

	@Override
	public void load()
					throws ClientException {
		if (!isLoaded()) {
			Stream.generate(vertex_supplier).peek((v) -> {
				if (v != null) {
					vertexes.add(v);
				}
			}).anyMatch((v) -> v == null);
			if (format.getVertexDynamicSize() > 0) {
				dynamic_bb = BufferUtils.createByteBuffer(
								format.getVertexDynamicSize() * vertexes.size());
			}
			if (format.getVertexStaticSize() > 0) {
				static_bb = BufferUtils.createByteBuffer(format.getVertexStaticSize() *
																								 vertexes.size());
			}
			if (format.getVertexStreamSize() > 0) {
				stream_bb = BufferUtils.createByteBuffer(format.getVertexStreamSize() *
																								 vertexes.size());
			}
			if (!isLoaded()) {
				throw new ClientException(
								"Provided VertexFormat does not contain any information!");
			}
		}
	}

	public void markDynamicUpdate() {
		this.update_dynamic = true;
	}

	public void markStaticUpdate() {
		this.update_static = true;
	}

	@Override
	public void unload()
					throws ClientException {
		dynamic_bb = null;
		stream_bb = null;
		static_bb = null;
		vertexes.clear();
	}

	@Override
	public void update()
					throws GLException {
		if (update_dynamic) {
			updateDynamic();
		}
		if (update_static) {
			updateStatic();
		}
		updateStream();
	}

}
