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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that holds some licenses which can have aliases.<br>
 * This class also contains a GPLv3 ({@link #GPL3}) or Apache 2.0 ({@link #APACHE2}) License Object.
 * @author criztovyl
 *
 */
public class Licenses extends HashMap<String, License> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8136621019188860973L;
	
	private HashMap<String, String> aliases;
	
	/**
	 * Creates new Licenses.
	 */
	public Licenses(){
		super();
		aliases = new HashMap<>();
	}
	/**
	 * Adds a license.
	 * @param license the license
	 */
	public void addLicense(License license){
		put(license.getName().toLowerCase(), license);
	}
	/**
	 * Locates a license
	 * @param name the license name (or alias)
	 * @return a {@link License}
	 */
	public License getLicense(String name){
		name = name.toLowerCase();
		return get(name) == null ? get(aliases.get(name)) : get(name);
	}
	/**
	 * Adds an alias for a license.
	 * @param alias the alias
	 * @param name the license
	 */
	public void addAlias(String alias, String name){
		aliases.put(alias.toLowerCase(), name.toLowerCase());
	}
	/**
	 * Creates a set of all license names
	 * @return a {@link Set}
	 */
	public Set<String> names(){
		HashSet<String> names = new HashSet<>();
		for(License license : values())
			names.add(license.getName());
		return names;
	}
	
	/*
	 * Static Licenses
	 */
	/**
	 * The GPLv3 License.<br>
	 * Includes the typical GPL header ("This program comes with ABSOLUTELY NO WARRANTY...")
	 */
	public static License GPL3 = new License("GPLv3", "de/joinout/criztovyl/tools/licenses/gpl.txt", true, String.format("This program comes with ABSOLUTELY NO WARRANTY; for details see full license."
				+ "%nThis is free software, and you are welcome to redistribute it%n"
				+ "under certain conditions; see full license for details."));
	/**
	 * The Apache 2.0 License.
	 */
	public static License APACHE2 = new License("ApacheV2", "de/joinout/criztovyl/tools/licenses/apache.txt", true);
}
