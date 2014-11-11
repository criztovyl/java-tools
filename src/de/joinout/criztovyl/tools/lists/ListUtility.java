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
package de.joinout.criztovyl.tools.lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author criztovyl
 *
 */
public class ListUtility<T> {
	
	List<T> list;

	public ListUtility(List<T> list){
		this.list = list;
	}
	
	public List<String> eachtoString(ActionMethod<T, String> luaction){
		
		List<String> strings = new ArrayList<>();
		for(T t : list)
			strings.add(luaction.action(t));
		
		return strings;
			
	}
	public List<T> each(ActionMethod<T, T> luaction){
		
		List<T> list_ = new ArrayList<>();
		
		for(T t : list)
			list_.add(luaction.action(t));
		
		return list_;
	}
}
