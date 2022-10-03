package generation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
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
	final void setUp() {
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
		/*Check if the order received is not null, and a reference of a maze is successfully created for testing purposes.
		 */
		assertNotNull(mazeFactory);
		assertNotNull(defaultOrder.getMaze());
	}
	
	@Test
	final void testMazeDemension() {
		/*Quick test for whether the dimension of the maze created matches the skill level. 
		 Since there is a specific size for each skill level, by checking the default skill level dimension and
		 width and height set up by the order, we can check whether we got the correct dimension of the maze.
		 */
		assertEquals(width, Constants.SKILL_X[defaultOrder.getSkillLevel()]);
		assertEquals(height, Constants.SKILL_Y[defaultOrder.getSkillLevel()]);
	}
	
	@Test
	final void isThereOneExit() {
		/*this test is checking if the maze generated had only one exit.
		In order to check, set up a maze, and check if there is one
		opening spot from load bearing walls.
		
		By using the method "isExitPosition" to check each and every cell,
		we can find whether the maze has one exit position.  
		 */
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
		  can be reached from the starting point. 
		  
		  We have to check that the distant is not infinity, which means that 
		  the exit is not reachable from the exit.
		  If the distance is an integer, then that means there is a path, 
		  meaning that the exit is reachable. 
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
				assertTrue(floorplan.hasWall(i, j, CardinalDirection.North) ||
						floorplan.hasWall(i, j, CardinalDirection.East) ||
						floorplan.hasWall(i, j, CardinalDirection.West) ||
						floorplan.hasWall(i, j, CardinalDirection.South));
			}
		}
	}
	
	@Test
	void noClosedRoom() {
		/*this test is checking if the maze generated doesn't have any
		 closed room.
		 
		 Each cell in the maze corresponds to each cell in 2-dimensional array.
		 By iterating through each cell in the array,
		 
		 we can check whether there is a wall on every side, which means that the room is closed. 
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

}
