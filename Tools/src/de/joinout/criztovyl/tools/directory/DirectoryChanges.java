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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.files.FileList;

/**
 * Locates changes between directories.
 * 
 * @author criztovyl
 * 
 */
public class DirectoryChanges {

	private final FileList current, previous;
	private Logger logger;

	/**
	 * Creates a new instance.
	 * 
	 * @param previous
	 *            the previous list
	 * @param current
	 *            the current list
	 */
	public DirectoryChanges(FileList current, FileList previous) {

		this.current = new FileList(current);
		this.previous = new FileList(previous);

		logger = LogManager.getLogger();
	}

	/**
	 * Creates a new instance. <code>current</code> will created upon a path and <code>previous</code> will be loaded from JSON.
	 * 
	 * @param path
	 *            the path
	 * @throws IOException If an I/O error occurs when creating {@link FileList} by {@link FileList#FileList(Path, boolean)}
	 */
	public DirectoryChanges(Path path) throws IOException {
		this(new FileList(path, false).relative(), new FileList(path, true).relative());
	}

	/**
	 * Creates a new instance. <code>current</code> and <code>previous</code> are created from {@link Path}s by {@link FileList#FileList(Path, boolean)} and {@link FileList#relative()}.
	 * 
	 * @param older
	 *            the older directory
	 * @param newer
	 *            the newer directory
	 * @throws IOException 
	 */
	public DirectoryChanges(Path older, Path newer) throws IOException {
		this(new FileList(newer, false).relative(), new FileList(older, false).relative());
	}

	/**
	 * Creates a new DirectoryChanges upon the both {@link FileList}s which are created with the given regular exception for excluding files.
	 * @param current the current directory
	 * @param previous the previous directory
	 * @param ignoreRegex the regular exception for excluding files.
	 * @throws IOException 
	 */
	public DirectoryChanges(Path current, Path previous, String ignoreRegex) throws IOException {
		this(new FileList(current, ignoreRegex).relative(), new FileList(previous, ignoreRegex).relative());
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
	 * @return a {@link Set} of {@link Path}s
	 */
	public Set<Path> getDeletedFiles() {

		// Copy set of before
		final HashSet<Path> del = new HashSet<>(previous);

		// Remove all which is in current set
		del.removeAll(current);

		return del;
	}

	/**
	 * Locates all changed files, does not include new or deleted files.<br>
	 * As first there is created/received a map with hash-strings as keys and
	 * {@link Path}s as values for the current and previous
	 * {@link FileList} via {@link FileList#getMappedHashedModifications()}.
	 * Then all keys of the previous map are removed from the current map and
	 * the remaining values are returned.<br>
	 * Only files which content changed are included.
	 * 
	 * @return a {@link Set} of {@link Path}s that where modified.
	 * @see FileList#getMappedHashedModifications()
	 */
	public Set<Path> getChangedFiles() {

		// Get all new and deleted files, they are not included in the modified
		// files, add them to list which files are ignored
		final Set<Path> ignore = new HashSet<>();
		ignore.addAll(getDeletedFiles());
		ignore.addAll(getNewFiles());

		if(logger.isDebugEnabled())
			logger.debug("Files ignored: {}", new TreeSet<>(ignore));

		// Create a map for modificated files and put modifications map from current directory
		final HashMap<String, Path> mod = new HashMap<>(
				current.getMappedHashedModifications(ignore));

		//Receive modifications from previous directory
		Map<String, Path> mod_p = previous.getMappedHashedModifications(ignore);

		//Intersect map keys
		Set<String> intersection = new HashSet<>(mod.keySet());
		intersection.retainAll(mod_p.keySet());

		if(logger.isDebugEnabled()){
			logger.debug("Modifications map of previous list: {}", new TreeMap<>(mod_p));
			logger.debug("Modifications map of current list: {}", new TreeMap<>(mod));
			logger.debug("Intersection of above: {}", intersection);
		}
		
		//Merge maps
		mod.putAll(mod_p);

		// Remove everything which is in both maps
		mod.keySet().removeAll(new TreeSet<>(intersection));

		//Only files which contents changed stay in map
		//Iterate over keys
		for(Iterator<String> i = mod.keySet().iterator(); i.hasNext(); ){

			//Get path
			Path path = mod.get(i.next());
			
			//Check if file has changed (may throw I/O exception)
			try{
				if(contentChanged(path))

					//Remove if is not newer then complement file
					if(!FileUtils.isFileNewer(path.getFile(), getComplementPath(path).getFile()))
						i.remove();
					else
						;

				//Has not changed, remove from map
				else
					i.remove();
			} catch (IOException e){ //Catch IOException, remove from map to avoid further errors
				i.remove();
				if(logger.isWarnEnabled())
					logger.warn("Caught IOException while testing if file is newer: \"{}\". Removing from modifications to prevent further errors.");
				if(logger.isErrorEnabled())
					logger.catching(e);
			}
		}

		//Return changed files
		return new HashSet<>(mod.values());
	}

	/**
	 * Locates all new files by subtract the previous set from the current set
	 * 
	 * @return a {@link Set} of {@link Path}s
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

	/**
	 * Saves the <code>previous</code> and <code>current</code> {@link FileList} by {@link FileList#save()}.
	 */
	public void save(){
		current.save();
		previous.save();
	}

	/**
	 * Checks, if two files are really different in the both lists by checking whether the checksums are different.
	 * @param path a {@link Path} (should be from <code>current</code> or <code>previous</code>)
	 * @return true if checksums are different and false if they are equal or the path is a directory.
	 * @throws IOException If an I/O error occurs.
	 */
	public boolean contentChanged(Path path) throws IOException{

		path = makeRelative(path);

		try {
			return FileUtils.checksumCRC32(current.getDirectory().append(path).realPath().getFile()) != FileUtils.checksumCRC32(previous.getDirectory().append(path).realPath().getFile());
		}  catch(IllegalArgumentException e){ //Catch if is directory
			logger.warn("Cannot compare file {}, is directory.", path);
			return false;
		}
	}
	/**
	 * Makes a path relative also if you don't know whether it is from <code>current</code> or <code>previous</code> {@link FileList}. 
	 * @param path the {@link Path}
	 * @return a {@link Path}
	 */
	public Path makeRelative(Path path){
	
		path = path.relativeTo(current.getDirectory());
		path = path.relativeTo(previous.getDirectory());
		
		return path;
	}
	
	/**
	 * Calculates the path of the complementary directory.<br>
	 * i.e. if <code>current</code> is <code>currentdir/</code> and <code>previous</code> is <code>prevdir/</code> and you run this on <code>currentdir/dir/file</code> you will get <code>prevdir/</code>.
	 * @param path the path as {@link Path}
	 * @return a {@link Path}
	 */
	public Path getComplementDirectory(Path path){
		
		return path.getParent(makeRelative(path)).equals(current.getDirectory()) ? previous.getDirectory() : current.getDirectory();
		
	}
	
	/**
	 * Calculates the path of the complementary file.<br>
	 * Following the example from {@link #getComplementDirectory(Path)} you will get <code>prevdir/dir/file</code>.
	 * @param path the path as a {@link Path}
	 * @return a {@link Path}
	 */
	public Path getComplementPath(Path path) {
		return getComplementDirectory(path).append(makeRelative(path));
	}

}
