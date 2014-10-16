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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.joinout.criztovyl.tools.file.Path;

/**
 * @author criztovyl
 * 
 */
public class MultiFileGrep {

	private final Logger logger;

	public MultiFileGrep(String regex, List<Path> paths) {

		logger = LogManager.getLogger();

		for (final Path file : paths) {
			System.out.println(file);
			System.out.println();
			try {
				for (final String line : FileUtils.readLines(file.getFile()))
					if (line.matches(regex))
						System.out.println(line);
			} catch (final FileNotFoundException e) {
				logger.warn("File {} not found!", file);
				logger.debug(e);
			} catch (final IOException e) {
				logger.error("IOException!", e);
			}
			System.out.println();
		}
	}

	public MultiFileGrep(String regex, Path... paths) {
		this(regex, Arrays.asList(paths));
	}

}
