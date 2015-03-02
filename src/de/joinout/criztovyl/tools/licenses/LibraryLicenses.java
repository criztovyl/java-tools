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
 * A class that holds some {@link LibraryLicenses} which can have aliases.<br>
 * Also contains some static libraries:
 * <ul>
 * <li>JSON from JSON.org: {@link #JSON}</li>
 * <li>Commons I/O from Apache: {@link #COMMONS_IO}</li>
 * <li>Commons Codec from Apache: {@link #COMMONS_CODEC}</li>
 * <li>Log4J from Apache: {@link #LOG4J}</li>
 * </ul>
 * @author criztovyl
 *
 */
public class LibraryLicenses extends HashMap<String, LibraryLicense>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6178964395180531617L;

	/**
	 * Names format for {@link #names(int)}
	 * TODO: Rename to FORMAT_NAME
	 */
	public static final int F_NAMES = 0;
	/**
	 * Tiny format for {@link #names(int)}
	 * TODO: Rename to FORMAT_TINY
	 */
	public static final int F_TINY = 1;
	private HashMap<String, String> aliases;
	
	/**
	 * Creates new {@link LibraryLicenses}.
	 */
	public LibraryLicenses(){
		super();
		aliases = new HashMap<>();
	}
	/**
	 * Adds a library
	 * @param library the library
	 */
	public void addLibrary(LibraryLicense library){
		put(library.getLibraryName().toLowerCase(), library);		
	}
	/**
	 * Locates a library by a name (or alias)
	 * @param name the name or alias
	 * @return a {@link LibraryLicenses}
	 */
	public LibraryLicense getLibrary(String name){
		name = name.toLowerCase();
		return get(name) == null ? get(aliases.get(name)) : get(name);
	}
	/**
	 * Adds a alias for a library name
	 * @param alias the alias
	 * @param name the library name
	 */
	public void addAlias(String alias, String name){
		aliases.put(alias.toLowerCase(), name.toLowerCase());
	}
	/**
	 * Creates a set of all library names.
	 * @see #names(int)
	 * @see #F_NAMES
	 * @return a {@link Set}
	 */
	public Set<String> names(){
		return names(F_NAMES);
	}
	/**
	 * Creates a set of names in a defined format.<br>
	 * Current formats:
	 * <ul>
	 * <li>Names: {@link #F_NAMES} ({@link LibraryLicense#getLibraryName()})</li>
	 * <li>Tiny: {@link #F_TINY} ({@link LibraryLicense#getTiny()})</li>
	 * </ul>
	 * @param format the names format
	 * @return a {@link Set}
	 */
	public Set<String> names(int format){
		HashSet<String> names = new HashSet<>();
		for(LibraryLicense library : values())
			switch(format){
			case F_NAMES: names.add(library.getLibraryName()); break;
			case F_TINY: names.add(library.getTiny()); break;
			default: names.add(library.getLibraryName()); break;
			}
		return names;
	}

	//Static Libraries
	/**
	 * JSON (by JSON.org) Library
	 */
	public static LibraryLicense JSON = new LibraryLicense(new License("JSON", "de/joinout/criztovyl/tools/licenses/json.txt", true), "JSON", "Douglas Crockford from JSON.org", "2002");
	/**
	 * Commons I/O (by Apache) Library
	 */
	public static LibraryLicense COMMONS_IO =  new LibraryLicense(Licenses.APACHE2, "Commons I/O", "The Apache Software Foundation", "2002 - 2014");
	/**
	 * Commons Codec (by Apache) Library
	 */
	public static LibraryLicense COMMONS_CODEC = new LibraryLicense(Licenses.APACHE2, "Commons Codec", "The Apache Software Foundation", "2002 - 2014");
	/**
	 * Log4J (by Apache) Library.
	 */
	public static LibraryLicense LOG4J = new LibraryLicense(Licenses.APACHE2, "Log4J", "The Apache Software Foundation", "2002 - 2014");
}
