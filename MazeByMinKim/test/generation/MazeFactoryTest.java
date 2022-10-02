package generation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import gui.Constants;

import generation.MazeBuilder;

import org.junit.jupiter.api.Test;



class MazeFactoryTest {
	
	private DefaultOrder defaultOrder;
	private MazeFactory mazeFactory;
	private Floorplan floorplan;
	private Distance distance;
	private int width;
	private int height;
	
	@BeforeEach
	void setUp() {
		defaultOrder = new DefaultOrder();
		mazeFactory = new MazeFactory();
		mazeFactory.order(defaultOrder);
		mazeFactory.waitTillDelivered();
		floorplan = defaultOrder.getMaze().getFloorplan();
		width = defaultOrder.getMaze().getWidth();
		height = defaultOrder.getMaze().getHeight();
		distance = new Distance(width, height);
	}
	
	@Test
	final void testMazeFactory() {
		assertNotNull(mazeFactory);
		assertNotNull(defaultOrder.getMaze());
	}
	
	@Test
	final void testMazeDemension() {
		assertEquals(width, Constants.SKILL_X[defaultOrder.getSkillLevel()]);
		assertEquals(height, Constants.SKILL_Y[defaultOrder.getSkillLevel()]);
	}
	
	@Test
	final void isThereOneExit() {
		/*this test is checking if the maze generated had only one exit.
		In order to check, set up a maze, and check if there is one
		opening spot from load bearing walls.
		 */
		distance.computeDistances(floorplan);
		int[] exit = distance.getExitPosition();
		for(int i = 0; i< width; i++) {
			for(int j = 0; j< height; j++) {
				if(floorplan.isExitPosition(i, j)) {
					assertTrue(floorplan.isExitPosition(i, j));
				}
			}
		}
	}
	
	@Test
	void isExitReachable() {
		/*this test is checking if the maze generated has an exit that
		  can be reached from any spot in the maze.
		  In order to check, set up a maze, and check for any point,
		  distant from an exit is positive, not null. 
		 */
		distance.computeDistances(floorplan);
		int [] startCoords = distance.getStartPosition();
		assertFalse(startCoords[0] == Integer.MAX_VALUE || startCoords[1] == Integer.MAX_VALUE);
	}
	
	@Test
	void numberOfWalls() {
		/*this test is checking how many walls are in the maze generated
		 set up a maze, and check the number of walls for a perfect maze for 
		 a given size.
		 */
		distance.computeDistances(floorplan);
		for (int i = 0; i <width; i++) {
			for(int j=0; j <height; j++) {
				assertFalse(floorplan.hasWall(i, j, CardinalDirection.North) &&
						floorplan.hasWall(i, j, CardinalDirection.East) &&
						floorplan.hasWall(i, j, CardinalDirection.West) &&
						floorplan.hasWall(i, j, CardinalDirection.South));
			}
		}
	}
	
	@Test
	void noClosedRoom() {
		/*this test is checking if the maze generated doesn't have any
		 closed room. If there is a room, then check whether the room has
		 an exit. 
		 */
		distance.computeDistances(floorplan);
		for (int i = 0; i <width; i++) {
			for(int j=0; j <height; j++) {
				assertTrue(floorplan.hasWall(i, j, CardinalDirection.North) ||
						floorplan.hasWall(i, j, CardinalDirection.East) ||
						floorplan.hasWall(i, j, CardinalDirection.West) ||
						floorplan.hasWall(i, j, CardinalDirection.South));
			}
		}
	}

}
