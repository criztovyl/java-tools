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
package de.joinout.criztovyl.tools.gui;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * @author criztovyl
 *
 */
public class GUITools {
	
	private Rectangle clientArea;
	private Control last;
	
	public GUITools(){
		
	}
	
	public void setClientArea(Rectangle clientArea){
		this.clientArea = clientArea;
	}
	
	public void setClientArea(Group reference){
		this.clientArea = reference.getClientArea();
	}
	
	public Rectangle getClientArea(){
		return clientArea;
	}
	
	public void left(Control widget){
		
		widget.setLocation(getReference().x - widget.getBounds().width, getReference().y);
		
		setLast(widget);

	}
	
	public void right(Control widget){

		widget.setLocation(getReference().x + getReference().width, getReference().y);

		setLast(widget);
	}
	
	public void above(Control widget){

		widget.setLocation(getReference().x, getReference().y - widget.getBounds().height);

		setLast(widget);
	}
	
	public void below(Control widget){

		widget.setLocation(getReference().x, getReference().y + getReference().height);

		setLast(widget);
	}
	
	public void setWidth(int width){
		last.setSize(width, last.getSize().y);
	}
	
	public void setHeight(int height){
		last.setSize(last.getSize().x, height);
	}
	
	public void inside(Control widget){

		widget.setLocation(clientArea.x, clientArea.y);

		setLast(widget);
	}
	
	public void belowRow(Control widget){

		widget.setLocation(clientArea.x, getReference().y + getReference().height);

		setLast(widget);
	}
	
	public void aboveRow(Control widget){

		widget.setLocation(clientArea.x, getReference().y - widget.getBounds().height);

		setLast(widget);
	}
	
	private Rectangle getReference(){
		return last.getBounds();
	}
	
	private void setLast(Control widget){
		last = widget;
	}
}
