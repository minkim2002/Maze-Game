package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import generation.DefaultOrder;
import generation.MazeFactory;
import gui.Robot.Direction;

/**
 * A set of test cases of Unreliable Robot.
 * 
 * @author Min Kim
 *
 */

class UnreliableRobotTest extends ReliableRobotTest{

	
	@BeforeEach
	public void setUp(){
		robot = new UnreliableRobot(0,0,0,0);
		WallFollower wallFollower = new WallFollower();
		
		//Get a reference of Control
		control = new Control();
		control.deterministic = true;
		control.setRobotAndDriver(robot, wallFollower);

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
		UnreliableSensor unreliableSensorForward = new UnreliableSensor();
		unreliableSensorForward.setSensorDirection(Direction.FORWARD);
		robot.addDistanceSensor(unreliableSensorForward, Direction.FORWARD);
		unreliableSensorForward.setMaze(maze);

		// Set up Left Sensor
		UnreliableSensor unreliableSensorLeft = new UnreliableSensor();
		unreliableSensorLeft.setSensorDirection(Direction.LEFT);
		robot.addDistanceSensor(unreliableSensorLeft, Direction.LEFT);
		unreliableSensorLeft.setMaze(maze);

		// Set up Right Sensor
		UnreliableSensor unreliableSensorRight = new UnreliableSensor();
		unreliableSensorRight.setSensorDirection(Direction.RIGHT);
		robot.addDistanceSensor(unreliableSensorRight, Direction.RIGHT);
		unreliableSensorRight.setMaze(maze);
		
		// Set up Backward Sensor
		UnreliableSensor unreliableSensorBackward = new UnreliableSensor();
		unreliableSensorBackward.setSensorDirection(Direction.BACKWARD);
		robot.addDistanceSensor(unreliableSensorBackward, Direction.BACKWARD);
		unreliableSensorBackward.setMaze(maze);

		robot.setController(control);
	}
	
	/**
	 * After each test, by making the robot into a reliable robot,
	 * we can test that we can also turn an unreliable robot into a reliable robot
	 * with simply changing the parameter.
	 */
	@AfterEach
	public void toReliable(){
		robot = new UnreliableRobot(1,1,1,1);
	}
	
	/**
	 * Test whether the startFailureAndRepairProcess method is working accurately.
	 * startFailureAndRepairProcess method with correct inputs should start repair cycle
	 * without throwing an exception.
	 */
	@Test
	final void TeststartFailureAndRepairProcess() {
		boolean isWorking = true;
		
		//Test for forward Sensor
		try {
			robot.startFailureAndRepairProcess(Direction.FORWARD, 4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		
		//Test for Left Sensor
		try {
			robot.startFailureAndRepairProcess(Direction.LEFT, 4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		
		//Test for Right Sensor
		try {
			robot.startFailureAndRepairProcess(Direction.RIGHT, 4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		
		//Test for Backward Sensor
		try {
			robot.startFailureAndRepairProcess(Direction.BACKWARD, 4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
	}
	
	
	/**
	 * Test whether the startFailureAndRepairProcess method is working accurately.
	 * startFailureAndRepairProcess method should throw an exception when there is 
	 * no direction specified.
	 */
	@Test
	final void TeststartFailureAndRepairProcessNotWorking() {
		boolean isWorking = true;
		try {
			robot.startFailureAndRepairProcess(null, 4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
	}
	
	
	/**
	 * Test whether the stopFailureAndRepairProcess method is working accurately.
	 * stopFailureAndRepairProcess method should stop the repair cycle by interrupting 
	 * the thread and terminating the cycle process without throwing an exception.
	 */
	@Test
	final void TeststopFailureAndRepairProcess() {
		boolean isWorking = true;
		
		//Test for Forward Sensor
		try {
			robot.startFailureAndRepairProcess(Direction.FORWARD, 4000, 2000);
			robot.stopFailureAndRepairProcess(Direction.FORWARD);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		
		//Test for Left Sensor
		try {
			robot.startFailureAndRepairProcess(Direction.LEFT, 4000, 2000);
			robot.stopFailureAndRepairProcess(Direction.LEFT);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		
		//Test for Right Sensor
		try {
			robot.startFailureAndRepairProcess(Direction.RIGHT, 4000, 2000);
			robot.stopFailureAndRepairProcess(Direction.RIGHT);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		
		//Test for Backward Sensor
		try {
			robot.startFailureAndRepairProcess(Direction.BACKWARD, 4000, 2000);
			robot.stopFailureAndRepairProcess(Direction.BACKWARD);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
	}
	
	/**
	 * Test whether the stopFailureAndRepairProcess method is working accurately.
	 * stopFailureAndRepairProcess method should throw an exception when there is no
	 * current cycle that it can terminate.
	 */
	@Test
	final void TeststopFailureAndRepairProcessNotWorking() {
		boolean isWorking = true;
		
		//Test for Forward Sensor
		try {
			robot.stopFailureAndRepairProcess(Direction.FORWARD);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
		
		//Test for Left Sensor
		try {
			robot.stopFailureAndRepairProcess(Direction.LEFT);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
		
		//Test for Right Sensor
		try {
			robot.stopFailureAndRepairProcess(Direction.RIGHT);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
		
		//Test for Backward Sensor
		try {
			robot.stopFailureAndRepairProcess(Direction.BACKWARD);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
	}

}
