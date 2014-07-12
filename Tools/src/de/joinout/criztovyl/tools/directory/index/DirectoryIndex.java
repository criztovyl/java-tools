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
package de.joinout.criztovyl.tools.directory.index;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.file.version.Versions;

/**
 * Represents a set of all files in a directory.
 * @author criztovyl
 *
 */
public class DirectoryIndex  extends HashSet<Path>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6774992912420672264L;

	public static final String VERSIONS_DIRECTORY = ".versions.directoryindex";

	private Logger logger;
	private Path directory;
	private String ignoreRegex;
	private HashMap<Path, Calendar> wastedPaths;
	private Versions versions;
	/**
	 * Creates a new directory index.<br>
	 * Starts indexing immediately.
	 * @param directory The directory to be indexed
	 * @throws IOException 
	 */
	public DirectoryIndex(Path directory) throws IOException{
		this(directory, "");
	}

	/**
	 * Creates a new directory index and ignore all files and folders matching the given regular expression.<br>
	 * Starts indexing immediality.<br>
	 * <i>The root directory for the indexing is not matched against the regular expression.</i>
	 * @param directory The directory to be indexed
	 * @param ignoreRegex The regular expression, run on the relative path
	 * @throws IOException 
	 */
	public DirectoryIndex(Path directory, String ignoreRegex){

		//Setup HashSet
		super();

		//Setup variables
		setupVars(directory, ignoreRegex);

		//Start indexing :)
		add(getDirectory());

	}
	public DirectoryIndex(HashSet<Path> files, Path directory, String ignoreRegex){

		//Setup HashSet and add files
		super();
		super.addAll(files);

		//Setup variables
		setupVars(directory, ignoreRegex);

	}
	/*
	 * (non-Javadoc)
	 * @see java.util.HashSet#add(java.lang.Object)
	 */
	public boolean add(Path path){

		//Ignore versions directory
		//return true, its no error
		if(path.getPath().equals(versions.getPath()))
			return true;

		if(logger.isTraceEnabled())
			logger.trace("Indexing {} ...", path);

		//Check if is file and add to index
		if(path.getFile().isFile()){

			//Only add if not matches regular expression
			if(!isIgnored(path)){

				//Add
				super.add(path);

				if(logger.isDebugEnabled())
					logger.debug("Indexed {}.", path);
			}
			else
				if(logger.isInfoEnabled())
						logger.info("{} ignored, matches ignore regex.", path);
				else;
		}

		//Check if is directory and add to index. If not ignored, also add the subfiles/-directories
		else if(path.getFile().isDirectory()){
			super.add(path);
			for(String sub : path.getFile().list())
				add(path.append(sub));
		}
		else;

		//Return true
		return true;
	}
	/**
	 * Creates a new directory index by appending a file.<br>
	 * Will transfer the {@link #ignoreRegex}
	 * @param file the file to be appended
	 * @return the new directory index
	 */
	public DirectoryIndex append(String file){
		return new DirectoryIndex(getDirectory().append(file), ignoreRegex);
	}
	/**
	 * Creates a new directory index of an sub-directory without indexing it again
	 * @param directory the sub directory path relative to the directory index
	 * @return a new directory index with the files from the sub-directory
	 */
	public DirectoryIndex subIndex(Path directory){

		//Create path inside of directory
		directory = getDirectory().append(directory);

		//Create set
		HashSet<Path> files = new HashSet<>();

		//Iterate over paths
		for(Path path : this)

			//Check if path is in directory
			if(path.isInDirectory(directory)){
				if(logger.isDebugEnabled())
					logger.debug("File/Dir {} is in {}, adding to new index", path, directory);

				//Add to set
				files.add(path);
			}
			else
				if(logger.isDebugEnabled())
					logger.debug("File/Dir {} is not in {}", path, directory);

		//Return new directory index
		return new DirectoryIndex(files, directory, ignoreRegex);
	}
	/**
	 * Pass-through string as path to {@link #subIndex(Path)}
	 * @param directory the directory
	 * @return a new directory index
	 * @see #subIndex(Path)
	 */
	public DirectoryIndex subIndex(String directory){
		return subIndex(new Path(directory));
	}
	/**
	 * Checks if the relative path matches an item
	 * @param relPath the relative path
	 * @return true if there is an item with the relative path, otherwise false
	 */
	public boolean containsRelative(Path relPath){

		//Iterate over all items and return true if relative paths are equal
		for(Path item : this)
			if(item.relativeTo(getDirectory()).equals(relPath))
				return true;

		//Not found, return false
		return false;
	}
	/**
	 * Creates a Path object from a relative path.<br>
	 * Don't checks if file is indexed!
	 * @param relativePath the path
	 * @return the Path object for the file.
	 */
	public Path createPathFromRelative(Path relativePath){

		//Create path by appending relative path to directory. Use the separator of the directory.
		return getDirectory().append(relativePath.getPath(getDirectory().getSeparator()));
	}
	/**
	 * Searches for an path with the given relative path.
	 * @param relPath the relative path
	 * @return if there is an item with the given relative path, the item, otherwise {@code null}
	 */
	public Path getByRelative(Path relPath){

		//Iterate over all items and return if relative paths are equal
		for(Path item : this)
			if(item.relativeTo(getDirectory()).equals(relPath))
				return item;

		//Not found, return null
		return null;
	}
	/**
	 * Returns the base directory all files are relative to.
	 * @return the directory
	 */
	public Path getDirectory() {
		return directory;
	}
	/**
	 * Lists all indexed files and directories in string, one per line, sorted by path by {@link String#compareTo(String)}.
	 * @return A sorted String
	 */
	public String getListString(){

		if(logger.isTraceEnabled())
			logger.trace("Listing all indexed files and directories...");

		//Create String for file list
		String listString = "";
		String newline = String.format("%n");

		//Iterate over all index files, use tree set for sorting
		for(Path file : new TreeSet<>(this)){

			//Add relative path to list
			String file_s =  getRelativePath(file).getPath();
			if(!file_s.equals(""))
				listString += file_s + newline;
		}

		//Return list
		return listString.replaceAll(newline + "$", "");
	}
	/**
	 * Replace base directory in path that it is relative to the base directory. Remove / from beginning.<br>
	 * <i>Works only on item that are in the base directory</i>
	 * @param path the file for the path
	 * @return if file is indexed and in the directory, the relative path, otherwise the absolute.
	 */
	public Path getRelativePath(Path path){

		//pass-through to path object
		return path.relativeTo(getDirectory());
	}
	/**
	 * A map where the wasted file path is the key and the wasted time (as milliseconds) the value.
	 * @return the map
	 */
	public HashMap<Path, Calendar> getWastedFiles(){
		return wastedPaths;
	}
	/**
	 * Checks if a path matches the {@link #ignoreRegex}. Will be run on the relative path.
	 * @param path the path.
	 * @return true if the relative path matches the regular expression, otherwise false
	 */
	public boolean isIgnored(Path path){
		return ignoreRegex.equals("") ? false : getRelativePath(path).getPath(directory.getSeparator()).matches(ignoreRegex);
	}
	/**
	 * Removes a path from the index and moves it to the trash/waste
	 * @param path the path
	 */
	public void remove(Path path){
		
		//Create version
		versions.addVersion(path);
		
		//Put to wasted files
		wastedPaths.put(path, versions.get(path).getLatestVersion());
		
		//Remove from set
		super.remove(path);
	}
	/**
	 * Checks if the file is wasted.
	 * @param path the file
	 * @return true if the file is in the list of wasted files.
	 */
	public boolean isWastedFile(Path path){
		return wastedPaths.containsKey(path);
	}
	/**
	 * Sets up the variables needed here.
	 * @param directory the base directory
	 * @param ignoreRegex the regular expression for ignoring files and directories
	 */
	private void setupVars(Path directory, String ignoreRegex){

		this.logger = LogManager.getLogger();
		this.directory = directory;
		this.ignoreRegex = ignoreRegex;
		this.wastedPaths = new HashMap<>();
		versions = new Versions(getDirectory().append(VERSIONS_DIRECTORY));

	}
	/**
	 * @return the versions
	 */
	public Versions getVersions(){
		return versions;
	}
	/**
	 * Locates all files that are in both indexes.
	 * @param second the second index
	 * @return a set of paths relative to the index directory.
	 */
	public HashSet<Path> intersect(Set<Path> second){

		//Create set
		HashSet<Path> list = new HashSet<>(this);
		
		//Only keep elements also in second map
		list.retainAll(second);
		
		//Tada!
		return list;
	}
	
	/**
	 * Locates all files that are only in one of the both indexes.
	 * @param second the second index
	 * @return a {@link HashSet} of paths relative to the index directory.
	 */
	public HashSet<Path> symmetricDifferences(Set<Path> second){
		
		//Create Set with contents from both sets
		HashSet<Path> symmDiff = new HashSet<>(this);
		symmDiff.addAll(second);
		
		//Remove intersection
		symmDiff.removeAll(intersect(second));
		
		//Tada!
		return symmDiff;
		
	}
}
