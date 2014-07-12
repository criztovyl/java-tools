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
package de.joinout.criztovyl.tools.strings;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Represents a class that reads strings from a properties file
 * @author criztovyl
 * 
 */
public class Strings {

	private final ResourceBundle resourceBundle;

	/**
	 * Creates a new strings object
	 * @param bundleName the name to the properties file
	 */
	public Strings(String bundleName) {
		
		//Set bundle
		resourceBundle = ResourceBundle.getBundle(bundleName);
	}

	/**
	 * Receives the key from the properties file and formats it if possible
	 * @param key the string key
	 * @param args the arguments for {@link String#format(String, Object...)}
	 * @return a formatted string
	 */
	public String getString(String key, Object... args) {
		
		try {
			//Return formatted string
			return String.format(resourceBundle.getString(key), args);
		} catch (final MissingResourceException e) {
			//Return string key
			return '!' + key + '!';
		}
	}
}
