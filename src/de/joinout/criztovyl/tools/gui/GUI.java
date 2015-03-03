package de.joinout.criztovyl.tools.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * Represents a simple SWT GUI
 * @author criztovyl
 */
public class GUI {
	
    private Shell shell;
    
    private Logger logger;
  
    /**
     * Creates a new GUI.<br>
     * The Shell will be created on the default display.
     * @see Display#getDefault()
     */
    public GUI(){
    	this(new Shell(Display.getDefault()));
    }
    /**
     * Creates a new GUI.<br>
     * Takes a shell.
     * @param the shell.
     */
    public GUI(Shell shell){
    	logger = LogManager.getLogger();
    	this.shell = shell;
    }
    /**
     * Starts the GUI
     */
    public void start(){
    	
        if(logger.isInfoEnabled())
            logger.info("Starting new SWT...");
        
        //Open shell
        shell.open();
        
        //Shell loop
        while(!shell.isDisposed()){
        	
            if(!shell.getDisplay().readAndDispatch())
            	shell.getDisplay().sleep();
        }
        
        if(logger.isInfoEnabled())
        	logger.info("Exiting SWT...");
        
        //Shell loop ended, dispose.
        //shell.getDisplay().dispose();
        
        if(logger.isInfoEnabled())
        	logger.info("Exited.");
    }
    /**
     * Returns the shell of this GUI.
     * @return a {@link Shell}
     */
    public Shell getShell(){
    	return shell;
    }
}
