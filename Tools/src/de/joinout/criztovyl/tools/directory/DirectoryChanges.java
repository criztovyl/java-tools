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

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.files.FileElement;
import de.joinout.criztovyl.tools.files.FileList;

/**
 * Locates files changed in a directory
 * 
 * @author criztovyl
 * 
 */
public class DirectoryChanges {

	public static FileList getNewerFileList(FileList a, FileList b) {
		final FileList a_ = new FileList(a);
		final FileList b_ = new FileList(b);

		final Calendar aCal = a_.getLastListDate();
		final Calendar bCal = b_.getLastListDate();

		// Return if age is equal
		if (aCal.compareTo(bCal) == 0)
			return null;

		// Detect and return newer directory
		return aCal.compareTo(bCal) > 0 ? a_ : b_;
	}

	private final FileList current, previous;
	private Logger logger;

	/**
	 * Creates a new instance with the both {@link FileList}s.
	 * 
	 * @param older
	 *            the older list
	 * @param newer
	 *            the newer list
	 */
	public DirectoryChanges(FileList older, FileList newer) {

		current = new FileList(newer);
		previous = new FileList(older);
		
		logger = LogManager.getLogger();
	}

	/**
	 * Creates a new instance, creates a new {@link FileList} on the path and
	 * try to load the previous data.
	 * 
	 * @param path
	 *            the path
	 */
	public DirectoryChanges(Path path) {

		current = new FileList(path, false).relative();
		previous = new FileList(path, true).relative();
		
		logger = LogManager.getLogger();
	}

	/**
	 * Creates a new instance and create new {@link FileList}s on them.
	 * 
	 * @param older
	 *            the older directory
	 * @param newer
	 *            the newer directory
	 */
	public DirectoryChanges(Path older, Path newer) {

		current = new FileList(newer, false).relative();
		previous = new FileList(older, false).relative();
		
		logger = LogManager.getLogger();
	}

	/**
	 * 
	 * @return the current {@link FileList}.
	 */
	public FileList getCurrentList() {
		return current;
	}

	/**
	 * Locates all deleted files by subtract the current set from the previous.
	 * 
	 * @return a set of {@link FileElement}
	 */
	public Set<Path> getDeletedFiles() {

		// Copy set of before
		final HashSet<Path> del = new HashSet<>(previous);

		// Remove all which is in current set
		del.removeAll(current);

		return del;
	}

	/**
	 * Locates all modified files, does not include new or deleted files.<br>
	 * As first there is created/received a map with hash-strings as keys and
	 * {@link FileElement}s as values for the current and previous
	 * {@link FileList} via {@link FileList#getMappedHashedModifications()}.
	 * Then all keys of the previous map are removed from the current map and
	 * the remaining values are returned.
	 * 
	 * @return a {@link Set} of {@link FileElement} that where modified.
	 */
	public Set<Path> getModifiedFiles() {

		// Get all new and deleted files, they are not included in the modified
		// files
		final Set<Path> ignore = new HashSet<>();
		ignore.addAll(getDeletedFiles());
		ignore.addAll(getNewFiles());

		// Create a map for modificated files
		final HashMap<String, Path> mod = new HashMap<>(
				current.getMappedHashedModifications(ignore));

		if(logger.isDebugEnabled())
			logger.debug("Modifications map of current list: {}", mod);
		
		Map<String, Path> mod_p = previous.getMappedHashedModifications(ignore);

		if(logger.isDebugEnabled())
			logger.debug("Modifications map of previous list: {}", mod_p);
		// Remove all keys which are in before-map
		mod.keySet().removeAll(mod_p.keySet()
				);

		return new HashSet<>(mod.values());
	}

	/**
	 * Locates all new files by subtract the previous set from the current set
	 * 
	 * @return a set of {@link FileElement}
	 */
	public Set<Path> getNewFiles() {

		// Copy current set
		final HashSet<Path> nevv = new HashSet<>(current);

		// Remove all what is in before-set
		nevv.removeAll(previous);

		return nevv;
	}
	/**
	 * 
	 * @return the previous {@link FileList}
	 */
	public FileList getPreviousList(){
		return previous;
	}

	public void save(){
		current.save();
	}
	
	/**
	 * Checks, if two files are really different in the both lists by checking if the checksums are different.
	 * @param path the relative {@link Path} of the file. May not relative, {@link Path#relativeTo(Path)} will called with {@link FileList#getDirectory()} of previous and current file list.
	 * @return true if checksums differ and false if the are equal or there was an {@link IOException}.
	 */
	public boolean contentChanged(Path path){
		
		path = path.relativeTo(current.getDirectory());
		path = path.relativeTo(previous.getDirectory());
				
		try {
			return FileUtils.checksumCRC32(current.getDirectory().append(path).realPath().getFile()) != FileUtils.checksumCRC32(previous.getDirectory().append(path).realPath().getFile());
		} catch (IOException e) {
			logger.error("Cannot compare file {}, IOException thrown.", path, e);
			return false;
		} catch(IllegalArgumentException e){
			logger.warn("Cannot compare file {}, is directory.", path);
			return false;
		}
	}
	
}
