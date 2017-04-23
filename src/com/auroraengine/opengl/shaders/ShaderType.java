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

import com.auroraengine.debug.AuroraException;
import com.auroraengine.opengl.GLVersion;
import com.auroraengine.utils.NotNull;
import java.util.stream.Stream;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

/**
 * An enumeration of the shader types: Vertex, Fragment, Geometry, etc..
 *
 * @author LittleRover
 */
public enum ShaderType {
	@GLVersion(version = 20)
	VERTEX(GL20.GL_VERTEX_SHADER, "vert"),
	@GLVersion(version = 20)
	FRAGMENT(GL20.GL_FRAGMENT_SHADER, "frag"),
	@GLVersion(version = 32)
	GEOMETRY(GL32.GL_GEOMETRY_SHADER, "geom"),
	@GLVersion(version = 40)
	TESS_CONTROL(GL40.GL_TESS_CONTROL_SHADER),
	@GLVersion(version = 40)
	TESS_EVALUATION(GL40.GL_TESS_EVALUATION_SHADER),
	@GLVersion(version = 43)
	COMPUTE(GL43.GL_COMPUTE_SHADER);
	// TODO: The other shader types.
	// TODO: All shader extensions.

	ShaderType(int p_gl_type, @NotNull String... p_extensions) {
		this.gl_type = p_gl_type;
		this.extensions = p_extensions;
	}
	/**
	 * Returns the type of the shader as a GL code. The values are
	 * GL_VERTEX_SHADER, GL_FRAGMENT_SHADER, etc..
	 */
	public final int gl_type;
	@NotNull
	private final String[] extensions;

	/**
	 * Returns the shader type that is denoted by the given extension or throws an
	 * exception if there is not a matching one.
	 *
	 * TODO: Ensure all valid extensions are registered.
	 *
	 * @param ext The extension to check.
	 *
	 * @return The associated shader type.
	 *
	 * @throws AuroraException If no shader type was found.
	 */
	@NotNull
	public static ShaderType fromExtension(@NotNull String ext)
					throws AuroraException {
		return Stream.of(ShaderType.values()).filter((s) -> Stream.of(s.extensions)
						.anyMatch((str) -> str.equals(ext))).findAny().orElseThrow(()
						-> new AuroraException("Invalid Extension Provided: " + ext));
	}
}
