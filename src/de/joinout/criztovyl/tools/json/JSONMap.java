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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import de.joinout.criztovyl.tools.json.creator.JSONCreator;
import de.joinout.criztovyl.tools.json.iterator.JSONStringArrayIterator;

/**
 * A class that transforms a {@link Map} to JSON.<br>
 * If keys and/or values can be represented by a {@link String}, they will be stored as such.<br>
 * If key can't be a string, the map will be split of into a key and values array where the indexes are synchronised.<br>
 * @author criztovyl
 *
 */
public class JSONMap<K, V>{

	private Map<K, V> map;
	private static String KEYS = "keys";
	private static String VALUES = "values";
	private JSONObject json;

	/**
	 * Creates a new {@link JSONMap} from a {@link Map}.
	 * @param map the map
	 * @param keyJ the {@link JSONCreator} for the keys
	 * @param valJ the {@link JSONCreator} for the values
	 */
	public JSONMap(Map<K, V> map, JSONCreator<K> keyJ, JSONCreator<V> valJ){

		//Set up variables
		this.map = map;
		JSONObject json = new JSONObject();
		JSONArray keys = new JSONArray();
		JSONArray values = new JSONArray();

		//Check whether key can be string
		if(keyJ.canBeString()){ //If so iterate over key set and put key as string.

			for(K k : map.keySet())
				//Put value also as key, if can be one
				json.put(keyJ.string(k), valJ.canBeString() ? valJ.string(map.get(k)) : valJ.getJSON(map.get(k)));

		}
		else{ // If not, iterate and put the JSON-representation.

			for(K k : map.keySet()){

				keys.put(keyJ.getJSON(k));
				
				//If value can be string, put as such
				values.put(valJ.canBeString() ? valJ.string(map.get(k)) : valJ.getJSON(map.get(k)));

			}

			json.put(KEYS, keys);
			json.put(VALUES, values);
		}

		this.json = json;
	}

	/**
	 * Creates a new {@link JSONMap} from a {@link JSONObject}.
	 * @param json the JSON data
	 * @param keyJ the {@link JSONCreator} for the keys
	 * @param valJ the {@link JSONCreator} for the values
	 */
	public JSONMap(JSONObject json, JSONCreator<K> keyJ, JSONCreator<V> valJ){

		//Set up variables
		this.json = json;
		map = new HashMap<>();

		//Check whether key can be string
		if(keyJ.canBeString()){ //If so, add as string

			//Iterate over map names.
			//If map is empty, names will be null so create an empty array if names are null.
			for(String key : new JSONStringArrayIterator(json.names() == null ? new JSONArray() : json.names()))
				
				//If value also can be a string, but as one
				map.put(keyJ.fromString(key), valJ.canBeString() ? valJ.fromString(json.getString(key)) : valJ.fromJSON(json.getJSONObject(key)));
		}
		else{ //If not, put the JSON-representation

			//Set up arrays of keys and values
			JSONArray keys = json.getJSONArray(KEYS);
			JSONArray values = json.getJSONArray(VALUES);

			//Iterate over key array and take values from the arrays
			for(int i = 0; i < keys.length(); i++)
				
				//If value can be a string, load as one
				map.put(keyJ.fromJSON(keys.getJSONObject(i)), valJ.canBeString() ? valJ.fromString(values.getString(i)) : valJ.fromJSON(values.getJSONObject(i)));
		}
	}

	/**
	 * 
	 * @return the corresponding {@link JSONObject}
	 */
	public JSONObject getJSON(){
		return json;
	}

	/**
	 * 
	 * @return the corresponding {@link Map}
	 */
	public Map<K, V> getMap(){
		return map;
	}

}
