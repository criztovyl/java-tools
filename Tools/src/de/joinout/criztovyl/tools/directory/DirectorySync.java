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
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.joinout.criztovyl.tools.CloneUtils;
import de.joinout.criztovyl.tools.file.Path;

/**
 * Synchronises files of two directories.
 * 
 * @author criztovyl
 * 
 */
public class DirectorySync extends DirectoryChanges {

	private final Logger logger;

	/**
	 * Creates a new directory sync.
	 * 
	 * @param base
	 *            the directory the data is taken from
	 * @param branch
	 *            the directory the files are stored in
	 * @throws IOException If an I/O error occurs
	 */
	public DirectorySync(Path base, Path branch) throws IOException {
		this(base, branch, "");
	}

	/**
	 * Creates a new directory sync.
	 * @param base the base directory
	 * @param branch the branch directory
	 * @param ignoreRegex the regular expression for ignoring files
	 * @throws IOException If an I/O error occurs in {@link DirectoryChanges#DirectoryChanges(Path, Path, String)}
	 */
	public DirectorySync(Path base, Path branch, String ignoreRegex) throws IOException {
		super(base, branch, ignoreRegex);
		
		logger = LogManager.getLogger();
		
		if(getCurrentList().isEmpty())
			logger.warn("Base diretory is empty, will delete _complete_ branch directory if you run DiretorySync#removeDeletedFiles(true)!");
	}

	/**
	 * Copies the new files from the source to the target.
	 */
	public void copyNewFiles() {

		// Iterate over new files
		for (final Path path : getNewFiles()) {

			// Calculate source directory
			final Path srcD = getCurrentList().getDirectory().append(path)
					.getFile().exists() ? getCurrentList().getDirectory()
					: getPreviousList().getDirectory();
			final Path targetD = srcD.equals(getCurrentList().getDirectory()) ? getPreviousList()
					.getDirectory() : getCurrentList().getDirectory();

			try {

				// Clone file
				CloneUtils.cloneFile(srcD.append(path), targetD.append(path));

			} catch (final IOException e) {

				if (logger.isWarnEnabled())
					logger.warn(
							"Caught IOException while copying file {} with source {} and target {}.",
							path, srcD, targetD);
				if (logger.isDebugEnabled())
					logger.debug("IOException.", e);
			}
		}
	}
	
	/**
	 * Runs {@link #removeDeletedFiles(boolean)} with boolean false.
	 * @see #removeDeletedFiles(boolean)
	 */
	public void removeDeletedFiles(){
		removeDeletedFiles(false);
	}
	/**
	 * Removes all deleted files.
	 * @param force whether files should be deleted if base directory is empty.
	 */
	public void removeDeletedFiles(boolean force) {
		
		if(getCurrentList().isEmpty() && !force){
			logger.warn("Not removing any files, base diretory is empty.");
			return;
		}
		
		//Iterate over files
		for(Path path : getDeletedFiles()){
			
			//Make absolute
			path = getPreviousList().getDirectory().append(path);
			
			//Check if path needed to delete is a directory
			if(path.getFile().isDirectory())
				
				try { //Try to delete directory recursively.
					FileUtils.deleteDirectory(path.getFile());
				} catch (IOException e) { //Catch general IOException.
					
					if(logger.isWarnEnabled())
						logger.warn("There was an IOException while deleting the disappeared directory {}: {}", path, e.toString());

					if(logger.isDebugEnabled())
						logger.debug("IOException.", e);
				}
			else
				try { //Try to delete file, using NIO to get an exception whether something went wrong.
					Files.delete(path.getNIOPath());
				} catch (NoSuchFileException e){ //Catch NoSuchFileException (NIO FileNotFoundException), file may has been deleted by an earlier
					if(logger.isDebugEnabled())
						logger.debug("File {} not found, may be already deleted?", path);
				} catch (IOException e) {

					if(logger.isWarnEnabled())
						logger.warn("There was an IOException while deleting the disappeared directory {}: {}", path, e.toString());

					if(logger.isDebugEnabled())
						logger.debug("IOException.", e);
				}
		}
			
	}
	
	/**
	 * Copies new files, updates changed files and remove deleted files.
	 * @see #copyNewFiles()
	 * @see #updateChangedFiles()
	 * @see #removeDeletedFiles()
	 */
	public void sync(){
		copyNewFiles();
		updateChangedFiles();
		removeDeletedFiles();
	}

	/**
	 * Updates the changed files.
	 */
	public void updateChangedFiles() {

		// Iterate of changed files
		for (final Path path : getChangedFiles())
			try {

				// Clone file
				CloneUtils.cloneFile(path, getComplementPath(path));

			} catch (final IOException e) {

				if (logger.isWarnEnabled())
					logger.warn("Caught IOException while copying file {}: {}",
							path, e.toString());
				if (logger.isDebugEnabled())
					logger.debug("IOException.", e);
			}
	}



}
