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
package de.joinout.criztovyl.tools.files;

import java.util.ArrayList;

import de.joinout.criztovyl.tools.file.Path;

/**
 * This class represents a directory.
 * @author criztovyl
 *
 */
public class Directory {
	
	private Path path;

	public Directory(Path path){
		
		this.path = path;
		
	}
	/**
	 * 
	 * @return a list of paths of the contents
	 */
	public ArrayList<Path> getContents(){
		
		//Create new list
		ArrayList<Path> list = new ArrayList<>();
		
		//Iterate of file list array, append to path and add to list
		for(String file : path.getFile().list())
			list.add(path.append(file));
		
		//Return list
		return list;
		
	}
	
}
