package gui;

import java.util.ArrayList;

import generation.CardinalDirection;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * SmartWizard class introduces an Wizard Algorithm with better functionality.
 * While it inherits most of the components from Wizard, it can analyze the efficiency
 * of jump and move, and when jump is more efficient than move, it makes the robot jump,
 * instead of moving. 
 * 
 * @author Min Kim
 *
 */

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
				if(!jumpInsteadMove(robot.getCurrentDirection()))
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
	
	/**
	 * JumpInsteadMove, which is an exclusive helper method for drive2Exit method for SmartWizard,
	 * compares the cost of jumping and moving, and pick a more efficient option the robot can take.
	 * If the method figures out that jumping is not more efficient than moving, it doesn't do anything.
	 * @param cd the CardinalDirection of the current direction the robot is looking at
	 * @throws Exception
	 */
	public boolean jumpInsteadMove(CardinalDirection cd) throws Exception {
		switch (cd) {
		
		//When the robot is looking to the North
		case North:
			//When the robot has an obstacle right in front of it, and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.FORWARD) == 0
					&& (robot.getCurrentPosition()[1] + 1) < referenceMaze.getHeight()) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1] + 1) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 6) {
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right next to it (Left side), and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.LEFT) == 0 
					&& (robot.getCurrentPosition()[0] - 1) >= 0) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0] - 1,
						robot.getCurrentPosition()[1]) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1])-7) {
					robot.rotate(Turn.LEFT);
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right next to it(Right side), and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.RIGHT) == 0
					&& (robot.getCurrentPosition()[0] + 1) < referenceMaze.getWidth()) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0] + 1,
						robot.getCurrentPosition()[1]) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.RIGHT);
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right behind of it, and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.BACKWARD) == 0 
					&& (robot.getCurrentPosition()[1] - 1) >= 0) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1] - 1) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.AROUND);
					//Jump!
					robot.jump();
					return true;
				}
			}

			break;
			
		//When the robot is looking to the North
		case West:
			//When the robot has an obstacle right in front of it, and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.FORWARD) == 0
					&& (robot.getCurrentPosition()[0] - 1) >= 0) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0]-1,
						robot.getCurrentPosition()[1]) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 6) {
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right next to it (Left side), and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.LEFT) == 0 
					&& (robot.getCurrentPosition()[1]-1) >= 0) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1]-1) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.LEFT);
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right next to it(Right side), and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.RIGHT) == 0
					&& (robot.getCurrentPosition()[1]+1) < referenceMaze.getHeight()) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1]+1) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.RIGHT);
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right behind of it, and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.BACKWARD) == 0 
					&& (robot.getCurrentPosition()[0] + 1) < referenceMaze.getWidth()) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0]+1,
						robot.getCurrentPosition()[1]) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.AROUND);
					//Jump!
					robot.jump();
					return true;
				}
			}

			break;
			
		//When the robot is looking to the North
		case East:
			//When the robot has an obstacle right in front of it, and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.FORWARD) == 0
					&& (robot.getCurrentPosition()[0] + 1) < referenceMaze.getWidth()) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0]+1,
						robot.getCurrentPosition()[1]) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 6) {
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right next to it (Left side), and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.LEFT) == 0 
					&& (robot.getCurrentPosition()[1]+1) < referenceMaze.getHeight()) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1]+1) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.LEFT);
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right next to it(Right side), and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.RIGHT) == 0
					&& (robot.getCurrentPosition()[1]-1) >= 0) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1]-1) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.RIGHT);
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right behind of it, and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.BACKWARD) == 0 
					&& (robot.getCurrentPosition()[0] - 1) > 0) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0]-1,
						robot.getCurrentPosition()[1]) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 7) {
					robot.rotate(Turn.AROUND);
					//Jump!
					robot.jump();
					return true;
				}
			}

			break;
			
		//When the robot is looking to the South
		case South:
			//When the robot has an obstacle right in front of it, and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.FORWARD) == 0
					&& (robot.getCurrentPosition()[1] -1) >= 0) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1] -1) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 6) {
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right next to it (Left side), and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.LEFT) == 0 
					&& (robot.getCurrentPosition()[0] + 1) < referenceMaze.getWidth()) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0] + 1,
						robot.getCurrentPosition()[1]) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 6) {
					robot.rotate(Turn.LEFT);
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right next to it(Right side), and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.RIGHT) == 0
					&& (robot.getCurrentPosition()[0] - 1) >= 0) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0] - 1,
						robot.getCurrentPosition()[1]) <= referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
								robot.getCurrentPosition()[1]) - 6) {
					robot.rotate(Turn.RIGHT);
					//Jump!
					robot.jump();
					return true;
				}
			}
			//When the robot has an obstacle right behind of it, and one step beyond is not out of maze
			if (robot.distanceToObstacle(Direction.BACKWARD) == 0 
					&& (robot.getCurrentPosition()[1] + 1) < referenceMaze.getHeight()) {
				//If when robot jumping can take you more than 6 steps closer to the exit than moving one,
				//Which means that jumping is more efficient than move (jumping = 40 < 6(moving + sensing) = 42)
				if (referenceMaze.getDistanceToExit(robot.getCurrentPosition()[0],
						robot.getCurrentPosition()[1] + 1) <= referenceMaze
								.getDistanceToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]) - 6) {
					robot.rotate(Turn.AROUND);
					//Jump!
					robot.jump();
					return true;
				}
			}

			break;
		}
		return false;
	} 
}
