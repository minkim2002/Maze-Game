package gui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.CardinalDirection;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * A set of test cases of Reliable Robot
 * 
 * @author Min Kim
 *
 */

class ReliableRobotTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Test whether a correct reference to the controller to cooperate with is assigned.
	 */
	@Test
	final void TestSetController() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test whether a correct distance sensor to 
	 * the robot such that it measures in the given direction is added
	 */
	@Test
	final void TestAddDistanceSensor() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Test whether the getCurrentPosition method is getting a correct position.
	 */
	@Test
	final void TestGetCurrentPosition() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test whether the getCurrentDirection method is getting a correct direction.
	 */
	@Test
	final void TestGetCurrentDirection() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test whether the getBatteryLevel method is getting a correct battery level.
	 */
	@Test
	final void TestGetBatteryLevel() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test whether the setBatteryLevel method is setting a matching battery level.
	 */
	@Test
	final void TestSetBatteryLevel() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test whether the getEnergyForFullRotation method is getting a correct battery
	 * usage for full rotation and rotation under 360.
	 */
	@Test
	final void TestGetEnergyForFullRotation() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test whether the getEnergyForStepForward method is getting a correct battery
	 * usage for one step forward and multiple steps forward
	 */
	@Test
	final void TestGetEnergyForStepForward() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test whether the getOdometerReading method is getting a correct distance traveled.
	 */
	@Test
	final void TestGetOdometerReading() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test whether the resetOdometer method is resetting odometer to zero.
	 */
	@Test
	final void TestResetOdometer() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test whether the rotate method is rotating the robot in an accurate matter.
	 */
	@Test
	final void TestRotate() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Test whether the move method is moving the robot in an accurate manner.
	 */
	@Test
	final void TestMove() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Test whether the jump method is making the robot jump in an accurate manner.
	 */
	@Test
	final void TestJump() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Test if the method accurately tells 
	 * if the current position is right at the exit but still inside the maze. 
	 */
	@Test
	final void TestIsAtExit() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test if the method accurately tells if current position is inside a room. 
	 */	
	@Test
	final void TestIsInsideRoom() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test if the method accurately tells if the robot has stopped for reasons like lack of energy, 
	 */
	@Test
	final void TestHasStopped() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test if the method accurately tells the distance to an obstacle (a wall) 
	 * in the given direction.
	 */
	@Test
	final void TestDistanceToObstacle(){
		// TODO Auto-generated method stub
	}
	
	/**
	 * Test if the method accurately tells if a sensor can identify the exit 
	 * in the given direction relative to 
	 * the robot's current forward direction from the current position.
	 */
	@Test
	final void TestCanSeeThroughTheExitIntoEternity() {
		// TODO Auto-generated method stub
	}

}
