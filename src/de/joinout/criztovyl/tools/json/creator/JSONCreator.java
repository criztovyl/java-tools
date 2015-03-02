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

import org.json.JSONObject;

/**
 * A class to guarantee JSON compatibility.
 * Some Objects are already implemented as static fields in {@link JSONCreators}.
 * @author criztovyl
 *
 */
public interface JSONCreator<T> extends JSONable<T>{

	/**
	 * Creates JSON data from a object.
	 * @param t the object
	 * @return the {@link JSONObject} of the data
	 */
	public JSONObject getJSON(T t);
	/**
	 * Creates an object from JSON data
	 * @param json the {@link JSONObject} with the data
	 * @return the object represented by the JSON data
	 */
	public T fromJSON(JSONObject json);
	
	/**
	 * Indicates whether the object can be represented by a {@link String}
	 * @return true if yes, otherwise false
	 */
	public boolean canBeString();
	
	/**
	 * Creates a {@link String} out of an object. 
	 * @param t the object that should be transformed into a {@link String}.
	 * @return A {@link String} or <code>null</code> when {@link #canBeString()} is <code>false</code>.
	 */
	public String string(T t);
	
	/**
	 * Creates the object represented by a {@link String}.
	 * @param str the string
	 * @return the object or <code>null</code> when {@link #canBeString()} is <code>false</code>.
	 */
	public T fromString(String str);
	
	/**
	 * 
	 * @return the {@link Class} that guarantees JSON support
	 */
	public Class<?> getCreatorClass();
}
