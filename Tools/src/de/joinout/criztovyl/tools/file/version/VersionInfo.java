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

import java.util.ArrayList;
import java.util.Calendar;

import de.joinout.criztovyl.tools.file.Path;

/**
 * This class represents a {@link Path} of a file with the deletion dates and versions.
 * @author criztovyl
 *
 */
public class VersionInfo extends ArrayList<Calendar>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7239583617711281110L;
	/**
	 * Creates a new wasted path on the given path string with the given separator.
	 */
	public VersionInfo(){
		
		super();
		 
	}
	/**
	 * Appends the given version to the given path
	 * @param date the date for the version
	 * @param path the path
	 * @return the path of the given version
	 */
	public Path appendVersion(Path path, Calendar date){
		return path.addSuffix(Long.toString(date.getTimeInMillis()));
	}
	/**
	 * Appends the latest version to the given path
	 * @param path the path
	 * @return the path of the latest version
	 */
	public Path appendLatestVersion(Path path){
		return appendVersion(path, get(size() >= 1 ? size()-1 : 0));
	}
	/**S
	 * @return a {@link Calendar} for the version if there is any, otherwise {@code null}
	 */
	public Calendar getLatestVersion(){
		return size() >= 1 ? get(size()-1) : null;
	}
}
