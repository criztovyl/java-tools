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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * @author criztovyl
 *
 */
public class BetterGUI extends GUI{

	private Control last;

	private boolean nextBreak;
	private boolean stdPack;

	/**
	 * Creates a new instance.<br>
	 * Pass-through to {@link #BetterGUI(Display, boolean)} with <code>null</code> and <code>true</code>.
	 */
	public BetterGUI(){
		this(null, true);
	}
	
	/**
	 * Creates a new instance.<br>
	 * Pass-through to {@link #BetterGUI(Display, boolean)} with <code>null</code> and <code>stdPack</code>.
	 * @param stdPack whether controls should be packed by default
	 */
	public BetterGUI(boolean stdPack){
		this(null, stdPack);
	}
	
	/**
	 * Creates a new instance with a specified display.
	 * Pass-through to {@link #BetterGUI(Display, boolean)} with <code>display</code> and <code>true</code>.
	 * @param display the display
	 * 
	 */
	public BetterGUI(Display display){
		this(display, true);
	}
	
	/**
	 * Creates a new instance with a specified display.
	 * Pass-through to {@link #BetterGUI(Display, boolean)} with <code>display</code> and <code>true</code>.
	 * @param display the display
	 * 
	 */
	public BetterGUI(Display display, boolean stdPack){
		
		super(display);

		//Initialise
		nextBreak = false;
		
		//Set pack
		this.stdPack = stdPack;
	}
	
	/**
	 * Places a control to the left of the previous control.
	 * @param control the control to place
	 */
	public void left(Control control){
		left(control, stdPack);
	}

	/**
	 * Places a control to the left of the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void left(Control control, boolean pack){

		control.setLocation(
				getLast().getBounds().x - control.getBounds().width,
				getLast().getBounds().y);

		last_n_pack(control, pack);
	}

	/**
	 * Places a control to the right of the previous control.
	 * @param control the control to place
	 */
	public void right(Control control){
		right(control, stdPack);
	}

	/**
	 * Places a control to the right of the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void right(Control control, boolean pack){

		control.setLocation(
				getLast().getBounds().x + getLast().getBounds().width,
				getLast().getBounds().y);

		last_n_pack(control, pack);
	}

	/**
	 * Places a control above the previous control.
	 * @param control the control to place
	 */
	public void above(Control control){
		above(control, stdPack);
	}

	/**
	 * Places a control above the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void above(Control control, boolean pack){

		control.setLocation(
				getLast().getBounds().x,
				getLast().getBounds().y - control.getBounds().height);

		last_n_pack(control, pack);
	}

	/**
	 * Places a control below the previous control.
	 * @param control the control to place
	 */
	public void below(Control control){
		below(control, stdPack);
	}

	/**
	 * Places a control below the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void below(Control control, boolean pack){

		control.setLocation(
				getLast().getBounds().x,
				getLast().getBounds().y + getLast().getBounds().height);

		last_n_pack(control, pack);
	}

	/**
	 * Resizes the width of the last control.
	 * @param width the new width
	 */
	public void resizeWidth(int width){
		resize(new Point(width, getLast().getSize().y));
	}
	
	/**
	 * Resizes the width of the last control.<br>
	 * Can be resize by a factor if <code>relative</code> is true.
	 * @param width the width or factor
	 * @param relative
	 */
	public void resizeWidh(double width, boolean relative){
		if(relative)
			//Reference is null so getLast().getSize() is used.
			resize(width, 1, null);
		else
			resizeWidth((int) width);
	}
	/**
	 * Resizes the height of the last control.
	 * @param height the new height
	 */
	public void resizeHeight(int height){
		resize(new Point(getLast().getSize().x, height));
	}
	
