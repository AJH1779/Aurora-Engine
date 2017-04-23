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
package com.auroraengine.data;

import com.auroraengine.debug.AuroraLogs;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds the title, version, author, and file data of the program.
 *
 * @author LittleRover
 */
public class ProgramProperties {

	private static final Logger LOG = AuroraLogs
					.getLogger(ProgramProperties.class.getName());
	/**
	 * This is the file in the appdata folder that the game data should be stored
	 * to.
	 */
	@Client
	public static final File APPDATA = getAppDataFile();
	/**
	 * This is the current version of the program as should be placed on
	 * publications.
	 *
	 * TODO: Maintain this String.
	 */
	public static final String AURORA_CORE_VERSION = "InDev V0.1.0";

	private static File getAppDataFile() {
		String temp = System.getProperty("os.name").toLowerCase();
		File file;
		if (temp.contains("win")) {
			temp = System.getenv("APPDATA");
			if (temp == null) {
				temp = System.getProperty("user.home", ".");
			}
			file = new File(temp);
		} else if (temp.contains("mac")) {
			file = new File(System.getProperty("user.home", "."),
											"Library/Application Support/");
		} else if (temp.contains("nix") || temp.contains("sunos")) {
			file = new File(System.getProperty("user.home", "."));
		} else {
			file = new File(System.getProperty("user.home", "."));
		}
		if (!file.exists() && !file.mkdirs()) {
			throw new RuntimeException(
							"The following directory could not be created: " + file);
		}
		LOG.log(Level.INFO, "Default Directory is {0}", file.getAbsolutePath());
		return file;
	}

	/**
	 * Creates a new properties file for the specified program, named and
	 * versioned.
	 *
	 * @param prog_name
	 * @param prog_version
	 */
	public ProgramProperties(String prog_name, String prog_version) {
		this.prog_name = prog_name;
		this.prog_version = prog_version;
		this.prog_dir = new File(APPDATA, "." + prog_name);
		this.spec_dir = new File(prog_dir, "prog_version");
	}
	@Client
	private final File prog_dir;

	private final String prog_name;
	private final String prog_version;
	private final File spec_dir;

	/**
	 * Returns the directory for the program.
	 *
	 * @return
	 */
	@Client
	public File getProgramDirectory() {
		return prog_dir;
	}

	/**
	 * Returns the name of the program.
	 *
	 * @return
	 */
	public String getProgramName() {
		return prog_name;
	}

	/**
	 * Returns the version of the program.
	 *
	 * @return
	 */
	public String getProgramVersion() {
		return prog_version;
	}

	/**
	 * Returns the specific version directory for the program.
	 *
	 * @return
	 */
	@Client
	public File getVersionedDirectory() {
		return spec_dir;
	}
}
