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
package de.joinout.criztovyl.tools.objective;

import java.util.ArrayList;
import java.util.List;

/**
 * @author criztovyl
 *
 */
public abstract class AbstractTree<T> implements Tree<T>{
	
	private Tree<T> parent;
	private ArrayList<T> children;
	
	public AbstractTree(){
		this.children = new ArrayList<>();
	}
	
	public AbstractTree(Tree<T> parent){
		this();
		this.parent = parent;
	}
	
	@Override
	public List<T> getChildren(){
		return children;
	}
	
	@Override
	public Tree<T> getParentTree(){
		return parent;
	}
	
	@Override
	public boolean add(T t){
		return children.add(t);
	}

}
