package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.DefaultOrder;
import generation.MazeFactory;
import gui.Robot.Direction;

class WallFollwerTest extends WizardTest {

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

	@Test
	public final void IsOperationalTest() {
		boolean isWorking = true;
		try {
			driver.isOperational(false)
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
