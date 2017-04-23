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

import static org.lwjgl.opengl.GL15.*;

/**
 *
 * @author LittleRover
 */
public enum BufferUsage {
	STATIC(GL_STATIC_DRAW),
	DYNAMIC(GL_DYNAMIC_DRAW),
	STREAM(GL_STREAM_DRAW);

	BufferUsage(int p_gl_draw) {
		gl_draw = p_gl_draw;
	}
	public final int gl_draw;
}
