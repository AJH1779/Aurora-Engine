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
package com.auroraengine.opengl.shaders;

/**
 *
 * @author LittleRover
 */
public abstract class GLUniform {

	// TODO: Not implemented, will need focus to be implemented.
	// View the specifications for uniforms and how they should be implmeneted.
	/**
	 * Creates a new object representing a uniform variable within a shader.
	 *
	 * @param name
	 * @param index
	 */
	public GLUniform(String name, int index) {
		this.name = name;
		this.index = index;
	}
	final int index;
	final String name;
}
