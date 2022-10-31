package gui;

import java.util.ArrayDeque;

import gui.Robot.Direction;

public class SmarterWallFollower extends WallFollower {
	
	/**
	 * Drive the robot towards the exit using the left wall-follower algorithm.
	 * If the robot is stuck in the room, circling around the inner wall, the driver can
	 * identify and get out of the room by moving away from the inner wall.
	 * @return true if the algorithm successfully drives the robot out of the maze.
	 * @throws Exception thrown if robot stopped due to a specific reason.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		while (!robot.hasStopped()) {
			// keep track of a robot's current position.
			int[] currentPosition;
			try {
				Thread.sleep(500);
				//When the robot is in the room
				while (robot.isInsideRoom()) {
					//make the robot position tracker
					ArrayDeque<int[]> positionRecord = new ArrayDeque<>();
					//move the robot
					drive1Step2Exit();
					currentPosition = robot.getCurrentPosition();
					//keep the record
					positionRecord.addLast(currentPosition);
					if (positionRecord.size() > 1) {
						//If the robot ended up in the same spot, which means that the robot is
						//circling around
						if (positionRecord.peekFirst()[0] == positionRecord.peekLast()[0]
								&& positionRecord.peekFirst()[1] == positionRecord.peekLast()[1]) {
							//move the robot until it hit the outer room wall.
							while (robot.distanceToObstacle(Direction.FORWARD)!=0) {
								robot.move(1);
							}
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
