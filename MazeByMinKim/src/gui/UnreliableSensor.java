package gui;

import gui.Robot.Direction;

/**
 * 
 * @author Min Kim
 * 
 * This class has the responsibility to get the robot's current
 * position and figure out how far the robot is to a wall from
 * the current position and the direction; however, it is subject
 * to sudden failure and at some point a repair operation that brings
 * the sensor back into an operational state.
 * 
 * This class inherits ReliableRobot and collaborates with floor plan of Maze to
 * measure distances towards obstacles, RepairProcess, RobotDriver(either WallFollower or Wizard),
 * and Robot.
 */

public class UnreliableSensor extends ReliableSensor {
	
	protected Thread repairCycle;
	
	public UnreliableSensor() {
		super();
	}
	
	public UnreliableSensor(Direction direction) {
		super(direction);
	}
	
	/**
	 * Method starts a concurrent, independent failure and repair
	 * process that makes the sensor fail and repair itself.
	 * 
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
		throws UnsupportedOperationException {
		try {
			//initiate the repair cycle
			RepairCycle cycle = new RepairCycle(meanTimeBetweenFailures, meanTimeToRepair, this);
			repairCycle = new Thread(cycle);
			repairCycle.start();
		} catch (Exception e) {
			repairCycle = null;
		}
	}
	
	/**
	 * Method stops a failure and repair process and
	 * leaves the sensor in an operational state.
	 * 
	 * Intended use: If called after starting a process, this method
	 * will stop the process as soon as the sensor is operational.
	 * 
	 * If called with no running failure and repair process, 
	 * the method will return an UnsupportedOperationException.
	 * 
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		if (repairCycle != null && repairCycle.isAlive()) {
			try {
				repairCycle.interrupt();
				isOperational = true;
				repairCycle = null;
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
