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
 * A class that transforms a {@link Map} to JSON.
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

		this.map = map;

		JSONObject json = new JSONObject();

		JSONArray keys = new JSONArray();

		JSONArray values = new JSONArray();

		if(keyJ.keyCanBeString()){

			for(K k : map.keySet())
				json.put(keyJ.keyString(k), map.get(k));

		}
		else

			for(K k : map.keySet()){

				keys.put(keyJ.getJSON(k));
				values.put(valJ.getJSON(map.get(k)));

			}

		json.put(KEYS, keys);
		json.put(VALUES, values);

		this.json = json;
	}

	/**
	 * Creates a new {@link JSONMap} from a {@link JSONObject}.
	 * @param json the JSON data
	 * @param keyJ the {@link JSONCreator} for the keys
	 * @param valJ the {@link JSONCreator} for the values
	 */
	public JSONMap(JSONObject json, JSONCreator<K> keyJ, JSONCreator<V> valJ){

		this.json = json;

		map = new HashMap<>();

		if(keyJ.keyCanBeString()){

			for(String key : new JSONStringArrayIterator(json.names()))
				map.put(keyJ.fromKeyString(key), valJ.fromJSON(json.getJSONObject(key)));
		}
		else{

			JSONArray keys = json.getJSONArray(KEYS);

			JSONArray values = json.getJSONArray(VALUES);

			for(int i = 0; i < keys.length(); i++){
				map.put(keyJ.fromJSON(keys.getJSONObject(i)), valJ.fromJSON(values.getJSONObject(i)));
			}
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
