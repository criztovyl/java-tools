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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import de.joinout.criztovyl.tools.json.creator.JSONCreator;

/**
 * A class that helps with JSON and {@link List}s.<br>
 * Internally works with {@link ArrayList}s.
 * @author Christoph "criztovyl" Schulz
 *
 */
public class JSONList<T> extends JSONCollection<T>{
	
	/**
	 * Sets up a JSON helper for a list.
	 * @param list the list.
	 * @param creator the creator for the generic class.
	 */
	public JSONList(List<T> list, JSONCreator<T> creator){
		super(list, creator);
	}
	/**
	 * Sets up a JSON helper for a list.
	 * @param json the JSON data
	 * @param creator the creator for the generic class.
	 */
	public JSONList(JSONObject json, JSONCreator<T> creator){
		super(json, creator);
	}
	/**
	 * Sets up a JSON helper for a list.
	 * @param creator the creator for the generic class.
	 */
	public JSONList(JSONCreator<T> creator){
		super(creator);
	}
	
	/**
	 * The list.
	 * @return a {@link List}.
	 */
	public List<T> getList() {
		return new ArrayList<>(getCollection());
	}
	
	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.JSONCollection#getCollection()
	 */
	@Override
	public Collection<T> getCollection() {
		return getCollection(new ArrayList<T>());
	}

	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.creator.JSONable#getJSONCreator()
	 */
	public JSONCreator<Collection<T>> getJSONCreator() {
		return new JSONCreator<Collection<T>>() {

			public JSONObject getJSON(Collection<T> t) {
				return new JSONList<>(new ArrayList<>(t), getCreator()).getJSON();
			}

			public Collection<T> fromJSON(JSONObject json) {
				return new JSONList<>(json, getCreator()).getCollection();
			}

			public boolean canBeString() {
				return false;
			}

			public String string(Collection<T> t) {
				return null;
			}

			public List<T> fromString(String str) {
				return null;
			}

			public Class<?> getCreatorClass() {
				return List.class;
			}

			public JSONCreator<Collection<T>> getJSONCreator() {
				return this;
			}
		};
	}

	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.JSONCollection#getKey()
	 */
	@Override
	public String getKey() {
		return "list";
	}

}
