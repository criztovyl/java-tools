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
package de.joinout.criztovyl.tools;

import de.joinout.criztovyl.tools.licenses.LibraryLicense;
import de.joinout.criztovyl.tools.licenses.License;
import de.joinout.criztovyl.tools.licenses.Licenses;

/**
 * A class that holds general informations about this program.
 * <ul>
 * <li>the {@link License}: {@link #LICENSE}</li>
 * <li>the {@link LibraryLicense}: {@link #LIBRARY}</li>
 * </ul>
 * @author criztovyl
 *
 */
public class Tools {
	
	/**
	 * Tools {@link License}
	 */
	public static License LICENSE = Licenses.GPL3;
	
	/**
	 * Tools {@link LibraryLicense}
	 */
	public static LibraryLicense LIBRARY = new LibraryLicense(LICENSE, "criztovyl's Tools Library", "Christoph `criztovyl´ Schulz");
}
