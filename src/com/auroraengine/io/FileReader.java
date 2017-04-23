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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A file reader for the specified object type.
 *
 * @author LittleRover
 * @param <T> The type of object this file reader loads.
 */
public abstract class FileReader<T> {

	/**
	 * The default constructor which creates a reader for the specified file regex
	 * patterns. Typically this is something of the form "*.png".
	 *
	 * @param regexes
	 */
	public FileReader(String[] regexes) {
		for (String regex : regexes) {
			addDefaultFiletype(regex);
		}
	}
	private final ArrayList<String> extensions = new ArrayList<>(8);

	/**
	 * Adds the specified regex to the default file regex patterns.
	 *
	 * @param regex
	 */
	public final void addDefaultFiletype(String regex) {
		this.extensions.add(regex);
	}

	/**
	 * Loads the specified object from the file immediately.
	 *
	 * @param f
	 *
	 * @return
	 *
	 * @throws IOException
	 */
	public abstract T get(File f)
					throws IOException;

	/**
	 * Returns true if the file matches one of the default file regex patterns.
	 *
	 * @param f
	 *
	 * @return
	 */
	public final boolean isDefaultFiletype(File f) {
		return isDefaultFiletype(f.getName());
	}

	/**
	 * Returns true if the string matches one of the default file regex patterns.
	 *
	 * @param str
	 *
	 * @return
	 */
	public final boolean isDefaultFiletype(String str) {
		return extensions.stream().anyMatch((ext) -> (str.matches(ext)));
	}

	/**
	 * Removes the specified regex from the default file regex patterns.
	 *
	 * @param regex
	 *
	 * @return
	 */
	public final boolean removeDefaultFiletype(String regex) {
		return this.extensions.remove(regex);
	}
}
