package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Maze;
import generation.MazeFactory;
import gui.Robot.Direction;
import gui.Robot.Turn;


/**
 * A set of test cases of Smart Wizard Algorithm
 * 
 * @author Min Kim
 *
 */

class SmartWizardTest {

	public ReliableRobot robot;
	public Maze maze;
	public Control control;
	public StatePlaying state;
	public ReliableSensor sensorForward;
	public ReliableSensor sensorBackward;
	public ReliableSensor sensorLeft;
	public ReliableSensor sensorRight;
	public SmartWizard driver;
	
	/**
	 * Set up the maze and create a new reliable sensor to simulate a robot
	 * inside the maze for each test 
	 */

	@BeforeEach
	void setUp() throws Exception {
		robot = new ReliableRobot();
		driver = new SmartWizard();
		
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
	 * Test if the helper method, JumpInsteadMove, 
	 * works correctly regardless of the robot's direction and position.
	 * The method should analyze and determine whether jumping
	 * instead of walking is a better option and carry out the appropriate
	 * action.
	 * 
	 * Since there are 16 possible cases (4 cardinal directions times 4 sensor directions)
	 * A lot of individual test cases are included to incorporate as most cases as possible.
	 */
	@Test
	final void testJump(){
		//When the robot is at a starting position
		//Cardinal Direction: North, West, East, South, respectively
		try {
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.North));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.West));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.East));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.South));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		//When the robot moved 1 step
		//Cardinal Direction: North, West, East, South, respectively
		try {
			robot.move(1);
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.North));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			robot.move(1);
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.West));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			robot.move(1);
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.East));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			robot.move(1);
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.South));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		//When the robot moved 11 steps
		//Cardinal Direction: North, West, East, South, respectively
		try {
			int i = 1;
			while(i<=11) {
				driver.drive1Step2Exit();
				i++;
			}
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.North));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			int i = 1;
			while(i<=11) {
				driver.drive1Step2Exit();
				i++;
			}
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.West));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			int i = 1;
			while(i<=11) {
				driver.drive1Step2Exit();
				i++;
			}
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.East));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			int i = 1;
			while(i<=11) {
				driver.drive1Step2Exit();
				i++;
			}
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.South));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		//When the robot moved 22 steps
		//Cardinal Direction: North, West, East, South, respectively
		try {
			int i = 1;
			while(i<=22) {
				driver.drive1Step2Exit();
				i++;
			}
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.North));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			int i = 1;
			while(i<=22) {
				driver.drive1Step2Exit();
				i++;
			}
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.West));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			int i = 1;
			while(i<=22) {
				driver.drive1Step2Exit();
				i++;
			}
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.East));
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		try {
			int i = 1;
			while(i<=22) {
				driver.drive1Step2Exit();
				i++;
			}
			assertEquals(false, driver.jumpInsteadMove(CardinalDirection.South));
		} catch (Exception e) {
			System.out.println("Something went wrong");
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
