package gui;

import static org.junit.jupiter.api.Assertions.*;

import javax.imageio.plugins.tiff.ExifGPSTagSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Maze;
import generation.MazeFactory;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * A set of test cases of Wizard Algorithm
 * 
 * @author Min Kim
 *
 */

class WizardTest {
	public ReliableRobot robot;
	public Maze maze;
	public Control control;
	public StatePlaying state;
	public ReliableSensor sensorForward;
	public ReliableSensor sensorBackward;
	public ReliableSensor sensorLeft;
	public ReliableSensor sensorRight;
	public Wizard driver;
	
	/**
	 * Set up the maze and create a new reliable sensor to simulate a robot
	 * inside the maze for each test 
	 */

	@BeforeEach
	void setUp() throws Exception {
		robot = new ReliableRobot();
		driver = new Wizard();
		
		//Get a reference of Control
		control = new Control();
		control.deterministic = true;
		control.setRobotAndDriver(robot, driver);
		
		state = new StatePlaying();
		
		MazeFactory mazeFactory = new MazeFactory();
		DefaultOrder mazeOrder = new DefaultOrder();
		
		//Order Maze
		mazeFactory.order(mazeOrder);
		mazeFactory.waitTillDelivered();
		
		maze=mazeOrder.getMaze();
		state.setMaze(maze);
		
		control.setState(state);
		
		//Set up Forward Sensor
		sensorForward = new ReliableSensor();
		sensorForward.setSensorDirection(Direction.FORWARD);
    	robot.addDistanceSensor(sensorForward, Direction.FORWARD);
    	sensorForward.setMaze(maze);
    	
    	//Set up Left Sensor
    	sensorLeft = new ReliableSensor();
    	sensorLeft.setSensorDirection(Direction.LEFT);
    	robot.addDistanceSensor(sensorLeft, Direction.LEFT);
    	sensorLeft.setMaze(maze);
    	
    	//Set up Right Sensor
    	sensorRight = new ReliableSensor();
    	sensorRight.setSensorDirection(Direction.RIGHT);
    	robot.addDistanceSensor(sensorRight, Direction.RIGHT);
    	sensorRight.setMaze(maze);
    	
    	//Set up Backward Sensor
    	sensorBackward = new ReliableSensor();
    	sensorBackward.setSensorDirection(Direction.BACKWARD);
    	robot.addDistanceSensor(sensorBackward, Direction.BACKWARD);
    	sensorBackward.setMaze(maze);	
    	
    	robot.setController(control);
    	driver.setMaze(maze);
    	driver.setRobot(robot);
	}
	
	/**
	 * Test if the entire wizard algorithm is working correctly. Find an exit position by using
	 * the information from the reference maze. Then, identify which direction is the exit.
	 * 
	 * By using the drive2Exit method, the robot will arrive at the exit position, 
	 * and compare the robot's position and the actual exit position. 
	 * If it matches, algorithm works.
	 * 
	 * Compare the robot's direction and the actual direction of the exit. 
	 * It it matches, algorithm works.
	 */
	@Test
	final void checkDrive() {
		int[] exitPosition = driver.referenceMaze.getExitPosition();
		CardinalDirection cd;
		
		if(exitPosition[1] == 0 && !driver.referenceMaze.hasWall(exitPosition[0], exitPosition[1], CardinalDirection.North)){
			cd = CardinalDirection.North;
		} else if(exitPosition[0] == 0 && !driver.referenceMaze.hasWall(exitPosition[0], exitPosition[1], CardinalDirection.West)) {
			cd = CardinalDirection.West;
		} else if(exitPosition[1] == maze.getHeight()-1 && !driver.referenceMaze.hasWall(exitPosition[0], exitPosition[1], CardinalDirection.South)) {
			cd = CardinalDirection.South;
		} else {
			cd = CardinalDirection.East;
		}
		try {
			boolean isWorking = driver.drive2Exit();
			assertTrue(isWorking);
			int[] currentPosition = driver.robot.getCurrentPosition();
			assertEquals(currentPosition, exitPosition);
			assertEquals(driver.robot.getCurrentDirection(), cd);
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Test if the drive2Exit method is functioning well, which means
	 * it successfully leads the robot out of the maze.
	 */
	@Test
	final void testDrive2Exit() {
		try {
			assertEquals(driver.drive2Exit(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test if the drive1Step2Exit method is functioning well, which means
	 * it successfully leads the robot one step towards the exit.
	 */
	@Test
	final void testDrive1Step2Exit(){
		try {
			assertEquals(driver.drive1Step2Exit(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test whether the isNeighborOutsideMaze method 
	 * determines if the neighboring cell is outside of the maze
	 *
	 */
	@Test
	public final void testNeighborOutsideMaze() {
		// at the starting position, only the western neighbor should be outside of the maze
		int[] currentPosition = {0, 1};
		assertFalse(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.North));
		assertFalse(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.East));
		assertFalse(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.South));
		assertTrue(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.West));
		
		// at a position in the middle of the maze, all neighbors should be inside of the maze
		currentPosition[0] = 1; currentPosition[1] = 2;
		assertFalse(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.North));
		assertFalse(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.East));
		assertFalse(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.South));
		assertFalse(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.West));
		
		// at the corner position, there should be exactly two neighbors outside of the maze
		currentPosition[0] = 3; currentPosition[1] = 0;
		assertTrue(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.North));
		assertTrue(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.East));
		assertFalse(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.South));
		assertFalse(driver.isNeighborOutsideMaze(currentPosition, CardinalDirection.West));
	}
	
	
	/**
	 * Test if the getEnergyConsumption method is getting a correct energy consumption.
	 */
	@Test
	final void testGetEnergyConsumption() {
		control.robot.move(1);
		assertEquals(6, driver.getEnergyConsumption());
	}
	
	/**
	 * Test if the getEnergyConsumption method is getting a correct path length.
	 */
	@Test
	final void testGetPathLength() {
		assertEquals(control.robot.getOdometerReading(), driver.getPathLength());
	}

}
