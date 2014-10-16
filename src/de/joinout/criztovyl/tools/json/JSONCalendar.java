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
package de.joinout.criztovyl.tools.json;

import java.util.Calendar;

import org.json.JSONObject;

/**
 * A class that holds a {@link Calendar} in an {@link JSONObject}.
 * @author criztovyl
 * 
 */
public class JSONCalendar{

	/**
	 * The milliseconds since epoch
	 */
	public static final String JSON_EPOCH_MILLIS = "epochMillis";

	private final Calendar calendar;

	private JSONObject json;

	/**
	 * Creates a new JsonCalendar from a {@link Calendar}.
	 * 
	 * @param calendar
	 *            the calendar
	 */
	public JSONCalendar(Calendar calendar) {
		this.calendar = calendar;

		createJson();
	}

	/**
	 * Creates a new JsonCalendar from a JSON object.
	 * 
	 * @param json
	 *            the JSON object,
	 */
	public JSONCalendar(JSONObject json) {

		// Create calendar and clear
		calendar = Calendar.getInstance();
		calendar.clear();

		// Receive
		calendar.setTimeInMillis(json.getLong(JSONCalendar.JSON_EPOCH_MILLIS));

		// Create JSON
		createJson();
	}

	/**
	 * Creates a new JsonCalendar from a {@link Long}
	 * 
	 * @param lonq
	 *            the long
	 */
	public JSONCalendar(Long lonq) {

		// Create calendar and clear
		calendar = Calendar.getInstance();
		calendar.clear();

		// Set
		calendar.setTimeInMillis(lonq);

		// Create JSON
		createJson();
	}

	/**
	 * Creates the JSON Object
	 */
	private void createJson() {

		// Create object
		json = new JSONObject();

		// Store
		json.put(JSONCalendar.JSON_EPOCH_MILLIS, calendar.getTimeInMillis());

	}

	/**
	 * 
	 * @return the calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * Time will be stored as milliseconds since epoch.
	 * 
	 * @return the calendar as a JSON object.
	 */
	public JSONObject getJSON() {
		return json;
	}

}
