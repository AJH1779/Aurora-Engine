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
import com.auroraengine.debug.AuroraLogs;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Denotes an object that will be shared between multiple GLObjects.
 *
 * @author LittleRover
 */
public abstract class GLSharedObject implements GLObject {
	private static final Logger LOG = AuroraLogs.getLogger(GLSharedObject.class
					.getName());
	private final HashSet<GLObject> created_users = new HashSet<>();
	private final HashSet<GLObject> loaded_users = new HashSet<>();

	/**
	 * Forcibly creates this object. Should not generally be called.
	 *
	 * @throws GLException If an exception occurs during creation.
	 */
	protected abstract void forceCreate()
					throws GLException;

	/**
	 * Forcibly destroys this object but does not destroy dependencies. In general
	 * this should not be called.
	 */
	protected abstract void forceDestroy();

	/**
	 * Forcibly loads this object. Should not generally be called.
	 *
	 * @throws ClientException If an exception occurs during loading.
	 */
	protected abstract void forceLoad()
					throws ClientException;

	// TODO: Remove exceptions from unloading.
	/**
	 * Forcibly unloads this object but does not unload dependencies. In general,
	 * this should not be called.
	 *
	 * @throws ClientException If an exception occurs during unloading.
	 */
	protected abstract void forceUnload()
					throws ClientException;

	/**
	 * Adds the provided object to the list of created dependents of this object
	 * and creates this object if it hasn't already been created. The user must be
	 * a loaded dependent of this object.
	 *
	 * @param user The user
	 *
	 * @throws GLException If the provided object is not a loaded dependent, or
	 *                     there was an exception thrown during creation.
	 */
	public void create(GLObject user)
					throws GLException {
		if (!created_users.contains(user)) {
			throw new GLException(
							"User is not a loader!");
		}
		if (created_users.add(user)) {
			forceCreate();
		}
	}

	/**
	 * Should not be called.
	 *
	 * @throws GLException
	 *
	 * @deprecated Active use of this instead of unload(GLObject) indicates an
	 * error.
	 */
	@Override
	@Deprecated
	public final void create()
					throws GLException {
		// TODO: Exception go here? AssertionError?
		LOG.warning("Will not create object on this request - Shared.");
	}

	/**
	 * Removes the provided user as a dependency of this and destroys this object
	 * if it has no more created dependents.
	 *
	 * @param user
	 */
	public void destroy(GLObject user) {
		if (created_users.remove(user) && created_users.isEmpty()) {
			forceDestroy();
		}
	}

	/**
	 * Attempts to destroy this, but will fail if there still exist dependencies.
	 *
	 * @deprecated Active use of this instead of unload(GLObject) indicates an
	 * error.
	 */
	@Override
	@Deprecated
	public final void destroy() {
		if (created_users.isEmpty()) {
			forceDestroy();
		} else {
			LOG.log(Level.WARNING,
							"Attempted to destroy a shared object, a good idea to check this.");
		}
	}

	/**
	 * Returns a stream of the created dependents on this object.
	 *
	 * @return
	 */
	public Stream<GLObject> getCreatedDependents() {
		return created_users.stream();
	}

	/**
	 * Returns a stream of the loaded dependents on this object.
	 *
	 * @return
	 */
	public Stream<GLObject> getLoadedDependents() {
		return loaded_users.stream();
	}

	/**
	 * Adds the provided object to the list of loaded dependents of this object
	 * and creates this object if it hasn't already been created. The user must be
	 * a loaded dependent of this object to become a created dependent.
	 *
	 * @param user The user
	 *
	 * @throws GLException If the there was an exception thrown during loading.
	 */
	public void load(GLObject user)
					throws ClientException {
		if (!loaded_users.contains(user)) {
			throw new ClientException(
							"User is not a loader!");
		}
		if (loaded_users.add(user) && !isCreated()) {
			forceCreate();
		}
	}

	/**
	 * Shouldn't be called.
	 *
	 * @throws ClientException
	 *
	 * @deprecated Active use of this instead of unload(GLObject) indicates an
	 * error.
	 */
	@Override
	@Deprecated
	public final void load()
					throws ClientException {
		LOG.warning("Will not load object on this request - Shared.");
	}

	/**
	 * Removes the provided object from the list of loaded dependents of this
	 * object and unloads this object if there are resultantly no dependents. The
	 * user must not be a created dependent of this object.
	 *
	 * @param user The user
	 *
	 * @throws ClientException If the user is a created dependent or if an
	 *                         exception was thrown during unloading.
	 */
	public void unload(GLObject user)
					throws ClientException {
		if (created_users.contains(user)) {
			throw new ClientException(
							"Must call destroy(GLObject) before calling unload(GLObject)");
		}
		if (loaded_users.remove(user) && loaded_users.isEmpty()) {
			forceUnload();
		}
	}

	/**
	 * Shouldn't be called.
	 *
	 * @throws ClientException
	 *
	 * @deprecated Active use of this instead of unload(GLObject) indicates an
	 * error.
	 */
	@Override
	@Deprecated
	public final void unload()
					throws ClientException {
		if (loaded_users.isEmpty()) {
			forceUnload();
		} else {
			LOG.log(Level.WARNING,
							"Attempted to unload something which still has loaded dependencies.");
		}
	}
}
