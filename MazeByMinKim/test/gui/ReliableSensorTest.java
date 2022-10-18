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

	@BeforeEach
	public final void setUp() {
		robot = new ReliableRobot();
		Wizard wizard = new Wizard();
		
		//Get a reference of Control
		control = new Control();
		control.deterministic = true;
		control.setRobotAndDriver(robot, wizard);

		state = new StatePlaying();

		MazeFactory mazeFactory = new MazeFactory();
		DefaultOrder mazeOrder = new DefaultOrder();
		
		//Order Maze
		mazeFactory.order(mazeOrder);
		mazeFactory.waitTillDelivered();
		
		maze = mazeOrder.getMaze();
		state.setMaze(maze);

		control.setState(state);

		// Set up forward Sensor
		reliableSensorForward = new ReliableSensor();
		reliableSensorForward.setSensorDirection(Direction.FORWARD);
		robot.addDistanceSensor(reliableSensorForward, Direction.FORWARD);
		reliableSensorForward.setMaze(maze);

		// Set up Left Sensor
		reliableSensorLeft = new ReliableSensor();
		reliableSensorLeft.setSensorDirection(Direction.LEFT);
		robot.addDistanceSensor(reliableSensorLeft, Direction.LEFT);
		reliableSensorLeft.setMaze(maze);

		// Set up Right Sensor
		reliableSensorRight = new ReliableSensor();
		reliableSensorRight.setSensorDirection(Direction.RIGHT);
		robot.addDistanceSensor(reliableSensorRight, Direction.RIGHT);
		reliableSensorRight.setMaze(maze);

		// Set up Backward Sensor
		reliableSensorBackward = new ReliableSensor();
		reliableSensorBackward.setSensorDirection(Direction.BACKWARD);
		robot.addDistanceSensor(reliableSensorBackward, Direction.BACKWARD);
		reliableSensorBackward.setMaze(maze);

		robot.setController(control);
	}

	/**
	 * Test if the distanceToObstacle method doesn't work when it has no power
	 */
	@Test
	final void TestDistanceToObstacleNoPower() {
		boolean isOut = false;
		float[] power = { -1 };
		try {
			robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(),
					power);
		} catch (Exception e) {
			isOut = true;
		}
		assertEquals(true, isOut);
	}

	/**
	 * Test if the distanceToObstacle method doen't work when there is no reference for power
	 */
	@Test
	final void TestDistanceToObstaclePowerisNull() {
		boolean isOut = false;
		float[] power = null;
		try {
			robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(),
					power);
		} catch (Exception e) {
			isOut = true;
		}
		assertEquals(true, isOut);
	}

	/**
	 * Test if the distanceToObstacle method doen't work when the sensor is not operational
	 */
	@Test
	final void TestDistanceToObstacleNoOperational() {
		boolean isOper = true;
		float[] power = { 0 };
		reliableSensorForward.isOperational = false;
		try {
			robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(),
					power);
		} catch (Exception e) {
			isOper = false;
		}
		assertEquals(false, isOper);
	}

	/**
	 * Test if the distanceToObstacle method doen't work when it doesn't have enough power for sensing
	 */
	@Test
	final void TestDistanceToObstacleNotEnoughPower() {
		boolean isLow = false;
		float[] power = { 0 };
		reliableSensorForward.isOperational = false;
		try {
			robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(),
					power);
		} catch (Exception e) {
			isLow = true;
		}
		assertEquals(true, isLow);
	}

	/**
	 * Test if the distanceToObstacle method correctly returns the distance, that is not infinity, when
	 * it has all the information it needs
	 */
	@Test
	final void TestDistanceToObstacle() {
		float[] power = { 100 };
		try {
			assertEquals(true, robot.reliableSensorForward.distanceToObstacle(control.getCurrentPosition(),
					control.getCurrentDirection(), power) < Integer.MAX_VALUE);
		} catch (Exception e) {

		}
	}

	/**
	 * Test if the distanceToObstacle method correctly returns the distance, with the left sensor of
	 * the robot
	 */
	@Test
	final void TestNorth() {
		float[] power = { 100 };
		try {
			assertEquals(robot.reliableSensorLeft.distanceToObstacle(control.getCurrentPosition(),
					control.getCurrentDirection(), power) < Integer.MAX_VALUE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test if the method distanceToObstacle correctly returns the distance, with the back sensor of
	 * the robot
	 */
	@Test
	final void TestWest() {
		float[] power = { 100 };
		try {
			assertEquals(robot.reliableSensorBackward.distanceToObstacle(control.getCurrentPosition(),
					control.getCurrentDirection(), power) < Integer.MAX_VALUE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test if the method distanceToObstacle correctly returns the distance, with the right sensor of
	 * the robot
	 */
	@Test
	final void TestSouth() {
		float[] power = { 100 };
		state.cd = CardinalDirection.South;
		try {
			assertEquals(robot.reliableSensorRight.distanceToObstacle(control.getCurrentPosition(),
					control.getCurrentDirection(), power) < Integer.MAX_VALUE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test if the sensor is not taking null reference of the maze.
	 */
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
	 * Test if the sensor is not working without specifying the actual direction it
	 * will be mounted on
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