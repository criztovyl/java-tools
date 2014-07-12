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
package de.joinout.criztovyl.tools.directory.index;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.joinout.criztovyl.tools.file.Path;
/**
 * A class for doing thing with directory indexes
 * @author criztovyl
 *
 */
public class DirectoryIndexTools {
	
	private String separator, regex;
	
	/**
	 * Creates a new class with the given separator and regular expression.
	 * @param separator
	 * @param regex
	 */
	public DirectoryIndexTools(String separator, String regex){
		this.separator = separator;
		this.regex = regex;
	}
	
	/**
	 * Creates a list of directory indexes from a list of paths.
	 * 
	 * @param paths
	 *            the list of paths
	 * @return the list of the directory indexes
	 */
	public ArrayList<DirectoryIndex> indexFromPathList(ArrayList<String> paths) {
		
		//Get logger
		Logger logger = LogManager.getLogger();

		if (logger.isTraceEnabled())
			logger.trace("Setting up DiretoryIndexes from a path list...");

		// Create list for indexes been created
		final ArrayList<DirectoryIndex> indexes = new ArrayList<DirectoryIndex>();

		if (logger.isTraceEnabled())
			logger.trace("Iterating over paths...");

		// Iterate over all paths, create directory index and add to index list
		// if file exists
		for (final String path : paths)

			// Add to index list if file exists
			if (new Path(path, separator).getFile().exists()) {

				if (logger.isTraceEnabled())
					logger.trace("Creating DirectoryIndex...");

				indexes.add(new DirectoryIndex(new Path(path, separator), regex));

				if (logger.isDebugEnabled())
					logger.debug("Created DirectoryIndex for {}", path);
			}

			else

			if (logger.isWarnEnabled())
				logger.warn("No such file or directory '{}'!", path);
		
		// return indexes
		return indexes;
	}

}
