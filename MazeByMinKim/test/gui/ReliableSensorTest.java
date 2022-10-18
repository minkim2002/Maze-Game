package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.NullEnum;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Maze;
import generation.MazeBuilder;
import generation.MazeBuilderBoruvka;
import generation.MazeFactory;
import gui.Robot.Direction;

/**
 * A set of test cases of Reliable Sensor
 * 
 * @author Min Kim
 *
 */

public class ReliableSensorTest extends ReliableSensor {
	private ReliableRobot robot;
	private Maze maze;
	private Control control;
	private StatePlaying state;
	private ReliableSensor reliableSensorForward;
	private ReliableSensor reliableSensorBackward;
	private ReliableSensor reliableSensorLeft;
	private ReliableSensor reliableSensorRight;
	
	/**
	 * Set up the maze and create a new reliable sensor to simulate a robot
	 * inside the maze for each test 
	 */
	@BeforeEach
	public final void setUp() {
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
	}
	
	/**
	 * Test if the method correctly returns the distance from the obstacle to the direction 
	 * the robot is looking at.
	 */
	@Test
	final void TestDistanceToObstacleNoPower() {
		boolean isOut = false;
		float[] power = {-1};
		try {
			robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power);
		} catch (Exception e) {
			isOut = true;
		}
		assertEquals(true, isOut);
	}
	
	/**
	 * Test if the method correctly returns the distance from the obstacle to the direction 
	 * the robot is looking at.
	 */
	@Test
	final void TestDistanceToObstaclePowerisNull() {
		boolean isOut = false;
		float[] power = null;
		try {
			robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power);
		} catch (Exception e) {
			isOut = true;
		}
		assertEquals(true, isOut);
	}
	
	@Test
	final void TestDistanceToObstacleNoOperational() {
		boolean isOper = true;
		float[] power = {0};
		reliableSensorForward.isOperational = false;
		try {
			robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power);
		} catch (Exception e) {
			isOper = false;
		}
		assertEquals(false, isOper);
	}
	
	@Test
	final void TestDistanceToObstacleNotEnoughPower() {
		boolean isLow = false;
		float[] power = {0};
		reliableSensorForward.isOperational = false;
		try {
			robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power);
		} catch (Exception e) {
			isLow = true;
		}
		assertEquals(true, isLow);
	}
	
	@Test
	final void TestDistanceToObstacle() {
		float[] power = {100};
		try {
			assertEquals(true, robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power) < Integer.MAX_VALUE);
		} catch (Exception e) {
			
		}
	}
	
	@Test
	final void TestNorth() {
		float[] power = {100};
		try {
			assertEquals(robot.reliableSensorLeft.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power)<Integer.MAX_VALUE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	final void TestWest() {
		float[] power = {100};
		try {
			assertEquals(robot.reliableSensorBackward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power)<Integer.MAX_VALUE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	final void TestSouth() {
		float[] power = {100};
		state.cd = CardinalDirection.South;
		try {
			assertEquals(robot.reliableSensorRight.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power)<Integer.MAX_VALUE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	final void TestSenseNorth() {
		float[] power = {100};
		state.cd = CardinalDirection.North;
		try {
			assertEquals(robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power)<Integer.MAX_VALUE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	final void TestSenseWest() {
		float[] power = {100};
		state.cd = CardinalDirection.West;
		try {
			assertEquals(robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power)<Integer.MAX_VALUE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	final void TestSenseSouth() {
		float[] power = {100};
		state.cd = CardinalDirection.South;
		try {
			assertEquals(robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(), power)<Integer.MAX_VALUE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	final void TestSetNullMaze() {
		Maze maze = null;
		boolean isNull = false;
		try {
			robot.reliableSensorForward.setMaze(maze);
		} catch (Exception e) {
			isNull = true;
		}
		assertEquals(isNull, true);

	}

	/**
	 * Test if the direction of each sensor depends on the cardinal
	 * direction the robot is looking at is set. 
	 */
	@Test
	final void TestSetNullSensorDirection() {
		Direction direction = null;
		boolean isNull = false;
		try {
			robot.reliableSensorForward.setSensorDirection(direction);
		} catch (Exception e) {
			isNull = true;
		}
		assertEquals(isNull, true);

	}

}