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

/**
 *
 * @author LittleRover
 */
public interface VertexFormat {
	public void disable();

	public void enable();

	public int getVertexDynamicSize();

	/**
	 * Returns the size of the vertex in this format in bytes.
	 *
	 * @return
	 */
	public int getVertexSize();

	public int getVertexStaticSize();

	public int getVertexStreamSize();

	public void putDynamicData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer);

	public void putStaticData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer);

	public void putStreamData(Stream<Vertex> p_vertexes, ByteBuffer p_buffer);

}
