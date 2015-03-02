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
import java.nio.file.FileSystems;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
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

	protected final String path;

	protected final String separator;

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
	 * @param path the {@link Path}
	 */
	public Path(Path path) {

		this.path = path.path;
		this.separator = path.separator;

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
	 * Pass-through {@link Path} as {@link String} to {@link #append(String)}.
	 * 
	 * @param path the {@link Path}
	 * @return the new {@link Path}
	 * @see #append(String)
	 */
	public Path append(Path path) {
		return append(path.getPath());
	}

	/**
	 * Appends a file to the path.<br>
	 * 
	 * @param file
	 *            the file to be appended.
	 * @return the new {@link Path}
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

			//Check if path string is equal (cast object to Path)
			return getPath(((Path) anObject).getSeparator()).equals(((Path) anObject).getPath());
		}
		//Check if is string
		else if (anObject instanceof String){

			//Check if path string is equal to string
			return getPath().equals((String) anObject);
		}
		else
			return super.equals(anObject);

	}

	/**
	 * Extracts the name after the last separator, e.g. /etc/alternatives ==&gt;
	 * alternatives
	 * 
	 * @return the base name of this path
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
	 * Creates the parent of this path by using {@link #getParent(String)} with {@link #getBasename()}.
	 * @return the parent of this path.
	 * @see #getParent(String)
	 * @see #getBasename()
	 */
	public Path getParent() {
		return getParent(getBasename());
	}
	/**
	 * Creates the parent by specifying a child.
	 * @param child the child as a {@link Path}
	 * @return a {@link Path}
	 */
	public Path getParent(Path child){
		return getParent(child.getPath());
	}
	/**
	 * Creates the parent by specifying a child.
	 * @param child the child as a {@link String}
	 * @return a {@link Path}
	 */
	public Path getParent(String child){
		return new Path(getPath().replaceAll(Pattern.quote(child) + "$", ""),
				getSeparator());
	}

	/**
	 * @return the path with {@link #getSeparator()} as separator and no
	 *         trailing separator.
	 */
	public String getPath() {
		return path.replaceAll(getSeparator() + "$", "");
	}

	/**
	 * Creates a path string with a different separator.
	 * @param separator
	 *            the new separator
	 * @return the path with the given separator without trailing separator.
	 */
	public String getPath(String separator) {
		return path.replace(getSeparator(), separator).replaceAll(
				separator + "$", "");
	}

	/**
	 * @return the used path separator
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
	 * Checks if this path is inside of a directory.
	 * 
	 * @param dir
	 *            the directory
	 * @return true if the path starts with the directory path, otherwise
	 *         false
	 */
	public boolean isInDirectory(Path dir) {
		return getPath().startsWith(dir.getPath(getSeparator()));
	}

	/**
	 * Pass-through {@link String} as {@link Path} to {@link #isInDirectory(Path)}
	 * 
	 * @param dir
	 *            the directory as a string
	 * @return true if path starts with directory path, otherwise false
	 * @see #isInDirectory(Path)
	 */
	public boolean isInDirectory(String dir) {
		return isInDirectory(new Path(dir));
	}

	/**
	 * Creates a new path relative to a specified directory.<br>
	 * 
	 * @param dir
	 *            the directory
	 * @return the relative path or this path with the separator from the given directory.
	 */
	public Path relativeTo(Path dir) {
		//If file is in directory remove directory path from beginning of path, otherwise return this path with the directory separator.
		return isInDirectory(dir) ? new Path(getPath(dir.getSeparator()).replaceAll("^" + dir.getPath(), "").replaceAll("^" + dir.getSeparator(), "")) : new Path(getPath(dir.getSeparator()), dir.getSeparator());
	}

	/**
	 * Removes the last suffix from the path, including the dot ;)
	 * 
	 * @return the new path
	 */
	public Path removeSuffix() {

		// Regular Expressions for the win!
		// Remove the suffix and the dot.

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
	 * Creates a file on this {@link Path} if it not exists.
	 * @throws IOException If an I/O error occurs
	 * @see FileUtils#touch(File)
	 * @see File#exists()
	 */
	public void touch() throws IOException{
		if(!getFile().exists())
			FileUtils.touch(getFile());
	}

	/**
	 * Creates the real path of this path. Mainly resolves .- and ..-Directories and symbolic links.<br>
	 * @return a {@link Path}
	 * @throws IOException If an I/O error occurs.
	 * @see File#getCanonicalPath()
	 */
	public Path realPath() throws IOException{
		return new Path(getFile().getCanonicalPath(), getSeparator());
	}

	/**
	 * @return this {@link Path} as a {@link java.nio.file.Path}.
	 */
	public java.nio.file.Path getNIOPath() {
		return FileSystems.getDefault().getPath(getPath());
	}
}
