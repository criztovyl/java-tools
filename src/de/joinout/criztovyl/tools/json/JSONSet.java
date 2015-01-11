/**
    This is a part of my tool collection.
    Copyright (C) 2015 Christoph "criztovyl" Schulz

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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import de.joinout.criztovyl.tools.json.creator.AbstractJSONCreator;
import de.joinout.criztovyl.tools.json.creator.JSONCreator;

/**
 * A class that helps with JSON and {@link Set}s.<br>
 * Internally a {@link HashSet} is used.
 * @author Christoph "criztovyl" Schulz
 *
 */
public class JSONSet<T> extends JSONCollection<T> {

	/**
	 * Sets up a new JSON helper for sets.
	 * @param creator the {@link JSONCreator} for the generic class
	 */
	public JSONSet(JSONCreator<T> creator) {
		super(creator);
	}
	/**
	 * Sets up a new JSON helper for sets.
	 * @param set the {@link Set}
	 * @param creator the {@link JSONCreator} for the generic class
	 */
	public JSONSet(Set<T> set, JSONCreator<T> creator){
		super(set, creator);
	}
	/**
	 * Sets up a new JSON helper for sets.
	 * @param json the JSON data
	 * @param creator the {@link JSONCreator} for the generic class
	 */
	public JSONSet(JSONObject json, JSONCreator<T> creator){
		super(json, creator);
	}
	/**
	 * The set.
	 * @return a {@link Set}.
	 */
	public Set<T> getSet(){
		return new HashSet<>(getCollection());
	}

	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.creator.JSONable#getJSONCreator()
	 */
	public JSONCreator<Collection<T>> getJSONCreator() {
		return new AbstractJSONCreator<Collection<T>>() {

			@Override
			public JSONObject getJSON(Collection<T> t) {
				return new JSONSet<>(new HashSet<>(t), getCreator()).getJSON();
			}

			@Override
			public Collection<T> fromJSON(JSONObject json) {
				return new JSONSet<>(json, getCreator()).getCollection();
			}

			@Override
			public Class<?> getCreatorClass() {
				return Set.class;
			}
		};
	}

	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.JSONCollection#getCollection()
	 */
	@Override
	public Collection<T> getCollection() {
		return getCollection(new HashSet<T>());
	}

}
