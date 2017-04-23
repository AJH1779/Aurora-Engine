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
import com.auroraengine.math.Colour;
import com.auroraengine.math.LDVec;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class Vertex {
	private static final Logger LOG = AuroraLogs.getLogger(Vertex.class.getName());

	public Vertex(int p_index, LDVec p_position) {
		this.index = p_index;
		this.position = p_position;
	}
	Colour colour;
	final int index;
	LDVec normal;
	final LDVec position;
	final LDVec[] tex_coords = new LDVec[8];
	// TODO: MAX TEXTURE LAYERS OR SOMETHING.

	public Colour getColour() {
		return colour;
	}

	public LDVec getNormal() {
		return normal;
	}

	public LDVec getPosition() {
		return position;
	}

	public LDVec getTexCoord(int p_index) {
		return tex_coords[p_index];
	}

	public void setColour(Colour p_colour) {
		this.colour = p_colour;
	}

	public void setNormal(LDVec p_normal) {
		this.normal = p_normal;
	}

	public void setTexCoord(LDVec p_tex_coord, int p_index) {
		this.tex_coords[p_index] = p_tex_coord;
	}
}
