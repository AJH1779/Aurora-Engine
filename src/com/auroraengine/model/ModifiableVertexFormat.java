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

import java.nio.ByteBuffer;
import java.util.stream.Stream;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author LittleRover
 */
public class ModifiableVertexFormat implements VertexFormat {

	public ModifiableVertexFormat() {

	}
	private int dynamic_size = 0;
	private int static_size = 12;
	private int stream_size = 0;
	private BufferUsage usage_colour = null;
	private BufferUsage usage_normal = null;
	private BufferUsage usage_position = BufferUsage.STATIC;
	private final BufferUsage[] usage_tex_coords = new BufferUsage[8];

	private void putData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer,
											 BufferUsage p_usage) {
		p_vertexes.forEach((v) -> {
			// TODO: How to pack these in.
			if (usage_position == p_usage) {
				v.position.writeXYZ(p_buffer);
			}
			if (usage_normal == p_usage) {
				v.normal.writeXYZ(p_buffer);
			}
			if (usage_colour == p_usage) {
				// TODO: The different colour formats.
				p_buffer.putInt(v.colour.getRGBA());
			}
			for (int i = 0; i < usage_tex_coords.length; i++) {
				if (usage_tex_coords[i] == p_usage) {
					v.tex_coords[i].writeXY(p_buffer);
				}
			}
		});
	}

	@Override
	public void disable() {
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		if (usage_normal != null) {
			GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		}
		if (usage_colour != null) {
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		}
		for (BufferUsage usage_tex_coord : usage_tex_coords) {
			if (usage_tex_coord != null) {
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				break;
			}
		}
	}

	@Override
	public void enable() {
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		if (usage_normal != null) {
			GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		}
		if (usage_colour != null) {
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		}
		for (BufferUsage usage_tex_coord : usage_tex_coords) {
			if (usage_tex_coord != null) {
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				break;
			}
		}
	}

	@Override
	public int getVertexDynamicSize() {
		return dynamic_size;
	}

	/**
	 * Returns the size of the vertex in this format in bytes.
	 *
	 * @return
	 */
	@Override
	public int getVertexSize() {
		return static_size + stream_size + dynamic_size;
	}

	@Override
	public int getVertexStaticSize() {
		return static_size;
	}

	@Override
	public int getVertexStreamSize() {
		return stream_size;
	}

	@Override
	public void putDynamicData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer) {
		putData(p_vertexes, p_buffer, BufferUsage.DYNAMIC);
	}

	@Override
	public void putStaticData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer) {
		putData(p_vertexes, p_buffer, BufferUsage.STATIC);
	}

	@Override
	public void putStreamData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer) {
		putData(p_vertexes, p_buffer, BufferUsage.STREAM);
	}

	public void setColourUsage(BufferUsage usage) {
		switch (this.usage_colour) {
			case STATIC: {
				static_size -= 4;
				break;
			}
			case DYNAMIC: {
				dynamic_size -= 4;
				break;
			}
			case STREAM: {
				stream_size -= 4;
				break;
			}
		}
		this.usage_colour = usage;
		switch (this.usage_colour) {
			case STATIC: {
				static_size += 4;
				break;
			}
			case DYNAMIC: {
				dynamic_size += 4;
				break;
			}
			case STREAM: {
				stream_size += 4;
				break;
			}
		}
	}

	public void setNormalUsage(BufferUsage usage) {
		// TODO: More efficient normal storage
		switch (this.usage_normal) {
			case STATIC: {
				static_size -= 12;
				break;
			}
			case DYNAMIC: {
				dynamic_size -= 12;
				break;
			}
			case STREAM: {
				stream_size -= 12;
				break;
			}
		}
		this.usage_normal = usage;
		switch (this.usage_normal) {
			case STATIC: {
				static_size += 12;
				break;
			}
			case DYNAMIC: {
				dynamic_size += 12;
				break;
			}
			case STREAM: {
				stream_size += 12;
				break;
			}
		}
	}

	public void setPositionUsage(BufferUsage usage) {
		// TODO: More efficient position storage, e.g. 2-coord
		switch (this.usage_position) {
			case STATIC: {
				static_size -= 12;
				break;
			}
			case DYNAMIC: {
				dynamic_size -= 12;
				break;
			}
			case STREAM: {
				stream_size -= 12;
				break;
			}
		}
		this.usage_position = usage;
		switch (this.usage_position) {
			case STATIC: {
				static_size += 12;
				break;
			}
			case DYNAMIC: {
				dynamic_size += 12;
				break;
			}
			case STREAM: {
				stream_size += 12;
				break;
			}
		}
	}

	public void setTexCoordUsage(BufferUsage usage, int p_index) {
		// TODO: More efficient tex coords
		// TODO: More flexible tex coords (3-vector)
		switch (this.usage_tex_coords[p_index]) {
			case STATIC: {
				static_size -= 8;
				break;
			}
			case DYNAMIC: {
				dynamic_size -= 8;
				break;
			}
			case STREAM: {
				stream_size -= 8;
				break;
			}
		}
		this.usage_tex_coords[p_index] = usage;
		switch (this.usage_tex_coords[p_index]) {
			case STATIC: {
				static_size += 8;
				break;
			}
			case DYNAMIC: {
				dynamic_size += 8;
				break;
			}
			case STREAM: {
				stream_size += 8;
				break;
			}
		}
	}

}
