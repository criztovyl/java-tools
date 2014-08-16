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
package de.joinout.criztovyl.tools.directory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.files.FileElement;
import de.joinout.criztovyl.tools.files.FileList;
import de.joinout.criztovyl.tools.json.JSONFile;

/**
 * Locates files changed in a directory
 * @author criztovyl
 *
 */
public class DirectoryChanges {
	
	private FileList current, before;
	
	public DirectoryChanges(Path path){
		
		this.current = new FileList(path);
		this.before = new FileList(new JSONFile(path).getData());
	}
	
	/**
	 * Locates all deleted files by subtract the current set from the before-set
	 * @return a set of {@link FileElement}
	 */
	public Set<FileElement> getDeletedFiles(){
		
		//Copy set of before
		HashSet<FileElement> del = new HashSet<>(before);
		
		//Remove all which is in current set
		del.removeAll(current);
		
		return del;
	}
	/**
	 * Locates all new files by subtract the before-set from the current set 
	 * @return a set of {@link FileElement}
	 */
	public Set<FileElement> getNewFiles(){
		
		//Copy current set
		HashSet<FileElement> nevv = new HashSet<>(current);
		
		//Remove all what is in before-set
		nevv.remove(before);
		
		return nevv;
	}
	public Set<FileElement> getModifiedFiles(){
		
		//Get all new and deleted files, they are not included in the modified files
		Set<FileElement> ignore = new HashSet<>();
		ignore.addAll(getDeletedFiles());
		ignore.addAll(getNewFiles());
		
		//Create a map for modificated files
		HashMap<String, FileElement> mod = new HashMap<>(current.getMappedHashedModifications(ignore));
		
		//Remove all keys which are in before-map
		mod.keySet().removeAll(before.getMappedHashedModifications(ignore).keySet());
		
		return new HashSet<>(mod.values());
	}
	
}
