package gui;

import java.util.logging.Logger;

import generation.DefaultOrder;
import generation.Factory;
import generation.Floorplan;
import generation.Maze;
import generation.MazeFactory;
import gui.Constants.UserInput;
import gui.Robot.Direction;

/**
 * Class handles the user interaction
 * while the game is in the second stage
 * where the user sees a progress bar while a maze
 * is generated by the factory.
 * This class is part of a state pattern for the
 * Controller class. It is a ConcreteState.
 * 
 * It implements a state-dependent behavior
 * that controls the display and reacts to key board input from a user. 
 * At this point user keyboard input is first dealt with a key listener in Control
 * and then handed over with a call to the handleUserInput method.
 * 
 * Responsibilities:
 * Show the generating screen and the progress during generation,
 * Accept input to interrupt and cancel the maze generation and return to title screen,  
 * Generate a maze with the maze factory.
 *
 * This code contains refactored code from Maze.java by 
 * Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class StateGenerating extends DefaultOrder implements State {
	/**
	 * The logger is used to track execution and report issues.
	 * Collaborators are the UI and the MazeFactory.
	 * Level INFO: logs mitigated issues such as unexpected user input
	 * Level FINE: logs information flow in and out of its fields.
	 */
	private static final Logger LOGGER = Logger.getLogger(StateGenerating.class.getName());

	/**
	 * The view determines what is seen on the screen.
	 */
    SimpleScreens view;
    
    /**
     * The panel is the capability to draw on the screen.
     */
    MazePanel panel;
    
    /**
     * Control is the context class of the State pattern.
     * The reference is needed to pull some pieces of information
     * plus switch control to the next state, which 
     * is the maze generating state.
     */
    Control control;
    
    /** 
     * The filename for a file that stores a maze.
     * If filename is null: generate a maze. 
     * If filename is set to a name of an xml file with a maze: load maze from file
     */
    private String filename;
    
    // about the maze and its generation
    // this information is stored in the super class DefaultOrder
    // private int seed; // the seed value used for the random number generator
    // private int skillLevel; // user selected skill level, i.e. size of maze
    // private Builder builder; // selected maze generation algorithm
    // private boolean perfect; // selected type of maze, i.e. 
    // perfect == true: no loops, i.e. no rooms
    // perfect == false: maze can support rooms
   
    // The factory is used to calculate a new maze configuration
    // The maze is computed in a separate thread which makes 
    // communication with the factory slightly more complicated.
    // Check the factory interface for details.
    protected Factory factory;
    
    // The maze configuration produced by the factory and the progress
    // is stored in the superclass DefaultOrder
    //private MazeConfiguration mazeConfig; 
    //private int percentdone;        // describes progress during generation phase

    /** 
     * Started is used to enforce ordering constraint on method calls.
     * start() must be called before handleUserInput()
     * to make sure the control variable has been set.
     * initial setting: false, start sets it to true.
     */
    boolean started;
    
    /**
     * Constructor uses default settings such that a Depth-First-Search algorithm 
     * is used as the generation method a maze of smallest possible size.
     */
    public StateGenerating() {
    	// default constructor of superclass 
    	// decides on skill level, builder, perfect, progress, seed and alike
    	//
    	// initialization of some fields is delayed and done in start method
    	view = null; // initialized in start method
    	panel = null; // provided by start method
    	control = null; // provided by start method
    	started = false; // method start has not been called yet
    	
    	filename = null; // no information yet, provided by set method if at all

        factory = new MazeFactory() ;
       
    }
    
    /**
     * Sets the filename.
     * Calling with null does not hurt
     * but is pointless as maze will be then
     * generated randomly, which is the default
     * behavior anyway.
     * The filename must be set before 
     * {@link #start(Control, MazePanel) start} is
     * called or it will have no effect for this round
     * of the game.
     * @param filename provides the name of a file to
     * load a previously generated and stored maze from.
     * Can be null.
     */
    public void setFileName(String filename) {
    	// fail if control flow is wrong
    	// the filename must be set before start method is called
    	assert !started: "filename handed too late to be effective."; 
    	
        this.filename = filename;  
    }
    

    /**
     * Loads maze from file and returns a corresponding maze configuration.
     * @param filename, not null
     * @return the maze that has been loaded from the given file
     */
    private Maze loadMazeConfigurationFromFile(String filename) {
        // load maze from file
        MazeFileReader mfr = new MazeFileReader(filename) ;
        // obtain MazeConfiguration
        return mfr.getMazeConfiguration();
    }
    /**
     * Start the maze generation.
     * @param controller needed to be able to switch states, not null
     * @param panel is the UI entity to produce the generating screen on 
     */
    public void start(Control controller, MazePanel panel) {
        started = true;
        // keep the reference to the controller to be able to call method to switch the state
        control = controller;
        // keep the reference to the panel for drawing
        this.panel = panel;
        // init the view
        view = new SimpleScreens();
        // reset percentage for progress
        super.updateProgress(0);
        // if given a filename, load maze from file
        // otherwise, show view and order maze from factory
        if (filename != null) {
            // load maze from file
        	// TODO: add progress reporting to MazeFileReader
            // push results into controller, imitating maze factory delivery
            deliver(loadMazeConfigurationFromFile(filename));
            // reset filename, next round will be generated again
            filename = null;  
        } else {
            // common case: generate maze with some algorithm
            assert null != factory : "StateGenerating.start: factory must be present";
            // draw the initial screen
            draw();
            // make maze factory produce a maze 
            // operates with background thread
            // method returns immediately, 
            // maze will be delivered later by calling this.deliver method
            // this object implements Order, so it carries the spec for the maze
            // to be generated
            if (!factory.order(this)) {
            	// This situation is not supposed to happen!
            	// Factory is currently busy working on another maze order
            	// current strategy: give up on this new order
            	// and let background thread proceed with last one
            	LOGGER.info("Factory refused order, apparently busy: mitigation strategy is to let last order proceed");
            	// possible alternative strategy: cancel old order, order this one
            }
        }
    }
    
    /**
     * The method provides an appropriate response to user keyboard input. 
     * The control calls this method to communicate input and delegate its handling.
     * Method requires {@link #start(Controller, MazePanel) start} to be
     * called before.
     * @param userInput provides the user request, here only return to title screen is supported
     * @param value is not used in this state, exists only for consistency across State classes
     * @return true if handled, false if input comes before start was called
     */
    public boolean handleUserInput(UserInput userInput, int value) {
    	// user input too early, not sure how this could happen
        if (!started) {
        	LOGGER.info("Premature keyboard input:" + userInput + "with value " + value + ", ignored for mitigation");
            return false;
        }

        // user could interrupt generation by pressing esc
        if (userInput == UserInput.RETURNTOTITLE) {
            factory.cancel();
            switchToTitle();
        }
        else {
        	LOGGER.info("Unexpected command:" + userInput + "with value " + value + ", ignored for mitigation");
        }    
        return true;
    }
    /**
     * The deliver method is the call back method for the background
     * thread operated in the maze factory to deliver the ordered
     * product, here the generated maze in its container, 
     * the MazeConfiguration object.
     */
    @Override
    public void deliver(Maze mazeConfig) {
    	super.deliver(mazeConfig);
        // WARNING: DO NOT REMOVE, USED FOR GRADING PROJECT ASSIGNMENT
        if (Floorplan.deepdebugWall)
        {   // for debugging: dump the sequence of all deleted walls to a log file
            // This reveals how the maze was generated
            mazeConfig.getFloorplan().saveLogFile(Floorplan.deepedebugWallFileName);
        }
        switchFromGeneratingToPlaying(mazeConfig);
    }
    
    /**
     * Switches the controller to the playing screen.
     * This is where the user or a robot can navigate through
     * the maze and play the game.
     * @param maze contains a maze to play
     */
    public void switchFromGeneratingToPlaying(Maze maze) {
    	assert null != maze : "StateGenerating.switchFromGeneratingToPlaying: maze must exist!";
    	// need to instantiate and configure the playing state
        StatePlaying currentState = new StatePlaying();
        control.setState(currentState);
        currentState.setMaze(maze);
        
        if(control.robot!=null && control.driver!=null) {
        	control.robot.setController(control);
        	control.driver.setMaze(maze);
        	for(Direction direction: Direction.values()) {
        		try {
        			//start the repair cycle
					control.robot.startFailureAndRepairProcess(direction, 4000, 2000);
					//If there are multiple sensors to be repaired, wait 1.3 sec
					Thread.sleep(1300);
				} catch (Exception e) {
					System.out.println("Reliable Sensor");
				}
        	}
        }
        
        
        LOGGER.fine("Control switches from generating to playing screen, maze generation completed.");
        
        // update the context class with the new state
        // and hand over control to the new state
        
        currentState.start(control, panel);
    }
    /**
     * Switches the controller to the initial screen.
     */
    public void switchToTitle() {
    	// need to instantiate and configure the title state
        StateTitle currentState = new StateTitle();
        
        LOGGER.fine("Control switches from generating to title screen, maze generation interrupted.");
        
        // update the context class with the new state
        // and hand over control to the new state

        control.setState(currentState);
        currentState.start(control, panel);
    }

    /**
     * Allows external increase to percentage in generating mode.
     * Internal value is only updated if it exceeds the last value and is less or equal 100
     * @param percentage gives the new percentage on a range [0,100]
     */
    @Override
    public void updateProgress(int percentage) {
    	int donesofar = getProgress();
    	if (donesofar < percentage && percentage <= 100) {
    		super.updateProgress(percentage);
            draw() ;
        }
    }
    /**
     * Draws the current state of maze generation on the screen
     */
    private void draw() {
    	// draw the content on the panel
    	view.redrawGenerating(panel, getProgress());
        // update the screen with the buffer graphics
        panel.update() ;
    }

}



