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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.joinout.criztovyl.tools.json.creator.JSONCreator;
import de.joinout.criztovyl.tools.json.iterator.JSONObjectArrayIterator;
import de.joinout.criztovyl.tools.json.iterator.JSONStringArrayIterator;

/**
 * @author criztovyl
 *
 */
public class JSONList<T>{
	
	private static String KEY = "list";
	
	private JSONObject json;
	private List<T> list;
	
	JSONCreator<T> creator;
	
	public JSONList(List<T> list, JSONCreator<T> creator){
		
		this.list = list;
		this.creator = creator;
		json = new JSONObject();
		
		JSONArray array = new JSONArray();
		
		for(T t : list)
			array.put(creator.canBeString() ? creator.string(t) : creator.getJSON(t));
		
		json.put(KEY, array);
	}
	
	public JSONList(JSONObject json, JSONCreator<T> creator){
		
		this.json = json;
		this.creator = creator;
		this.list = new ArrayList<>();
		
		JSONArray array = json.getJSONArray(KEY);
		
		if(creator.canBeString())
			for(String str : new JSONStringArrayIterator(array))
				list.add(creator.fromString(str));
		else
			for(JSONObject jsonObject : new JSONObjectArrayIterator(array))
				list.add(creator.fromJSON(jsonObject));
	}
	
	public JSONObject getJSON(){
		return json;
	}
	
	public List<T> getList(){
		return list;
	}

}
