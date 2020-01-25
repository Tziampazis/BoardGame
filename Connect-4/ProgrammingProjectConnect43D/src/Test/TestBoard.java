package Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import BordPlanning.Board;
import BordPlanning.Mark;

public class TestBoard {
	private static Board board; 
	
	
	@Before
//	public static void setUpBeforeClass() throws Exception {
	public void setUp() {
		board = new Board();
	}

//}

	@Test
	public void testindex() {
		assertEquals(board.index(1, 1),5);
		}
	
	@Test
	public void testiToX() {
		assertEquals(board.iToX(4),0);
		}
	
	@Test
	public void testiToY() {
		assertEquals(board.iToY(4),1);
		}
	
	@Test
	public void testDeepCopy() {
		board.setField(1, 1, Mark.XX);
		assertEquals(board.getField(0, 1, 1), board.deepCopy().getField(0, 1, 1));
		}
	
	@Test
	public void testIsField(){
		assertEquals(board.isField(3, 3),true);
		assertFalse(board.isField(4, 3));
		assertFalse(board.isField(3, 4));
		assertFalse(board.isField(3, -1));
		assertFalse(board.isField(-1, 3));
		assertEquals(board.isField(0, 0),true);
	}

	@Test
	public void testGetField(){
		board.setField(1, 1, Mark.XX);
		assertEquals(board.getField(0, 1, 1),Mark.XX);
	}
	
	@Test 
	public void testHasEmptyField(){
		for (int z = 0; z < board.getDimZ(); z++) {
			board.setField(2, 2, Mark.XX);
		}		
		assertFalse(board.hasEmptyField(2, 2));
		
		board.setField(1, 1, Mark.EMPTY);
		assertTrue(board.hasEmptyField(1, 1));
	}
	
