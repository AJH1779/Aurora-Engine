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
package com.auroraengine.client;

import com.auroraengine.data.ProgramProperties;
import java.io.File;
import java.util.logging.Logger;

/**
 * This is a representation of the user session, containing user specific
 * information including program preferences and data management.
 *
 * @author LittleRover
 */
public final class Session {
	private static final Logger LOG = Logger.getLogger(Session.class.getName());

	/**
	 * Creates a new session using the specified username and the provided
	 * properties for the program.
	 *
	 * @param name       The username
	 * @param properties The properties of the program
	 */
	public Session(String name, ProgramProperties properties) {
		this.directory = new File(properties.getProgramDirectory(), "users/" + name);
	}
	private final File directory;

	/**
	 * Returns the file directory specific to the user.
	 *
	 * @return
	 */
	public File getDirectory() {
		return directory;
	}
}
