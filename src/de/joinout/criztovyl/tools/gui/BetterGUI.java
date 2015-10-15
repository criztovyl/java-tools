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
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;

/**
 * @author criztovyl
 * 
 */
public class BetterGUI extends GUI{

	private Control last;
	private Scrollable inside;

	private boolean nextBreak;
	private boolean stdPack;

	/**
	 * Creates a new instance.<br>
	 * Controls will be packed by default and new shell will be created on
	 * {@link Display#getDefault()}.<br>
	 * To change use one of the following:
	 * <ul>
	 * <li>{@link #BetterGUI(boolean)} for packing</li>
	 * <li>{@link #BetterGUI(Display)} for display</li>
	 * <li>{@link #BetterGUI(Shell)} for shell</li>
	 * <li>{@link #BetterGUI(Display, boolean)} for display and packing</li>
	 * <li>{@link #BetterGUI(Shell, boolean)} for shell and packing</li>
	 * </ul>
	 */
	public BetterGUI(){
		this(Display.getDefault(), true);
	}
	
	/**
	 * Creates a new instance.<br>
	 * A new shell will be created on {@link Display#getDefault()}.<br>
	 * To change use {@link #BetterGUI(Display, boolean)} or {@link #BetterGUI(Shell, boolean)}
	 * @param stdPack whether controls should be packed by default
	 */
	public BetterGUI(boolean stdPack){
		this(Display.getDefault(), stdPack);
	}
	
	/**
	 * Creates a new instance.<br>
	 * A new shell will be created on specified display and controls will be packed by default.<br>
	 * To change use one of the following:
	 * <ul>
	 * <li>{@link #BetterGUI(boolean)} for packing</li>
	 * <li>{@link #BetterGUI(Shell)} for shell</li>
	 * <li>{@link #BetterGUI(Shell, boolean)} for shell and packing</li>
	 * </ul>
	 * @param display the display
	 * 
	 */
	public BetterGUI(Display display){
		this(display, true);
	}
	
	/**
	 * Creates a new instance.<br>
	 * Specified shell will be used.<br>
	 * Controls are packed by default.<br>
	 * To change use {@link #BetterGUI(Shell, boolean)}.
	 * @param shell the shell
	 */
	public BetterGUI(Shell shell){
		this(shell, true);
	}
	
	/**
	 * Creates a new instance.<br>
	 * A new shell will be created on the specified display.<br>
	 * To change use {@link #BetterGUI(Shell, boolean)}.
	 * @param display the display
	 * @param stdPack whether controls should be packed by default.
	 */
	public BetterGUI(Display display, boolean stdPack){
		this(new Shell(display), stdPack);
	}
	/**
	 * Creates a new instance.
	 * @param shell the shell to use
	 * @param stdPack whether controls should be packed by default.
	 */
	public BetterGUI(Shell shell, boolean stdPack){
		super(shell);
		
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
				getLastBounds().x - control.getBounds().width,
				getLastBounds().y);

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
				getLastBounds().x + getLastBounds().width,
				getLastBounds().y);

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
				getLastBounds().x,
				getLastBounds().y - control.getBounds().height);

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
				getLastBounds().x,
				getLastBounds().y + getLastBounds().height);

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
	 * Can be resized by a factor if <code>relative</code> is true.
	 * @param width the width or factor
	 * @param relative whether use width as factor or not.
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
	 * Can be resized by a factor if <code>relative</code> is true.
	 * @param height the height  or factor
	 * @param relative whether use width as factor or not.
	 */
	public void resizeHeight(double height, boolean relative){
		if(relative)
			//Reference is null so getLast().getSize() is used.
			resize(height, 1, null);
		else
			resizeHeight((int) height);
	}
	/**
	 * Resizes the last control by a {@link Point}'s x &amp; y coordinate.<br>
	 * x is width and y is height.
	 * @param size the point
	 */
	public void resize(Point size){
		resize(size.x, size.y, false);
	}
	
	/**
	 * Resizes the last control by a x &amp; y coordinate.<br>
	 * x is width and y height.<br>
	 * If relative is true, last control will be resized by multiply old and new x/y.
	 * @param x with/width factor.
	 * @param y height/height factor
	 * @param relative whether use width/height as factor or not.
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
				getInside().getClientArea().x, 
				getInside().getClientArea().y);

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
	 * Places a control below the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void belowRow(Control control, boolean pack) {

		control.setLocation(
				getInside().getClientArea().x, 
				getLastBounds().y + getLastBounds().height);

		last_n_pack(control, pack);

	}

	/**
	 * Places a control into row above the previous control.
	 * @param control the control to place
	 */
	public void aboveRow(Control control){
		aboveRow(control, stdPack);
	}

	/**
	 * Places a control into row above the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 */
	public void aboveRow(Control control, boolean pack){

		control.setLocation(
				getInside().getClientArea().x, 
				getLastBounds().y - control.getBounds().height);

		last_n_pack(control, pack);
	}

	/**
	 * Places a control below the previous control.
	 * @param control the control to place
	 * @deprecated Use {@link #add(Control)}.
	 */
	@Deprecated
	public void begin(Control control){
		begin(control, stdPack);
	}

	/**
	 * Places a control inside the previous control and set whether control should be "packed" ({@link Control#pack()}).
	 * @param control the control to place
	 * @param pack whether control should be packed
	 * @deprecated Use {@link #add(Control, boolean)}.
	 */
	@Deprecated
	public void begin(Control control, boolean pack){

		control.setLocation(0, 0);

		last_n_pack(control, pack);
	}

	/**
	 * Places a control right to the previous control.
	 * @param control the control to place
	 */
	public void add(Control control){
		add(control, stdPack);
	}

	/**
	 * Places a control right to the previous control and set whether control should be "packed" ({@link Control#pack()}).
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
	 * The {@link Scrollable} to place inside.
	 * @return a {@link Scrollable}, if <code>inside</code> wasn't set yet, the Shell will be used.
	 */
	private Scrollable getInside(){
		return inside == null ? getShell() : inside;
	}
	
	/**
	 * The last control.<br>
	 * Is <code>null</code> until no control added.
	 * @return a {@link Control}
	 */
	public Control getLast(){
		return last;
	}
	
	/**
	 * Sets the last used {@link Control}
	 * @param control the {@link Control}
	 */
	private void setLast(Control control){
		
		//Set last
		last = control;
		
		//Set inside too, if was Scrollable
		if(control instanceof Scrollable)
			inside = (Scrollable) control;
	}
	
	/**
	 * The last {@link Control}'s bounds {@link Rectangle}.<br>
	 * As last can be <code>null</code> but every control added, also the first, relies on 
	 * {@link Control#getBounds()}, its needed, that you can get bounds also if last control is
	 * <code>null</code>, without doing a redundant <code>null</code>-Check.
	 * @return a {@link Rectangle}, if <code>last</code> is <code>null</code> all values will be 0.
	 */
	private Rectangle getLastBounds(){
		return getLast() == null ? new Rectangle(0, 0, 0, 0) : getLast().getBounds();
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
	/**
	 * Places a control below a specified control.
	 * @param control the control
	 * @param reference the reference
	 */
	public void below(Control control, Control reference){
		below(control, reference, stdPack);
	}
	/**
	 * Places a control below a specified control.
	 * @param control the control
	 * @param reference the reference
	 * @param pack whether the Control should be packed
	 */
	public void below(Control control, Control reference, boolean pack){
		
		Rectangle refBounds = reference.getBounds();
		
		control.setLocation(
				refBounds.x,
				refBounds.y + refBounds.height);
		
		last_n_pack(control, pack);
	}
}
