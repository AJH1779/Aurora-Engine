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

import com.auroraengine.debug.AuroraLogs;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author LittleRover
 */
public abstract class Face {
	private static final Logger LOG = AuroraLogs.getLogger(Face.class.getName());

	protected Face(Vertex p_a, Vertex p_b, Vertex p_c) {
		this.vertexes = new Vertex[]{p_a, p_b, p_c};
	}
	private final Vertex[] vertexes;

	public int getIndexCount() {
		return vertexes.length;
	}

	public Stream<Vertex> getVertexStream() {
		return Stream.of(vertexes);
	}
}
