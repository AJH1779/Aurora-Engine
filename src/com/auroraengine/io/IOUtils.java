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
package com.auroraengine.io;

import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.utils.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class IOUtils {
	private static final Logger LOG = AuroraLogs
					.getLogger(IOUtils.class.getName());

	/**
	 * Returns the contents of the file as a string, any Exceptions thrown wrapped
	 * in an AuroraException.
	 *
	 * @param p_file The file
	 *
	 * @return the contents of the file.
	 *
	 * @throws AuroraException Any wrapped IOExceptions or NoSuchElementExceptions
	 */
	@NotNull
	public static String fileToString(@NotNull File p_file)
					throws AuroraException {
		return pathToString(p_file.toPath());
	}

	/**
	 * Returns the string contained in the object denoted by the provided path.
	 *
	 * @param p_path The path to read from.
	 *
	 * @return The contents of the path as a string.
	 *
	 * @throws AuroraException If an IOException or NoSuchElementException is
	 *                         thrown.
	 */
	public static String pathToString(@NotNull Path p_path)
					throws AuroraException {
		String data = null;
		try (Scanner s = new Scanner(p_path)) {
			data = s.useDelimiter("\\Z").next();
		} catch (IOException | NoSuchElementException ex) {
			LOG.log(Level.WARNING, "Failed to read file: {0}", p_path);
			throw new AuroraException(ex);
		}
		LOG.log(Level.FINE, "Successfully read file: {0}", p_path);
		return data;
	}

	/**
	 * Returns a path denoting the place of the specified resource.
	 *
	 * @param p_resource The name of the resource to load.
	 *
	 * @return The resource path.
	 */
	@NotNull
	public static Path resourceToPath(@NotNull String p_resource)
					throws AuroraException {
		// TODO: Verify that this works correctly when just using the jar.
		try {
			return Paths.get(Thread.currentThread().getContextClassLoader()
							.getResource(p_resource).toURI());
		} catch (URISyntaxException ex) {
			throw new AuroraException(ex);
		}
	}

	private IOUtils() {
	}
}
