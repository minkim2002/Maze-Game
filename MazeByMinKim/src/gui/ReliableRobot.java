package gui;

import java.awt.print.Printable;
import gui.Constants.UserInput;

import generation.CardinalDirection;
import generation.Maze;

/**
 * This class has the responsibility to know how far the wall is from the
 * direction it is looking at, move toward, turn 90 degree angle, and jump over
 * a wall.
 * 
 * This class implements Robot and uses ReliableSensor to add the sensors to the
 * robot.
 * 
 * @author Min Kim
 *
 */

public class ReliableRobot implements Robot {

	private Control control;
	private DistanceSensor reliableSensorForward;
	private DistanceSensor reliableSensorLeft;
	private DistanceSensor reliableSensorBackward;
	private DistanceSensor reliableSensorRight;

	private Maze referenceMaze;
	private int width;
	private int height;

	private float battery;
	private int distanceTraveled;
	private boolean isStopped;

	public ReliableRobot() {
		addDistanceSensor(reliableSensorForward, Direction.FORWARD);
		addDistanceSensor(reliableSensorLeft, Direction.LEFT);
		addDistanceSensor(reliableSensorBackward, Direction.BACKWARD);
		addDistanceSensor(reliableSensorRight, Direction.RIGHT);
		setBatteryLevel(3500);
	}

	/**
	 * Set a reference to the controller to cooperate with. The controller serves as
	 * the main source of information for the robot about the current position, the
	 * presence of walls, the reaching of an exit.
	 * 
	 * @param controller the communicator for robot
	 */
	@Override
	public void setController(Control controller) {
		// check if controller is null, controller is not in playing state, or it
		// doesn't have a maze
		if (controller == null || !(controller.currentState instanceof StatePlaying) || controller.getMaze() == null)
			throw new IllegalArgumentException();
		control = controller;
		isStopped = false;
		setBatteryLevel(3500);
		resetOdometer();
		referenceMaze = controller.getMaze();
		width = referenceMaze.getWidth();
		height = referenceMaze.getHeight();
		reliableSensorForward.setMaze(referenceMaze);
		reliableSensorLeft.setMaze(referenceMaze);
		reliableSensorBackward.setMaze(referenceMaze);
		reliableSensorRight.setMaze(referenceMaze);
	}

	/**
	 * Adds a distance sensor to the robot such that it measures in the given
	 * direction. This method is used when a robot is initially configured to get
	 * ready for operation. A robot can have at most four sensors in total, and at
	 * most one for any direction.
	 * 
	 * @param sensor           the distance sensor to be added
	 * @param mountedDirection the direction that it points to relative to the
	 *                         robot's forward direction
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		sensor = new ReliableSensor(mountedDirection);
	}

	/**
	 * Get the current position as (x,y) coordinates for the maze as an array of
	 * length 2 with [x,y].
	 * 
	 * @return array of length 2, x = array[0], y = array[1]
	 * @throws Exception if position is outside of the maze
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		int[] currentPosition = control.getCurrentPosition();
		// check if the current position is outside of the maze
		if (currentPosition[0] < 0 || currentPosition[0] >= width || currentPosition[1] < 0
				|| currentPosition[1] >= height) {
			throw new Exception();
		}
		return currentPosition;
	}

	/**
	 * Get the robot's current direction.
	 * 
	 * @return the robot's current direction in absolute terms
	 */
	@Override
	public CardinalDirection getCurrentDirection() {
		return control.getCurrentDirection();
	}

	/**
	 * Returns the current battery level.
	 * 
	 * @return current battery level
	 */
	@Override
	public float getBatteryLevel() {
		return battery;
	}

	/**
	 * Set the current battery level.
	 * 
	 * @param level the current battery level
	 */
	@Override
	public void setBatteryLevel(float level) {
		// check if level is negative
		if (level < 0) {
			throw new IllegalArgumentException();
		}
		battery = level;
	}

	/**
	 * Gives the energy consumption for a full 360 degree rotation. Scaling by other
	 * degrees approximates the corresponding consumption.
	 * 
	 * @return energy for a full rotation
	 */
	@Override
	public float getEnergyForFullRotation() {
		return 12;
	}

	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step. For
	 * moving a distance of n steps takes n times the energy for a single step.
	 * 
	 * @return energy for a single step forward
	 */
	@Override
	public float getEnergyForStepForward() {
		return 6;
	}

	/**
	 * Gets the distance traveled by the robot. The robot has an odometer that
	 * calculates the distance the robot has moved. Whenever the robot moves
	 * forward, the distance that it moves is added to the odometer counter. The
	 * odometer reading gives the path length if its setting is 0 at the start of
	 * the game. The counter can be reset to 0 with resetOdomoter().
	 * 
	 * @return the distance traveled measured in single-cell steps forward
	 */
	@Override
	public int getOdometerReading() {
		return distanceTraveled;
	}

