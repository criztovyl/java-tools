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
package de.joinout.criztovyl.tools.json.creator;

import java.util.Calendar;

import org.json.JSONObject;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.json.JSONCalendar;

/**
 * A class with some {@link JSONCreator}s inside.
 * @author criztovyl
 *
 */
public class JSONCreators {
	
	/**
	 * A {@link JSONCreator} for {@link Path}s.
	 * {@link Path} supports it natively via {@link Path#getJSON()} and {@link Path#Path(JSONObject)}.
	 */
	public static JSONCreator<Path> PATH = new JSONCreator<Path>() {

		public JSONObject getJSON(Path t) {
			return t.getJSON();
		}

		public Path fromJSON(JSONObject json) {
			return new Path(json);
		}

		public boolean keyCanBeString() {
			return false;
		}

		public String keyString(Path t) {
			return null;
		}

		public Path fromKeyString(String str) {
			return null;
		}
	};
	
	/**
	 * A {@link JSONCreator} for a {@link Calendar}.
	 * Supported by {@link JSONCalendar} via {@link JSONCalendar#getJSON()} and {@link JSONCalendar#JSONCalendar(JSONObject)}.
	 */
	public static JSONCreator<Calendar> CALENDAR = new JSONCreator<Calendar>() {
		
		public JSONObject getJSON(Calendar t) {
			return new JSONCalendar(t).getJSON();
		}
		
		public Calendar fromJSON(JSONObject json) {
			return new JSONCalendar(json).getCalendar();
		}

		public boolean keyCanBeString() {
			return false;
		}

		public String keyString(Calendar t) {
			return null;
		}

		public Calendar fromKeyString(String str) {
			return null;
		}
	};

	/**
	 * A {@link JSONCreator} for a {@link String}.
	 */
	public static JSONCreator<String> STRING = new JSONCreator<String>() {
		
		public JSONObject getJSON(String t) {
			return new JSONObject().putOpt("string", t);
		}
		
		public String fromJSON(JSONObject json) {
			return json.getString("string");
		}

		public boolean keyCanBeString() {
			return true;
		}

		public String keyString(String t) {
			return t;
		}

		public String fromKeyString(String str) {
			return str;
		}
	};
}
