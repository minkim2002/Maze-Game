package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import generation.CardinalDirection;
import generation.Floorplan;
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
	
	
	protected Maze referenceMaze; 
	private int width;
	private int height;
	protected Direction referenceDirection;
	protected boolean isOperational; 
	
	private Map<ArrayList<Integer>, CardinalDirection> getDirMap;
	private Map<CardinalDirection, ArrayList<Integer>> getCoordMap;
	
	
	public ReliableSensor() {
		isOperational = true;
		mapping();
	}
	
	public ReliableSensor(Direction direction) {
		this();
		setSensorDirection(direction);
	}
	
	/**
	 * returns the distance from the obstacle to the direction 
	 * the robot's particular sensor is looking at. Use Floorplan method to get
	 * the distance
	 * @param currentPosition current position the robot is located in the maze
	 * @param currentDirection current direction the robot is looking at
	 * @param powersupply current power the robot has
	 * @return integer value of the distance
	 */
	
	
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			 throws Exception{
		// check if power is less than 0
		if (powersupply[0] < 0) {
			throw new IndexOutOfBoundsException();
		}
		// check if any of the parameters are null or if currentPosition is outside of the maze
		if (currentPosition == null || currentDirection == null || powersupply == null ||
			currentPosition[0] < 0 || currentPosition[0] >= width || currentPosition[1] < 0 || currentPosition[1] >= height) {
			throw new IllegalArgumentException();
		}
		// check if sensor is not operational or the power is not enough for sensing
		if (!isOperational) {
			throw new Exception("SensorFailure");
		}
		if (powersupply[0] < getEnergyConsumptionForSensing()) {
			throw new Exception("PowerFailure");
		}
		// figure out which absolute (cardinal) direction we should move in
		CardinalDirection currentDir = convertToFixedDir(referenceDirection, currentDirection);
		// keep track of the distance with a counter
		int distance = 0;
		// while there isn't a wallboard in the given direction
		while (!referenceMaze.hasWall(currentPosition[0], currentPosition[1], currentDir)) {
			distance = sense(currentPosition, currentDir, powersupply, distance);
			if (distance == Integer.MAX_VALUE) {
				return distance;
			}
		}
		powersupply[0] -= getEnergyConsumptionForSensing();
		return distance;
	}
	
	protected int sense(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply, int distance)
			throws Exception {
		// check if the next cell is outside of the maze (meaning the current cell is located at the exit) and
		// return Integer.MAX_VALUE if so
		// else sense the next cell and update the distance counter
		switch (currentDirection) {
			case North:
				if (currentPosition[1]-1 < 0) {
					powersupply[0] -= getEnergyConsumptionForSensing();
					if (powersupply[0] < getEnergyConsumptionForSensing()) {
						throw new Exception("PowerFailure");
					}
					return Integer.MAX_VALUE;
				}
				currentPosition[1] -= 1;
				break;
			case West:
				if (currentPosition[0]-1 < 0) {
					powersupply[0] -= getEnergyConsumptionForSensing();
					if (powersupply[0] < getEnergyConsumptionForSensing()) {
						throw new Exception("PowerFailure");
					}
					return Integer.MAX_VALUE;
				}
				currentPosition[0] -= 1;
				break;
			case East:
				if (currentPosition[0]+1 >= width) {
					powersupply[0] -= getEnergyConsumptionForSensing();
					if (powersupply[0] < getEnergyConsumptionForSensing()) {
						throw new Exception("PowerFailure");
					}
					return Integer.MAX_VALUE;
				}
				currentPosition[0] += 1;
				break;
			case South:
				if (currentPosition[1]+1 >= height) {
					powersupply[0] -= getEnergyConsumptionForSensing();
					if (powersupply[0] < getEnergyConsumptionForSensing()) {
						throw new Exception("PowerFailure");
					}
					return Integer.MAX_VALUE;
				}
				currentPosition[1] += 1;
				break;	
		}
		distance++;
		return distance;
	}
	
	/**
	 * Set the reference of the maze created
	 * @param maze the maze created
	 */
	@Override
	public void setMaze(Maze maze) {
		if(maze==null || maze.getFloorplan() == null) {
			throw new IllegalArgumentException();
		}
		this.referenceMaze = maze;
		this.width = maze.getWidth();
		this.height = maze.getHeight();
	}

	/**
	 * Set the direction of each sensor.
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		if(mountedDirection == null) {
			throw new IllegalArgumentException();
		}
		this.referenceDirection = mountedDirection;
	}
	
	/**
	 * Get the numeric value of the amount of energy consumption for
	 * sensing
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		return 1;
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
	
	/**
	 * Creates the maps to store the mappings used in the convertToAbsoluteDirection method
	 */
	protected void mapping() {
		
		// all of the CardinalDirections in the order that will map consistently to the transformations
		CardinalDirection[] carDirs = {CardinalDirection.North, CardinalDirection.West, CardinalDirection.East, CardinalDirection.South};
		int cardirIdx = 0;
		// map coordinates involving only +/-1 to each direction 
		getDirMap = new HashMap<ArrayList<Integer>, CardinalDirection>();
		// map each direction to its coordinates
		getCoordMap = new HashMap<CardinalDirection, ArrayList<Integer>>();
		
		
		int[] range = {-1, 1};
		for (int i = 0; i <= 1; i++) {
			for (int j = 0; j <= 1; j++) {
				ArrayList<Integer> pair = new ArrayList<Integer>();
				pair.add(range[i]); 
				pair.add(range[j]);
				getDirMap.put(pair, carDirs[cardirIdx]);
				getCoordMap.put(carDirs[cardirIdx], pair);
				cardirIdx++;
			}
		}
	}
	
	/**
	 * Converts a relative direction to an absolute direction based on the current CardinalDirection
	 * @param direction that we want to use for the conversion
	 * @param currDir is the current direction of the robot, we use this in conjunction with the relative direction
	 * to obtain the new absolute (cardinal) direction
	 * @return CardinalDirection of the relative direction
	 */
	protected CardinalDirection convertToFixedDir(Direction direction, CardinalDirection currentDirection) {
		assert(getCoordMap != null);
		ArrayList<Integer> dir = getCoordMap.get(currentDirection); 
		ArrayList<Integer> convertedDir = new ArrayList<Integer>();
		switch (direction) {
			case BACKWARD:
				convertedDir.add(dir.get(0)*-1); 
				convertedDir.add(dir.get(1)*-1);
				break;
			case LEFT:
				convertedDir.add(dir.get(1)*-1); 
				convertedDir.add(dir.get(0));
				break;
			case RIGHT:
				convertedDir.add(dir.get(1)); 
				convertedDir.add(dir.get(0)*-1);
				break;
			default:
				return currentDirection;
		}
		return getDirMap.get(convertedDir);
	}

}
