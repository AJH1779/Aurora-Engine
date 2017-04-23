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
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLSharedObject;
import com.auroraengine.opengl.GLVersion;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL20;

/**
 * An object which describes a shader program. Currently not made.
 */
@Client
@GLVersion(version = 20)
public class GLProgram extends GLSharedObject {
	private static final Logger LOG = Logger.getLogger(GLProgram.class.getName());

	public GLProgram(GLShader p_vertex, GLShader p_fragment)
					throws ClientException {
		if (p_vertex.type != ShaderType.VERTEX) {
			throw new ClientException("Invalid Shader: Expected VERTEX, got " +
																p_vertex.type.name());
		}
		if (p_fragment.type != ShaderType.FRAGMENT) {
			throw new ClientException("Invalid Shader: Expected FRAGMENT, got " +
																p_fragment.type.name());
		}
		this.vertex = p_vertex;
		this.fragment = p_fragment;
	}
	private GLShader fragment;
	private int index = 0;
	private boolean loaded = false;
	private GLShader vertex;

	@Override
	public void forceCreate()
					throws GLException {
		if (index != 0) {
			index = GL20.glCreateProgram();
			if (index == 0) {
				throw new GLException("Couldn't create program object.");
			}
			try {
				vertex.create(this);
				fragment.create(this);
			} catch (GLException ex) {
				GL20.glDeleteProgram(index);
				throw ex;
			}
			GL20.glAttachShader(index, vertex.index);
			GL20.glAttachShader(index, fragment.index);
			GLException.checkGL("Attaching Shaders");
			// glBindAttribLocation
			GL20.glLinkProgram(index);
			GLException.checkGL("Linking Program");
		}
	}

	@Override
	public void forceDestroy() {
		if (index != 0) {
			GL20.glDetachShader(index, vertex.index);
			GL20.glDetachShader(index, fragment.index);
			vertex.destroy(this);
			fragment.destroy(this);
			GL20.glDeleteProgram(index);
		}
	}

	@Override
	public void forceLoad()
					throws ClientException {
		if (!loaded) {
			vertex.load(this);
			fragment.load(this);
			loaded = true;
		}
	}

	@Override
	public void forceUnload()
					throws ClientException {
		if (loaded) {
			vertex.unload(this);
			fragment.unload(this);
			loaded = false;
		}
	}

	@Override
	public boolean isCreated() {
		return index != 0;
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Sets this program as being currently in use.
	 *
	 * Note that this may not be the best way of using this.
	 *
	 * @throws GLException
	 */
	@Override
	public void update()
					throws GLException {
		GL20.glUseProgram(index);
	}
}
