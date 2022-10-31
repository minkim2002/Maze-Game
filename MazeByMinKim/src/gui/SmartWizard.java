package gui;

import generation.CardinalDirection;
import gui.Robot.Turn;

public class SmartWizard extends Wizard{
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// get the next neighbor closest to the exit
		int[] currentPosition = robot.getCurrentPosition();
		int[] neighbor = referenceMaze.getNeighborCloserToExit(currentPosition[0], currentPosition[1]);
		// rotate robot in that direction
		CardinalDirection currentDirection = robot.getCurrentDirection();
		int[] changeDirection = { neighbor[0] - currentPosition[0], neighbor[1] - currentPosition[1] };
		switch (currentDirection) {
		case North:
			if (changeDirection[0] == -1)
				robot.rotate(Turn.RIGHT);
			else if (changeDirection[0] == 1)
				robot.rotate(Turn.LEFT);
			else if (changeDirection[1] == 1)
				robot.rotate(Turn.AROUND);
			break;
		case East:
			if (changeDirection[0] == -1)
				robot.rotate(Turn.AROUND);
			else if (changeDirection[1] == -1)
				robot.rotate(Turn.RIGHT);
			else if (changeDirection[1] == 1)
				robot.rotate(Turn.LEFT);
			break;
		case South:
			if (changeDirection[0] == -1)
				robot.rotate(Turn.LEFT);
			else if (changeDirection[0] == 1)
				robot.rotate(Turn.RIGHT);
			else if (changeDirection[1] == -1)
				robot.rotate(Turn.AROUND);
			break;
		case West:
			if (changeDirection[0] == 1)
				robot.rotate(Turn.AROUND);
			else if (changeDirection[1] == -1)
				robot.rotate(Turn.LEFT);
			else if (changeDirection[1] == 1)
				robot.rotate(Turn.RIGHT);
			break;
		}
		// move by either jumping or moving
		if (referenceMaze.hasWall(currentPosition[0], currentPosition[1], currentDirection))
			robot.jump();
		else
			robot.move(1);
		// throw an exception when robot has stopped
		if (robot.hasStopped())
			throw new Exception();
		return true;
	}
}
