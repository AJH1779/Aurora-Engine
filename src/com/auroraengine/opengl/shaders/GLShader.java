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

import com.auroraengine.client.ClientException;
import com.auroraengine.data.Client;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.io.IOUtils;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLSharedObject;
import com.auroraengine.opengl.GLVersion;
import com.auroraengine.utils.NotNull;
import com.auroraengine.utils.Nullable;
import java.io.File;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lwjgl.opengl.GL20;

/**
 * An object which describes a shader object.
 */
@Client
@GLVersion(version = 20)
public class GLShader extends GLSharedObject {
	private static final Logger LOG = Logger.getLogger(GLShader.class.getName());

	/**
	 * Creates a new shader using the specified file. The extension of the file
	 * determines the type of the shader.
	 *
	 * @param p_file The file to read.
	 *
	 * @throws ClientException If the file extension does not correspond to a
	 *                         known shader type.
	 */
	public GLShader(@NotNull File p_file)
					throws ClientException {
		this.file = p_file;

		try {
			this.type = ShaderType.fromExtension(p_file.getName()
							.substring(0, p_file.getName().lastIndexOf('.')));
		} catch (AuroraException ex) {
			throw new ClientException(ex);
		}
	}

	/**
	 * Creates a new shader of the specified type using the provided code. The
	 * code may be altered after use. Use this for testing shaders.
	 *
	 * @param p_code The shader code.
	 * @param p_type The type of shader.
	 */
	public GLShader(@NotNull String p_code, @NotNull ShaderType p_type) {
		this.code = p_code;
		this.type = p_type;
	}
	@Nullable
	private String code;
	@Nullable
	private File file;
	int index = 0;
	@NotNull
	final ShaderType type;
	final HashSet<GLUniform> uniforms = new HashSet<>();

	private void compile()
					throws GLException {
		try {
			GL20.glShaderSource(index, code);
			GLException.checkGL("Sourcing Shader");
			GL20.glCompileShader(index);
			GLException.checkGL("Compiling Shader");
		} catch (GLException ex) {
			GL20.glDeleteShader(index);
			throw ex;
		}
		Matcher m = UNIFORM_PATTERN.matcher(code);
	}
	private static final Pattern UNIFORM_PATTERN = Pattern
					.compile("[^;]*uniform [^;]*");

	@Override
	protected void forceCreate()
					throws GLException {
		if (index == 0) {
			index = GL20.glCreateShader(type.gl_type);
			if (index == 0) {
				throw new GLException("Failed to create shader!");
			}
			compile();
		}
	}

	@Override
	protected void forceDestroy() {
		if (index != 0) {
			GL20.glDeleteShader(index);
			index = 0;
		}
	}

	@Override
	protected void forceLoad()
					throws ClientException {
		if (code == null) {
			try {
				code = IOUtils.fileToString(file);
			} catch (AuroraException ex) {
				throw new ClientException(ex);
			}
		}
	}

	@Override
	protected void forceUnload()
					throws ClientException {
		if (file != null) {
			code = null;
		}
	}

	/**
	 * Returns the code used for this shader.
	 *
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Returns the index of the shader.
	 *
	 * @return the index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Returns the type of this shader.
	 *
	 * @return The shader type.
	 */
	public ShaderType getShaderType() {
		return type;
	}

	@Override
	public boolean isCreated() {
		return index != 0;
	}

	@Override
	public boolean isLoaded() {
		return code != null;
	}

	/**
	 * Sets the code to the specified new string if it was not obtained using a
	 * file, returning true if the code was set, false otherwise. If the shader
	 * has already been created, the code will be compiled and an exception thrown
	 * if there is an error.
	 *
	 * @param p_code The new code
	 *
	 * @return True if the code was set, false otherwise.
	 *
	 * @throws GLException If when attempting to compile the code, there is an
	 *                     exception thrown.
	 */
	public boolean setCode(String p_code)
					throws GLException {
		if (file == null) {
			this.code = p_code;
			if (isCreated()) {
				compile();
			}
			return true;
		}
		return false;
	}

	@Override
	public void update()
					throws GLException {
		// TODO: Update for a shader? Is it necessary?
	}
}
