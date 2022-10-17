package gui;

import java.util.function.IntPredicate;

import generation.CardinalDirection;
import generation.Maze;
import gui.Robot.Turn;

/**
 * This class has the responsibility to get out of the maze as quick
 * as possible, be the most efficient in terms of energy consumption
 * and path length.
 * 
 * This class implements RobotDriver and uses Maze to find the exit by
 * looking for a neighbor that is closer to the exit. Also, it uses 
 * Reliable Robot to navigate through the maze.
 * 
 * @author Min Kim
 *
 */

public class Wizard implements RobotDriver {
	
	/**
	 * Assigns a robot platform to the driver. 
	 * The driver uses a robot to perform, this method provides it with this necessary information.
	 * @param r robot to operate
	 */
	
	Robot robot;
	Maze referenceMaze;
	
	@Override
	public void setRobot(Robot r) {
		robot = r;
	}
	
	/**
	 * Set the robot driver with the maze information.
	 * @param maze represents the maze
	 */
	@Override
	public void setMaze(Maze maze) {
		referenceMaze = maze;
	}
	
	/**
	 * Drives the robot towards the exit following
	 * its solution strategy and given the exit exists and  
	 * given the robot's energy supply lasts long enough. 
	 * When the robot reached the exit position and its forward
	 * direction points to the exit the search terminates and 
	 * the method returns true.
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception.
	 * If the method determines that it is not capable of finding the
	 * exit it returns false, for instance, if it determines it runs
	 * in a cycle and can't resolve this.
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		int[] neighbor = new int[2];
		while(referenceMaze.getDistanceToExit((robot.getCurrentPosition())[0], (robot.getCurrentPosition())[1])!=0) {
			neighbor = referenceMaze.getNeighborCloserToExit((robot.getCurrentPosition())[0], (robot.getCurrentPosition())[1]);
			if(neighbor[0] == robot.getCurrentPosition()[0]++) {
				robot.rotate(Turn.RIGHT);
				robot.move(1);
			}
			else if(neighbor[0] == robot.getCurrentPosition()[0]--) {
				robot.rotate(Turn.LEFT);
				robot.move(1);
			}
			else if(neighbor[1] == robot.getCurrentPosition()[1]++) {
				robot.move(1);
			}
		}
		return true;
	}
	
	/**
	 * Drives the robot one step towards the exit following
	 * its solution strategy and given the exists and 
	 * given the robot's energy supply lasts long enough.
	 * the wizard uses the information of the Maze object 
	 * (handed to it via the setMaze method) to find the exit by
	 * looking for a neighbor that is closer to the exit.
	 * It returns true if the driver successfully moved
	 * the robot from its current location to an adjacent
	 * location.
	 * At the exit position, it rotates the robot 
	 * such that if faces the exit in its forward direction
	 * and returns false. 
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception. 
	 * @return true if it moved the robot to an adjacent cell, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		int[] neighbor = new int[2];
		CardinalDirection cardinalDirection;
		if(referenceMaze.getDistanceToExit((robot.getCurrentPosition())[0], (robot.getCurrentPosition())[1])!=0) {
			neighbor = referenceMaze.getNeighborCloserToExit((robot.getCurrentPosition())[0], (robot.getCurrentPosition())[1]);
			if(neighbor[0] == robot.getCurrentPosition()[0]++) {
				robot.rotate(Turn.RIGHT);
				robot.move(1);
			}
			else if(neighbor[0] == robot.getCurrentPosition()[0]--) {
				robot.rotate(Turn.LEFT);
				robot.move(1);
			}
			else if(neighbor[1] == robot.getCurrentPosition()[1]++) {
				robot.move(1);
			} return true;
		} else {
			if(referenceMaze.hasWall(getPathLength(), getPathLength(), cardinalDirection))
		}
		return true;
	}
	
	/**
	 * Returns the total energy consumption of the journey, i.e.,
	 * the difference between the robot's initial energy level at
	 * the starting position and its energy level at the exit position. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total energy consumption of the journey
	 */
	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		return robot.getBatteryLevel();
	}
	
	/**
	 * Returns the total length of the journey in number of cells traversed. 
	 * Being at the initial position counts as 0. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total length of the journey in number of cells traversed
	 */
	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return 0;
	}

}
