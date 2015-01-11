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

import org.json.JSONArray;
import org.json.JSONObject;

import de.joinout.criztovyl.tools.json.creator.JSONCreator;
import de.joinout.criztovyl.tools.json.creator.JSONable;
import de.joinout.criztovyl.tools.json.iterator.JSONObjectArrayIterator;
import de.joinout.criztovyl.tools.json.iterator.JSONStringArrayIterator;

/**
 * An abstract helper for JSON and {@link Collection}s.
 * @author Christoph "criztovyl" Schulz
 *
 */
public abstract class JSONCollection<T> implements JSONable<Collection<T>> {
	
	private JSONCreator<T> creator;
	private JSONObject json;
	
	/**
	 * Sets up a new JSON collection helper.
	 * @param creator the {@link JSONCreator} for the generic class.
	 */
	public JSONCollection(JSONCreator<T> creator){
		this.creator = creator;
		json = new JSONObject();
	}
	/**
	 * Sets up a new JSON collection helper.
	 * @param coll the {@link Collection}
	 * @param creator the {@link JSONCreator} for the generic class.
	 */
	public JSONCollection(Collection<T> coll, JSONCreator<T> creator){
		this(creator);
		
		JSONArray array = new JSONArray();
		
		for(T t : coll)
			array.put(creator.canBeString() ? creator.string(t) : creator.getJSON(t));
		
		getJSON().put(getKey(), array);
	}
	/**
	 * Sets up a new JSON collection helper.
	 * @param json the JSON data
	 * @param creator the {@link JSONCreator} for the generic class.
	 */
	public JSONCollection(JSONObject json, JSONCreator<T> creator){
		this(creator);
		this.json = json;
	}
	/**
	 * The {@link JSONCreator} for the generic class.
	 * @return a {@link JSONCreator}
	 */
	protected JSONCreator<T> getCreator(){
		return creator;
	}
	/**
	 * The JSON data.
	 * @return a {@link JSONObject};
	 */
	public JSONObject getJSON(){
		return json;
	}
	
	/**
	 * The collection.
	 * @return a {@link Collection}
	 */
	public abstract Collection<T> getCollection();
	
	/**
	 * Creates a Collection from the JSON data.
	 * @param collection a instance of a {@link Collection} as it is an interface.
	 * @return a {@link Collection}
	 */
	public Collection<T> getCollection(Collection<T> collection){
		
		JSONArray array = json.getJSONArray(getKey());
		
		if(creator.canBeString())
			for(String str : new JSONStringArrayIterator(array))
				collection.add(creator.fromString(str));
		else
			for(JSONObject jsonObject : new JSONObjectArrayIterator(array))
				collection.add(creator.fromJSON(jsonObject));
		
		return collection;
	}
	/**
	 * The key for the JSON data.<br>
	 * Maybe overwritten if extended.
	 * @return a String.
	 */
	public String getKey(){
		return "collection";
	}
}
