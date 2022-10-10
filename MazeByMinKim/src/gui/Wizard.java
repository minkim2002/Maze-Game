package gui;

import generation.Maze;

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

	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return 0;
	}

}
