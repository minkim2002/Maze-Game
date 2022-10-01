package generation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MazeFactoryTest {

	@Test
	void isThereOneExit() {
		/*this test is checking if the maze generated had only one exit.
		In order to check, set up a maze, and check if there is one
		opening spot from load bearing walls.
		 */
	}
	
	void isExitReachable() {
		/*this test is checking if the maze generated has an exit that
		  can be reached from any spot in the maze.
		  In order to check, set up a maze, and check for any point,
		  distant from an exit is positive, not null. 
		 */
	}
	
	void numberOfWalls() {
		/*this test is checking how many walls are in the maze generated
		 set up a maze, and check the number of walls for a perfect maze for 
		 a given size.
		 */
	}
	
	void noClosedRoom() {
		/*this test is checking if the maze generated doesn't have any
		 closed room.
		 */
	}

}
