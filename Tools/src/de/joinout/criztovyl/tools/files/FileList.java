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

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.json.JSONCalendar;
import de.joinout.criztovyl.tools.json.iterator.JSONObjectArrayIterator;

/**
 * @author criztovyl
 * 
 */
public class FileList extends HashSet<FileElement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1421636678029205170L;
	
	private static final String JSON_DIR = "directory";
	
	private static final String JSON_IGNORE_REGEX = "ignoreRegex";
	
	private static final String JSON_LAST_LIST_DATE = "lastListDate";
	
	private static final String JSON_LIST = "files";
	
	public static final String JSON_FILE_NAME = ".dirSync.fileList";

	private Path directory;

	private String ignoreRegex;

	private Logger logger;

	private Calendar lastListDate;

	/**
	 * Creates a new copy of a file list.
	 * 
	 * Does not update the list.
	 * 
	 * @param fileList
	 */
	public FileList(FileList fileList) {

		// Copy set
		super(fileList);

		setupVars(directory, "");

	}

	/**
	 * Creates a new list of files.
	 * 
	 * Adds all files from the directory to the list.
	 * 
	 * @param directory
	 */
	public FileList(Path directory) {
		
		//Setup set
		super();

		setupVars(directory, "");
		
		//Add all files from directory
		add(getDirectory());
		
		//Set last list date to now
		lastListDate = Calendar.getInstance();

	}
	
	/**
	 * Creates a new list of files.
	 * 
	 * Data is taken from JSON, list is not updated.
	 * @param json the JSON data
	 */
	public FileList(JSONObject json){
		
		super();
		
		setupVars(new Path(json.getString(JSON_DIR)), json.getString(JSON_IGNORE_REGEX));
		
		lastListDate = new JSONCalendar(json.getLong(JSON_LAST_LIST_DATE)).getCalendar();
		
		for(JSONObject fe : new JSONObjectArrayIterator(json.getJSONArray(JSON_LIST)))
			super.add(new FileElement(fe));
		
	}
	
	/**
	 * Creates a new list of files.
	 * 
	 * Data is taken from JSON, list is not updated.
	 * @param json the JSON data, as a String
	 */
	public FileList(String json){
		this(new JSONObject(json));
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.HashSet#add(java.lang.Object)
	 */
	@Override
	public boolean add(Path path) {

		// All paths should be relative to this directory.
		path = path.relativeTo(getDirectory());

		// Boolean for return
		boolean changed = false;

		if (logger.isTraceEnabled())
			logger.trace("Indexing {} ...", path);

		// Check if is file and add to index
		if (path.getFile().isFile()) {

			// Only add if not matches regular expression
			if (!isIgnored(path)) {

				// Add and receive if changed
				changed = super.add(new FileElement(path));

				if (logger.isDebugEnabled())
					logger.debug("Indexed {}.", path);

			} else if (logger.isInfoEnabled())

				logger.info("{} ignored, matches ignore regex.", path);

			else
				;
		}

		// Check if is directory and add to index. If not ignored, also add the
		// subfiles/-directories
		else if (path.getFile().isDirectory() && !isIgnored(path)) {

			// Add base directory to list and receive if changed
			changed = super.add(new FileElement(path));

			// Iterate over sub-directories and add them too
			for (final String sub : path.getFile().list())

				// Also receive if changed, keep true if already true
				changed = changed || add(path.append(sub));

		} else
			;

		// Return boolean whether set changed
		return changed;
	}

	/*
	 * Overriding method to also ignore files which matches the regular
	 * expression and added via and collection.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends FileElement> c) {

		boolean changed = false;

		// Iterate over paths and add via #add
		for (final Path path : c)

			// Receive if changed but keep true if already true
			changed = changed || add(path);

		// Return whether changed
		return changed;
	}

	public Path getDirectory() {
		return directory;
	}

	public Calendar getLastListDate() {
		return lastListDate;
	}

	/**
	 * Checks if a path matches the {@link #ignoreRegex}. Will be run on the
	 * relative path.
	 * 
	 * @param path
	 *            the path.
	 * @return true if the relative path matches the regular expression,
	 *         otherwise false
	 */
	public boolean isIgnored(Path path) {

		// Path is made relative again as can be used from external
		return ignoreRegex.equals("") ? false : path.relativeTo(getDirectory())
				.getPath(getDirectory().getSeparator()).matches(ignoreRegex);
	}

	/**
	 * Set variables
	 * @param directory
	 * @param ignoreRegEx
	 */
	private void setupVars(Path directory, String ignoreRegEx) {

		logger = LogManager.getLogger();
		this.directory = directory;
		ignoreRegex = ignoreRegEx;
	}
	
	/**
	 * Creates a JSON object
	 * @return a JSON object with the data of this object
	 */
	public JSONObject getJSON(){
		
		JSONObject json = new JSONObject();
		
		json.put(JSON_DIR, getDirectory());
		
		json.put(JSON_IGNORE_REGEX, ignoreRegex);
		
		json.put(JSON_LAST_LIST_DATE, new JSONCalendar(lastListDate).getJSON());
		
		JSONArray files = new JSONArray();
		
		for(FileElement fe : this)
			files.put(fe.getJSON());
		
		json.put(JSON_LIST, files);
		
		return json;
	}
	/**
	 * Generates a map with the file name and modification date hashed together as key and the {@link FileElement} as value.
	 * @return a map.
	 */
	public Map<String, FileElement> getMappedHashedModifications(){
		
		return getMappedHashedModifications(null);
	}
	/**
	 * Generates a map with the file name and modification date hashed together as key and the {@link FileElement} as value.<br>
	 * You can ignore some {@link FileElement}s.
	 * @param ignore a set which contains {@link FileElement}s that should be ignored
	 * @return a map.
	 */
	public Map<String, FileElement> getMappedHashedModifications(Set<FileElement> ignore){
		
		Map<String, FileElement> map = new HashMap<>();
		
		//Create empty set if ignore is null
		if(ignore == null)
			ignore = Collections.EMPTY_SET;
		
		//Iterate
		for(FileElement fe : this)
			
			//Ignore if is in set
			if(!ignore.contains(fe))
				
				//Put with hashed path and modification time as key and FileElement as value
				map.put(DigestUtils.sha1Hex(fe.getPath() + fe.getFile().lastModified()), fe);
		
		//Return
		return map;
	}

}
