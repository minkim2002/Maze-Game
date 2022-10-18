package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.DefaultOrder;
import generation.Maze;
import generation.MazeFactory;
import gui.Robot.Direction;

/**
 * A set of test cases of Wizard Algorithm
 * 
 * @author Min Kim
 *
 */

class WizardTest {
	private ReliableRobot robot;
	private Maze maze;
	private Control control;
	private StatePlaying state;
	private ReliableSensor reliableSensorForward;
	private ReliableSensor reliableSensorBackward;
	private ReliableSensor reliableSensorLeft;
	private ReliableSensor reliableSensorRight;
	private Wizard wizard;
	
	/**
	 * Set up the maze and create a new reliable sensor to simulate a robot
	 * inside the maze for each test 
	 */

	@BeforeEach
	void setUp() throws Exception {
		robot = new ReliableRobot();
		Wizard wizard = new Wizard();

		control = new Control();
		control.deterministic = true;
		control.setRobotAndDriver(robot, wizard);
		
		state = new StatePlaying();
		
		MazeFactory mazeFactory = new MazeFactory();
		DefaultOrder mazeOrder = new DefaultOrder();
		
		mazeFactory.order(mazeOrder);
		mazeFactory.waitTillDelivered();
		
		maze=mazeOrder.getMaze();
		state.setMaze(maze);
		
		control.setState(state);
		
		
		reliableSensorForward = new ReliableSensor();
    	reliableSensorForward.setSensorDirection(Direction.FORWARD);
    	robot.addDistanceSensor(reliableSensorForward, Direction.FORWARD);
    	reliableSensorForward.setMaze(maze);
    	
    	reliableSensorLeft = new ReliableSensor();
    	reliableSensorLeft.setSensorDirection(Direction.LEFT);
    	robot.addDistanceSensor(reliableSensorLeft, Direction.LEFT);
    	reliableSensorLeft.setMaze(maze);
    	
    	reliableSensorRight = new ReliableSensor();
    	reliableSensorRight.setSensorDirection(Direction.RIGHT);
    	robot.addDistanceSensor(reliableSensorRight, Direction.RIGHT);
    	reliableSensorRight.setMaze(maze);
    	
    	reliableSensorBackward = new ReliableSensor();
    	reliableSensorBackward.setSensorDirection(Direction.BACKWARD);
    	robot.addDistanceSensor(reliableSensorBackward, Direction.BACKWARD);
    	reliableSensorBackward.setMaze(maze);	
    	
    	robot.setController(control);
    	wizard = new Wizard();
    	wizard.setMaze(maze);
    	wizard.setRobot(robot);
	}
	
	/**
	 * Test if the drive2Exit method is functioning well.
	 */
	@Test
	final void testDrive2Exit() {
		try {
			assertEquals(wizard.drive2Exit(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test if the drive1Step2Exit method is functioning well.
	 */
	@Test
	final void testDrive1Step2Exit(){
		try {
			assertEquals(wizard.drive1Step2Exit(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test if the getEnergyConsumption method is getting a correct energy consumption.
	 */
	@Test
	final void testGetEnergyConsumption() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test if the getEnergyConsumption method is getting a correct path length.
	 */
	@Test
	final void testGetPathLength() {
		// TODO Auto-generated method stub
	}

}
