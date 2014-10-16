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
package de.joinout.criztovyl.tools.json;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import de.joinout.criztovyl.tools.file.Path;

/**
 * This class represents a file that can contain JSON data and helps read and
 * write it.
 * 
 * @author criztovyl
 * 
 */
public class JSONFile {

	private final Path path;

	private final Logger logger;

	private String data;

	/**
	 * Loads JSON data from the given file
	 * 
	 * @param path
	 */
	public JSONFile(Path path) {

		logger = LogManager.getLogger();

		this.path = path;

		data = new JSONObject().toString();

		try {
			final BufferedReader br = new BufferedReader(new FileReader(
					path.getFile()));

			String str = "";
			String data = "";

			while ((str = br.readLine()) != null)
				data += str;

			br.close();

			if (!data.equals(""))
				this.data = data;

		} catch (final FileNotFoundException e) {
			logger.catching(e);
		} catch (final IOException e) {
			logger.catching(e);
		}

	}

	/**
	 * Loads the JSON data from the {@link JSONArray}.
	 * 
	 * @param path
	 *            the path to the JSON file
	 * @param json
	 *            the JSON object
	 */
	public JSONFile(Path path, JSONArray json) {

		logger = LogManager.getLogger();

		this.path = path;
		data = json.toString();
	}

	/**
	 * Loads the JSON data from the {@link JSONObject}.
	 * 
	 * @param path
	 *            the path to the JSON file
	 * @param json
	 *            the JSON object
	 */
	public JSONFile(Path path, JSONObject json) {

		logger = LogManager.getLogger();

		this.path = path;
		data = json.toString();
	}

	/**
	 * 
	 * @return the raw JSON data string
	 */
	public String getData() {
		return data;
	}

	/**
	 * 
	 * @return the {@link JSONObject} represented by the given data.
	 */
	public JSONArray getJSONArray() {
		return new JSONArray(data);
	}

	/**
	 * 
	 * @return the {@link JSONObject} represented by the given data.
	 */
	public JSONObject getJSONObject() {
		final JSONObject json = new JSONObject(data);

		return json;
	}

	public Runnable runnableWrite() {
		return new Runnable() {

			public void run() {
				write();

			}
		};
	}

	/**
	 * Writes the JSON data to the file.
	 */
	public void write() {

		try {
			final FileWriter fw = new FileWriter(path.getFile());

			fw.write(data);

			fw.flush();
			fw.close();
		} catch (final IOException e) {
			logger.catching(e);
		}

	}

}
