package Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import BordPlanning.Board;
import BordPlanning.ComputerPlayer;
import BordPlanning.Mark;
import BordPlanning.SmartStrategy;

public class TestSmart {
	Board board;
	SmartStrategy smart;
	ComputerPlayer com;

	@Before
	public void setUp(){
		board = new Board();
		smart = new SmartStrategy();
		com = new ComputerPlayer(Mark.XX,smart);
	}

	@Test
	public void testRows() {
		board.setField(0,0, 0, Mark.OO);
		board.setField(0,1, 0, Mark.OO);
		board.setField(0,2, 0, Mark.OO);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(0,3, 0),Mark.XX);
	}
	@Test
	public void testColumn() {
		board.setField(0,0, 0, Mark.OO);
		board.setField(0,0, 1, Mark.OO);
		board.setField(0,0, 2, Mark.OO);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(0,0, 3),Mark.XX);
	}
	@Test
	public void testDiagonalXY() {
		board.setField(0,0, 0, Mark.OO);
		board.setField(0,1, 1, Mark.OO);
		board.setField(0,2, 2, Mark.OO);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(0,3, 3),Mark.XX);
	}
	@Test
	public void testDiagonalXYMinus() {
		board.setField(0,0, 3, Mark.OO);
		board.setField(0,1, 2, Mark.OO);
		board.setField(0,2, 1, Mark.OO);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(0,3, 0),Mark.XX);
	}
	@Test
	public void testDiagonalZY() {
		board.setField(0,0, 0, Mark.OO);
		board.setField(0,1, 0, Mark.XX);
		board.setField(1,1, 0, Mark.OO);
		board.setField(0,2, 0, Mark.XX);
		board.setField(1,2, 0, Mark.XX);
		board.setField(2,2, 0, Mark.OO);
		board.setField(0,3, 0, Mark.XX);
		board.setField(1,3, 0, Mark.XX);
		board.setField(2,3, 0, Mark.OO);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(3,3, 0),Mark.XX);
	}
	@Test
	public void testDiagonalZX() {
		board.setField(0,0, 0, Mark.OO);
		board.setField(0,0, 1, Mark.XX);
		board.setField(1,0, 1, Mark.OO);
		board.setField(0,0, 2, Mark.XX);
		board.setField(1,0, 2, Mark.XX);
		board.setField(2,0, 2, Mark.OO);
		board.setField(0,0, 3, Mark.XX);
		board.setField(1,0, 3, Mark.XX);
		board.setField(2,0, 3, Mark.OO);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(3,0, 3),Mark.XX);
	}
	@Test
	public void testDiagonalYZPlus() {
		board.setField(0,0, 0, Mark.OO);
		board.setField(0,1, 1, Mark.XX);
		board.setField(1,1, 1, Mark.OO);
		board.setField(0,2, 2, Mark.XX);
		board.setField(1,2, 2, Mark.XX);
		board.setField(2,2, 2, Mark.OO);
		board.setField(0,3, 3, Mark.XX);
		board.setField(1,3, 3, Mark.XX);
		board.setField(2,3, 3, Mark.OO);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(3,3, 3),Mark.XX);
	}
	@Test
	public void testDiagonalYZMinus() {
		board.setField(0,0, 3, Mark.OO);
		board.setField(0,1, 2, Mark.XX);
		board.setField(1,1, 2, Mark.OO);
		board.setField(0,2, 1, Mark.XX);
		board.setField(1,2, 1, Mark.XX);
		board.setField(2,2, 1, Mark.OO);
		board.setField(0,3, 0, Mark.XX);
		board.setField(1,3, 0, Mark.XX);
		board.setField(2,3, 0, Mark.OO);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(3,3, 0),Mark.XX);
	}
	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	
	@Test
	public void testRowSmart() {
		board.setField(0, 0, Mark.XX);
		board.setField(1, 0, Mark.XX);
		board.setField(2, 0, Mark.XX);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(0,0,3),Mark.XX);
	}
	
	@Test
	public void testColumnSmart() {
		board.setField(0,0, 0, Mark.XX);
		board.setField(0,1, 0, Mark.XX);
		board.setField(0,2, 0, Mark.XX);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(0,3, 0),Mark.XX);
	}
	@Test
	public void testDiagonalXYSmart() {
		board.setField(0,0, 0, Mark.XX);
		board.setField(0,1, 1, Mark.XX);
		board.setField(0,2, 2, Mark.XX);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(0,3, 3),Mark.XX);
	}
	@Test
	public void testDiagonalXYMinusSmart() {
		board.setField(0,0, 3, Mark.XX);
		board.setField(0,1, 2, Mark.XX);
		board.setField(0,2, 1, Mark.XX);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(0,3, 0),Mark.XX);
	}
	@Test
	public void testDiagonalZYSmart() {
		board.setField(0,0, 0, Mark.XX);
		board.setField(0,1, 0, Mark.OO);
		board.setField(1,1, 0, Mark.XX);
		board.setField(0,2, 0, Mark.OO);
		board.setField(1,2, 0, Mark.OO);
		board.setField(2,2, 0, Mark.XX);
		board.setField(0,3, 0, Mark.OO);
		board.setField(1,3, 0, Mark.OO);
		board.setField(2,3, 0, Mark.XX);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(3,3, 0),Mark.XX);
	}
	@Test
	public void testDiagonalZXSmart() {
		board.setField(0,0, 0, Mark.XX);
		board.setField(0,0, 1, Mark.OO);
		board.setField(1,0, 1, Mark.XX);
		board.setField(0,0, 2, Mark.OO);
		board.setField(1,0, 2, Mark.OO);
		board.setField(2,0, 2, Mark.XX);
		board.setField(0,0, 3, Mark.OO);
		board.setField(1,0, 3, Mark.OO);
		board.setField(2,0, 3, Mark.XX);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(3,0, 3),Mark.XX);
	}
	@Test
	public void testDiagonalYZPlusSmart() {
		board.setField(0,0, 0, Mark.XX);
		board.setField(0,1, 1, Mark.OO);
		board.setField(1,1, 1, Mark.XX);
		board.setField(0,2, 2, Mark.OO);
		board.setField(1,2, 2, Mark.XX);
		board.setField(2,2, 2, Mark.XX);
		board.setField(0,3, 3, Mark.OO);
		board.setField(1,3, 3, Mark.OO);
		board.setField(2,3, 3, Mark.XX);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(3,3, 3),Mark.XX);
	}
	@Test
	public void testDiagonalYZMinusSmart() {
		board.setField(0,0, 3, Mark.XX);
		board.setField(0,1, 2, Mark.OO);
		board.setField(1,1, 2, Mark.XX);
		board.setField(0,2, 1, Mark.OO);
		board.setField(1,2, 1, Mark.OO);
		board.setField(2,2, 1, Mark.XX);
		board.setField(0,3, 0, Mark.OO);
		board.setField(1,3, 0, Mark.OO);
		board.setField(2,3, 0, Mark.XX);
		com.makeMove(board);
		smart.determineMove(board, Mark.XX);
		assertEquals(board.getField(3,3, 0),Mark.XX);
	}
}
