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
package de.joinout.criztovyl.tools.json.iterator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A class for iterating over an {@link JSONArray} with {@link JSONObject}s.
 * 
 * @author criztovyl
 * 
 */
public class JSONObjectArrayIterator extends JSONArrayIterator<JSONObject> {

	/**
	 * Creates a new iterator for a {@link JSONArray} with {@link JSONObject}s.
	 * 
	 * @param array
	 *            the {@link JSONArray}
	 */
	public JSONObjectArrayIterator(JSONArray array) {
		super(array);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.joinout.criztovyl.tools.json.iterator.JSONArrayIterator#next()
	 */
	@Override
	public JSONObject next() {
		return getArray().getJSONObject(index());
	}

}
