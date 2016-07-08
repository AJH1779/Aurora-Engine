/*
 * Copyright 2016 AuroraEngine.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.auroraengine.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A file reader for the specified object type.
 *
 * @author Arthur
 * @param <T> The type of object this file reader loads.
 */
public abstract class FileReader<T> {

	private final ArrayList<String> extensions = new ArrayList<>(8);

	/**
	 * The default constructor which creates a reader for the specified file
	 * regex patterns. Typically this is something of the form "*.png".
	 *
	 * @param regexes
	 */
	public FileReader(String[] regexes) {
		for (String regex : regexes) {
			addDefaultFiletype(regex);
		}
	}

	/**
	 * Adds the specified regex to the default file regex patterns.
	 *
	 * @param regex
	 */
	public final void addDefaultFiletype(String regex) {
		this.extensions.add(regex);
	}

	/**
	 * Removes the specified regex from the default file regex patterns.
	 *
	 * @param regex
	 * @return
	 */
	public final boolean removeDefaultFiletype(String regex) {
		return this.extensions.remove(regex);
	}

	/**
	 * Returns true if the file matches one of the default file regex patterns.
	 *
	 * @param f
	 * @return
	 */
	public final boolean isDefaultFiletype(File f) {
		return isDefaultFiletype(f.getName());
	}

	/**
	 * Returns true if the string matches one of the default file regex
	 * patterns.
	 *
	 * @param str
	 * @return
	 */
	public final boolean isDefaultFiletype(String str) {
		return extensions.stream().anyMatch((ext) -> (str.matches(ext)));
	}

	/**
	 * Loads the specified object from the file immediately.
	 *
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public abstract T get(File f) throws IOException;
}
