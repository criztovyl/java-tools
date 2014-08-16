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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.file.version.Versions;
import de.joinout.criztovyl.tools.json.JSONCalendar;

/**
 * Represents a set of all files in a directory.
 * 
 * @author criztovyl
 * 
 */
public class DirectoryIndex extends HashSet<Path> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6774992912420672264L;

	public static final String VERSIONS_DIRECTORY = ".versions.directoryIndex";

	public static final String LAST_INDEX_FILE = ".last.directoryIndex";
	
	public static final String JSON_LAST_INDEX_DATE = "lastIndexDate";
	
	public static final String JSON_FILES = "files";
	
	public static final String DIRECTORY = "directory";
	
	public static final String JSON_IGNORE_REGEX = "regex";
	
	public static final String JSON_WASTED_FILES = "wastedFiles";

	private Logger logger;
	private Path directory;
	private String ignoreRegex;
	private HashMap<Path, Calendar> wastedPaths;
	private Modifications modifications;
	private Versions versions;

	/**
	 * Creates a new directory index from a list of files.<br>
	 * All files will be indexed again.<br>
	 * @param files the files list
	 * @param directory the base directory
	 * @param ignoreRegex the regular expression for files which should be ignored
	 */
	public DirectoryIndex(HashSet<Path> files, Path directory,
			String ignoreRegex) {

		// Setup HashSet and add files
		super();
		super.addAll(files);

		// Setup variables
		setupVars(directory, ignoreRegex);

	}

	/**
	 * Creates a new directory index.<br>
	 * Starts indexing immediately.
	 * 
	 * @param directory
	 *            The directory to be indexed
	 * @throws IOException
	 */
	public DirectoryIndex(Path directory) throws IOException {
		this(directory, "");
	}

	/**
	 * Creates a new directory index and ignore all files and folders matching
	 * the given regular expression.<br>
	 * Starts indexing immediality.<br>
	 * <i>The root directory for the indexing is not matched against the regular
	 * expression.</i>
	 * 
	 * @param directory
	 *            The directory to be indexed
	 * @param ignoreRegex
	 *            The regular expression, run on the relative path
	 * @throws IOException
	 */
	public DirectoryIndex(Path directory, String ignoreRegex) {

		// Setup HashSet
		super();

		// Setup variables
		setupVars(directory, ignoreRegex);

		// Start indexing :)
		add(getDirectory());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.HashSet#add(java.lang.Object)
	 */
	@Override
	public boolean add(Path path) {

		//All paths should be relative to this directory.
		path = path.relativeTo(getDirectory());

		//Boolean for return
		boolean changed = false;

		// Ignore versions directory.
		// Return false to cancel further actions.
		if (path.getPath().equals(versions.getPath().relativeTo(getDirectory()))
				|| path.isInDirectory(versions.getPath().relativeTo(getDirectory())))
			return false;

		//Ignore file which indicated when indexed last time. Return false to cancel further actions.
		if(path.getPath().equals(new Path(LAST_INDEX_FILE).relativeTo(getDirectory())))
			return false;

			if (logger.isTraceEnabled())
				logger.trace("Indexing {} ...", path);

		// Check if is file and add to index
		if (path.getFile().isFile()) {

			// Only add if not matches regular expression
			if (!isIgnored(path)) {

				// Add and receive if changed, keep true if already is true
				changed = changed || super.add(path);

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

			//Add base directory to list and receive if changed (keep true if already is true)
			changed = changed || super.add(path);

			//Iterate over sub-directories and add them too
			for (final String sub : path.getFile().list())

				//Also receive if changed
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
	public boolean addAll(Collection<? extends Path> c) {

		boolean changed = false;

		// Iterate over paths and add via #add
		for (final Path path : c)

			//Receive if changed but keep true if already true
			changed = changed || add(path);

		// Return whether changed
		return changed;
	}

	/**
	 * Creates a new directory index by appending a path.<br>
	 * Will transfer the {@link #ignoreRegex}<br>
	 * Won't index again.
	 * 
	 * @param path
	 *            the path to be appended
	 * @return the new directory index.
	 */
	public DirectoryIndex append(String path) {
		return new DirectoryIndex(subIndex(path), getDirectory().append(path), ignoreRegex);
	}

	/**
	 * Locates all elements that are only not in the other set.
	 * 
	 * @param second
	 *            the other set
	 * @return a new {@link HashSet} of paths relative to this directory
	 */
	public HashSet<Path> differences(HashSet<Path> second) {

		// Creat Set (copy set)
		final HashSet<Path> diff = new HashSet<>(this);

		// Delete similarities
		diff.removeAll(intersect(second));

		// Tada!
		return diff;
	}

	/**
	 * Locates all elements that not in the other set.<br>
	 * All paths are appended to {@link #getDirectory()} by
	 * {@link Path#append(Path)} to make the (more) absolute.<br>
	 * (More absolute because {@link #getDirectory()} may also relative).
	 * 
	 * @param second
	 *            the other set
	 * @return a new {@link HashSet} of paths which were made (more) absolute
	 */
	public HashSet<Path> differencesAbsolute(HashSet<Path> second) {

		// Create new set (because of ConcurrentModificationException)
		final HashSet<Path> absoluteDifferences = new HashSet<>();

		// Iterate over differences and make paths absolute
		for (final Path path : differences(second))
			absoluteDifferences.add(getDirectory().append(path));

		// Tada!
		return absoluteDifferences;
	}

	/**
	 * Locates all files that are only in one of the both indexes.
	 * 
	 * @param second
	 *            the second index
	 * @return a {@link HashSet} of paths relative to the index directory.
	 */
	public HashSet<Path> differencesSymmetric(HashSet<Path> second) {

		// Create Set with contents from both sets
		final HashSet<Path> symmDiff = new HashSet<>();
		symmDiff.addAll(this);
		symmDiff.addAll(second);

		// Remove elements which are in both sets
		symmDiff.removeAll(intersect(second));

		// Tada!
		return symmDiff;

	}

	/**
	 * Searches for an path with the given relative path.<br>
	 * Have to be relative to {@link #getDirectory()}.
	 * @param path the path
	 * @return if there is an item with the given relative path, the item, otherwise {@code null}
	 */
	public Path get(Path path){

		//Iterate over all items and return if relative paths are equal
		for(Path item : this)
			if(item.equals(path))
				return item;

		//Not found, return null
		return null;
	}

	/**
	 * Returns the base directory all files are relative to.
	 * 
	 * @return the directory
	 */
	public Path getDirectory() {
		return directory;
	}
	
	public JSONObject getJSON(){
		
		JSONObject json = new JSONObject();
		
		
		
		return json;
	}

	/**
	 * Lists all indexed files and directories in string, one per line, sorted
	 * by path by {@link String#compareTo(String)}.
	 * 
	 * @return A sorted String
	 */
	public String getListString() {

		if (logger.isTraceEnabled())
			logger.trace("Listing all indexed files and directories...");

		// Create String for file list
		String listString = "";
		final String newline = String.format("%n");

		// Iterate over all index files, use tree set for sorting
		for (final Path file : new TreeSet<>(this)) {

			// Add relative path to list
			final String file_s = file.getPath();
			if (!file_s.equals(""))
				listString += file_s + newline;
		}

		// Return list
		return listString.replaceAll(newline + "$", "");
	}

	/**
	 * <i>Will not update the modifications!</i><br>
	 * Use {@link #updateModifications()} to update.
	 * @return the modifications
	 */
	public Modifications getModifications() {

		//Reset
		modifications.clear();

		//Calculate
		modifications.addAll(this);

		//Tada!
		return modifications;
	}

	/**
	 * @return the versions
	 */
	public Versions getVersions() {
		return versions;
	}

	/**
	 * A map where the wasted file path is the key and the wasted time (as
	 * milliseconds) the value.
	 * 
	 * @return the map
	 */
	public HashMap<Path, Calendar> getWastedFiles() {
		return wastedPaths;
	}

	/**
	 * Locates all files that are in both indexes.
	 * 
	 * @param second
	 *            the second index
	 * @return a set of paths relative to the index directory.
	 */
	public HashSet<Path> intersect(HashSet<Path> second) {

		// Create set
		final HashSet<Path> list = new HashSet<>(this);

		// Only keep elements also in second map
		list.retainAll(second);

		// Tada!
		return list;
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

		//Path is again made relative as can be used from external.		
		return ignoreRegex.equals("") ? false : path.relativeTo(getDirectory())
				.getPath(directory.getSeparator()).matches(ignoreRegex);
	}

	/**
	 * Removes a path from the index and creates a last version.
	 * 
	 * @param path
	 *            the relative path
	 * @return this, for chaining
	 */
	public DirectoryIndex remove(Path path) {

		// Create version
		versions.addVersion(path);

		// Put to wasted files
		wastedPaths.put(path, versions.get(path).getLatestVersion());

		// Remove from set
		super.remove(path);

		//For chaining
		return this;
	}

	/**
	 * Sets up the variables needed here.
	 * 
	 * @param directory
	 *            the base directory
	 * @param ignoreRegex
	 *            the regular expression for ignoring files and directories
	 */
	private DirectoryIndex setupVars(Path directory, String ignoreRegex) {

		logger = LogManager.getLogger();
		this.directory = directory;
		this.ignoreRegex = ignoreRegex;
		wastedPaths = new HashMap<>();
		versions = new Versions(getDirectory().append(
				DirectoryIndex.VERSIONS_DIRECTORY));

		//Get date when directory was indexed last time (or leave null if something fails)
		Path lastIndexedDatePath = getDirectory().append(LAST_INDEX_FILE);
		Calendar lastTimeIndexed = null;
		if(lastIndexedDatePath.getFile().exists()){
			try {
				JSONObject json = new JSONObject(FileUtils.readFileToString(lastIndexedDatePath.getFile()));
				
				JSONCalendar jsonCalendar =  new JSONCalendar(json.getJSONObject(JSON_LAST_INDEX_DATE));

				lastTimeIndexed = jsonCalendar.getCalendar();

			} catch (JSONException e) {
				if(logger.isWarnEnabled())
					logger.warn("Something went wrong while parsing the JSON Object.");
				if(logger.isDebugEnabled())
					logger.debug("Caught Exception.", e);				
			} catch (IOException e) {
				if(logger.isWarnEnabled())
					logger.warn("Something went wrong with the last index date file.");
				if(logger.isDebugEnabled())
					logger.debug("Caught exception.", e);
			}
		}
		else{
			try {
				lastTimeIndexed = Calendar.getInstance();

				JSONCalendar jsonCalendar = new JSONCalendar(lastTimeIndexed);
				jsonCalendar.getJSON().write(new FileWriter(lastIndexedDatePath.getFile()));
			} catch (JSONException e) {
				if(logger.isWarnEnabled())
					logger.warn("Something went wrong while creating the JSON Object.");
				if(logger.isDebugEnabled())
					logger.debug("Caught Exception.", e);				
			} catch (IOException e) {
				if(logger.isWarnEnabled())
					logger.warn("Something went wrong with the creation of the last index date file.");
				if(logger.isDebugEnabled())
					logger.debug("Caught exception.", e);
			}
		}

		//Create modifications object
		modifications = new Modifications(getDirectory(), lastTimeIndexed);

		//For chaining
		return this;

	}

	/**
	 * Creates a new directory index of an sub-directory without indexing it
	 * again
	 * 
	 * @param directory
	 *            the sub directory path relative to the directory index
	 * @return a new directory index with the files from the sub-directory
	 */
	public DirectoryIndex subIndex(Path directory) {

		// Create path inside of directory
		directory = getDirectory().append(directory);

		// Create set
		final HashSet<Path> files = new HashSet<>();

		// Iterate over paths
		for (final Path path : this)

			// Check if path is in directory
			if (path.isInDirectory(directory)) {
				if (logger.isDebugEnabled())
					logger.debug("File/Dir {} is in {}, adding to new index",
							path, directory);

				// Add to set
				files.add(path);
			} else if (logger.isDebugEnabled())
				logger.debug("File/Dir {} is not in {}", path, directory);

		// Return new directory index
		return new DirectoryIndex(files, directory, ignoreRegex);
	}

	/**
	 * Pass-through string as path to {@link #subIndex(Path)}
	 * 
	 * @param directory
	 *            the directory
	 * @return a new directory index
	 * @see #subIndex(Path)
	 */
	public DirectoryIndex subIndex(String directory) {
		return subIndex(new Path(directory));
	}

	/**
	 * Updates the modification map.
	 * @return this, for chaining.
	 */
	public DirectoryIndex updateModifications(){
		//Reset
		modifications.clear();

		//Calculate
		modifications.addAll(this);

		//For chaining
		return this;
	}
}
