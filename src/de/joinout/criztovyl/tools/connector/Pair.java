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
package de.joinout.criztovyl.tools.connector;

/**
 * A simple pair of two different objects.
 * @author criztovyl
 *
 */
public class Pair<A, B> {
	
	private A a;
	private B b;

	/**
	 * Creates a new pair of two different objects.
	 * @param a one object
	 * @param b another object
	 */
	public Pair(A a, B b){
		this.a = a;
		this.b = b;
	}
	/**
	 * 
	 * @return one object
	 */
	public A getFirst(){
		return a;
	}
	/**
	 * 
	 * @return the corresponding object of {@link #getFirst()}
	 */
	public B getSecond(){
		return b;
	}
}
