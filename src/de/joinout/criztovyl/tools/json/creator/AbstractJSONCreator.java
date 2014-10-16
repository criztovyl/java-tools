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
 * @author criztovyl
 *
 */
public abstract class AbstractJSONCreator<T> implements JSONCreator<T>{

	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.creator.JSONCreator#getJSON(java.lang.Object)
	 */
	public abstract JSONObject getJSON(T t);

	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.creator.JSONCreator#fromJSON(org.json.JSONObject)
	 */
	public abstract T fromJSON(JSONObject json);

	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.creator.JSONCreator#canBeString()
	 */
	public boolean canBeString() {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.creator.JSONCreator#string(java.lang.Object)
	 */
	public String string(T t) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.creator.JSONCreator#fromString(java.lang.String)
	 */
	public T fromString(String str) {
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.json.creator.JSONCreator#getCreatorClass()
	 */
	public abstract Class<?> getCreatorClass();

}
