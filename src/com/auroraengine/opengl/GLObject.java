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
package com.auroraengine.opengl;

import com.auroraengine.client.ClientException;

/**
 *
 * @author LittleRover
 */
public interface GLObject {
	/**
	 * Must be called before create() can be used, unless otherwise specified.
	 * Loads any resources connected to the object, can be called by any thread.
	 *
	 * @throws ClientException
	 */
	public void load()
					throws ClientException;

	/**
	 * Can be called before destroy() is used, removing the data stored on the
	 * non-GL side. load() must be called before create() may be used again.
	 *
	 * @throws ClientException
	 */
	public void unload()
					throws ClientException;

	/**
	 * Returns true if and only if the object can be created using create().
	 *
	 * @return True if the object can be created with create().
	 */
	public boolean isLoaded();

	/**
	 * Must be called to make this object usable by the GL thread.
	 *
	 * @throws GLException
	 */
	public void create()
					throws GLException;

	/**
	 * Returns true if and only if the object can be modified using update() and
	 * destroyed using destroy()
	 *
	 * @return
	 */
	public boolean isCreated();

	/**
	 * Must be called by the GL context whenever the object changes.
	 *
	 * @throws GLException
	 */
	public void update()
					throws GLException;

	/**
	 * Must always be called by a created object on shutdown by the GL thread.
	 */
	public void destroy();
}
