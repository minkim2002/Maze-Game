package gui;

/**
 * 
 * @author Min Kim
 * 
 * This class is in charge of the process of repair of the sensor.
 * It is a helper method of UnreliableSensor.
 *
 */

public class RepairCycle implements Runnable {
	protected int operatingTime;
	protected int repairTime;
	protected UnreliableSensor unreliableSensor;

	public RepairCycle(int operationgTime, int repairTime, UnreliableSensor sensor) {
		this.operatingTime = operationgTime;
		this.repairTime = repairTime;
		unreliableSensor = sensor;
	}
	
	/**
	 * Method carrying out the repair process
	 */
	@Override
	public void run() {
		try {
			while(true) {
				// wait for the sensor to fail 
				Thread.sleep(operatingTime);
				// sensor failed
				unreliableSensor.isOperational = false;
				
				// wait for the sensor to be fixed
				Thread.sleep(repairTime);
				// sensor is working again
				unreliableSensor.isOperational = true;
			}
		} catch (InterruptedException e) {
			System.out.println("process interrupted");
			return; 
		}
		
	}

}
