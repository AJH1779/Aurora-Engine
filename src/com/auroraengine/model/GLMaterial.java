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

import com.auroraengine.client.ClientException;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLSharedObject;
import com.auroraengine.opengl.shaders.GLProgram;

/**
 * Denotes an implementation of a shader program with textures for use by a
 * model.
 *
 * @author LittleRover
 */
public class GLMaterial extends GLSharedObject {
	public GLMaterial(GLProgram program) {
		this.program = program;
	}
	private final GLProgram program;
	// Settings for the program
	// Textures?

	@Override
	protected void forceCreate()
					throws GLException {
		program.create(this);
	}

	@Override
	protected void forceDestroy() {
		program.destroy(this);
	}

	@Override
	protected void forceLoad()
					throws ClientException {
		program.load(this);
	}

	@Override
	protected void forceUnload()
					throws ClientException {
		program.unload(this);
	}

	@Override
	public boolean isCreated() {
		return program.isCreated();
	}

	@Override
	public boolean isLoaded() {
		return program.isLoaded();
	}

	@Override
	public void update()
					throws GLException {
		program.update();
	}

}
