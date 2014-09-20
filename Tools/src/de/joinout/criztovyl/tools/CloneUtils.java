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

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.files.FileList;

/**
 * Copies files and directories and keeps the modification time.
 * @author criztovyl
 * 
 */
public class CloneUtils {

	/**
	 * Pass-through to {@link #cloneDirectory(Path, Path)} by creating {@link Path}s from the {@link String}s.
	 * @param src the source path {@link String}
	 * @param target the target path {@link String}
	 * @throws IOException If an I/O error occurs
	 * @see  #cloneDirectory(Path, Path)
	 */
	public static void cloneDirectory(String src, String target) throws IOException {
		cloneDirectory(new Path(src), new Path(target));
		
	}
	
	/**
	 * Copies a directory to a different one and keep all modification times.
	 * @see #cloneFile(Path, Path) 
	 * @param directory
	 *            the source
	 * @param target
	 *            the target
	 * @throws IOException If an I/O error occurs
	 */
	public static void cloneDirectory(Path directory, Path target)
			throws IOException {

		// Set up lost for src
		final FileList fl = new FileList(directory).relative();

		// Iterate over files
		for (final Path path : fl) {

			// Create src and target path
			final Path targetP = target.append(path).realPath();
			final Path srcP = fl.getDirectory().append(path);

			// Clone (existing) files only
			if (srcP.getFile().isFile())
				CloneUtils.cloneFile(srcP, targetP);
		}

	}

	/**
	 * Pass-through to {@link #cloneFile(Path, Path)} by creating {@link Path}s from the {@link String}s.
	 * @param src the source path {@link String}
	 * @param target the target path {@link String}
	 * @throws IOException If an I/O error occurs
	 */
	public static void cloneFile(String src, String target) throws IOException{
		cloneFile(new Path(src), new Path(target));
	}
	/**
	 * Copies a file and keep the same modification date/time.
	 * @param src
	 *            the source path
	 * @param target
	 *            the target path
	 * @throws IOException If an I/O error occurs
	 */
	public static void cloneFile(Path src, Path target) throws IOException {

		// Cancel if is no file
		if (!src.getFile().isFile())
			return;

		// Copy file
		FileUtils.copyFile(src.getFile(), target.getFile());

		// Long is not accurate enough, so re-set lastModifed @ src file
		src.getFile().setLastModified(src.getFile().lastModified());

		// Copy lastModified to target file
		target.getFile().setLastModified(src.getFile().lastModified());

	}
}
