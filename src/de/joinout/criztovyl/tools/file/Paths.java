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
package de.joinout.criztovyl.tools.file;

/**
 * Some static paths.
 * @author criztovyl
 *
 */
public class Paths {
	
	/**
	 * Path to user's home
	 */
	public static Path HOME = new Path("user.home", true);
	
	/**
	 * Creates a "dot dir" for the given name
	 * (A dot dir is a hidden configuration directory in the user's home directory)
	 * @param name
	 * @return a {@link Path}.
	 */
	public static Path dotDir(String name){
		return HOME.append("." + name);
	}
}
