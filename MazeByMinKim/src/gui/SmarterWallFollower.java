package gui;

import java.util.ArrayDeque;

import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * SmarterWallFollower class introduces a WallFollower algorithm with a better functionality.
 * While it inherits most of the components form the WallFollower class, it additionally
 * handles a situation where the robot is stuck in an inner wall in a room.
 * 
 * @author Min Kim
 */

public class SmarterWallFollower extends WallFollower {
	
	/**
	 * Drive the robot towards the exit using the left smarter wall-follower algorithm.
	 * If the robot is stuck in the room, circling around the inner wall, the driver can
	 * identify and get out of the room by moving away from the inner wall.
	 * @return true if the algorithm successfully drives the robot out of the maze.
	 * @throws Exception thrown if robot stopped due to a specific reason.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		ArrayDeque<int[]> positionRecord = new ArrayDeque<>();
		int[] currentPosition;
		while (!robot.hasStopped()) {
			// keep track of a robot's current position.
			try {
				//make the robot position tracker
				//When the robot is in the room
				if (robot.isInsideRoom()) {
					//move the robot
					drive1Step2Exit();
					currentPosition = robot.getCurrentPosition();
					//keep the record
					positionRecord.add(currentPosition);
					if (positionRecord.size() > 1) {
						//If the robot ended up in the same spot, which means that the robot is
						//circling around
						if (positionRecord.peekFirst()[0] == positionRecord.peekLast()[0]
								&& positionRecord.peekFirst()[1] == positionRecord.peekLast()[1]) {
							//rotate the robot to the right then
							//move the robot until it hit the outer room wall.
							robot.rotate(Turn.RIGHT);
							while (robot.distanceToObstacle(Direction.FORWARD)!=0) {
								robot.move(1);
							}
							robot.rotate(Turn.RIGHT);
						}
					}
				}
				//If the robot is not inside a room, the driver moves the robot once.
				drive1Step2Exit();
				// Update the robot's position.
				currentPosition = robot.getCurrentPosition();
			} catch (Exception e) {
				throw new Exception();
			}
			// Check if the robot is at the exit.
			if (robot.isAtExit()) {
				// Take one final step to the right direction, and return true.
				exit2End(currentPosition);
				return true;
			}
		}
		return false;
	}
}
