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
package de.joinout.criztovyl.tools.licenses;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.joinout.criztovyl.tools.streams.LineCollector;

/**
 * A class that holds a license with it's text, header and name.<br>
 * It also can create the header and a short form of the license for a thing.
 * @author criztovyl
 *
 */
public class License{
	
	private String text, header, name;
	private Logger logger;

	/**
	 * Creates a copy of a license.
	 * @param license the license
	 */
	public License(License license){
		
		this.header = license.header;
		this.text = license.text;
		this.name = license.name;
		logger = LogManager.getLogger();
	}
	/**
	 * Creates a new license with a name, a text and a header.<br>
	 * The text can be loaded from a resource.
	 * @param name the name
	 * @param string the license text or the resource name
	 * @param isResource whether string should be the license text or a resource name
	 * @param header the license header
	 */
	public License(String name, String string, boolean isResource, String header){
		this(name, string, isResource);
		this.header = header;
	}
	/**
	 * Creates a new license with a name and a text.<br>
	 * The text can be loaded from a resource.
	 * @param name the name
	 * @param string the license text or the resource name
	 * @param isResource whether string should be the license text or a resource name
	 */
	public License(String name, String string, boolean isResource){
		
		//Set up name and logger
		
		this.name = name;
		this.logger = LogManager.getLogger();
		
		if(isResource){
			try { //Try to load license form resource
				
				//Collect lines from resource stream.
				LineCollector rc = new LineCollector(getClass().getClassLoader().getResourceAsStream(string));
				
				//Set text
				this.text = rc.asString();
				
			} catch (IOException e) {
				
				if(logger.isWarnEnabled())
					logger.warn("Could not read Stream \"{}\", IOException: {}", string, e.toString());
				
				if(logger.isErrorEnabled())
					logger.catching(e);
			}
		}
		else
			//Set text
			this.text = string;
	}
	/**
	 * @return the license text
	 */
	public String getText(){
		return text;
	}
	/**
	 * 
	 * @return the license name
	 */
	public String getName(){
		return name;
	}
	/**
	 * Creates a tiny form of this license for a thing.<br>
	 * Example:<br>
	 * <pre>Thing (GPL License)</pre>
	 * @param thing the thing
	 * @return a {@link String}
	 */
	public String getTiny(String thing){
		return String.format("%s (%s License)", thing, getName());
	}
	/**
	 * Creates a short form of this license for a thing.<br>
	 * Example:<br>
	 * <pre>Thing (C) 2014 Christoph `criztovyl´ Schulz. Licensed under GPLv3.</pre>
	 * @param thing the name of the thing
	 * @param years "copyright" years
	 * @param author the author
	 * @return a {@link String}
	 */
	public String getShort(String thing, String years, String author){
		// Format: [Thing] (C) [Year] [Author]. Licensed under [License Name].
		return String.format("%s (C) %s %s. Licensed under %s.", thing, years, author, getName());
	}
	/**
	 * Creates the License header for a thing.
	 * Example:<br>
	 * <pre>
	 * Thing (C) 2014 Christoph `criztovyl´ Schulz. Licensed under GPLv3.
	 * 
	 * This program comes with ABSOLUTELY NO WARRANTY; for details see full license.
	 * This is free software, and you are welcome to redistribute it
	 * under certain conditions; see full license for details.</pre>
	 * @param thing the thing
	 * @param years "copyright" years
	 * @param author the author
	 * @return a {@link String}
	 */
	public String getHeader(String thing, String years, String author){
		//Format: [Short License][new-line (twice)][License Header]
		return String.format("%s%n%n%s", getShort(thing, years, author), header);
	}

}
