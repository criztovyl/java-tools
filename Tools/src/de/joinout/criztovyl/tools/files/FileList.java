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

import java.util.AbstractCollection;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.json.JSONCalendar;
import de.joinout.criztovyl.tools.json.JSONFile;
import de.joinout.criztovyl.tools.json.JSONMap;
import de.joinout.criztovyl.tools.json.creator.JSONCreators;

/**
 * This is a class that holds a list of files and directories in a directory. It also can scan a directory and find all files inside.<br>
 * After scanning, the file list is saved to a file for later compares.<br>
 * You can ignore files which matches an special regular expression.<br>
 * You can store and load this object to JSON.<br>
 * The list also holds when it was last time listed, also for later compares.<br>
 * @author criztovyl
 * 
 */
public class FileList extends AbstractCollection<Path> implements Set<Path>{

	/**
	 * JSON key for the base directory.
	 */
	private static final String JSON_DIR = "directory";

	/**
	 * JSON key for the ignore regular expression.
	 */
	private static final String JSON_IGNORE_REGEX = "ignoreRegex";

	/**
	 * JSON key for the last list date.
	 */
	private static final String JSON_LAST_LIST_DATE = "lastListDate";

	/**
	 * JSON key for the files discovered.
	 */
	private static final String JSON_LIST = "files";
	
	/**
	 * JSON key for the modifications map.
	 */
	private static final String JSON_MODIFICATIONS = "modifications";

	/**
	 * The file name for the JSON file.
	 */
	public static final String JSON_FILE_NAME = ".dirSync.fileList";

	private Map<Path, Calendar> map;

	private Path directory;

	private String ignoreRegex;

	private Logger logger;

	private Calendar lastListDate, listDate;

	private JSONFile jsonFile;
	
	private boolean jsonOnly;

	/**
	 * Creates a new list of files.
	 * 
	 * Adds all files from the directory to the list.
	 * 
	 * @param directory
	 */
	public FileList(Path path){
		this(path, false);
	}
	/**
	 * Creates a new copy of a file list.
	 * 
	 * Does not updates the list.
	 * 
	 * @param fileList
	 */
	public FileList(FileList fileList) {

		// Create set, create new logger and setup other variables
		super();

		//Setup environment
		setupVars(fileList.directory, fileList.ignoreRegex, fileList.jsonOnly);

		map = fileList.map;

		lastListDate = fileList.lastListDate;

		listDate = fileList.listDate;

	}
	/**
	 * Creates a new list of files.
	 * 
	 * Data is taken from JSON, list is not updated.
	 * 
	 * @param json
	 *            the JSON data
	 */
	public FileList(JSONObject json) {

		//Setup collection
		super();

		//Setup environment
		setupVars(new Path(json.getJSONObject(FileList.JSON_DIR)),
				json.getString(FileList.JSON_IGNORE_REGEX), true);

		//If present, set last list date
		if (json.has(FileList.JSON_LAST_LIST_DATE))
			lastListDate = new JSONCalendar(
					json.getJSONObject(FileList.JSON_LAST_LIST_DATE)).getCalendar();

		//Setup files list
		map = new JSONMap<>(json, JSONCreators.PATH, JSONCreators.CALENDAR).getMap();


	}

	/**
	 * Creates a new list of files.
	 * 
	 * Adds all files from the directory to the list or load them from the JSON file within (if exists).
	 * 
	 * The list from JSON would not be updated until you call {@link #add(Path)} or {@link #addAll(Collection)} or similar.
	 * 
	 * @param directory the base directory.
	 * @param loadJSON whether should load all data from JSON only.
	 */
	public FileList(Path directory, boolean loadJSON) {

		// Setup collection
		super();

		//Setup environment
		setupVars(directory, "", loadJSON);

		JSONObject json = jsonFile.getJSONObject();

		//Check if should load data from JSON file
		if(loadJSON){

			if(json.has(JSON_LIST)){
				map = new JSONMap<>(json.getJSONObject(JSON_LIST), JSONCreators.PATH, JSONCreators.CALENDAR).getMap();
			}
			else
				map = new HashMap<>();
		}
		else{

			//Setup map
			map = new HashMap<>();

			// Add all files from directory
			add(getDirectory());
		}

		// Set list date to now
		listDate = Calendar.getInstance();

		// try to set last list date
		if (json.has(FileList.JSON_LAST_LIST_DATE))
			lastListDate = new JSONCalendar(
					json.getJSONObject(FileList.JSON_LAST_LIST_DATE))
		.getCalendar();
		else
			lastListDate = null;

	}

