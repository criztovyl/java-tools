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
import java.util.List;

import org.json.JSONObject;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.json.JSONCalendar;
import de.joinout.criztovyl.tools.json.JSONList;

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
	public static JSONCreator<Path> PATH = new AbstractJSONCreator<Path>() {

		@Override
		public JSONObject getJSON(Path t) {
			return t.getJSON();
		}

		@Override
		public Path fromJSON(JSONObject json) {
			return new Path(json);
		}
		
		@Override
		public Class<?> getCreatorClass() {
			return Path.class;
		}

	};
	
	/**
	 * A {@link JSONCreator} for a {@link Calendar}.
	 * Supported by {@link JSONCalendar} via {@link JSONCalendar#getJSON()} and {@link JSONCalendar#JSONCalendar(JSONObject)}.
	 */
	public static JSONCreator<Calendar> CALENDAR = new AbstractJSONCreator<Calendar>() {
		
		@Override
		public JSONObject getJSON(Calendar t) {
			return new JSONCalendar(t).getJSON();
		}
		
		@Override
		public Calendar fromJSON(JSONObject json) {
			return new JSONCalendar(json).getCalendar();
		}
		@Override
		public Class<?> getCreatorClass() {
			return Calendar.class;
		}
		
	};

	/**
	 * A {@link JSONCreator} for a {@link String}.
	 */
	public static JSONCreator<String> STRING = new JSONCreator<String>() {
		
		public JSONObject getJSON(String t) {
			return new JSONObject().put("string", t);
		}
		
		public String fromJSON(JSONObject json) {
			return json.getString("string");
		}

		public boolean canBeString() {
			return true;
		}

		public String string(String t) {
			return t;
		}

		public String fromString(String str) {
			return str;
		}
		@Override
		public Class<?> getCreatorClass() {
			return String.class;
		}
	};
	
	/**
	 * A {@link JSONCreator} for an {@link Integer}.
	 */
	public static JSONCreator<Integer> INTEGER = new JSONCreator<Integer>() {
		
		public String string(Integer t) {
			return t.toString();
		}
		
		public JSONObject getJSON(Integer t) {
			JSONObject json = new JSONObject();
			json.put("integer", t);
			return json;
		}
		
		public Integer fromString(String str) {
			return Integer.parseInt(str);
		}
		
		public Integer fromJSON(JSONObject json) {
			return (Integer) json.getInt("integer");
		}
		
		public boolean canBeString() {
			return true;
		}
		@Override
		public Class<?> getCreatorClass() {
			return Integer.class;
		}
	};
	
	/**
	 * A {@link JSONCreator} for a {@link String} {@link List}.
	 * @see JSONList
	 */
	public static JSONCreator<List<String>> STRING_LIST = new AbstractJSONCreator<List<String>>() {

		@Override
		public JSONObject getJSON(List<String> t) {
			return new JSONList<>(t, JSONCreators.STRING).getJSON();
		}

		@Override
		public List<String> fromJSON(JSONObject json) {
			return new JSONList<>(json, JSONCreators.STRING).getList();
		}

		@Override
		public Class<?> getCreatorClass() {
			return List.class;
		}
		
	};
	
	/**
	 * A {@link JSONCreator} for an {@link Integer} {@link List}.
	 * @see JSONList 
	 */
	public static JSONCreator<List<Integer>> INTEGER_LIST = new AbstractJSONCreator<List<Integer>>() {

		@Override
		public JSONObject getJSON(List<Integer> t) {
			return new JSONList<>(t, JSONCreators.INTEGER).getJSON();
		}

		@Override
		public List<Integer> fromJSON(JSONObject json) {
			return new JSONList<>(json, JSONCreators.INTEGER).getList();
		}

		@Override
		public Class<?> getCreatorClass() {
			return List.class;
		}
		
	};
	/**
	 * Try to load a {@link JSONCreators} for the given class.
	 * @param clazz the class
	 * @return the {@link JSONCreators} if found or <code>null</code> if not.
	 */
	public static JSONCreator<?> getCreator(Class<?> clazz){
		
		String name = clazz.getName();
		
		if(name.equals(STRING.getCreatorClass().getName()))
			return STRING;
		
		else if(name.equals(CALENDAR.getCreatorClass().getName()))
			return CALENDAR;
		
		else if(name.equals(PATH.getCreatorClass().getName()))
			return PATH;
		
		else
			return null;
	}
	
}
