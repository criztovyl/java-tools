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
import java.util.TreeSet;

import org.json.JSONArray;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.json.JSONCalendar;
import de.joinout.criztovyl.tools.json.iterator.JSONLongArrayIterator;

/**
 * This class represents a {@link Path} of a file with the deletion dates and
 * versions.
 * 
 * @author criztovyl
 * 
 */
public class VersionInfo extends ArrayList<Calendar>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7239583617711281110L;

	/**
	 * Creates a new empty version info.
	 */
	public VersionInfo() {

		//Setup list
		super();

	}
	
	/**
	 * Creates a new  version info from JSON data.
	 * @param json the json data
	 */
	public VersionInfo(JSONArray json){
		
		TreeSet<Calendar> set = new TreeSet<>();
		
		for(Long lonq : new JSONLongArrayIterator(json))
			set.add(new JSONCalendar(lonq).getCalendar());
		
		this.addAll(set);
		
		
	}

	/**
	 * Appends the latest version to the given path
	 * 
	 * @param path
	 *            the path
	 * @return the path of the latest version
	 */
	public Path appendLatestVersion(Path path) {
		return appendVersion(path, get(size() >= 1 ? size() - 1 : 0));
	}

	/**
	 * Appends the given version to the given path
	 * 
	 * @param date
	 *            the date for the version
	 * @param path
	 *            the path
	 * @return the path of the given version
	 */
	public Path appendVersion(Path path, Calendar date) {
		return path.addSuffix(Long.toString(date.getTimeInMillis()));
	}

	/**
	 * 
	 * @return a {@link Calendar} for the version if there is any, otherwise
	 *         {@code null}
	 */
	public Calendar getLatestVersion() {
		return size() >= 1 ? get(size() - 1) : null;
	}
	
	/**
	 * Creates a JSON array of this object.
	 * @return a {@link JSONArray}
	 */
	public JSONArray getJSON(){
		
		//Create array
		JSONArray json = new JSONArray();

		//Iterate over elements
		for(Calendar cal : this)
			
			//Store
			json.put(new JSONCalendar(cal).getJSON());
		
		//Tada!
		return json;
	}
}
