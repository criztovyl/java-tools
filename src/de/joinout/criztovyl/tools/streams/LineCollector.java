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
package de.joinout.criztovyl.tools.streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.joinout.criztovyl.tools.strings.StringUtils;

/**
 * Collects all lines from a input and offers them in different formats.<br>
 * <table summary="">
 * <tr><td>
 * Current formats
 * <ul>
 * <li>String: {@link #asString()}</li>
 * <li>String Array: {@link #asStringArray()}</li>
 * <li>String List: {@link #asStringList()}</li>
 * </ul>
 * </td><td>
 * Current input formats
 * <ul>
 * <li>{@link BufferedReader}</li>
 * <li>{@link InputStream}</li>
 * </ul>
 * </td></tr>
 * </table>
 * @author criztovyl
 *
 */
public class LineCollector{
	private ArrayList<String> lines;
	
	/**
	 * Collects all lines from a {@link BufferedReader}.
	 * @param reader the reader 
	 * @throws IOException If an I/O error occurs
	 */
	public LineCollector(BufferedReader reader) throws IOException{
		
		//Set up variables
		lines = new ArrayList<>();
		String line = "";
		
		//Read line per line and add to list
		while((line = reader.readLine()) != null)
			lines.add(line);
		
	}
	/**
	 * Collects all lines from an {@link InputStream}.
	 * @param is the input stream
	 * @throws IOException If an I/O error occurs
	 */
	public LineCollector(InputStream is) throws IOException{
		this(new BufferedReader(new InputStreamReader(is)));
	}
	/**
	 * Converts collected lines into an {@link String} array.
	 * @return a {@link String} array
	 */
	public String[] asStringArray(){
		//Transform to String array
		return asStringList().toArray(new String[0]);
	}
	/**
	 * Converts collected lines into an {@link String} {@link List}.
	 * @return a {@link List}
	 */
	public List<String> asStringList(){		
		return lines;
	}
	/**
	 * Converts collected lines into an single {@link String}.
	 * @return a {@link String}
	 */
	public String asString(){
		//Transform into a single String
		return new StringUtils(lines).join(StringUtils.STYLE_NEWLINE);
	}
	
}
