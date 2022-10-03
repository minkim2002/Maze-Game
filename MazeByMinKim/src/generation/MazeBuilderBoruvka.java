package generation;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderPrim.class.getName());

	public MazeBuilderBoruvka() {
		super();
		LOGGER.config("Using Boruvka's algorithm to generate maze.");
	}
	
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		return 0;
	}
	
	@Override
	public void generatePathways() {
		
		// Expanding the MST till all cells are connected.
				// for each round, find a minimum cost path for each cell, then connect the cells
				// through the minimum costs.
				// While each and every cell is not visited, iterate the process.
				//
				// How does this handle rooms, as the algorithm seems to break into rooms, 
				// but for the lack of wallboards it will not expand through cells that
				// are in the middle of the room and have no adjacent wallboards at all.
				// Of course, these cells are naturally connected to cells adjacent to 
				// cells all other cells inside the room. So it is sufficient if at 
				// least one cell in the room is connected with the MST.
				// This will happen at wallboards that are potential doors, i.e. not 
				// marked as borderwalls for a room. The algorith will eventually reach 
				// cell at a door outside of the room and recognize that the adjacent cell
				// inside the room has not been visited yet and this will add the wallboard
				// to the list of candidates.
				// Implication: 
				// After termination, many cells will be marked as visited, but some cells
				// inside a room may not be marked as such although they belong to the MST.
				//
		
		// in order to have a randomized algorithm,
		// we randomly select and extract a wallboard from our candidate set
		// this also reduces the set to make sure we terminate the loop
		
		//Assign a random value to every edges.
		
		//Find the minimum value for each cell, connect the cells.
		//delete wallboard from maze, note that this takes place from both directions
		//add the adjacent cell to the MST and update the list of candidates		
		// note that each wallboard can get added to the list of candidates at most once. 
		

		// when we run out of candidates, we can't expand our MST any further for lack 
		// of wallboards that we are allowed to tear down and that would lead to a cell
		// that is not part of the MST yet.
		// So this must be it.

	}
	
	/**
	 * Add the given cell (x,y) to the MST by marking it as visited and add its wallboards
	 * that lead to cells outside of the MST to the list of candidates (unless they are borderwalls).
	 * @param x the x coordinate of interest
	 * @param y the y coordinate of interest
	 * @param candidates the new elements should be added to, must not be null
	 */
	protected void addCellToMST(int x, int y, final ArrayList<Wallboard> candidates) {
		floorplan.setCellAsVisited(x, y); // the flag is never reset, so this ensure we never go to (x,y) again
		updateListOfWallboards(x, y, candidates); // checks to see if it has wallboards to new cells, if it does it adds them to the list
	}
	
	
	/**
	 * Pick a random position in the list of candidates, remove the candidate from the list and return it
	 * @param candidates is the list of candidates to randomly remove a wall board from
	 * @return candidate from the list, randomly chosen
	 */
	private Wallboard extractWallboardFromCandidateSetRandomly(final ArrayList<Wallboard> candidates) {
		return candidates.remove(random.nextIntWithinInterval(0, candidates.size()-1)); 
	}
	

	/**
	 * Updates a list of all wallboards that could be removed from the maze based on wallboards towards new cells.
	 * For the given x, y coordinates, one checks all four directions
	 * and for the ones where one can tear down a wallboard, a 
	 * corresponding wallboard is added to the list of wallboards.
	 * @param x the x coordinate of interest
	 * @param y the y coordinate of interest
	 * @param wallboards the new elements should be added to, must not be null
	 */
	private void updateListOfWallboards(int x, int y, ArrayList<Wallboard> wallboards) {
		if (reusedWallboard == null) {
			reusedWallboard = new Wallboard(x, y, CardinalDirection.East) ;
		}
		for (CardinalDirection cd : CardinalDirection.values()) {
			reusedWallboard.setLocationDirection(x, y, cd);
			if (floorplan.canTearDown(reusedWallboard)) // 
			{
				wallboards.add(new Wallboard(x, y, cd));
			}
		}
	}
	// exclusively used in updateListOfWallboards
	Wallboard reusedWallboard; // reuse a wallboard in updateListOfWallboards to avoid repeated object instantiation


}
