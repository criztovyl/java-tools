/**
    This is a part of my tool collection.
    Copyright (C) 2014 Christoph "criztovyl" Schulz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.joinout.criztovyl.tools.log4j;

import de.joinout.criztovyl.tools.file.Path;

/**
 * A Class that sets up a Log4J environment
 * 
 * @author criztovyl
 * 
 */
public class Log4JEnvironment {

	public static final String USER_DIR = "user.home";

	public static final String SYSPROP_KEY = "log4j.configurationFile";

	public static final String DIR = ".log4j";

	public static final String SILENT_FILE = "log4j2.xml";

	public static final String VERBOSE_FILE = "log4j2-test.xml";

	public static final String SWITCH_FILE = "test.rc";

	public static final String NOCFG_FILE = "disable.rc";

	private Path configFile;

	private final Path silent;

	private final Path verbose;

	private final Path switchP;

	private final Path disable;

	public Log4JEnvironment() {
		this(new Path(Log4JEnvironment.USER_DIR, true)
				.append(Log4JEnvironment.DIR), Log4JEnvironment.SILENT_FILE,
				Log4JEnvironment.VERBOSE_FILE, Log4JEnvironment.SWITCH_FILE,
				Log4JEnvironment.NOCFG_FILE);
	}

	/**
	 * Sets up the Log4J environment
	 * 
	 * @param configDir
	 *            the path for the configuration directory
	 * @param silentName
	 *            the path for the configuration file for less logging output
	 * @param verboseName
	 *            the path for the configuration file for verbose logging output
	 * @param switchName
	 *            the path for the file that enables the verbose logging output
	 *            if it exists.
	 * @param noCfgName
	 *            the path for the file that disable all manual configuration if
	 *            it exists.
	 */
	public Log4JEnvironment(Path configDir, String silentName,
			String verboseName, String switchName, String noCfgName) {

		// Set up paths
		silent = configDir.append(silentName);
		verbose = configDir.append(verboseName);
		switchP = configDir.append(switchName);
		disable = configDir.append(noCfgName);
		configFile = null;

		// No environment when disable file exists
		if (!disable.getFile().exists()) {

			// Load test configuration only when present and if test.rc exists
			if (configDir.getFile().exists() && verbose.getFile().exists())
				configFile = verbose;

			// Load silent/normal configuration only when present and if test.rc
			// does _not_ exists
			if (!switchP.getFile().exists() && silent.getFile().exists())
				configFile = silent;
		}

		if (configFile != null)
			System.setProperty(Log4JEnvironment.SYSPROP_KEY, configFile
					.getFile().getPath());
	}

	/**
	 * 
	 * @return the configuration file set by this object, may be null
	 */
	public Path getConfigFile() {
		return configFile;
	}

	/**
	 * @return the disable
	 */
	public Path getDisable() {
		return disable;
	}

	/**
	 * @return the silent
	 */
	public Path getSilent() {
		return silent;
	}

	/**
	 * @return the switchP
	 */
	public Path getSwitchP() {
		return switchP;
	}

	/**
	 * @return the verbose
	 */
	public Path getVerbose() {
		return verbose;
	}
}
