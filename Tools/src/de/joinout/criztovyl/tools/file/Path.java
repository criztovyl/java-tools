/**
    This is a part of a program useful for files and directories.
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
package de.joinout.criztovyl.tools.file;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

/**
 * An object, that holds an path and it's separator.
 * 
 * @author criztovyl
 * 
 */
public class Path implements Comparable<Path>{

	private static final String JSON_PATH = "path";

	private static final String JSON_SEPARATOR = "seperator";

	protected String path;

	protected String separator;

	/**
	 * Creates a new path from a JSON object
	 * 
	 * @param json
	 *            the JSON object
	 */
	public Path(JSONObject json) {

		path = json.getString(Path.JSON_PATH);
		separator = json.getString(Path.JSON_SEPARATOR);

	}

	/**
	 * Creates a new copy of a path
	 * 
	 * @param path
	 */
	public Path(Path path) {

		this.path = path.path;
		separator = path.separator;

	}

	/**
	 * Creates a new path with {@link File#separator} as separator.
	 * 
	 * @param path
	 *            the file path
	 */
	public Path(String path) {
		this(path, File.separator);
	}

	/**
	 * Creates a new path from the given file path that can be a key for
	 * {@link System#getProperty(String)}. Separator is {@link File#separator}
	 * 
	 * @param path
	 *            the path
	 * @param sysPropKey
	 *            if true, path will be fetched from the given system property,
	 *            otherwise use path as normal file path
	 */
	public Path(String path, boolean sysPropKey) {
		this(path, sysPropKey, File.separator);
	}

	/**
	 * Creates a new path from the given file path that can be a key for
	 * {@link System#getProperty(String)} with the given separator
	 * 
	 * @param path
	 *            the path
	 * @param sysPropKey
	 *            if true, path will be fetched from the given system property,
	 *            otherwise use path as normal file path
	 * @param separator
	 *            the separator
	 */
	public Path(String path, boolean sysPropKey, String separator) {

		if (separator == null)
			separator = "";

		// Set up separator
		this.separator = sysPropKey || separator.equals("") ? File.separator
				: separator;

		// Create path string
		this.path = sysPropKey ? System.getProperty(path) : path.replaceAll(
				separator + "$", "");
	}

	/**
	 * Creates a new path with the given separator.
	 * 
	 * @param path
	 *            the path
	 * @param separator
	 *            the separator
	 */
	public Path(String path, String separator) {
		this(path, false, separator);
	}

	/**
	 * Creates a new Path with the given suffix from this path, will be appended
	 * with a preceding dot.<br>
	 * Example:<br>
	 * Add suffix "old" to /home/lotta/file will lead to /home/lotta/file.old
	 * 
	 * @param suffix
	 *            the suffix
	 * @return the path with the suffix
	 */
	public Path addSuffix(String suffix) {
		return new Path(getPath() + "." + suffix, getSeparator());
	}

	/**
	 * See {@link #append(String)}
	 * 
	 * @param path
	 * @return see {@link #append(String)}
	 */
	public Path append(Path path) {
		return append(path.getPath());
	}

	/**
	 * Appends a file to the path.<br>
	 * <i><b>Does not check if path is valid!</b></i>
	 * 
	 * @param file
	 *            the file to be appended.
	 * @return the new Path
	 */
	public Path append(String file) {
		return new Path(path + getSeparator() + file, getSeparator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Path o) {
		return getPath().compareTo(o.getPath());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object anObject) {
		
		if(this == anObject)
			return true;
		
		//Check if is Path
		if(anObject instanceof Path){
			
			//Cast
			Path aPath = (Path) anObject;
			
			//Check if path string is equal
			return getPath(aPath.getSeparator()).equals(aPath.getPath());
		}
		//Check if is string
		else if (anObject instanceof String){
			
			//Check if path string is equal to string
			return getPath().equals((String) anObject);
		}
		else
			return false;
		
	}

	/**
	 * Extracts the name after the last separator, e.g. /etc/alternatives ==>
	 * alternatives
	 * 
	 * @return the base name of this directory
	 */
	public String getBasename() {
		final String[] split = getPath().split(getSeparator());
		return split[split.length - 1];
	}

	/**
	 * 
	 * @return the file represented by the path. File may not exists.
	 */
	public File getFile() {
		return new File(getPath(File.separator));
	}

	/**
	 * Creates a JSON object
	 * 
	 * @return the JSON data of this object
	 */
	public JSONObject getJSON() {

		final JSONObject json = new JSONObject();

		json.put(Path.JSON_PATH, path);

		json.put(Path.JSON_SEPARATOR, separator);

		return json;
	}

	/**
	 * 
	 * @return the parent of this path.
	 */
	public Path getParent() {
		return new Path(getPath().replaceAll(getBasename() + "$", ""),
				getSeparator());
	}

	/**
	 * 
	 * @return the path with {@link #getSeparator()} as separator and no
	 *         trailing separator.
	 */
	public String getPath() {
		return path.replaceAll(getSeparator() + "$", "");
	}

	/**
	 * 
	 * @param separator
	 *            The separator
	 * @return the path with the given separator and no trailing separator.
	 */
	public String getPath(String separator) {
		return path.replace(getSeparator(), separator).replaceAll(
				separator + "$", "");
	}

	/**
	 * 
	 * @return the separator used in the path
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @return the suffix of this path
	 */
	public String getSuffix() {
		// Extract suffix by removing everything before the last "." in the path
		// (the dot of the suffix)
		return getPath().replaceAll("^.*\\.", "");
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode(){
		return getPath().hashCode();
	}

	/**
	 * Checks if this path is inside of a directory
	 * 
	 * @param dir
	 *            the directory
	 * @return true if the path starts with the given directory path, otherwise
	 *         false
	 */
	public boolean isInDirectory(Path dir) {
		return getPath().startsWith(dir.getPath(getSeparator()));
	}

	/**
	 * Pass-through string as path to {@link #isInDirectory(Path)}
	 * 
	 * @param dir
	 *            the directory as a string
	 * @return true if path is in directory, otherwise false
	 * @see #isInDirectory(Path)
	 */
	public boolean isInDirectory(String dir) {
		return isInDirectory(new Path(dir));
	}

	/**
	 * Creates a new path relative to a specified directory.<br>
	 * May the path will not made relative, if path is not inside the given directory.
	 * 
	 * @param dir
	 *            the directory
	 * @return the relative path
	 */
	public Path relativeTo(Path dir) {
		return new Path(getPath(dir.getSeparator()).replaceAll(
				"^" + dir.getPath(), ".").replaceAll("^" + dir.getSeparator(),
				""));
	}

	/**
	 * Removes the last suffix from the path
	 * 
	 * @return the new path
	 */
	public Path removeSuffix() {

		// Regular Expressions for the win!
		// and the remove the suffix and the dot.

		return new Path(getPath().replaceAll("\\." + getSuffix() + "$", ""));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPath(File.separator);
	}
	
	/**
	 * Creates the real path of this path. Mainly resolves .- and ..-Directories and symbolic links.<br>
	 * @return a {@link Path}
	 * @throws IOException see {@link File#getCanonicalPath()}.
	 */
	public Path realPath() throws IOException{
		return new Path(getFile().getCanonicalPath(), getSeparator());
	}
}
