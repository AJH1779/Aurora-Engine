/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.data;

import com.auroraengine.debug.AuroraLogs;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds the title, version, author, and file data of the program.
 * @author Arthur
 */
@Client
public class ProgramProperties {

	private static final Logger LOG = AuroraLogs.getLogger(ProgramProperties.class);
	public static final String AURORA_CORE_VERSION = "InDev V0.1.0";
	@Client
	public static final File APPDATA = getAppDataFile();

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

	private final String prog_name, prog_version;
	@Client
	private final File prog_dir, spec_dir;

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
	 * Returns the directory for the program.
	 *
	 * @return
	 */
	@Client
	public File getProgramDirectory() {
		return prog_dir;
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
