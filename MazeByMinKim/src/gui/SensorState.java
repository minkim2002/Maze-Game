package gui;

/**
 * 
 * @author Min Kim
 * This interface offers a simple layout for each sensor state class
 *
 */
public interface SensorState {
	
	/**
	 * Chooses which action to take next depends on the state of a sensor.
	 */
	public boolean nextMove();
}
