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

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
 * This is a class that holds a list of files and directories inside a directory. It also can scan a directory and find all files inside.<br>
 * After scanning, the file list can be saved to a file for later use.<br>
 * You can ignore files which matches an regular expression.<br>
 * Object will be saved as JSON.<br>
 * The list also holds when it was last time listed, also for later usage.<br>
 * If a new {@link FileList} is created, normally it will locate all files in the directory it was created on.
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
	 * Creates a new {@link FileList} upon the given {@link Path}.
	 * 
	 * @param path the path
	 * @throws IOException If an I/O error occurs
	 * @see FileList#FileList(Path, boolean)
	 */
	public FileList(Path path) throws IOException{
		this(path, false);
	}
	/**
	 * Creates a copy of a {@link FileList}.
	 * 
	 * @param fileList the {@link FileList}
	 */
	public FileList(FileList fileList) {

		// Create set, create new logger and setup other variables
		super();

		//Copy variables
		
		directory = fileList.directory;

		ignoreRegex = fileList.ignoreRegex;

		jsonOnly = fileList.jsonOnly;

		map = fileList.map;

		lastListDate = fileList.lastListDate;

		listDate = fileList.listDate;
		
		//Set up logger
		
		logger = LogManager.getLogger();

	}
	/**
	 * Loads a {@link FileList} from a {@link JSONObject}.
	 * 
	 * @param json
	 *            the {@link JSONObject}
	 * @see FileList#setupVars(JSONObject)
	 */
	public FileList(JSONObject json) {

		//Set up collection
		super();

		//Set up object
		setupVars(json);

	}

	/**
	 * Creates a new {@link FileList} upon a path or loads it from a {@link JSONObject}.
	 * 
	 * @param directory the path
	 * @param jsonOnly whether should load from {@link JSONObject}
	 * @throws IOException if an I/O error occurs in #setupVars(Path, String, boolean)
	 */
	public FileList(Path directory, boolean jsonOnly) throws IOException {
		this(directory, jsonOnly, "");
	}
	/**
	 * Creates a new {@link FileList} upon the given {@link Path}. Defines a regular exception for excluding files.
	 * @param directory the path
	 * @param ignoreRegex the regular exception
	 * @throws IOException if an I/O error occurs in #setupVars(Path, String, boolean)
	 */
	public FileList(Path directory, String ignoreRegex) throws IOException{
		this(directory, false, ignoreRegex);
	}
	/**
	 * Creates a new {@link FileList} upon a path or loads it from a {@link JSONObject}.
	 * 
	 * @param directory the {@link Path}.
	 * @param jsonOnly whether should load from {@link JSONObject}
	 * @throws IOException if an I/O error occurs in #setupVars(Path, String, boolean)
	 */
	public FileList(Path directory, boolean jsonOnly, String ignoreRegex) throws IOException {

		// Set up collection
		super();

		//Set up variables
		setupVars(directory, "", jsonOnly);
		
		//Set up again, if should load JSON data (first time setup is done because #getDirectory need to been initialised)
		if(jsonOnly)
			setupVars(new JSONFile(getDirectory().append(JSON_FILE_NAME)).getJSONObject());

	}

	/**
	 * Loads a {@link FileList} from a JSON data {@link String}.
	 * 
	 * @param json the {@link String}
	 * @see #FileList(JSONObject)
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

		// Boolean for return
		boolean changed = false;

		if (logger.isTraceEnabled())
			logger.trace("Try to add {} ...", path);

		// Check if is file and add to index
		if (path.getFile().isFile()) {

			// Only add if does not match the regular expression
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

			// Iterate over sub-directories and -files and add them too
			for (final String sub : path.getFile().list()) {

				// Add and receive if changed, keep true if already true
				changed = changed || add(path.append(sub));
			}

		} else
			;

		// Return Boolean whether Set changed
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

	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	public boolean contains(Object o){
		
		if(o instanceof String)
			return contains(new Path((String) o));
		
		else if( o instanceof Path)
			return super.contains((Path) o);
		
		else
			return super.contains(o);
	}
	/**
	 * Returns the base directory.
	 * @return a {@link Path}.
	 */
	public Path getDirectory() {
		return directory;
	}

	/**
	 * Creates a {@link JSONObject} upon this {@link FileList}.
	 * 
	 * @return a {@link JSONObject}.
	 */
	public JSONObject getJSON() {

		//Create main JSON object
		final JSONObject json = new JSONObject();

		//Put base directory as JSON
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
	 * The date this list was listed last time.
	 * @return a {@link Calendar} or <code>null</code>, if listed first time.
	 */
	public Calendar getLastListDate() {
		return lastListDate;
	}

	/**
	 * The date this list was created.
	 * @return a {@link Calendar} or <code>null</code>, if loaded from JSON.
	 */
	public Calendar getListDate() {
		return listDate;
	}

	/**
	 * Pass-through to {@link #getMappedHashedModifications(Set, boolean)} with Set <code>null</code> and boolean {@link #isJSONonly()}.
	 * @see #getMappedHashedModifications(Set, boolean)
	 * @return a map with the hash as {@link String} as key and the {@link Path} as value.
	 */
	public Map<String, Path> getMappedHashedModifications() {
		return getMappedHashedModifications(null, jsonOnly);
	}

	/**
	 * Pass-through to {@link #getMappedHashedModifications(Set, boolean)} with given Set and boolean {@link #isJSONonly()}.
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
	 * 
	 * @param ignore
	 *            a set which contains {@link Path}s that should be
	 *            ignored. Can be <code>null</code>.
	 * @param jsonOnly whether data should be loaded from JSON only.
	 * @return a map with the hash as {@link String} as key and the {@link Path} as value.
	 */
	public Map<String, Path> getMappedHashedModifications(
			Set<Path> ignore, boolean jsonOnly) {

		final Map<String, Path> mods = new HashMap<>();

		// Create empty set if ignore is null
		if (ignore == null)
			ignore = new HashSet<>();
			else
				;

		// JSON data-file should be ignored, adding to list
		ignore.add(getDirectory().append(JSON_FILE_NAME));

		//Check if should use JSON only. If so, load map from JSON file.
		if(jsonOnly){
			if(jsonFile.getJSONObject().has(JSON_MODIFICATIONS)){
				return new JSONMap<>(jsonFile.getJSONObject().getJSONObject(JSON_MODIFICATIONS), JSONCreators.STRING, JSONCreators.PATH).getMap();
			}
			else
				//JSON file has no stored modifications, return empty map.
				return mods;
		}
		else
			// Iterate
			for (Path path : map.keySet()){

				Path pathF = getDirectory().append(path);
				try {
					pathF = pathF.realPath();
				} catch (IOException e) {
					logger.warn("Caught Exception while resolving real path of file {}", path, e);
				}

				// Check if should not ignored
				if (!ignore.contains(path))
					// Put with hashed path and modification time as key and
					// full path as value
					if(pathF.getFile().isFile())
						mods.put(DigestUtils.sha1Hex(path.getPath()
								+ Long.toString(pathF.getFile().lastModified())), pathF);

			}

		// Return
		return mods;
	}
	/**
	 * Calculates the newer of both {@link FileList}s.
	 * @param a one {@link FileList}
	 * @param b another {@link FileList}
	 * @return the newer {@link FileList} or <code>null</code> if one {@link FileList#getLastListDate()} is <code>null</code>.
	 */
	public static FileList getNewerFileList(FileList a, FileList b) {
		
		//Receive Calendars
		final Calendar aCal = a.getLastListDate();
		final Calendar bCal = b.getLastListDate();

		// Return if age is equal or one calendar is null
		if (aCal.compareTo(bCal) == 0 || aCal == null || bCal == null)
			return null;
		

		// Detect and return newer directory
		return aCal.compareTo(bCal) > 0 ? a : b;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	public boolean isEmpty(){
		return super.isEmpty() || size() == 1 && contains(JSON_FILE_NAME);
	}
	/**
	 * Checks if a path matches the {@link #ignoreRegex}. Will be run on the
	 * relative path.
	 * 
	 * @param path
	 *            the path.
	 * @return true if path string matches the regular expression,
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
	 * Saves this to a JSON file. Will be in in the base directory specified by {@link #getDirectory()} with the file name specified by {@link #JSON_FILE_NAME}.<br>
	 * If {@link #jsonOnly} is set, there will be no save.
	 */
	public void save() {
		if(!jsonOnly){
			lastListDate = listDate;
			new JSONFile(getDirectory().append(JSON_FILE_NAME), getJSON()).write();
		}
	}
	/**
	 * Setup the variables for the environment.
	 * 
	 * @param directory the base directory.
	 * @param ignoreRegEx the regular expressions for ignoring files.
	 * @param jsonOnly whether loaded from JSON, if true will not search for files.
	 * @throws IOException If an I/O error occurs when getting real path of the give directory.
	 * @see Path#realPath()
	 */
	private void setupVars(Path directory, String ignoreRegEx, boolean jsonOnly) throws IOException {

		logger = LogManager.getLogger();

		this.directory = directory.realPath();

		ignoreRegex = ignoreRegEx;

		jsonFile = new JSONFile(getDirectory().append(JSON_FILE_NAME));

		this.jsonOnly = jsonOnly;

		map = new HashMap<>();

		lastListDate = null;

		if(!jsonOnly){
			listDate = Calendar.getInstance();

			add(getDirectory());
		}
	}
	/**
	 * Sets up the {@link FileList} from a {@link JSONObject}
	 * @param json
	 */
	private void setupVars(JSONObject json){

		logger = LogManager.getLogger();

		directory = json.has(JSON_DIR) ? new Path(json.getJSONObject(JSON_DIR)) : new Path("");

		ignoreRegex = json.has(JSON_IGNORE_REGEX) ? json.getString(JSON_IGNORE_REGEX) : "";

		jsonFile = new JSONFile(getDirectory().append(JSON_FILE_NAME));

		jsonOnly = true;

		map = json.has(JSON_LIST) ? new JSONMap<>(json.getJSONObject(JSON_LIST), JSONCreators.PATH, JSONCreators.CALENDAR).getMap() : new HashMap<Path, Calendar>();

		listDate = null;

		lastListDate = json.has(FileList.JSON_LAST_LIST_DATE) ? new JSONCalendar(json.getJSONObject(FileList.JSON_LAST_LIST_DATE)).getCalendar() : null;
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
	 * @return true, if data is from JSON only, otherwise false.
	 */
	public boolean isJSONonly(){
		return jsonOnly;
	}

}
