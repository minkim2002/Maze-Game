package gui;

import java.util.ArrayList;

import generation.CardinalDirection;
import gui.Robot.Direction;
import gui.Robot.Turn;

public class SmartWizard extends Wizard {

	/**
	 * Drives the robot towards the exit following its solution strategy and given
	 * the exit exists and given the robot's energy supply lasts long enough. When
	 * the robot reached the exit position and its forward direction points to the
	 * exit the search terminates and the method returns true. If the robot failed
	 * due to lack of energy or crashed, the method throws an exception. If the
	 * method determines that it is not capable of finding the exit it returns
	 * false, for instance, if it determines it runs in a cycle and can't resolve
	 * this.
	 * 
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		while (!robot.hasStopped()) {
			int[] currentPosition;
			// run drive1Step2Exit
			try {
				jumpInsteadMove(robot.getCurrentDirection());
				drive1Step2Exit();
				// update position
				currentPosition = robot.getCurrentPosition();
			} catch (Exception e) {
				throw new Exception();
			}
			// check if it has reached the exit
			if (robot.isAtExit()) {
				// cross the exit to win the game
				exit2End(currentPosition);
				return true;
			}
		}
		return false;
	}

	private void jumpInsteadMove(CardinalDirection cd) throws Exception {
		switch (cd) {
		case North:

			if (robot.distanceToObstacle(Direction.FORWARD) == 0
					&& (robot.getCurrentPosition()[1] + 1) < referenceMaze.getHeight()) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1] + 1) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 6) {
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.LEFT) == 0 
					&& (robot.getCurrentPosition()[0] - 1) >= 0) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0] - 1,
						robot.getCurrentPosition()[1]) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1])-7) {
					robot.rotate(Turn.LEFT);
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.RIGHT) == 0
					&& (robot.getCurrentPosition()[0] + 1) < referenceMaze.getWidth()) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0] + 1,
						robot.getCurrentPosition()[1]) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.RIGHT);
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.BACKWARD) == 0 
					&& (robot.getCurrentPosition()[1] - 1) >= 0) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1] - 1) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.AROUND);
					robot.jump();
				}
			}

			break;
		case West:
			if (robot.distanceToObstacle(Direction.FORWARD) == 0
					&& (robot.getCurrentPosition()[0] - 1) >= 0) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0]-1,
						robot.getCurrentPosition()[1]) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 6) {
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.LEFT) == 0 
					&& (robot.getCurrentPosition()[1]-1) >= 0) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1]-1) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.LEFT);
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.RIGHT) == 0
					&& (robot.getCurrentPosition()[1]+1) < referenceMaze.getHeight()) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1]+1) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.RIGHT);
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.BACKWARD) == 0 
					&& (robot.getCurrentPosition()[0] + 1) < referenceMaze.getWidth()) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0]+1,
						robot.getCurrentPosition()[1]) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.AROUND);
					robot.jump();
				}
			}

			break;
		case East:
			if (robot.distanceToObstacle(Direction.FORWARD) == 0
					&& (robot.getCurrentPosition()[0] + 1) < referenceMaze.getWidth()) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0]+1,
						robot.getCurrentPosition()[1]) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 6) {
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.LEFT) == 0 
					&& (robot.getCurrentPosition()[1]+1) < referenceMaze.getHeight()) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1]+1) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.LEFT);
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.RIGHT) == 0
					&& (robot.getCurrentPosition()[1]-1) >= 0) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1]-1) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.RIGHT);
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.BACKWARD) == 0 
					&& (robot.getCurrentPosition()[0] - 1) > 0) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0]-1,
						robot.getCurrentPosition()[1]) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.AROUND);
					robot.jump();
				}
			}

			break;
		case South:

			if (robot.distanceToObstacle(Direction.FORWARD) == 0
					&& (robot.getCurrentPosition()[1] -1) >= 0) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1] -1) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 6) {
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.LEFT) == 0 
					&& (robot.getCurrentPosition()[0] + 1) < referenceMaze.getWidth()) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0] + 1,
						robot.getCurrentPosition()[1]) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.LEFT);
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.RIGHT) == 0
					&& (robot.getCurrentPosition()[0] - 1) >= 0) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0] - 1,
						robot.getCurrentPosition()[1]) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.RIGHT);
					robot.jump();
				}
			}
			if (robot.distanceToObstacle(Direction.BACKWARD) == 0 
					&& (robot.getCurrentPosition()[1] + 1) < referenceMaze.getHeight()) {
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1] + 1) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.AROUND);
					robot.jump();
				}
			}

			break;
		}

	}
}
