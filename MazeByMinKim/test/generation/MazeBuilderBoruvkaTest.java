package generation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import generation.Order.Builder;

/**  Boruvka Test: Test Boruvka's algorithms and methods used to implement the algorithm.
 * 
 * @author Min Kim
 *
 */

public class MazeBuilderBoruvkaTest extends MazeFactoryTest {
	

	private int height;
	private MazeBuilderBoruvka mazeBuilderBoruvka;
	
	private Factory testFactory;
	private Order mazeOrder;
	
	@BeforeEach
	final void setUp() {
		
		mazeBuilderBoruvka = new MazeBuilderBoruvka();
		testFactory = new MazeFactory();
		DefaultOrder baseOrder = new DefaultOrder();
		mazeOrder = new DefaultOrder(baseOrder.getSkillLevel(), Builder.Boruvka, baseOrder.isPerfect(), baseOrder.getSeed());
		
		testFactory.order(mazeOrder);
		testFactory.waitTillDelivered();
		
	}
	
	/**  Test whether a wallboard is assigned a correct value from 1 to 10.
	 */
	 @Test
	 public final void testEdgeWeight(){
		int weight = mazeBuilderBoruvka.getEdgeWeight(1,3,CardinalDirection.North);
		if (weight>0 && weight<11) {
			weight = 0;
		}
		assertEquals(weight, 0);
	 }
	 
	 /**  Test whether an index of a cell is correctly switched to a coordinate of a cell.
	  */
	 @Test
	 public final void testIndxtoDim() {
		 int a = 10;
		 int[] data = new int[2];
		 data = mazeBuilderBoruvka.indxTodim(a);
		 assertEquals(a/height, data[1]);
	 }
	 
	 /**  Test whether a coordinate of a cell is successfully switched to an index of a cell.
	  */
	 @Test
	 public final void testDimtoIndx(){
		 int a = 10;
		 int[] data = new int[2];
		 data = mazeBuilderBoruvka.indxTodim(a);
		 assertEquals(mazeBuilderBoruvka.dimToindx(data), 10);
	 }
	 
	 /**  Test whether a direction of a wallboard is correctly switched to a corresponding integer.
	  */
	 @Test
	 public final void testDirToInt() {
		 CardinalDirection cd = CardinalDirection.East;
		 assertEquals(mazeBuilderBoruvka.dirToInt(cd), 2);
	 }
	 
	 /**  Test whether an integer value of a direction is successfully converted into a direction.
	  */
	 @Test
	 public final void testIntToDir() {
		 CardinalDirection cd = CardinalDirection.East;
		 int x = 2;
		 assertEquals(mazeBuilderBoruvka.intToDir(x), cd);
	 }
	 
	 /**  Test whether a set successfully include an another index of a cell in a right direction. 
	  */
	 @Test
	 public final void testUpdateSet() {
		 Set<Integer> b = new HashSet<>();
		 int[] data = new int[3];
		 data[2] = 2;
		 int index = 5;
		 mazeBuilderBoruvka.updateSet(data, b);
		 int test=0;
		 for (int i: b) {
			 test = i;
		 }
		 assertEquals(index+1, test);
		 
	 }
	 
	 /**  Test whether a list successfully merge two sets and delete one when two sets have a same value. 
	  */
	 @Test
	 public final void testUpdateList(){
		 ArrayList<Set<Integer>> data = new ArrayList<>();
		 Set<Integer> a = new HashSet<>();
		 a.add(1);
		 a.add(3);
		 a.add(8);
		 Set<Integer> b = new HashSet<>();
		 b.add(3);
		 b.add(10);
		 b.add(5);
		 data.add(a);
		 data.add(b);
		 mazeBuilderBoruvka.updateList(data);
		 assertEquals(1, data.size());
		 
	 }
	 
	 /**  Test whether the method can point out when two sets have the same value.
	  */
	 @Test
	 public final void testIsDuplicate() {
		 Set<Integer> a = new HashSet<>();
		 a.add(1);
		 a.add(3);
		 a.add(8);
		 Set<Integer> b = new HashSet<>();
		 b.add(3);
		 b.add(10);
		 b.add(5);
		 assertEquals(mazeBuilderBoruvka.isDuplicate(a, b), true);
	 }
	 
	 /**  Test whether the method can find a minimum value from the set given
	  */
	 @Test
	 public final void findMin() {
		 Set<Integer> a = new HashSet<>();
		 a.add(1);
		 a.add(3);
		 a.add(8);
		 int min = mazeBuilderBoruvka.findMin(a);
		 assertEquals(1, min);
	 }
}
