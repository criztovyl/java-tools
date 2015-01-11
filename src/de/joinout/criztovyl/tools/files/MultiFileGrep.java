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
package de.joinout.criztovyl.tools.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.joinout.criztovyl.tools.file.Path;

/**
 * @author criztovyl
 * 
 */
public class MultiFileGrep {

	private final Logger logger;
	private String regex;
	private List<Path> paths;
	
	/**
	 * Setup with regular expression and path list.
	 * @param regex the regular expression
	 * @param paths the {@link Path} {@link List}
	 */
	public MultiFileGrep(String regex, List<Path> paths) {

		logger = LogManager.getLogger();
		
		this.regex = regex;
		
		this.paths = paths;
		
	}
	/**
	 * Setup with regular expression and some paths.
	 * @param regex the regular expression
	 * @param paths the {@link Path}s
	 */
	public MultiFileGrep(String regex, Path ...paths) {
		this(regex, Arrays.asList(paths));
	}
	/**
	 * Setup with regular expression and some paths as {@link String}s.
	 * @param regex the regular expression
	 * @param paths the {@link Path}s as {@link String}s
	 */
	public MultiFileGrep(String regex, String ...paths){
		
		this(regex, new ArrayList<Path>());
		
		ArrayList<Path> list = new ArrayList<>();
		for(String path_s : paths)
			list.add(new Path(path_s));
		
		this.paths = list;
	}
	/**
	 * Greps from all files and returns a {@link Map} with the matched file as a key and the matching lines as value. 
	 * @return a {@link HashMap} with a {@link Path} as key and a {@link String} {@link List} as value.
	 */
	public HashMap<Path, ArrayList<String>> grep(){

		//Create map
		HashMap<Path, ArrayList<String>> map = new HashMap<>();

		//Iterate over paths
		for (final Path file : paths) {

			try { //Try reading all lines from file

				//Iterate over lines
				for (final String line : FileUtils.readLines(file.getFile())){

					//Check if line matches
					if (line.matches(regex))

						//Add file to map if not present
						if(!map.containsKey(file))
							map.put(file, new ArrayList<String>());

					//Add line
					map.get(file).add(line);
				}

			} catch (final FileNotFoundException e) { // Catch if file not found
				logger.warn("File {} not found!", file);
				logger.debug(e);
			} catch (final IOException e) { // Catch general IOException
				logger.error("IOException!", e);
			}

		}
		
		//Return
		return map;
	}

}
