package gui;

import generation.CardinalDirection;
import generation.Maze;
import gui.Robot.Direction;

/**
 * This class has the responsibility to get the robot's current
 * position and figure out how far the robot is to a wall from
 * the current position and the direction.
 * 
 * This class implements DistanceSensor and uses Floorplan to
 * measure distances towards obstacles.
 * 
 * @author Min Kim
 *
 */
public class ReliableSensor implements DistanceSensor {
	
	/**
	 * returns the distance from the obstacle to the direction 
	 * the robot is looking at. Use Floorplan method to get
	 * the distance
	 * @param currentPosition current position the robot is located in the maze
	 * @param currentDirection current direction the robot is looking at
	 * @param powersupply current power the robot has
	 * @return integer value of the distance
	 */
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Set the reference of the maze created
	 * @param maze the maze created
	 */
	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub

	}

	/**
	 * Set the direction of each sensor depends on the cardinal
	 * direction the robot is looking at. 
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Get the numeric value of the amount of energy consumption for
	 * sensing
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
