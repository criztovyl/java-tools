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
package de.joinout.criztovyl.tools.file;

import java.io.File;

/**
 * @author criztovyl
 * 
 */
@Deprecated
public class FileName implements Comparable<FileName> {

	private final Path path;

	/**
	 * Creates a new FileName with the given path.
	 * 
	 * @param path
	 *            The Path
	 */
	public FileName(Path path) {
		this.path = path;
	}

	/**
	 * Creates a new file name with given path. Path separator will be
	 * {@link File#separator}.
	 * 
	 * @param path
	 *            The path to the file
	 */
	public FileName(String path) {
		this(path, false, File.separator);
	}

	/**
	 * Creates a new file name with given path that can be a system property
	 * key. Path separator will be {@link File#separator}.
	 * 
	 * @param path
	 *            The path to the file or the system property key
	 * @param sysProp
	 *            Boolean whether path should be system property key
	 */
	public FileName(String path, boolean sysProp) {
		this(path, sysProp, "");
	}

	/**
	 * Creates a new file name with given path that can be a system property key
	 * and the given path separator.
	 * 
	 * @param path
	 *            The path to the file or the system property key
	 * @param systemProp
	 *            Boolean whether path should be system property key
	 * @param separator
	 *            Path separator of given path
	 */
	public FileName(String path, boolean systemProp, String separator) {

		// Set up separator
		separator = systemProp || separator.equals("") ? File.separator
				: separator;

		// Create path string
		path = systemProp ? System.getProperty(path) : path.replaceAll(
				separator + "$", "");

		// Set up path
		this.path = new Path(path, separator);
	}

	/**
	 * Creates a new file name with given path and path separator.
	 * 
	 * @param path
	 *            The path to the file or the system property key
	 * @param separator
	 *            Path separator of given path
	 */
	public FileName(String path, String separator) {
		this(path, false, separator);
	}

	/**
	 * Appends a file to the path.<br>
	 * <i><b>Does not check if path is valid!</b></i>
	 * 
	 * @param file
	 *            the file to be appended.
	 * @return the new FileName
	 */
	public FileName append(String file) {
		return new FileName(path.append(file));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(FileName o) {
		return path.compareTo(o.path);
	}

	/**
	 * Creates a file with the given path with {@link File#separator} as path
	 * separator
	 * 
	 * @return the file from the path
	 */
	public File getFile() {
		return new File(getPath().getPath(File.separator));
	}

	/**
	 * Returns the path to the file with {@link Path#getSeparator()} as path
	 * separator.
	 * 
	 * @return the path to the file
	 */
	public Path getPath() {
		return path;
	}

	@Override
	public String toString() {
		return path.getPath(path.getSeparator());
	}

}
