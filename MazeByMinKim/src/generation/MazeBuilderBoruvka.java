package generation;

import java.awt.Dimension;
import java.awt.print.Printable;
import java.nio.channels.AlreadyConnectedException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;
import java.util.logging.Logger;

import javax.imageio.event.IIOReadUpdateListener;

import org.hamcrest.core.Is;
import org.junit.internal.runners.model.EachTestNotifier;




public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderPrim.class.getName());

	public MazeBuilderBoruvka() {
		super();
		LOGGER.config("Using Boruvka's algorithm to generate maze.");
	}
	
	int[][][] edgeWeight;
	Set<Integer> connected;
	ArrayList<Set<Integer>> path;
	
	@Override
	public void generatePathways() {
		
		// Expanding the MST till all cells are connected.
				// for each round, fi nd a minimum cost path for each cell, then connect the cells
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
		edgeWeight = new int [width][height][5];
		initialization(edgeWeight);
		
		//final Set<Integer> connected  = new HashSet<>();
		final ArrayList<Set<Integer>> path = new ArrayList<>();
		for (int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				Set<Integer> connected = new HashSet<>();
				connected.add(edgeWeight[i][j][0]);
				
				path.add(connected);
				
			}
		}
		
		while(path.size()>1) {
			for (Set<Integer> i : path){
				int data[] = findMinPlace(i);
				Wallboard curWallboard = new Wallboard(data[0],data[1],intToDir(data[2]));
				while(!floorplan.canTearDown(curWallboard))
				{
					data = findMinPlace(i);
					curWallboard = new Wallboard(data[0],data[1],intToDir(data[2]));
				}
				updateSet(data, i);
				floorplan.deleteWallboard(curWallboard);
				
			}
			updateList(path);
		}
		
		// in order to have a randomized algorithm,
		// we randomly select and extract a wallboard from our candidate set
		// this also reduces the set to make sure we terminate the loop
		
		
		
		//Find the minimum value for each cell, connect the cells.
		//delete wallboard from maze, note that this takes place from both directions
		//add the adjacent cell to the MST and update the list of candidates		
		// note that each wallboard can get added to the list of candidates at most once. 
		

		// when we run out of candidates, we can't expand our MST any further for lack 
		// of wallboards that we are allowed to tear down and that would lead to a cell
		// that is not part of the MST yet.
		// So this must be it.

	}
	
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		int weight;
		int cdNum = dirToInt(cd);
		
		if(edgeWeight[x][y][cdNum]!=0) {
			weight = edgeWeight[x][y][cdNum];
		}else {
			Random random = new Random();
			weight = random.nextInt(9)+1;
		}
		return weight;
		
	}
	
	public void initialization(int[][][] data) {
		int cellIndx = 0;
		for(int i = 0; i<width; i++) {
			for(int j = 0; j<height; j++) {
				for(int k = 0; k<=4; k++) {
					if(k==0) {
						edgeWeight[i][j][k]= cellIndx;
						cellIndx++;
					}
					else if((k==2 || k==4) && !(floorplan.isPartOfBorder(new Wallboard(i, j, intToDir(k))))) {
						edgeWeight[i][j][k]=  getEdgeWeight(i, j, intToDir(k));
					}else if(k==1 && !(floorplan.isPartOfBorder(new Wallboard(i, j, intToDir(k))))) {
						edgeWeight[i][j][k]= edgeWeight[i][j-1][k]; 
					}else if(k==3 && !(floorplan.isPartOfBorder(new Wallboard(i, j, intToDir(k))))) {
						edgeWeight[i][j][k]= edgeWeight[i-1][j][k]; 
					}
				}
			}
		}
	}
	
	public int[] findMinPlace (Set<Integer> a){
		int min = findMin(a);
		int[] dimension = new int [3];
		ArrayList<int[]> candidates = new ArrayList<int[]>();
		for(int i :a ) {
			int[] data = indxTodim(i);
			for(int k = 1; k<=4; k++) {
				int edge = getEdgeWeight(data[0], data[1], intToDir(k));
				if(edge==min) {
					dimension[0] = data[0];
					dimension[1] = data[1];
					dimension[2] = k;
					edgeWeight[data[0]][data[1]][k] = 11;
					candidates.add(dimension);
				}
			}
		}
		Random random = new Random();
		int index=0;
		if(!candidates.isEmpty()) {
			index=random.nextInt(candidates.size());
		}
		if(candidates.isEmpty()) {
			return dimension;
		}else {
			return candidates.get(index);
		}
	}
	
	private int findMin(Set<Integer> a) {
		int min = 11;
		int edge;
		for(int i : a) {
			int[] data = indxTodim(i);
			
			for(int k = 1; k<=4; k++) {
				edge = getEdgeWeight(data[0], data[1], intToDir(k));
				if(edge<min) {
					min = edge;
				}
			}
		}
	
		return min;
		
	}
	
	private int[] indxTodim(int a){
		int[] data = new int[2];
		data[0] = a % width;
		data[1] = a / width;
		return data;
	}
	
	private int dimToindx(int[] a) {
		return a[1] * width + a[0];
	}
	
	public int dirToInt(CardinalDirection cd) {
		
		if(cd.equals(CardinalDirection.North)){
			return 1;
		}
		if(cd.equals(CardinalDirection.East)){
			return 2;
		}
		if(cd.equals(CardinalDirection.West)){
			return 3;
		}
		if(cd.equals(CardinalDirection.South)){
			return 4;
		}
		return 0;
	}
	
	public CardinalDirection intToDir(int drInt) {
		
		if(drInt == 1){
			return CardinalDirection.North;
		}
		if(drInt == 2){
			return CardinalDirection.East;
		}
		if(drInt == 3){
			return CardinalDirection.West;
		}
		if(drInt == 4){
			return CardinalDirection.South;
		}
		return CardinalDirection.North;
	}
	
	public void updateSet(int[] a, Set<Integer> b) {
		int indx = dimToindx(a);
		if(a[2]==1) {
			b.add(indx-width);
		}
		else if (a[2]==2) {
			b.add(indx+1);
		}
		else if (a[2]==3) {
			b.add(indx-1);
		}else {
			b.add(indx+width);
		}
	}
	public void updateList(ArrayList<Set<Integer>> a) {
		for(int i = 0; i<a.size()-1; i++) {
			for(int j = 1; j<a.size(); j++) {
				if(isDuplicate(a.get(i), a.get(j))){
					for (int x : a.get(j)){
						a.get(i).add(x);
					}
					a.remove(j);
				}
			}
		}
	}
	private boolean isDuplicate(Set<Integer> a, Set<Integer> b) {
		for(int i : a){
			for(int j : b){
				if(i==j) {
					return true;
				}
			}
		}return false;
	}
}
	
