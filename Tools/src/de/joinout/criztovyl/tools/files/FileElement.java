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

import org.json.JSONObject;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.json.JSONCalendar;

/**
 * A {@link Path} with a date when the file was seen last time.
 * @author criztovyl
 * 
 */
public class FileElement extends Path {

	private static String JSON_PATH = "path";

	private static String JSON_DATE_LAST_SEEN = "lastSeen";

	private Calendar lastSeen;

	/**
	 * Create a file element from JSON data.
	 * @param json the JSON data
	 */
	public FileElement(JSONObject json) {

		//Setup path
		super(json.getJSONObject(FileElement.JSON_PATH));

		//Setup variables
		//If JSON data contains date when file was last see, use it, otherwise use now
		setupVars(json.has(JSON_DATE_LAST_SEEN) ? new JSONCalendar(json.getLong(JSON_DATE_LAST_SEEN)).getCalendar() : Calendar.getInstance());

	}

	/**
	 * Creates a new file element for a path
	 * @param path the path
	 */
	public FileElement(Path path) {

		// Setup path
		super(path);

		//Setup variables, set date when file was last seen to now
		setupVars(Calendar.getInstance());
	}

	/**
	 * Creates a new file element for a path string
	 * @param path the path string
	 */
	public FileElement(String path) {

		// Setup path
		super(path);

		//Setup variables, set date when file was last seen to now
		setupVars(Calendar.getInstance());

	}
	/**
	 * Checks if a object is the same
	 * @param other the other object
	 * @return true if path is equal, false otherwise
	 */
	public boolean equals(FileElement other) {
		return super.equals(other);
	}
	/**
	 * 
	 * @return the date when the file was seen last time
	 */
	public Calendar getLastSeen(){
		return lastSeen;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.file.Path#getJSON()
	 */
	public JSONObject getJSON() {

		final JSONObject json = new JSONObject();

		//Put date when file was last seen
		json.put(JSON_DATE_LAST_SEEN, new JSONCalendar(
				getLastSeen()).getJSON());


		//Put path
		json.put(FileElement.JSON_PATH, super.getJSON());

		return json;
	}

	/**
	 * Sets up variables
	 * @param lastSeen
	 */
	private void setupVars(Calendar lastSeen) {

		this.lastSeen = lastSeen;

	}

}
