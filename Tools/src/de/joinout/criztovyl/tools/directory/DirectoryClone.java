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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.joinout.criztovyl.tools.file.Path;
import de.joinout.criztovyl.tools.files.FileList;

/**
 * @author criztovyl
 *
 */
public class DirectoryClone {

	/**
	 * Clones a directory to a different one. Means that the contents will be copied and the {@link File#lastModified()} is also copied.
	 * @param directory the source
	 * @param target the target
	 * @throws IOException
	 */
	public static void clone(Path directory, Path target) throws IOException{

		//Set up lost for src
		FileList fl = new FileList(directory).relative();

		//Iterate over files
		for(Path path : fl){

			//Create src and target path
			Path targetP = target.append(path).realPath();
			Path srcP = fl.getDirectory().append(path);
			

			//Clone files only
			if(srcP.getFile().isFile()){
				//Copy file
				FileUtils.copyFile(srcP.getFile(), targetP.getFile());

				//Long is not accurate enough, so re-set lastModifed @ src file
				srcP.getFile().setLastModified(srcP.getFile().lastModified());
				
				//Copy lastModified to target file
				targetP.getFile().setLastModified(srcP.getFile().lastModified());
				
				
			}
		}

	}
}
