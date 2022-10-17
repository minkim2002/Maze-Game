package gui;

import static org.junit.jupiter.api.Assertions.*;

import javax.management.relation.RoleInfoNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Maze;
import generation.MazeBuilder;
import generation.MazeFactory;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * A set of test cases of Reliable Robot
 * 
 * @author Min Kim
 *
 */

class ReliableRobotTest {

	private ReliableRobot robot;
	private Maze maze;
	private Control control;
	private StatePlaying state;
	
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
	 * Test whether a correct reference to the controller to cooperate with is assigned.
	 */
	@Test
	final void TestSetNullController() {
		Control controller = null;
		boolean isNull = false;
		try {
			robot.setController(controller);
		} catch (Exception e) {
			isNull = true;
		}
		assertEquals(isNull, true);
	}
	
	/**
	 * Test whether the setController method provides actual maze.
	 */
	@Test
	final void TestSetMazeNullController() {
		boolean isMazeNull = false;
		try {
			robot.setController(control);
		} catch (Exception e) {
			isMazeNull = true;
		}
		assertEquals(isMazeNull, false);
	}
	
	
	/**
	 * Test whether the getCurrentPosition method is getting a correct position.
	 */
	@Test
	final void TestGetCurrentPosition() {
		try {
			assertEquals(control.getCurrentPosition()[0], robot.getCurrentPosition()[0]);
			assertEquals(control.getCurrentPosition()[1], robot.getCurrentPosition()[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test whether the getCurrentPosition method doesn't allow negative coordinates.
	 */
	@Test
	final void TestGetCurrentPositionOutofBound() {
		boolean isOut = false;
		state.setCurrentPosition(-1, -1);
		try {
			robot.getCurrentPosition();
		} catch (Exception e) {
			isOut=true;
		}
		assertEquals(isOut, true);
	}
	
	
	/**
	 * Test whether the getCurrentDirection method is getting a correct direction.
	 */
	@Test
	final void TestGetCurrentDirection() {
		try {
			assertEquals(control.getCurrentDirection(), robot.getCurrentDirection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test whether the getBatteryLevel method is getting a correct battery level.
	 */
	@Test
	final void TestGetBatteryLevel() {
		robot.setBatteryLevel(100);
		assertEquals(100, robot.getBatteryLevel());
	}
	
	/**
	 * Test whether the setBatteryLevel method doesn't let battery level to be a negative value.
	 */
	@Test
	final void TestSetBatteryLevelNegativeValue() {
		boolean isNegative = false;
		try {
			robot.setBatteryLevel(-3);
		} catch (Exception e) {
			isNegative = true;
		}
		assertEquals(isNegative, true);
	}
	
	/**
	 * Test whether the getEnergyForFullRotation method is getting a correct battery
	 * usage for full rotation and rotation under 360.
	 */
	@Test
	final void TestGetEnergyForFullRotation() {
		assertEquals(12, robot.getEnergyForFullRotation());
	}
	
	/**
	 * Test whether the getEnergyForStepForward method is getting a correct battery
	 * usage for one step forward and multiple steps forward
	 */
	@Test
	final void TestGetEnergyForStepForward() {
		assertEquals(6, robot.getEnergyForStepForward());
	}
	
	/**
	 * Test whether the getOdometerReading method is getting a correct distance traveled.
	 */
	@Test
	final void TestGetOdometerReading() {
		assertEquals(robot.distanceTraveled, robot.getOdometerReading());
	}
	
	/**
	 * Test whether the resetOdometer method is resetting odometer to zero.
	 */
	@Test
	final void TestResetOdometer() {
		robot.resetOdometer();
		assertEquals(0, robot.getOdometerReading());
	}
	
	/**
	 * Test whether the rotate method stops the robot from rotating 90 when there is not enough power
	 */
	@Test
	final void TestRotateNotEnoughPower() {
		robot.setBatteryLevel(2);
		try {
			robot.rotate(Turn.LEFT);
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the rotate method stops the robot from rotating 180 when there is not enough power
	 */
	@Test
	final void TestRotateNotEnoughPowerFor180() {
		robot.setBatteryLevel(5);
		try {
			robot.rotate(Turn.AROUND);
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the rotate method is rotating the robot once to the left in an accurate matter.
	 */
	@Test
	final void TestRotateLeft() {
		robot.setBatteryLevel(4);
		try {
			robot.rotate(Turn.LEFT);
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, false);
		assertEquals(robot.getBatteryLevel(), 1);
	}
	
	/**
	 * Test whether the rotate method is rotating the robot once to the right an accurate matter.
	 */
	@Test
	final void TestRotateRight() {
		robot.setBatteryLevel(4);
		try {
			robot.rotate(Turn.RIGHT);
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, false);
		assertEquals(robot.getBatteryLevel(), 1);
	}
	
	/**
	 * Test whether the rotate method is rotating the robot twice in an accurate matter.
	 */
	@Test
	final void TestRotateAround() {
		robot.setBatteryLevel(7);
		try {
			robot.rotate(Turn.AROUND);
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, false);
		assertEquals(robot.getBatteryLevel(), 1);
	}
	
	/**
	 * Test whether the rotate method is rotating the robot in an accurate matter then stop the robot when power supply is 3.
	 */
	@Test
	final void TestRotateLeftThenStop() {
		robot.setBatteryLevel(3);
		try {
			robot.rotate(Turn.LEFT);
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, true);
		assertEquals(robot.getBatteryLevel(), 0);
	}
	
	/**
	 * Test whether the move method is not making the robot move when there is not enough power.
	 */
	@Test
	final void TestMoveNotEnoughPower() {
		robot.setBatteryLevel(5);
		try {
			robot.move(1);
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the move method is moving the robot in an accurate manner.
	 */
	@Test
	final void TestMove() {
		robot.setBatteryLevel(7);
		try {
			robot.move(1);
		} catch (Exception e) {
		}
		assertEquals(robot.distanceTraveled, 1);
		assertEquals(robot.isStopped, false);
	}
	
	/**
	 * Test whether the move method is moving the robot in an accurate manner then stop the robot when power supply is 6.
	 */
	@Test
	final void TestMoveThenStop() {
		robot.setBatteryLevel(6);
		try {
			robot.move(1);
		} catch (Exception e) {
		}
		assertEquals(robot.distanceTraveled, 1);
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the move method is moving the robot in an accurate manner then stop the robot even when there are steps left when
	 * there is not enough power.
	 */
	@Test
	final void TestMoveLeftButStopped() {
		robot.setBatteryLevel(7);
		try {
			robot.move(2);
		} catch (Exception e) {
		}
		assertEquals(robot.getBatteryLevel(), 0);
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the jump method is not making the robot jump when there is not enough power.
	 */
	@Test
	final void TestJumpNotEnoughPower() {
		robot.setBatteryLevel(20);
		try {
			robot.jump();
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the jump method is making the robot jump in an accurate manner.
	 */
	@Test
	final void TestJump() {
		robot.setBatteryLevel(50);
		try {
			robot.jump();
		} catch (Exception e) {
		}
		assertEquals(robot.distanceTraveled, 1);
		assertEquals(robot.isStopped, false);
	}
	
	/**
	 * Test whether the jump method is stopping the robot from jumping when it is right at the east border
	 * looking at the east direction.
	 */
	@Test
	final void TestJumpEastStop() {
		robot.setBatteryLevel(50);
		state.setCurrentPosition(maze.getWidth()-1, maze.getHeight()-1);
		try {
			robot.jump();
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the jump method is stopping the robot from jumping when it is right at the west border
	 * looking at the west direction.
	 */
	@Test
	final void TestJumpWestStop() {
		robot.setBatteryLevel(50);
		robot.rotate(Turn.AROUND);
		state.cd = CardinalDirection.West;
		state.setCurrentPosition(0, 0);
		
		try {
			robot.jump();
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the jump method is stopping the robot from jumping when it is right at the north border
	 * looking at the north direction.
	 */
	@Test
	final void TestJumpNorthStop() {
		robot.setBatteryLevel(50);
		state.setCurrentPosition(maze.getHeight()-1, 0);
		state.cd = CardinalDirection.North;
		robot.rotate(Turn.LEFT);
		try {
			robot.jump();
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the jump method is stopping the robot from jumping when it is right at the south border
	 * looking at the south direction.
	 */
	@Test
	final void TestJumpSouthStop() {
		robot.setBatteryLevel(50);
		state.setCurrentPosition(maze.getWidth()-1, maze.getHeight()-1);
		state.cd = CardinalDirection.South;
		robot.rotate(Turn.RIGHT);
		try {
			robot.jump();
		} catch (Exception e) {
		}
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test whether the jump method is making the robot jump in an accurate manner then stop the robot when power supply is 40.
	 */
	@Test
	final void TestJumpThenStop() {
		robot.setBatteryLevel(40);
		try {
			robot.jump();
		} catch (Exception e) {
		}
		assertEquals(robot.distanceTraveled, 1);
		assertEquals(robot.isStopped, true);
	}
	
	/**
	 * Test if the method accurately tells 
	 * if the current position is right at the exit but still inside the maze. 
	 */
	@Test
	final void TestIsAtExit() {
		try {
			assertEquals(robot.isAtExit(), 
					control.getMaze().getFloorplan().isExitPosition(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test if the method accurately tells if current position is inside a room. 
	 */	
	@Test
	final void TestIsInsideRoom() {
		try {
			assertEquals(robot.isInsideRoom(), 
					control.getMaze().getFloorplan().isInRoom(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]));
		} catch (Exception e) {
			
		}
	}
	
	
	/**
	 * Test if the method accurately tells if the robot has stopped for reasons like lack of energy, 
	 */
	@Test
	final void TestHasStopped() {
		robot.isStopped = true;
		assertEquals(robot.hasStopped(), true);
	}
	
	/**
	 * Test if the method accurately tells the distance to an obstacle (a wall) 
	 * in the forward direction.
	 */
	@Test
	final void TestDistanceToObstacleForward(){
		assertEquals(true, robot.distanceToObstacle(Direction.FORWARD)<Integer.MAX_VALUE);
	}
	
	/**
	 * Test if the method accurately tells the distance to an obstacle (a wall) 
	 * in the backward direction.
	 */
	@Test
	final void TestDistanceToObstacleBackward(){
		assertEquals(true, robot.distanceToObstacle(Direction.BACKWARD)<Integer.MAX_VALUE);
	}
	
	/**
	 * Test if the method accurately tells the distance to an obstacle (a wall) 
	 * in the left direction.
	 */
	@Test
	final void TestDistanceToObstacleLeft(){
		assertEquals(true, robot.distanceToObstacle(Direction.LEFT)<Integer.MAX_VALUE);
	}
	
	/**
	 * Test if the method accurately tells the distance to an obstacle (a wall) 
	 * in the right direction.
	 */
	@Test
	final void TestDistanceToObstacleRight(){
		assertEquals(true, robot.distanceToObstacle(Direction.RIGHT)<Integer.MAX_VALUE);
	}
	
	/**
	 * Test if the method accurately tells if a sensor can identify the exit 
	 * in the given direction relative to 
	 * the robot's current forward direction from the current position.
	 */
	@Test
	final void TestCanSeeThroughTheExitIntoEternity() {
		assertEquals(false, robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD));
	}

}
