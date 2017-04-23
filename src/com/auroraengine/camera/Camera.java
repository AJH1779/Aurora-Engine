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
package com.auroraengine.camera;

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.entity.Entity;
import com.auroraengine.opengl.shaders.ShaderLibrary;
import com.auroraengine.world.LocalPosition;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class Camera extends Entity {
	private static final Logger LOG = AuroraLogs.getLogger(Camera.class.getName());

	/**
	 * Creates a new camera at the specified position.
	 *
	 * TODO: Implement this properly as an entity.
	 *
	 * TODO: Implement relevant constructors.
	 *
	 * @param pos The position of the camera
	 */
	public Camera(LocalPosition pos) {
		super(pos);
	}
	private ShaderLibrary shader_library;

	// TODO: Frustrum
	// TODO: Camera
	@Override
	public void update(double delt) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
