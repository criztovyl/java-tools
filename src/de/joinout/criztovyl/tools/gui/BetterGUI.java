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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

/**
 * @author criztovyl
 *
 */
public class BetterGUI extends GUI{

	private Rectangle clientArea;
	private Control last;

	private boolean nextBreak;
	private boolean stdPack;

	public BetterGUI(){
		this(Display.getDefault());
	}

	public BetterGUI(Display display){
		super(display);

		nextBreak = false;
		stdPack = true;
	}

	public void setClientArea(Rectangle clientArea){
		this.clientArea = clientArea;
	}

	public void setClientArea(Group reference){
		this.clientArea = reference.getClientArea();
	}

	public Rectangle getClientArea(){
		return clientArea == null ? new Rectangle(0, 0, 0, 0) : clientArea;
	}

	public void left(Control control){
		left(control, stdPack);
	}

	public void left(Control control, boolean pack){

		control.setLocation(getReference().x - control.getBounds().width, getReference().y);

		pack_n_last(control, pack);
	}

	public void right(Control control){
		right(control, stdPack);
	}

	public void right(Control control, boolean pack){

		control.setLocation(getReference().x + getReference().width, getReference().y);

		pack_n_last(control, pack);
	}

	public void above(Control control){
		above(control, stdPack);
	}

	public void above(Control control, boolean pack){

		control.setLocation(getReference().x, getReference().y - control.getBounds().height);

		pack_n_last(control, pack);
	}

	public void below(Control control){
		below(control, stdPack);
	}

	public void below(Control control, boolean pack){

		control.setLocation(getReference().x, getReference().y + getReference().height);

		pack_n_last(control, pack);
	}

	public void resizeWidth(int width){
		last.setSize(width, last.getSize().y);
	}

	public void resizeHeight(int height){
		last.setSize(last.getSize().x, height);
	}

	public void resize(Point size){
		resizeWidth(size.x);
		resizeHeight(size.y);
	}

	public void inside(Control control){
		inside(control, stdPack);
	}

	public void inside(Control control, boolean pack){

		control.setLocation(getClientArea().x, getClientArea().y);

		pack_n_last(control, pack);
	}

	public void belowRow(Control control){
		below(control, stdPack);
	}

	public void belowRow(Control control, boolean pack) {

		control.setLocation(
				getClientArea().x, 
				getReference().y + 
				getReference().height);

		pack_n_last(control, pack);

	}

	public void aboveRow(Control control){
		above(control, stdPack);
	}

	public void aboveRow(Control control, boolean pack){

		control.setLocation(getClientArea().x, getReference().y - control.getBounds().height);

		pack_n_last(control, pack);
	}

	public void begin(Control control){
		begin(control, stdPack);
	}

	public void begin(Control control, boolean pack){

		control.setLocation(0, 0);

		pack_n_last(control, pack);
	}

	public void add(Control control){
		add(control, stdPack);
	}

	public void add(Control control, boolean pack){

		if(nextBreak){
			belowRow(control, pack);
			nextBreak = false;
		}
		else
			right(control, pack);
	}

	public void nextRow(){
		nextBreak = true;
	}

	private Rectangle getReference(){
		return last == null ? new Rectangle(0, 0, 0, 0) : last.getBounds();
	}

	private void setLast(Control control){
		last = control;
	}

	private void pack_n_last(Control control, boolean pack){
		setLast(control);

		if(pack)
			control.pack();
	}
}
