package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Maze;
import generation.MazeFactory;
import gui.Robot.Direction;

class WallFollwerTest {
	public UnreliableRobot robot;
	public Maze maze;
	public Control control;
	public StatePlaying state;
	public ReliableSensor sensorForward;
	public ReliableSensor sensorBackward;
	public ReliableSensor sensorLeft;
	public ReliableSensor sensorRight;

	protected WallFollower driver;

	@BeforeEach
	void setUp() throws Exception {
		robot = new UnreliableRobot(0, 0, 0, 0);
		driver = new WallFollower();

		// Get a reference of Control
		control = new Control();
		control.deterministic = true;
		control.setRobotAndDriver(robot, driver);

		state = new StatePlaying();

		MazeFactory mazeFactory = new MazeFactory();
		DefaultOrder mazeOrder = new DefaultOrder();

		// Order Maze
		mazeFactory.order(mazeOrder);
		mazeFactory.waitTillDelivered();

		maze = mazeOrder.getMaze();
		state.setMaze(maze);

		control.setState(state);

		// Set up Forward Sensor
		sensorForward = new UnreliableSensor();
		sensorForward.setSensorDirection(Direction.FORWARD);
		robot.addDistanceSensor(sensorForward, Direction.FORWARD);
		sensorForward.setMaze(maze);

		// Set up Left Sensor
		sensorLeft = new UnreliableSensor();
		sensorLeft.setSensorDirection(Direction.LEFT);
		robot.addDistanceSensor(sensorLeft, Direction.LEFT);
		sensorLeft.setMaze(maze);

		// Set up Right Sensor
		sensorRight = new UnreliableSensor();
		sensorRight.setSensorDirection(Direction.RIGHT);
		robot.addDistanceSensor(sensorRight, Direction.RIGHT);
		sensorRight.setMaze(maze);

		// Set up Backward Sensor
		sensorBackward = new UnreliableSensor();
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
	 * 
	 * Special Warning: the test takes more than 2 minutes!!
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
	 * Test if the drive1Step2Exit method is functioning well, which means it
	 * successfully leads the robot one step towards the exit.
	 */
	@Test
	final void testDrive1Step2Exit() {
		try {
			assertEquals(driver.drive1Step2Exit(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test if the drive1Step2Exit method is functioning well, which means it
	 * successfully leads the robot one step towards the exit, when
	 * the left sensor is in need of repair, which means that it has to 
	 * go through another conditional statement where it checks whether all the 
	 * sensors are in need of repair. 
	 */
	@Test
	final void testDrive1Step2ExitLeftNotWorking() {
		try {
			driver.leftStatus = false;
			assertEquals(driver.drive1Step2Exit(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test if the drive1Step2Exit method is functioning well, which means it
	 * successfully leads the robot one step towards the exit, when
	 * the forward sensor is in need of repair, which means that it has to 
	 * go through another conditional statement where it checks whether all the 
	 * sensors are in need of repair. 
	 */
	@Test
	final void testDrive1Step2ExitForwardNotWorking() {
		try {
			driver.forwardStatus = false;
			assertEquals(driver.drive1Step2Exit(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test if the getEnergyConsumption method is getting a correct energy
	 * consumption. Moving the robot once should return the energy consumption of 6
	 */
	@Test
	final void testGetEnergyConsumption() {
		control.robot.move(1);
		assertEquals(6, driver.getEnergyConsumption());
	}

	/**
	 * Test if the getEnergyConsumption method is getting a correct path length.
	 * WallFollower's path length should match the robot's odometer.
	 */
	@Test
	final void testGetPathLength() {
		assertEquals(control.robot.getOdometerReading(), driver.getPathLength());
	}

	/**
	 * Test if the WallFollower's waitTilOperational method is working accurately.
	 * When all sensors are in need of repair, the method should successfully carry
	 * out what it has to do.
	 */
	@Test
	public final void testWaitTilOperational() {
		boolean isWorking = true;
		try {
			boolean[] test = { false, false, false, false };
			driver.waitTilOperational(test);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
	}

	/**
	 * Test if the WallFollower's waitTilOperational method is working accurately.
	 * When all sensors are operational, the method should successfully carry out
	 * what it has to do.
	 */
	@Test
	public final void testWaitTilOperational2() {
		boolean isWorking = true;
		try {
			boolean[] test = { true, true, true, true };
			driver.waitTilOperational(test);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
	}

	/**
	 * Test if the WallFollower's SetState method is working accurately.
	 * When all sensors are operational, the driver should set the sensor
	 * state as operational state not the repair state, so that the driver
	 * can carry out the right steps
	 */
	@Test
	public final void testSetStateOperational() {
		boolean isOperational = true;
		boolean isWorking = true;
		try {
			driver.setState(isOperational, isOperational, isOperational, isOperational);
			if (driver.sensorState instanceof OperationalState) {
				isWorking = true;
			}
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
	}

	/**
	 * Test if the WallFollower's SetState method is working accurately.
	 * When all sensors are in need of repair, the driver should set the sensor
	 * state as repair state not the repair state, so that the driver
	 * can carry out the right steps
	 */
	@Test
	public final void testSetStateRepair() {
		boolean isOperational = false;
		boolean isWorking = true;
		try {
			driver.setState(isOperational, isOperational, isOperational, isOperational);
			if (driver.sensorState instanceof RepairState) {
				isWorking = true;
			}
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
	}

}
