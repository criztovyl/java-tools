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

import java.util.Iterator;

import org.json.JSONArray;

/**
 * A abstract class for iterating over {@link JSONArray}s.<br>
 * You only have to define wich JSONArray#get you want to use.<br>
 * Array is available via {@link #getArray()} and index via {@link #index()}.<br>
 * As example with strings #next() would be:<br>
 * 
 * <pre>
 * <code>
 * public String next(){
 *     return getArray().getString(getIndex());
 * }
 * </code>
 * </pre>
 * 
 * @author criztovyl
 * 
 */
public abstract class JSONArrayIterator<T> implements Iterator<T>, Iterable<T> {

	private int curr_index;
	private final JSONArray array;

	/**
	 * Creates a new iterator on the given JSON array
	 * 
	 * @param array
	 */
	public JSONArrayIterator(JSONArray array) {
		this.array = array;
		this.curr_index = 0;
	}

	/**
	 * @return the {@link JSONArray}
	 */
	protected JSONArray getArray() {
		return array;
	}

	/**
	 * @return the current index of the iterator
	 */
	protected int index() {
		return curr_index++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return curr_index < array.length();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	public abstract T next();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		getArray().remove(index());

	}

}