	/**
	 * Resizes the height of the last control.<br>
	 * Can be resize by a factor if <code>relative</code> is true.
	 * @param height the height  or factor
	 * @param relative
	 */
	public void resizeHeight(double height, boolean relative){
		if(relative)
			//Reference is null so getLast().getSize() is used.
			resize(height, 1, null);
		else
			resizeHeight((int) height);
	}
	/**
	 * Resizes the last control by a {@link Point}'s x & y coordinate.<br>
	 * x is width and y is height.
	 * @param size the point
	 */
	public void resize(Point size){
		resize(size.x, size.y, false);
	}
	
	/**
	 * Resizes the last control by a x & y coordinate.<br>
	 * x is width and y height.<br>
	 * If relative is true, last control will be resized by multiply old and new x/y.
	 * @param size
	 * @param relative
	 */
	public void resize(double x, double y, boolean relative){
		
		//Check if should resize relative
		if(relative)
			//Reference is null so getLast().getSize() is used.
			resize(x, y, null);
		else
			getLast().setSize((int) x, (int) y);
	}
	
	/**
	 * Resizes the last control by a x- and y-factor.<br>
	 * x is width and y height.
	 * @param x the factor for the width
	 * @param y the factor for the height
	 * @param reference the reference (if <code>null</code>, {@link #getLast()}'s {@link Control#getSize()} is used.
	 */
	public void resize(double x, double y, Point reference){
		
		//Set reference to getLast().getSize() if is null
		reference = reference == null ? getLast().getSize() : reference;
		
		getLast().setSize((int) x * reference.x, (int) y * reference.y);
	}

	/**
	 * Places a control into the previous control.
	 * @param control the control to place
	 */
	public void inside(Control control){
		inside(control, stdPack);
	}

	/**
	 * Places a control inside the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void inside(Control control, boolean pack){

		control.setLocation(
				getShell().getShell().getClientArea().x, 
				getShell().getShell().getClientArea().y);

		last_n_pack(control, pack);
	}
	
	/**
	 * Places a control below the previous control.
	 * @param control the control to place
	 */
	public void belowRow(Control control){
		belowRow(control, stdPack);
	}

	/**
	 * Places a control inside the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void belowRow(Control control, boolean pack) {

		control.setLocation(
				getShell().getShell().getClientArea().x, 
				getLast().getBounds().y + getLast().getBounds().height);

		last_n_pack(control, pack);

	}

	/**
	 * Places a control below the previous control.
	 * @param control the control to place
	 */
	public void aboveRow(Control control){
		aboveRow(control, stdPack);
	}

	/**
	 * Places a control inside the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void aboveRow(Control control, boolean pack){

		control.setLocation(
				getShell().getClientArea().x, 
				getLast().getBounds().y - control.getBounds().height);

		last_n_pack(control, pack);
	}

	/**
	 * Places a control below the previous control.
	 * @param control the control to place
	 */
	public void begin(Control control){
		begin(control, stdPack);
	}

	/**
	 * Places a control inside the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void begin(Control control, boolean pack){

		control.setLocation(0, 0);

		last_n_pack(control, pack);
	}

	/**
	 * Places a control below the previous control.
	 * @param control the control to place
	 */
	public void add(Control control){
		add(control, stdPack);
	}

	/**
	 * Places a control inside the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void add(Control control, boolean pack){

		if(nextBreak){
			belowRow(control, pack);
			nextBreak = false;
		}
		else
			right(control, pack);
	}

	/**
	 * Places next control into next row below.
	 */
	public void nextRow(){
		nextBreak = true;
	}
	
	/**
	 * The last control.
	 * @return a {@link Control}
	 */
	public Control getLast(){
		return last;
	}
	
	/**
	 * Sets the last used {@link Control}
	 * @param control
	 */
	private void setLast(Control control){
		last = control;
	}
	
	/**
	 * Does {@link #setLast(Control)} and runs {@link Control#pack()} if wanted.
	 * @param control the last {@link Control}
	 * @param pack whether control should be packed. 
	 */
	private void last_n_pack(Control control, boolean pack){
		
		setLast(control);

		if(pack)
			control.pack();
	}
}
