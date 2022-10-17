package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		
		
		ReliableSensor reliableSensorForward = new ReliableSensor();
    	reliableSensorForward.setSensorDirection(Direction.FORWARD);
    	robot.addDistanceSensor(reliableSensorForward, Direction.FORWARD);
    	reliableSensorForward.setMaze(maze);
    	
    	ReliableSensor reliableSensorLeft = new ReliableSensor();
    	reliableSensorLeft.setSensorDirection(Direction.LEFT);
    	robot.addDistanceSensor(reliableSensorLeft, Direction.LEFT);
    	reliableSensorLeft.setMaze(maze);
    	
    	ReliableSensor reliableSensorRight = new ReliableSensor();
    	reliableSensorRight.setSensorDirection(Direction.RIGHT);
    	robot.addDistanceSensor(reliableSensorRight, Direction.RIGHT);
    	reliableSensorRight.setMaze(maze);
    	
    	ReliableSensor reliableSensorBackward = new ReliableSensor();
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
	final void TestDistanceToObstacle() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test if a reference of the maze created is set
	 */
	@Test
	final void TestSetMaze() {
		// TODO Auto-generated method stub
	}

	/**
	 * Test if the direction of each sensor depends on the cardinal
	 * direction the robot is looking at is set. 
	 */
	@Test
	final void TestSetSensorDirection() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Test if the method is getting the correct numeric 
	 * value of the amount of energy consumption for
	 * sensing
	 */
	@Test
	final void TestGetEnergyConsumptionForSensing() {
		// TODO Auto-generated method stub
	}

}