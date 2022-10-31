package gui;


import gui.Robot.Direction;

/**
 * 
 * @author Min Kim
 * 
 * This class has the responsibility to know how far the wall is from the
 * direction it is looking at, move toward, turn 90 degree angle, and jump over
 * a wall. It works with sensors and manages its energy.
 * 
 * This class inherits ReliableRobot and collaborates with Controller, RobotDriver,
 * and DistanceSensor(Either Reliable or Unreliable).
 *
 */
public class UnreliableRobot extends ReliableRobot{
	public UnreliableRobot(int fw, int le, int ri, int bw) {
		super();
		reliableSensorForward = (fw == 0 ? new UnreliableSensor(Direction.FORWARD)
				: new ReliableSensor(Direction.FORWARD));
		reliableSensorLeft = (le == 0 ? new UnreliableSensor(Direction.LEFT)
				: new ReliableSensor(Direction.LEFT));
		reliableSensorRight = (ri == 0 ? new UnreliableSensor(Direction.RIGHT)
				: new ReliableSensor(Direction.RIGHT));
		reliableSensorBackward = (bw == 0 ? new UnreliableSensor(Direction.BACKWARD)
				: new ReliableSensor(Direction.BACKWARD));
	}
	
	
	/**
	 * Method starts a concurrent, independent failure and repair
	 * process that makes the sensor fail and repair itself.
	 * 
	 * @param direction the direction the sensor is mounted on the robot
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures,
			int meanTimeToRepair) throws UnsupportedOperationException {
		try {
			switch (direction) {
				case FORWARD:
					reliableSensorForward.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
					break;
				case LEFT:
					reliableSensorLeft.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
					break;
				case RIGHT:
					reliableSensorRight.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
					break;
				case BACKWARD:
					reliableSensorBackward.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
					break;
			}
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException();
		}
	}
	
	
	/**
	 * This method stops a failure and repair process and
	 * leaves the sensor in an operational state.
	 * 
	 * Intended use: If called after starting a process, this method
	 * will stop the process as soon as the sensor is operational.
	 * 
	 * If called with no running failure and repair process, 
	 * the method will return an UnsupportedOperationException.
	 * 
	 * @param direction the direction the sensor is mounted on the robot
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		try {
			switch (direction) {
				case FORWARD:
					reliableSensorForward.stopFailureAndRepairProcess();
					break;
				case LEFT:
					reliableSensorLeft.stopFailureAndRepairProcess();
					break;
				case RIGHT:
					reliableSensorRight.stopFailureAndRepairProcess();
					break;
				case BACKWARD:
					reliableSensorBackward.stopFailureAndRepairProcess();
					break;
			}
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException();
		}
	}
}
