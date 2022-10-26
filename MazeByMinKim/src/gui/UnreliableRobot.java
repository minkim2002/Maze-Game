package gui;

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
		reliableSensorForward = (le == 0 ? new UnreliableSensor(Direction.FORWARD)
				: new ReliableSensor(Direction.FORWARD));
		reliableSensorLeft = (le == 0 ? new UnreliableSensor(Direction.LEFT)
				: new ReliableSensor(Direction.LEFT));
		reliableSensorRight = (le == 0 ? new UnreliableSensor(Direction.RIGHT)
				: new ReliableSensor(Direction.RIGHT));
		reliableSensorBackward = (le == 0 ? new UnreliableSensor(Direction.BACKWARD)
				: new ReliableSensor(Direction.BACKWARD));
	}
}
