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
import java.io.FileNotFoundException;
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
	 * @param f The file
	 *
	 * @return the contents of the file.
	 *
	 * @throws AuroraException Any wrapped IOExceptions or NoSuchElementExceptions
	 */
	@NotNull
	public static String fileToString(@NotNull File f)
					throws AuroraException {
		String data = null;
		try (Scanner s = new Scanner(f)) {
			data = s.useDelimiter("\\0").next(); // TODO: Verify the regex
		} catch (FileNotFoundException | NoSuchElementException ex) {
			LOG.log(Level.WARNING, "Failed to read file: {0}", f.getAbsolutePath());
			throw new AuroraException(ex);
		}
		LOG.log(Level.FINE, "Successfully read file: {0}", f.getAbsolutePath());
		return data;
	}

	private IOUtils() {
	}
}
