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

import de.joinout.criztovyl.tools.file.Path;

/**
 * Archive versions of files
 * @author criztovyl
 *
 */
public class Versions extends HashMap<Path, VersionInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -412046477922750618L;
	private Path path;
	private Logger logger;
	private Path lastVersioned;

	/**
	 * Creates a new versions folder at the specified location
	 * @param versionsPath the location as a {@link Path}
	 */
	public Versions(Path versionsPath){
		super();
		this.path = versionsPath;
		logger = LogManager.getLogger();

	}

	/**
	 * 
	 * @return the path of the versions directory
	 */
	public Path getPath(){
		return path;
	}

	/**
	 * Wastes a file
	 * @param path
	 */
	public void addVersion(Path path){

		//Create new version info for key if key not exists
		if(!containsKey(path))
			put(path, new VersionInfo());
		
		//Get current time
		Calendar version = Calendar.getInstance();
		
		//Add version
		get(path).add(version);
		
		//Create path to file inside of version directory
		Path versionPath = this.path.append(get(path).appendVersion(path, version));
		
		if(logger.isTraceEnabled())
			logger.trace("Creating version {} of {} at {}...", version.getTimeInMillis(), path, versionPath);

		//Catch IOException
		try{

			//Move
			if(path.getFile().isFile())
				FileUtils.moveFile(path.getFile(), versionPath.getFile());

			else if(path.getFile().isDirectory())
				FileUtils.moveDirectory(path.getFile(), versionPath.getFile());

			if(logger.isDebugEnabled())
				logger.trace("Versioned.");
			
			if(logger.isTraceEnabled())
				logger.trace("Setting last versioned to {}", versionPath);
			
			lastVersioned = versionPath;

		} catch(IOException e){

			if(logger.isErrorEnabled())
				logger.error("Exception while move file {} to {}: {}", path, versionPath, e.toString());

			if(logger.isDebugEnabled())
				logger.debug("Catched Exception.", e);

		}
	}

	/**
	 * Recovers the specified file version.
	 * @param file the version file
	 */
	public void recover(Path file){
		
		//Create version calendar and set version from file suffix
		Calendar version = Calendar.getInstance();
		version.setTimeInMillis(Long.parseLong(file.getSuffix()));

		//Remove suffix from path to get real file name
		Path path = file.relativeTo(this.path).removeSuffix();

		if(logger.isTraceEnabled())
			logger.trace("Recovering {} to {}", file, path);

		//Catch IOException
		try{

			//Move
			if(file.getFile().isFile())
				FileUtils.moveFile(file.getFile(), path.getFile());
			else if(file.getFile().isDirectory())
				FileUtils.moveDirectory(file.getFile(), path.getFile());

			if(logger.isTraceEnabled())
				logger.trace("Recovered {}.", file);

		} catch(IOException e){

			if(logger.isErrorEnabled())
				logger.error("Exception while move file {} out of version directory ({}): {}", file, path, e.toString());

			if(logger.isDebugEnabled())
				logger.debug("Catched Exception.", e);
		}
	}
	/**
	 * Recovers the newest version of the given path.
	 * @param path
	 */
	public void recoverLatest(Path path){
		recover(get(path).appendLatestVersion(path));
	}
	/**
	 * 
	 * @return the path to the last item that was wasted
	 */
	public Path getLastVersioned(){
		return lastVersioned;
	}
}
