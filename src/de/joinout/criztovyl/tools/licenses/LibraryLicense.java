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
package de.joinout.criztovyl.tools.licenses;

import java.util.Calendar;

import de.joinout.criztovyl.tools.strings.StringUtils;

/**
 * A class that holds a license of a library that can have sub-libraries.
 * @author criztovyl
 *
 */
public class LibraryLicense extends License{
	
	private String author, name, years;
	private LibraryLicenses sub;
	
	/**
	 * Creates a new license for a library.
	 * @param license the library license
	 * @param name the library name
	 * @param author the library author
	 */
	public LibraryLicense(License license, String name, String author){
		this(license, name, author, "");
	}
	/**
	 * Creates a new license for a library.
	 * @param license the library license
	 * @param name the library name
	 * @param author the library author
	 * @param years "copyright" years
	 */
	public LibraryLicense(License license, String name, String author, String years){
		
		super(license);
		
		this.name = name;
		this.author = author;
		this.years = years;
		this.sub = new LibraryLicenses();
	}
	/**
	 * 
	 * @return the author
	 */
	public String getAuthor(){
		return author;
	}
	/**
	 * 
	 * @return the library name
	 */
	public String getLibraryName(){
		return name;
	}
	/**
	 * 
	 * @return the "copyright" years
	 */
	public String getYears(){
		return years.equals("") ? Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) : years;
	}
	/**
	 * Pass-through to {@link License#getTiny(String)} with library name.
	 * @see License#getTiny(String)
	 * @return a {@link String}
	 */
	public String getTiny(){
		String usage = new StringUtils(sub.names(LibraryLicenses.F_TINY)).
				join(StringUtils.STYLE_KOMMA_AND);
		
		return String.format("%s (%s License)%s", 
				getLibraryName(), 
				getName(), 
				usage.equals("") ? "" : " [Using " + usage + "]");
	}
	/**
	 * Pass-through to {@link License#getShort(String, String, String)} with library name, years and author.
	 * @see License#getShort(String, String, String)
	 * @return a {@link String}
	 */
	public String getShort(){
		return String.format("%s%n\tUsing %s.", 
				super.getShort(getLibraryName(), getYears(), getAuthor()),
				new StringUtils(sub.names(LibraryLicenses.F_TINY)).join(StringUtils.STYLE_KOMMA_AND));
	}
	/**
	 * Pass-through to {@link License#getHeader(String, String, String)} with library name, years and author.
	 * @see License#getHeader(String, String, String)
	 * @return a {@link String}
	 */
	public String getHeader(){
		return super.getHeader(getLibraryName(), getYears(), getAuthor());
	}
	/*
	 * (non-Javadoc)
	 * @see de.joinout.criztovyl.tools.licenses.License#getLicenseText()
	 */
	public String getText(){
		return String.format("%s%n\tThe following Libraries where also used: %s.", 
				super.getText(), 
				new StringUtils(sub.names()).join(StringUtils.STYLE_KOMMA_AND));
	}
	/**
	 * Adds sub-libraries.
	 * @param libraries the sub-libraries
	 */
	public void addSubLibrary(LibraryLicense ...libraries){
		for(LibraryLicense library : libraries)
			sub.addLibrary(library);
	}
	/**
	 * 
	 * @return the sub-libraries as {@link LibraryLicenses}
	 */
	public LibraryLicenses getSubLibraries(){
		return sub;
	}
}
