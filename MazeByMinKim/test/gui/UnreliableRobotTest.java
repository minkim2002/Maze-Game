package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import generation.DefaultOrder;
import generation.MazeFactory;
import gui.Robot.Direction;

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
	
	@AfterEach
	public void toReliable(){
		robot = new UnreliableRobot(1,1,1,1);
	}
	
	@Test
	final void TeststartFailureAndRepairProcess() {
		boolean isWorking = true;
		try {
			robot.startFailureAndRepairProcess(Direction.FORWARD, 4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		try {
			robot.startFailureAndRepairProcess(Direction.LEFT, 4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		try {
			robot.startFailureAndRepairProcess(Direction.RIGHT, 4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		try {
			robot.startFailureAndRepairProcess(Direction.BACKWARD, 4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
	}
	
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
	
	@Test
	final void TeststopFailureAndRepairProcess() {
		boolean isWorking = true;
		try {
			robot.startFailureAndRepairProcess(Direction.FORWARD, 4000, 2000);
			robot.stopFailureAndRepairProcess(Direction.FORWARD);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		try {
			robot.startFailureAndRepairProcess(Direction.LEFT, 4000, 2000);
			robot.stopFailureAndRepairProcess(Direction.LEFT);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		try {
			robot.startFailureAndRepairProcess(Direction.RIGHT, 4000, 2000);
			robot.stopFailureAndRepairProcess(Direction.RIGHT);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
		try {
			robot.startFailureAndRepairProcess(Direction.BACKWARD, 4000, 2000);
			robot.stopFailureAndRepairProcess(Direction.BACKWARD);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
	}
	
	@Test
	final void TeststopFailureAndRepairProcessNotWorking() {
		boolean isWorking = true;
		try {
			robot.stopFailureAndRepairProcess(Direction.FORWARD);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
		try {
			robot.stopFailureAndRepairProcess(Direction.LEFT);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
		try {
			robot.stopFailureAndRepairProcess(Direction.RIGHT);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
		try {
			robot.stopFailureAndRepairProcess(Direction.BACKWARD);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
	}

}