	/**
	 * Creates a new list of files.
	 * 
	 * Data is taken from JSON, list is not updated.
	 * 
	 * @param json
	 *            the JSON data, as a String
	 */
	public FileList(String json) {
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
		// path = path.relativeTo(getDirectory());

		// Boolean for return
		boolean changed = false;

		if (logger.isTraceEnabled())
			logger.trace("Try to add {} ...", path);

		// Check if is file and add to index
		if (path.getFile().isFile()) {

			// Only add if not matches regular expression
			if (!isIgnored(path)) {

				// Add and receive if changed
				changed = null != map.put(path, Calendar.getInstance());

				if (logger.isDebugEnabled())
					logger.debug("Added {}.", path);

			} else if (logger.isInfoEnabled())

				logger.info("{} ignored, matches ignore regex.", path);

			else
				;
		}

		// Check if is directory and add to index. If not ignored, also add the
		// subfiles/-directories
		else if (path.getFile().isDirectory() && !isIgnored(path)) {

			// Add base directory to list and receive if changed
			changed = null != map.put(path, Calendar.getInstance());

			// Iterate over sub-directories and add them too
			for (final String sub : path.getFile().list()) {

				final boolean change = add(path.append(sub));

				// Also receive if changed, keep true if already true
				changed = changed || change;
			}

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
	public boolean addAll(Collection<? extends Path> c) {

		boolean changed = false;

		// Iterate over paths and add via #add
		for (final Path path : c)

			// Receive if changed but keep true if already true
			changed = changed || add(path);

		// Return whether changed
		return changed;
	}

	/**
	 * The base directory
	 * @return a {@link Path}.
	 */
	public Path getDirectory() {
		return directory;
	}

	/**
	 * Creates JSON data from this object.
	 * 
	 * @return a {@link JSONObject}.
	 */
	public JSONObject getJSON() {

		//Create main JSON object
		final JSONObject json = new JSONObject();

		//Put base directory
		json.put(FileList.JSON_DIR, getDirectory().getJSON());

		//Put ignore regular expression
		json.put(FileList.JSON_IGNORE_REGEX, ignoreRegex);

		//Put last list date if not null
		if (lastListDate != null)
			json.put(FileList.JSON_LAST_LIST_DATE, new JSONCalendar(
					lastListDate).getJSON());

		//Store files list/map
		json.put(FileList.JSON_LIST, new JSONMap<>(map, JSONCreators.PATH, JSONCreators.CALENDAR).getJSON());

		//Create JSON object for modifications
		final JSONObject modsJ = new JSONObject();

		//Receive modifications
		Map<String, Path> mods = getMappedHashedModifications();

		//Iterate over keys and store in object
		for(final String hash : mods.keySet())
			modsJ.put(hash, mods.get(hash).getJSON());

		//Store to main object
		json.put(JSON_MODIFICATIONS, modsJ);

		//Return JSON
		return json;
	}

	/**
	 * The date when this list was listed last time.
	 * @return a {@link Calendar} or <code>null</code>, if listed first time.
	 */
	public Calendar getLastListDate() {
		return lastListDate;
	}

	/**
	 * The date when this list was created.
	 * @return a {@link Calendar} or <code>null</code>, if loaded from JSON.
	 */
	public Calendar getListDate() {
		return listDate;
	}

	/**
	 * Generates a map with the file name and modification date hashed together
	 * as key and the {@link Path} as value.<br>
	 * No files will be ignored and the data is taken from the file themselves. 
	 * @see #getMappedHashedModifications(Set, boolean)
	 * @return a map with the hash as {@link String} as key and the {@link Path} as value.
	 */
	public Map<String, Path> getMappedHashedModifications() {
		return getMappedHashedModifications(null, false);
	}

	/**
	 * Generates a map with the file name and modification date hashed together
	 * as key and the {@link Path} as value. If {@link #isJSONonly()} is true, the data is loaded from JSON only.
	 * @param ignore a set which contains {@link Path}s that should be
	 *            ignored. Can be <code>null</code>.
	 * @see #getMappedHashedModifications(Set, boolean)
	 * @return a map with the hash as {@link String} as key and the {@link Path} as value.
	 */
	public Map<String, Path> getMappedHashedModifications(Set<Path> ignore) {
		return getMappedHashedModifications(ignore, jsonOnly);
	}
	/**
	 * Generates a map with the file name and modification date hashed together
	 * as key and the {@link Path} as value.<br>
	 * You can ignore some {@link Path}s.
	 * 
	 * @param ignore
	 *            a set which contains {@link Path}s that should be
	 *            ignored. Can be <code>null</code>.
	 * @param jsonOnly whether data should be loaded from JSON only.
	 * @return a map.
	 */
	public Map<String, Path> getMappedHashedModifications(
			Set<Path> ignore, boolean jsonOnly) {

		final Map<String, Path> mods = new HashMap<>();

		// Create empty set if ignore is null
		if (ignore == null)
			ignore = Collections.EMPTY_SET;

		// Iterate
		for (Path file : map.keySet()){

			//file = getDirectory().append(file);

			// Ignore if is in set
			if (!ignore.contains(file))

				if(jsonOnly){
					if(jsonFile.getJSONObject().has(JSON_MODIFICATIONS)){
						return new JSONMap<>(jsonFile.getJSONObject().getJSONObject(JSON_MODIFICATIONS), JSONCreators.STRING, JSONCreators.PATH).getMap();
					}
					else
						return mods;
				}
				else{
					
					file = getDirectory().append(file);
					// Put with hashed path and modification time as key and
					// FileElement as value
					if(file.getFile().isFile())
					mods.put(DigestUtils.sha1Hex(file.getPath()
							+ Long.toString(file.getFile().lastModified())), file);
				}
		}

		// Return
		return mods;
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

	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	public Iterator<Path> iterator() {
		return map.keySet().iterator();
	}

	/**
	 * Makes all {@link Path}s relative to the base directory ({@link FileList#getDirectory()}.
	 * @return a {@link FileList}.
	 */
	public FileList relative(){

		//Setup file list with given variables
		FileList fl = new FileList(this);

		fl.map = new HashMap<>();

		//Iterate over keys
		for(Path path : map.keySet())

			//Put with relative path and old value
			fl.map.put(path.relativeTo(getDirectory()), map.get(path));

		return fl;

	}
	/**
	 * Saves this to a JSON file. WIll be in in the base directory specified by {@link #getDirectory()} with the file name specified by {@link #JSON_FILE_NAME}.
	 */
	public void save() {
		lastListDate = listDate;
		new JSONFile(getDirectory().append(JSON_FILE_NAME), getJSON()).write();
	}
	/**
	 * Setup the variables for the environment.
	 * 
	 * @param directory the base directory.
	 * @param ignoreRegEx the regular expressions for ignoring files.
	 */
	private void setupVars(Path directory, String ignoreRegEx, boolean jsonOnly) {

		logger = LogManager.getLogger();
		this.directory = directory;
		ignoreRegex = ignoreRegEx;
		jsonFile = new JSONFile(getDirectory().append(JSON_FILE_NAME));
		this.jsonOnly = jsonOnly;
	}
	/* 
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return map.size();
	}
	/**
	 * 
	 * @return true, if data is from JSON only, otherwise false.se
	 */
	public boolean isJSONonly(){
		return jsonOnly;
	}

}