	/**
	 * Resets the odometer counter to zero.
	 */
	@Override
	public void resetOdometer() {
		distanceTraveled = 0;
	}

	/**
	 * Turn robot on the spot for amount of degrees.
	 * 
	 * @param turn the direction to turn and relative to current forward direction.
	 */
	@Override
	public void rotate(Turn turn) {
		// check battery level beforehand
		if (getBatteryLevel() < 3 || (turn == Turn.AROUND && getBatteryLevel() < 6)) {
			setBatteryLevel(0);
			isStopped = true;
			return;
		}
		// use control to turn the robot and update the battery level
		switch (turn) {
		case LEFT:
			setBatteryLevel(getBatteryLevel() - 3);
			control.handleKeyboardInput(UserInput.LEFT, 0);
			break;
		case RIGHT:
			setBatteryLevel(getBatteryLevel() - 3);
			control.handleKeyboardInput(UserInput.RIGHT, 0);
			break;
		case AROUND:
			setBatteryLevel(getBatteryLevel() - 6);
			control.handleKeyboardInput(UserInput.LEFT, 0);
			control.handleKeyboardInput(UserInput.LEFT, 0);
			break;
		}
		// check if the battery level is 0 and stop the robot if so
		if (getBatteryLevel() == 0) {
			isStopped = true;
		}
	}

	/**
	 * Moves robot forward a given number of steps. A step matches a single cell. If
	 * the robot runs out of energy somewhere on its way, it stops, which can be
	 * checked by hasStopped() == true and by checking the battery level. If the
	 * robot hits an obstacle like a wall, it remains at the position in front of
	 * the obstacle and also hasStopped() == true as this is not supposed to happen.
	 * This is also helpful to recognize if the robot implementation and the actual
	 * maze do not share a consistent view on where walls are and where not.
	 * 
	 * @param distance is the number of cells to move in the robot's current forward
	 *                 direction
	 */
	@Override
	public void move(int distance) {
		if (getBatteryLevel() < 6) {
			setBatteryLevel(0);
			isStopped = true;
			return;
		}
		// create a new variable to keep track of distance moved
		int distanceMoved = 0;
		// while the distance moved is less than the inputed distance
		while (distanceMoved < distance) {
			try {
				int[] currentPosition = getCurrentPosition();
				// check if there is an obstacle (wall) directly in front of the robot
				// and stop if so
				if (distanceMoved != distance
						&& referenceMaze.hasWall(currentPosition[0], currentPosition[1], getCurrentDirection())) {
					setBatteryLevel(0);
					isStopped = true;
					break;
				}
			} catch (Exception e) {
				System.out.println("outside Maze");
				return;
			}
			// move the robot one step forward and update the distance traveled and battery
			// level
			control.handleKeyboardInput(UserInput.UP, 0);
			distanceTraveled++;
			distanceMoved++;
			setBatteryLevel(getBatteryLevel() - 6);
			// check if the energy has been depleted and stop if so
			// (make sure to break out of the loop)
			if (getBatteryLevel() == 0) {
				isStopped = true;
				break;
			}
			// check if there's enough energy to continue moving
			if (distanceMoved != distance && getBatteryLevel() < 6) {
				setBatteryLevel(0);
				isStopped = true;
				return;
			}
		}
	}

	/**
	 * Makes robot move in a forward direction even if there is a wall in front of
	 * it. In this sense, the robot jumps over the wall if necessary. The distance
	 * is always 1 step and the direction is always forward. If the robot runs out
	 * of energy somewhere on its way, it stops, which can be checked by
	 * hasStopped() == true and by checking the battery level. If the robot tries to
	 * jump over an exterior wall and would land outside of the maze that way, it
	 * remains at its current location and direction, hasStopped() == true as this
	 * is not supposed to happen.
	 */
	@Override
	public void jump() {
		// check battery level beforehand
		if (getBatteryLevel() < 40) {
			setBatteryLevel(0);
			isStopped = true;
			return;
		}
		try {
			// fetch the current position of the robot
			int[] currentPosition = getCurrentPosition();
			// check if the wall in front is an exterior wall and stop if so
			switch (getCurrentDirection()) {
			case North:
				if (referenceMaze.hasWall(currentPosition[0], currentPos[1], CardinalDirection.North) && currPos[1] - 1 < 0) {
					setBatteryLevel(0);
					stopped = true;
					return;
				}
				break;
			case East:
				if (maze.hasWall(currPos[0], currPos[1], CardinalDirection.East) && currPos[0] + 1 == width) {
					setBatteryLevel(0);
					stopped = true;
					return;
				}
				break;
			case South:
				if (maze.hasWall(currPos[0], currPos[1], CardinalDirection.South) && currPos[1] + 1 == height) {
					setBatteryLevel(0);
					stopped = true;
					return;
				}
				break;
			case West:
				if (maze.hasWall(currPos[0], currPos[1], CardinalDirection.West) && currPos[0] - 1 < 0) {
					setBatteryLevel(0);
					stopped = true;
					return;
				}
				break;
			}
		} catch (Exception e) {
			System.out.println("Position outside maze!");
			return;
		}
		// execute the jump and update the distance traveled and battery level
		controller.keyDown(UserInput.Jump, 0);
		distTraveled++;
		setBatteryLevel(getBatteryLevel() - JUMP_COST);
		if (getBatteryLevel() == 0)
			stopped = true;
	}

