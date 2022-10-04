package generation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generation.Order.Builder;

class MazeBuilderBoruvkaTest extends MazeFactoryTest {
	
	private DefaultOrder defaultOrder;
	private MazeFactory mazeFactory;
	private Floorplan floorplan;
	private Distance distance;
	private int width;
	private int height;
	
	@BeforeEach
	final void setUp() {
		defaultOrder = new DefaultOrder(0, Builder.Boruvka, true, 13);
		mazeFactory = new MazeFactory();
		mazeFactory.order(defaultOrder);
		mazeFactory.waitTillDelivered();
		floorplan = defaultOrder.getMaze().getFloorplan();
		width = defaultOrder.getMaze().getWidth();
		height = defaultOrder.getMaze().getHeight();
		distance = new Distance(width, height);
	}
	
	 final void testEdgeWeight(int x, int y, CardinalDirection cd) {
		 
	 }

}
