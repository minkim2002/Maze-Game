package generation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import generation.Order.Builder;

class MazeBuilderBoruvkaTest extends MazeFactoryTest {
	

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
	
	 @Test
	 final void testEdgeWeight(){
		int weight = mazeBuilderBoruvka.getEdgeWeight(1,3,CardinalDirection.North);
		if (weight>0 && weight<11) {
			weight = 0;
		}
		assertEquals(weight, 0);
	 }
	 
	 @Test
	 final void testIndxtoDim() {
		 int a = 10;
		 int[] data = new int[2];
		 data = mazeBuilderBoruvka.indxTodim(a);
		 assertEquals(a/height, data[1]);
	 }
	 
	 @Test
	 final void testDimtoIndx(){
		 int a = 10;
		 int[] data = new int[2];
		 data = mazeBuilderBoruvka.indxTodim(a);
		 assertEquals(mazeBuilderBoruvka.dimToindx(data), 10);
	 }
	 
	 @Test
	 final void testDirToInt() {
		 CardinalDirection cd = CardinalDirection.East;
		 assertEquals(mazeBuilderBoruvka.dirToInt(cd), 2);
	 }
	 
	 @Test
	 final void testIntToDir() {
		 CardinalDirection cd = CardinalDirection.East;
		 int x = 2;
		 assertEquals(mazeBuilderBoruvka.intToDir(x), cd);
	 }
	 
	 @Test
	 final void testUpdateSet() {
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
	 
	 @Test
	 final void testUpdateList(){
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
	 
	 @Test
	 final void testIsDuplicate() {
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
	 
	 @Test
	 final void findMin() {
		 Set<Integer> a = new HashSet<>();
		 a.add(1);
		 a.add(3);
		 a.add(8);
		 int min = mazeBuilderBoruvka.findMin(a);
		 assertEquals(1, min);
	 }
}