	/**
	 * Tells if the current position is right at the exit but still inside the maze.
	 * The exit can be in any direction. It is not guaranteed that the robot is
	 * facing the exit in a forward direction.
	 * 
	 * @return true if robot is at the exit, false otherwise
	 * @throws Exception
	 */
	@Override
	public boolean isAtExit() {

	}

	/**
	 * Tells if current position is inside a room.
	 * 
	 * @return true if robot is inside a room, false otherwise
	 */
	@Override
	public boolean isInsideRoom() {
		if (distanceToObstacle(Direction.FORWARD) == 0) {
			return false;
		}
		if (distanceToObstacle(Direction.LEFT) == 0) {
			return false;
		}
		if (distanceToObstacle(Direction.RIGHT) == 0) {
			return false;
		}
		if (distanceToObstacle(Direction.BACKWARD) == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Tells if the robot has stopped for reasons like lack of energy, hitting an
	 * obstacle, etc. Once a robot is has stopped, it does not rotate or move
	 * anymore.
	 * 
	 * @return true if the robot has stopped, false otherwise
	 */
	@Override
	public boolean hasStopped() {
		if (getBatteryLevel() == 0) {
			return true;
		}
		if (distanceToObstacle(Direction.FORWARD) == 0 && !control.wayIsClear(1)) {
			return true;
		}
		return false;
	}

	/**
	 * Tells the distance to an obstacle (a wall) in the given direction. The
	 * direction is relative to the robot's current forward direction. Distance is
	 * measured in the number of cells towards that obstacle, e.g. 0 if the current
	 * cell has a wallboard in this direction, 1 if it is one step forward before
	 * directly facing a wallboard, Integer.MaxValue if one looks through the exit
	 * into eternity. The robot uses its internal DistanceSensor objects for this
	 * and delegates the computation to the DistanceSensor which need to be
	 * installed by calling the addDistanceSensor() when configuring the robot.
	 * 
	 * @param direction specifies the direction of interest
	 * @return number of steps towards obstacle if obstacle is visible in a straight
	 *         line of sight, Integer.MAX_VALUE otherwise
	 * @throws UnsupportedOperationException if robot has no sensor in this
	 *                                       direction or the sensor exists but is
	 *                                       currently not operational
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		try {
			if (direction == Direction.FORWARD) {
				reliableSensorForward.setSensorDirection(direction);
				return reliableSensorForward.distanceToObstacle(getCurrentPosition(), getCurrentDirection(),
						batterylevel);
			}
			if (direction == Direction.BACKWARD) {
				reliableSensorBackward.setSensorDirection(direction);
				return reliableSensorBackward.distanceToObstacle(getCurrentPosition(), getCurrentDirection(),
						batterylevel);
			}
			if (direction == Direction.LEFT) {
				reliableSensorLeft.setSensorDirection(direction);
				return reliableSensorLeft.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), batterylevel);
			} else {
				reliableSensorRight.setSensorDirection(direction);
				return reliableSensorRight.distanceToObstacle(getCurrentPosition(), getCurrentDirection(),
						batterylevel);
			}
		} catch (Exception e) {
			System.out.println("SensorFailure if the sensor is currently not operational or "
					+ "PowerFailure if the power supply is insufficient for the operation");
			return Integer.MAX_VALUE;
		}
	}

	/**
	 * Tells if a sensor can identify the exit in the given direction relative to
	 * the robot's current forward direction from the current position. It is a
	 * convenience method is based on the distanceToObstacle() method and transforms
	 * its result into a boolean indicator.
	 * 
	 * @param direction is the direction of the sensor
	 * @return true if the exit of the maze is visible in a straight line of sight
	 * @throws UnsupportedOperationException if robot has no sensor in this
	 *                                       direction or the sensor exists but is
	 *                                       currently not operational
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		if ((distanceToObstacle(Direction.FORWARD) == Integer.MAX_VALUE)) {
			return true;
		}
		if ((distanceToObstacle(Direction.LEFT) == Integer.MAX_VALUE)) {
			return true;
		}
		if ((distanceToObstacle(Direction.RIGHT) == Integer.MAX_VALUE)) {
			return true;
		}
		if ((distanceToObstacle(Direction.BACKWARD) == Integer.MAX_VALUE)) {
			return true;
		}
		return false;
	}

	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}