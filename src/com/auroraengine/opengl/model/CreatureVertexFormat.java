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

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.opengl.GLVersion;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author LittleRover
 */
@GLVersion(version = 15)
public class CreatureVertexFormat implements VertexFormat {
	private static final Logger LOG = AuroraLogs.getLogger(
					CreatureVertexFormat.class.getName());

	@Override
	public void disable() {
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	@Override
	public void enable() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	@Override
	public int getVertexDynamicSize() {
		return 24;
	}

	@Override
	public int getVertexSize() {
		return 32;
	}

	@Override
	public int getVertexStaticSize() {
		return 8;
	}

	@Override
	public int getVertexStreamSize() {
		return 0;
	}

	@Override
	public void putDynamicData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer) {
		p_vertexes.forEach((v) -> {
			v.position.writeXYZ(p_buffer);
			v.normal.writeXYZ(p_buffer);
		});
	}

	@Override
	public void putStaticData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer) {
		p_vertexes.forEach((v) -> {
			p_buffer.putInt(v.colour.getRGBA());
			v.tex_coords[0].writeXY(p_buffer);
		});
	}

	@Override
	public void putStreamData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer) {

	}

}
