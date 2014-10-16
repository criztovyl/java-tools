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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A class that helps with {@link String}s.
 * @author criztovyl
 *
 */
public class StringUtils {

	/**
	 * Transforms an {@link String} {@link Collection} to an single string and optional break after each element.
	 * @param strings the {@link Collection}
	 * @param attachLineBreaks whether should attach line break to each element
	 * @return a {@link String}
	 */
	public static String fromStringCollection(Collection<String> strings, boolean attachLineBreaks){
		
		//Set up
		String str = "";
		String lineend = String.format("%n");
		
		//Iterate
		for(String line : strings)
			str += attachLineBreaks ? line + lineend: line;
		
		//Return
		return str;
	}
	/**
	 * Transforms an {@link String} {@link Collection} to an single string.
	 * @param strings the {@link Collection}
	 * @return a {@link String}
	 */
	public static String fromStringCollection(Collection<String> strings){
		return fromStringCollection(strings, false);
	}
	/**
	 * Joins all elements from a {@link Collection} with a separator only the last element will be separated by special separator.
	 * @param stringsC the {@link Collection}
	 * @param separator the separator
	 * @param lastSeperator the special last separator
	 * @return a {@link String}
	 */
	public static String joinCollection(Collection<String> stringsC, String separator, String lastSeperator){
		
		ArrayList<String> strings = new ArrayList<>(stringsC);
		
		if(strings.isEmpty())
			return "";
		
		String str = "";
		String last = strings.remove(strings.size()-1);
		
		if(strings.size() == 0)
			str += last;
		else{
			for(String related : strings)
				str += related + separator;
			str = str.replaceAll(separator + "$", lastSeperator + last);
		}
		return str;
	}
	/**
	 * Joins all elements from a {@link Collection} with a separator.
	 * @param stringsC the {@link Collection}
	 * @param separator the separator
	 * @return a {@link String}
	 */
	public static String joinCollection(Collection<String> stringsC, String separator){
		return joinCollection(stringsC, separator, separator);
	}
	/**
	 * Joins all elements from a {@link Collection} with ", " only the last element will be separated by " and ".
	 * @param stringsC the {@link Collection}
	 * @return a {@link String}
	 */
	public static String joinCollection(Collection<String> stringsC){
		return joinCollection(stringsC, ", ", " and ");
	}
	/**
	 * Joins all {@link String}s with a separator. Optional last element will be separated by a special separator.
	 * @param separator the separator
	 * @param specialSeparator whether the first {@link String} from the {@link String}s should be the special separator
	 * @param strings the {@link String}s
	 * @return a {@link String}
	 */
	public static String join(String separator, boolean specialSeparator, String ...strings){
		
		//Create String list
		ArrayList<String> list = new ArrayList<>(Arrays.asList(strings));
		
		//Join
		//If special last separator should be used, "shift" it from the array (List#remove(0))
		return specialSeparator ? joinCollection(list, separator, list.remove(0)) : joinCollection(list, separator);
	}
	/**
	 * Pass-through to {@link #joinCollection(Collection)} by creating a {@link Collection} from {@link String}s.
	 * @param strings the {@link String}s
	 * @see StringUtils#joinCollection(Collection)
	 * @return a {@link String}
	 */
	public static String join(String ...strings){
		return joinCollection(stringCollection(strings));
	}
	/**
	 * Creates a Collection from String arguments
	 * @param strings the strings
	 * @return a {@link Collection} (a {@link ArrayList}).
	 */
	public static Collection<String> stringCollection(String ...strings){
		return new ArrayList<>(Arrays.asList(strings));
	}
	/**
	 * Splits at newline, prefix and joins by newline
	 * @param lines
	 * @param newline
	 * @param prefix
	 * @return a {@link String}
	 */
	public static String prefixLines(String lines, String newline, String prefix){
		String[] linesA = lines.split(newline);
		String linesN = "";
		for(String line : linesA)
			linesN += prefix + line + newline;
		return linesN;
	}
	/**
	 * 
	 * @return the OS-dependent new-line String from {@link String#format(String, Object...)} by "<code>%n</code>".  
	 */
	public static String newline(){
		return String.format("%n");
	}
}
