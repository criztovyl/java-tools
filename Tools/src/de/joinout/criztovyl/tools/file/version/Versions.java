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
package de.joinout.criztovyl.tools.file.version;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import de.joinout.criztovyl.tools.file.Path;

/**
 * Archive versions of files
 * 
 * @author criztovyl
 * 
 */
public class Versions extends HashMap<Path, VersionInfo> {
	
	public static final String JSON_PATH = "path";

	public static final String JSON_MAP = "map";
	
	public static final String JSON_PATH_SEPARATOR = "separator";

	/**
	 * 
	 */
	private static final long serialVersionUID = -412046477922750618L;
	private final Path path;
	private final Logger logger;
	private Path lastVersioned;

	/**
	 * Creates a new versions folder at the specified location
	 * 
	 * @param versionsPath
	 *            the location as a {@link Path}
	 */
	public Versions(Path versionsPath) {
		super();
		path = versionsPath;
		logger = LogManager.getLogger();

	}

	/**
	 * Creates a new version of the given file
	 * 
	 * @param path
	 */
	public void addVersion(Path path) {

		// Create new version info for key if key not exists
		if (!containsKey(path))
			put(path, new VersionInfo());

		// Get current time
		final Calendar version = Calendar.getInstance();

		// Add version
		get(path).add(version);

		// Create path to file inside of version directory
		final Path versionPath = this.path.append(get(path).appendVersion(path,
				version));

		if (logger.isTraceEnabled())
			logger.trace("Creating version {} of {} at {}...",
					version.getTimeInMillis(), path, versionPath);

		// Catch IOException
		try {

			// Copy
			if (path.getFile().isFile())
				FileUtils.copyFile(path.getFile(), versionPath.getFile());

			else if (path.getFile().isDirectory())
				FileUtils.copyDirectory(path.getFile(), versionPath.getFile());

			if (logger.isDebugEnabled())
				logger.trace("Versioned.");

			if (logger.isTraceEnabled())
				logger.trace("Setting last versioned to {}", versionPath);

			lastVersioned = versionPath;

		} catch (final IOException e) {

			if (logger.isErrorEnabled())
				logger.error("Exception while copy file {} to {}: {}", path,
						versionPath, e.toString());

			if (logger.isDebugEnabled())
				logger.debug("Caught Exception.", e);

		}
	}

	public JSONObject getJSON(){
	
		//Create object
		JSONObject json = new JSONObject();
		
		//Put path and separator
		json.put(JSON_PATH, getPath().getPath());
		json.put(JSON_PATH_SEPARATOR, getPath().getSeparator());
		
		//Create map object
		JSONObject map = new JSONObject();
		
		//Put entries from map
		for(Path path : this.keySet())
			map.put(path.getPath(json.getString(JSON_PATH_SEPARATOR)), this.get(path).getJSON());
		
		//Put map
		json.put(JSON_MAP, map);
		
		//Tada!
		return json;
	}
	/**
	 * 
	 * @return the path to the last item that was wasted
	 */
	public Path getLastVersioned() {
		return lastVersioned;
	}

	/**
	 * 
	 * @return the path of the versions directory
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * Recovers the specified file version.
	 * 
	 * @param file
	 *            the version file
	 */
	public void recover(Path file) {

		// Create version calendar and set version from file suffix
		final Calendar version = Calendar.getInstance();
		version.setTimeInMillis(Long.parseLong(file.getSuffix()));

		// Remove suffix from path to get real file name
		final Path path = file.relativeTo(this.path).removeSuffix();

		if (logger.isTraceEnabled())
			logger.trace("Recovering {} to {}", file, path);

		// Catch IOException
		try {

			// Copy
			if (file.getFile().isFile())
				FileUtils.copyFile(file.getFile(), path.getFile());
			else if (file.getFile().isDirectory())
				FileUtils.copyDirectory(file.getFile(), path.getFile());

			if (logger.isTraceEnabled())
				logger.trace("Recovered {}.", file);

		} catch (final IOException e) {

			if (logger.isErrorEnabled())
				logger.error(
						"Exception while copy file {} out of version directory ({}): {}",
						file, path, e.toString());

			if (logger.isDebugEnabled())
				logger.debug("Caught Exception.", e);
		}
	}

	/**
	 * Recovers the newest version of the given path.
	 * 
	 * @param path
	 */
	public void recoverLatest(Path path) {
		recover(get(path).appendLatestVersion(path));
	}
}
