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
	public ReliableRobot robot;
	public Maze maze;
	public Control control;
	public StatePlaying state;
	public ReliableSensor sensorForward;
	public ReliableSensor sensorBackward;
	public ReliableSensor sensorLeft;
	public ReliableSensor sensorRight;

	@BeforeEach
	public void setUp() {
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
		sensorForward = new ReliableSensor();
		sensorForward.setSensorDirection(Direction.FORWARD);
		robot.addDistanceSensor(sensorForward, Direction.FORWARD);
		sensorForward.setMaze(maze);

		// Set up Left Sensor
		sensorLeft = new ReliableSensor();
		sensorLeft.setSensorDirection(Direction.LEFT);
		robot.addDistanceSensor(sensorLeft, Direction.LEFT);
		sensorLeft.setMaze(maze);

		// Set up Right Sensor
		sensorRight = new ReliableSensor();
		sensorRight.setSensorDirection(Direction.RIGHT);
		robot.addDistanceSensor(sensorRight, Direction.RIGHT);
		sensorRight.setMaze(maze);

		// Set up Backward Sensor
		sensorBackward = new ReliableSensor();
		sensorBackward.setSensorDirection(Direction.BACKWARD);
		robot.addDistanceSensor(sensorBackward, Direction.BACKWARD);
		sensorBackward.setMaze(maze);

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
			robot.sensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(),
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
			robot.sensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(),
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
	void TestDistanceToObstacleNoOperational() {
		boolean isOper = true;
		float[] power = { 0 };
		sensorForward.isOperational = false;
		try {
			robot.sensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(),
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
	void TestDistanceToObstacleNotEnoughPower() {
		boolean isLow = false;
		float[] power = { 0 };
		sensorForward.isOperational = false;
		try {
			robot.sensorForward.distanceToObstacle(control.getCurrentPosition(), control.getCurrentDirection(),
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
			assertEquals(true, robot.sensorForward.distanceToObstacle(control.getCurrentPosition(),
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
			assertEquals(robot.sensorLeft.distanceToObstacle(control.getCurrentPosition(),
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
			assertEquals(robot.sensorBackward.distanceToObstacle(control.getCurrentPosition(),
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
	void TestSouth() {
		float[] power = { 100 };
		state.cd = CardinalDirection.South;
		try {
			assertEquals(robot.sensorRight.distanceToObstacle(control.getCurrentPosition(),
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
			robot.sensorForward.setMaze(maze);
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
			robot.sensorForward.setSensorDirection(direction);
		} catch (Exception e) {
			isNull = true;
		}
		assertEquals(isNull, true);

	}

}