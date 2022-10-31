package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Maze;
import generation.MazeFactory;
import gui.Robot.Direction;

class UnreliableSensorTest extends ReliableSensorTest{
	
	public UnreliableSensor unreliableSensorForward;
	public UnreliableSensor unreliableSensorLeft;
	public UnreliableSensor unreliableSensorRight;
	public UnreliableSensor unreliableSensorBackward;
	
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
	}
	
	/**
	 * Test whether the startFailureAndRepairProcess method is working accurately.
	 * startFailureAndRepairProcess method with correct inputs should start repair cycle
	 * without throwing an exception
	 */
	@Test
	final void TeststartFailureAndRepairProcess() {
		boolean isWorking = true;
		try {
			sensorForward.startFailureAndRepairProcess(4000, 2000);
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(true, isWorking);
	}
	
	/**
	 * Test whether the stopFailureAndRepairProcess method is working accurately.
	 * stopFailureAndRepairProcess method should stop the repair cycle by interrupting 
	 * the thread and terminating the cycle process without throwing an exception.
	 */
	@Test
	public final void TestStopFailureAndRepairProcess() {	
		boolean isWorking = true;
		try {
			sensorForward.startFailureAndRepairProcess(4000, 2000);
			sensorForward.stopFailureAndRepairProcess();
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
		try {
			sensorForward.stopFailureAndRepairProcess();
		} catch (Exception e) {
			isWorking = false;
		}
		assertEquals(false, isWorking);
	}

}