	@Test 
	public void testIsFull(){
		board = new Board();
		assertFalse(board.isFull());
		for (int z = 0; z < 4; z++) {
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {
					board.setField(z, y, x, Mark.XX);
				}
			}
		}
		assertTrue(board.isFull());
	}
	
	@Test
	public void testHasWinY(){
		board = new Board();
		assertFalse(board.hasWinY(Mark.XX));
		for (int z = 0; z < 1; z++) {
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 1; x++) {
					board.setField(z, y, x, Mark.XX);
				}
			}
		}
	assertTrue(board.hasWinY(Mark.XX));
	}
	
	@Test
	public void testHasWinX(){
		board = new Board();
		assertFalse(board.hasWinX(Mark.XX));
		for (int z = 0; z < 1; z++) {
			for (int y = 0; y < 1; y++) {
				for (int x = 0; x < 4; x++) {
					board.setField(z, y, x, Mark.XX);
				}
			}
		}
	assertTrue(board.hasWinX(Mark.XX));
	}
	
	@Test
	public void testHasWinZ(){
		board = new Board();
		assertFalse(board.hasWinZ(Mark.XX));
		for (int z = 0; z < 4; z++) {
			for (int y = 0; y < 1; y++) {
				for (int x = 0; x < 1; x++) {
					board.setField(z, y, x, Mark.XX);
				}
			}
		}
	assertFalse(board.hasWinZ(Mark.OO));
	assertTrue(board.hasWinZ(Mark.XX));
	}
	
	@Test
	public void testHasDiagonalXY(){
		board = new Board();
		assertFalse(board.hasDiagonalXY(Mark.XX));
		assertFalse(board.hasDiagonalXY(Mark.OO));
		board.setField(0, 0, Mark.XX);
		board.setField(1, 1, Mark.XX);
		board.setField(2, 2, Mark.XX);
		board.setField(3, 3, Mark.XX);
		assertFalse(board.hasDiagonalXY(Mark.OO));
		assertTrue(board.hasDiagonalXY(Mark.XX));
		
		board.setField(3, 0, Mark.XX);
		board.setField(2, 1, Mark.XX);
		board.setField(0, 3, Mark.XX);
		board.setField(1, 2, Mark.XX);
		assertFalse(board.hasDiagonalXY(Mark.OO));
		assertTrue(board.hasDiagonalXY(Mark.XX));
	}
	
	@Test
	public void testHasDiagonalZX(){
		board = new Board();
		assertFalse(board.hasDiagonalZX(Mark.XX));
		board.setField(0, 0, 0, Mark.XX);
		board.setField(1, 0, 1, Mark.XX);
		board.setField(2, 0, 2, Mark.XX);
		board.setField(3, 0, 3, Mark.XX);
		assertFalse(board.hasDiagonalZX(Mark.OO));
		assertTrue(board.hasDiagonalZX(Mark.XX));
	}
	
	@Test
	public void testHasDiagonalZY(){
		board = new Board();
		assertFalse(board.hasDiagonalZY(Mark.XX));
		board.setField(0, 0, 0, Mark.XX);
		board.setField(1, 1, 0, Mark.XX);
		board.setField(2, 2, 0, Mark.XX);
		board.setField(3, 3, 0, Mark.XX);
		assertFalse(board.hasDiagonalZY(Mark.OO));
		assertTrue(board.hasDiagonalZY(Mark.XX));
	}
	
	@Test
	public void testHasDiagonalOver(){
		board = new Board();
		assertFalse(board.hasDiagonalOver(Mark.XX));
		board.setField(0, 0, 0, Mark.XX);
		board.setField(1, 1, 1, Mark.XX);
		board.setField(2, 2, 2, Mark.XX);
		board.setField(3, 3, 3, Mark.XX);
		assertFalse(board.hasDiagonalOver(Mark.OO));
		assertTrue(board.hasDiagonalOver(Mark.XX));
	}
	
	@Test 
	public void testIsWinner(){
		board = new Board();
		assertFalse(board.isWinner(Mark.XX));
		board.setField(0, 0, Mark.XX);
		board.setField(1, 1, Mark.XX);
		board.setField(2, 2, Mark.XX);
		board.setField(3, 3, Mark.XX);
		assertTrue(board.hasDiagonalXY(Mark.XX));
		assertFalse(board.isWinner(Mark.OO));
		assertTrue(board.isWinner(Mark.XX));
		board = new Board();
		board.setField(0, 0, Mark.OO);
		board.setField(0, 1, Mark.OO);
		board.setField(0, 2, Mark.OO);
		board.setField(0, 3, Mark.OO);
		assertTrue(board.hasWinY(Mark.OO));
		assertFalse(board.isWinner(Mark.XX));
		assertTrue(board.isWinner(Mark.OO));
		board = new Board();
		board.setField(0, 0, Mark.OO);
		board.setField(1, 0, Mark.OO);
		board.setField(2, 0, Mark.OO);
		board.setField(3, 0, Mark.OO);
		assertTrue(board.hasWinX(Mark.OO));
		assertFalse(board.isWinner(Mark.XX));
		assertTrue(board.isWinner(Mark.OO));
		board = new Board();
		board.setField(0,0, 0, Mark.OO);
		board.setField(1,0, 0, Mark.OO);
		board.setField(2,0, 0, Mark.OO);
		board.setField(3,0, 0, Mark.OO);
		assertTrue(board.hasWinZ(Mark.OO));
		assertFalse(board.isWinner(Mark.XX));
		assertTrue(board.isWinner(Mark.OO));
		board = new Board();
		board.setField(0,0, 0, Mark.OO);
		board.setField(1,1, 0, Mark.OO);
		board.setField(2,2, 0, Mark.OO);
		board.setField(3,3, 0, Mark.OO);
		assertTrue(board.hasDiagonalZY(Mark.OO));
		assertFalse(board.isWinner(Mark.XX));
		assertTrue(board.isWinner(Mark.OO));
		board = new Board();
		board.setField(0,0, 0, Mark.OO);
		board.setField(1,0, 1, Mark.OO);
		board.setField(2,0, 2, Mark.OO);
		board.setField(3,0, 3, Mark.OO);
		assertTrue(board.hasDiagonalZX(Mark.OO));
		assertFalse(board.isWinner(Mark.XX));
		assertTrue(board.isWinner(Mark.OO));
		board = new Board();
		board.setField(0,0, 0, Mark.OO);
		board.setField(1,1, 1, Mark.OO);
		board.setField(2,2, 2, Mark.OO);
		board.setField(3,3, 3, Mark.OO);
		assertTrue(board.hasDiagonalOver(Mark.OO));
		assertFalse(board.isWinner(Mark.XX));
		assertTrue(board.isWinner(Mark.OO));
		
	}
	
	@Test 
	public void testhasWinner(){
		board = new Board();
		assertFalse(board.isWinner(Mark.XX));
		assertFalse(board.isWinner(Mark.OO));
		board.setField(0, 0, Mark.XX);
		board.setField(1, 1, Mark.XX);
		board.setField(2, 2, Mark.XX);
		board.setField(3, 3, Mark.XX);
		assertFalse(board.isWinner(Mark.OO));
		assertTrue(board.hasWinner());
		board.setField(0, 0, Mark.OO);
		board.setField(1, 1, Mark.OO);
		board.setField(2, 2, Mark.OO);
		board.setField(3, 3, Mark.OO);
		assertFalse(board.isWinner(Mark.XX));
		assertTrue(board.hasWinner());
	}
	
	@Test
	public void testReset(){
		board.reset();
		for(int z=0;z<board.getDimZ();z++){
			for(int i=0;i<board.getDimX();i++){
				for(int y=0;y<board.getDimY();y++){
					assertEquals(board.getField(z, y, i),Mark.EMPTY);
				}
			}
		}
	}
	
	@Test
	public void testSetField(){
		for (int z = 0; z < 4; z++) {
			board.setField(0, 0,Mark.XX);
			board.setField(1, 1, Mark.OO);
			board.setField(2, 2, 2, Mark.XX);
		}
		assertEquals(board.getField(2, 2, 2),Mark.XX);
		assertEquals(board.getField(0, 1, 1),Mark.OO);
		assertEquals(board.getField(0, 0, 0),Mark.XX);
		assertEquals(board.getField(3, 3, 3), Mark.EMPTY);
		assertEquals(board.getField(1, 3, 3), Mark.EMPTY);
	}
	
	@Test 
	public void testgameOver(){
		//board = new Board();
		assertFalse(board.gameOver());
		board.setField(0, 0, Mark.XX);
		board.setField(1, 1, Mark.XX);
		board.setField(2, 2, Mark.XX);
		board.setField(3, 3, Mark.XX);
		assertTrue(board.isWinner(Mark.XX));
		assertTrue(board.gameOver());
		
	}
	
	
}
