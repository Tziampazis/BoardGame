package BordPlanning;

public class Board {
	
		// ------------------------------ fields
	// ---------------------------------------------------
	
	// the amount of columns on the board: (dimX > 0)
	protected int dimX;
	// the amount of rows on the board: (dimY > 0)
	protected int dimY;
	// the amount of floors of the board: (dimZ > 0; dimZ == -1 ? dimZ ==
	// infinity)
	protected int dimZ;
	// the amount of marks in a row needed to win a game (win < dimX || win <
	// dimY || win < dimZ)
	protected int win;
	
	// the x coordinate of the last made move:
	private int col;
	// the y coordinate of the last made move:
	private int row;
	// the z coordinate of the last made move ( height):
	private int hei;
	
	// this prints the standard measurements of the board, including x, y
	// coordinates
	private String numbering;
	
	// the board is saved in a 3 dimensional array with values for x, y and z)
	// Mark[z][y][x]
	private Mark[][][] fields;
	
	// --------------------------------- constructors
	// -----------------------------------------------
	
	/*
	 * With the input of x, y and z coordinates and the win condition a new
	 * board is created if dimZ equals -1 so there are infinite floors to play
	 * on the z list in the array should be able to have a lot of floors in
	 * there.
	 */
	/*
	 * @
	 * 
	 * @requires dimX > 0 && dimY > 0 && dimZ > 0 && winz > 0;
	 * 
	 * @ensures \forall int z,y,x; z < dimZ & z >0&& y< dimY & y >0 && x< dimX &
	 * x > 0; field.setField(z,y,x) == Mark.Empty;
	 * 
	 * @
	 */
	public Board(int diX, int diY, int diZ, int winz) {
		dimX = diX;
		dimY = diY;
		dimZ = diZ;
		win = winz;
	
		col = 0;
		row = 0;
		hei = 0;
	
		fields = new Mark[dimZ][dimY][dimX];
	
		for (int z = 0; z < dimZ; z++) {
			for (int y = 0; y < dimY; y++) {
				for (int x = 0; x < dimX; x++) {
					fields[z][y][x] = Mark.EMPTY;
				}
			}
		}
	}
	
	public Board() {
		this(4, 4, 4, 4);
	}
	
	public Board(int i) {
		this(i, i, i, i);
	}
	
	// ----------------------------------------- methods/queries
	// ---------------------------------------
	
	/**
	 * create a copy of the current board
	 * 
	 * @return a copy of the board
	 */
	/*
	 * @requires copy.Board() != null;
	 * 
	 * @ensures (\forall int z z < dimZ & z >0)&&( int y < dimY & y >0 )&&( int
	 * x < dimX & x > 0); \result.getField(z,y,x) == getField(z,y,x);
	 * 
	 * @
	 */
	public Board deepCopy() {
		Board copy = new Board(getDimZ(), getDimY(), getDimZ(), getWin());
		for (int z = 0; z < dimZ; z++) {
			for (int y = 0; y < dimY; y++) {
				for (int x = 0; x < dimX; x++) {
					copy.setField(z, y, x, this.getField(z, y, x));
				}
			}
		}
		return copy;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~ transfer i <-> (x,y) ~~~~~~~~~~~~~~~~~~~~~~
	/*
	 * convert an x, y coordinate into an index
	 */
	/*
	 * @pure
	 * 
	 * @requires x>=0 && y>=0 && dimX > 0;
	 * 
	 * @ensures \result == x + dimX *y;
	 * 
	 * @
	 */
	public int index(int x, int y) {
		return x + dimX * y;
	}
	
	/*
	 * converts an index into x coordinate
	 */
	/*
	 * @pure
	 * 
	 * @requires i >= 0 && dimX > 0;
	 * 
	 * @ensures \result == i % dimX;
	 */
	public int iToX(int i) {
		return (i % dimX);
	}
	
	/*
	 * converts an index into a Y coordinate
	 */
	/*
	 * @pure
	 * 
	 * @requires i >= 0 && dimX > 0;
	 * 
	 * @ensures \result ==i-( i % dimX)/dimX;
	 */
	public int iToY(int i) {
		return (i - (i % dimX)) / dimX;
	}
	
	/*
	 * checks whether the inputed x and y are valid coordinates
	 */
	/*
	 * @pure
	 * 
	 * @requires i >= 0 && y>= 0 && dimX > 0;
	 * 
	 * @ensures \result == (0 <= x && x < dimX && 0 <= y && y <= dimY);
	 */
	public boolean isField(int x, int y) {
		return 0 <= x && x < dimX && 0 <= y && y < dimY;
	}
	
	/*
	 * gets the Mark that was put at the inputed x, y and z value
	 */
	/*
	 * @pure
	 * 
	 * @ensures \result == Mark.MEPTY || Mark.XX || Mark.OO;
	 */
	public Mark getField(int z, int y, int x) {
		return fields[z][y][x];
	}
	
	/*
	 * checks whether the inputed x and y have a corresponding empty z value if
	 * dimZ == -1 there is always a place at a certain x and y.
	 */
	/*
	 * @pure
	 * 
	 * @requires this.isField(x,y);
	 * 
	 * @ensures \result == (this.getField(z,y,x) = Mark.Empty);
	 */
	public boolean hasEmptyField(int x, int y) {
		for (int z = 0; z < dimZ; z++) {
			if (getField(z, y, x) == Mark.EMPTY) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * checks whether all the fields in the board are filled with a mark which
	 * does not equal Mark.EMPTY.
	 */
	/*
	 * @pure
	 * 
	 * @ensures \result == \forall int z,y,x; z < dimZ & z >0 && y< dimY & y >0
	 * && x< dimX & x > 0; field.setField(z,y,x) == Mark.Empty;
	 */
	public boolean isFull() {
		for (int z = 0; z < dimZ; z++) {
			for (int y = 0; y < dimY; y++) {
				for (int x = 0; x < dimX; x++) {
					if (getField(z, y, x) == Mark.EMPTY) {
						return false;
					}
				}
			}
	
		}
		System.out.println("The board is full");
		return true;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ has winner checks
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/*
	 * checks whether the last move has made a winner in a y row
	 */
	/*
	 * @pure
	 * 
	 * @requires this.getField(z,y,x) == Mark.XX || Mark.OO
	 * 
	 * @ensures \result isWinner(m) == Mark.XX || Mark.OO
	 */
	public boolean hasWinY(Mark m) {
		int countY = 0;
	
		int yplus = row;
		int ymin = row - 1;
	
		while (yplus < dimY && getField(hei, yplus, col) == m) {
			countY++;
			yplus++;
		}
		while (ymin >= 0 && getField(hei, ymin, col) == m) {
			countY++;
			ymin--;
		}
		return countY >= win;
	}
	
	/*
	 * checks whether the last move has made a winner in an x column
	 */
	/*
	 * @pure
	 * 
	 * @requires this.getField(z,y,x) == Mark.XX || Mark.OO
	 * 
	 * @ensures \result isWinner(m) == Mark.XX || Mark.OO
	 */
	public boolean hasWinX(Mark m) {
		int countX = 0;
	
		int xplus = col;
		int xmin = col - 1;
	
		while (xplus < dimX && getField(hei, row, xplus) == m) {
			countX++;
			xplus++;
		}
		while (xmin >= 0 && getField(hei, row, xmin) == m) {
			countX++;
			xmin--;
		}
	
		return countX >= win;
	}
	
	/**
	 * checks whether the last move has made a winner in a z floors line
	 * 
	 * @param m
	 *            is the mark for which it checks whether there is a win
	 * @return boolean whether row is true
	 */
	/*
	 * @pure
	 * 
	 * @requires this.getField(z,y,x) == Mark.XX || Mark.OO
	 * 
	 * @ensures \result isWinner(m) == Mark.XX || Mark.OO
	 */
	public boolean hasWinZ(Mark m) {
		int countZ = 0;
	
		int zplus = hei;
		int zmin = hei - 1;
	
		while (zplus < dimZ && getField(zplus, row, col) == m) {
			countZ++;
			zplus++;
		}
		while (zmin >= 0 && getField(zmin, row, col) == m) {
			countZ++;
			zmin--;
		}
		return countZ >= win;
	}
	
	/**
	 * checks whether there is a diagonal that is as long or longer than the win
	 * condition
	 * 
	 * @param m
	 *            the mark of one of the players
	 * @return boolean whether diagonal >= win
	 */
	/*
	 * @pure
	 * 
	 * @requires this.getField(z,y,x) == Mark.XX || Mark.OO;
	 * 
	 * @ensures \result == isWinner(m) == Mark.XX || Mark.OO;
	 */
	public boolean hasDiagonalXY(Mark m) {
		// for the row (i, i, z)
		int count1 = 0;
	
		int xplus = col;
		int xmin = col - 1;
	
		int yplus = row;
		int ymin = row - 1;
	
		while (xplus < dimX && yplus < dimY && getField(hei, yplus, xplus) == m) {
			count1++;
			xplus++;
			yplus++;
		}
		while (xmin >= 0 && ymin >= 0 && getField(hei, ymin, xmin) == m) {
			count1++;
			xmin--;
			ymin--;
		}
		if (count1 >= win) {
			System.out.println("hasDiagonalXY (i, i, h) (XY) (" + m.toString() + "): true");
			return true;
		}
	
		// for the line (i, dimY - 1 - i, z)
		int count2 = 0;
	
		int xplus2 = col;
		int xmin2 = col - 1;
	
		int yplus2 = row + 1;
		int ymin2 = row;
	
		while (xplus2 < dimX && ymin2 >= 0 && getField(hei, ymin2, xplus2) == m) {
			count2++;
			xplus2++;
			ymin2--;
		}
		while (xmin2 >= 0 && yplus2 < dimY && getField(hei, yplus2, xmin2) == m) {
			count2++;
			xmin2--;
			yplus2++;
		}
		if (count2 >= win) {
			System.out.println("hasDiagonalXY (i, dimY - i - 1, h) (XY) (" + m.toString() + "): true");
		}
		return count2 >= win;
	}
	
	/*
	 * @pure
	 * 
	 * @requires this.getField(z,y,x) == Mark.XX || Mark.OO;
	 * 
	 * @ensures \result == isWinner(m) == Mark.XX || Mark.OO;
	 */
	public boolean hasDiagonalZX(Mark m) {
		// for (i, y i)
		int count1 = 0;
	
		int xplus = col;
		int xmin = col - 1;
	
		int zplus = hei;
		int zmin = hei - 1;
	
		while (xplus < dimX && zplus < dimZ && getField(zplus, row, xplus) == m) {
			count1++;
			xplus++;
			zplus++;
		}
		while (xmin >= 0 && zmin >= 0 && getField(zmin, row, xmin) == m) {
			count1++;
			xmin--;
			zmin--;
		}
		if (count1 >= win) {
			System.out.println("hasDiagonalXZ (i,y,i) (" + m.toString() + "): true");
			return true;
		}
	
		// for(i, y, dimZ - i - 1)
		int count2 = 0;
	
		int xplus2 = col;
		int xmin2 = col - 1;
	
		int zplus2 = hei + 1;
		int zmin2 = hei;
	
		while (xplus2 < dimX && zmin2 >= 0 && getField(zmin2, row, xplus2) == m) {
			xplus2++;
			zmin2--;
			count2++;
		}
		while (xmin2 >= 0 && zplus2 < dimZ && getField(zplus2, row, xmin2) == m) {
			xmin2--;
			zplus2++;
			count2++;
		}
		if (count2 >= win) {
			System.out.println("hasDiagonalXZ (i, y, dimZ - 1 - i) (" + m.toString() + "): true");
		}
		return count2 >= win;
	}
	
	/*
	 * @pure
	 * 
	 * @requires this.getField(z,y,x) == Mark.XX || Mark.OO;
	 * 
	 * @ensures \result == isWinner(m) ==( Mark.XX || Mark.OO);
	 */
	public boolean hasDiagonalZY(Mark m) {
		// for the line (x,i,i)
		int count1 = 0;
	
		int yplus = row;
		int ymin = row - 1;
	
		int zplus = hei;
		int zmin = hei - 1;
	
		while (yplus < dimY && zplus < dimZ && getField(zplus, yplus, col) == m) {
			yplus++;
			zplus++;
			count1++;
		}
		while (ymin >= 0 && zmin >= 0 && getField(zmin, ymin, col) == m) {
			ymin--;
			zmin--;
			count1++;
		}
		if (count1 >= win) {
			System.out.println("hasDiagonalZY (x,i,i) (" + m.toString() + "): true");
			return true;
		}
	
		// for the line (x, i, dimZ - i - 1)
		int count2 = 0;
	
		int yplus2 = row;
		int ymin2 = row - 1;
	
		int zplus2 = hei + 1;
		int zmin2 = hei;
	
		while (yplus2 < dimY && zmin2 >= 0 && getField(zmin2, yplus2, col) == m) {
			yplus2++;
			zmin2--;
			count2++;
		}
		while (ymin2 >= 0 && zplus2 < dimZ && getField(zplus2, ymin2, col) == m) {
			ymin2--;
			zplus2++;
			count2++;
		}
		if (count2 >= win) {
			System.out.println("hasDiagonalZY (x, i, dimZ - i - 1) (" + m.toString() + "): true");
		}
		return count2 >= win;
	}
	
	/*
	 * @pure
	 * 
	 * @requires this.getField(z,y,x) == Mark.XX || Mark.OO;
	 * 
	 * @ensures \result == isWinner(m) == Mark.XX || Mark.OO;
	 */
	public boolean hasDiagonalOver(Mark m) {
		// for (i,i,i)
		int count1 = 0;
	
		int xplus1 = col;
		int xmin1 = col - 1;
	
		int yplus1 = row;
		int ymin1 = row - 1;
	
		int zplus1 = hei;
		int zmin1 = hei - 1;
	
		while (xplus1 < dimX && yplus1 < dimY && zplus1 < dimZ && getField(zplus1, yplus1, xplus1) == m) {
			count1++;
			xplus1++;
			yplus1++;
			zplus1++;
		}
		while (xmin1 >= 0 && ymin1 >= 0 && zmin1 >= 0 && getField(zmin1, ymin1, xmin1) == m) {
			count1++;
			xmin1--;
			ymin1--;
			zmin1--;
		}
		if (count1 >= win) {
			System.out.println("hasDiagonalOver (i,i,i) (" + m.toString() + "): true");
			return true;
		}
	
		// for (dimX - 1 - i, i, i)
		int count2 = 0;
	
		int xplus2 = col + 1;
		int xmin2 = col;
	
		int yplus2 = row;
		int ymin2 = row - 1;
	
		int zplus2 = hei;
		int zmin2 = hei - 1;
	
		while (xmin2 >= 0 && yplus2 < dimY && zplus2 < dimZ && getField(zplus2, yplus2, xmin2) == m) {
			count2++;
			xmin2--;
			yplus2++;
			zplus2++;
		}
		while (xplus2 < dimX && ymin2 >= 0 && zmin2 >= 0 && getField(zmin2, ymin2, xplus2) == m) {
			count2++;
			xplus2++;
			ymin2--;
			zmin2--;
		}
		if (count2 >= win) {
			System.out.println("hasDiagonalOver(dimX - 1 - i, i, i) (" + m.toString() + "): true");
			return true;
		}
	
		// for (i, dimY - i - 1, i)
		int count3 = 0;
	
		int xplus3 = col;
		int xmin3 = col - 1;
	
		int yplus3 = row + 1;
		int ymin3 = row;
	
		int zplus3 = hei;
		int zmin3 = hei - 1;
	
		while (xplus3 < dimX && ymin3 >= 0 && zplus3 < dimZ && getField(zplus3, ymin3, xplus3) == m) {
			xplus3++;
			ymin3--;
			zplus3++;
			count3++;
		}
		while (xmin3 >= 0 && yplus3 < dimY && zmin3 >= 0 && getField(zmin3, yplus3, xmin3) == m) {
			xmin3--;
			yplus3++;
			zmin3--;
			count3++;
		}
		if (count3 >= win) {
			System.out.println("hasDiagonalOver (i, dimY - 1 - i, i) (" + m.toString() + "): true");
			return true;
		}
	
		// for (i, i, dimZ - i - 1)
		int count4 = 0;
	
		int xplus4 = col;
		int xmin4 = col - 1;
	
		int yplus4 = row;
		int ymin4 = row - 1;
	
		int zplus4 = hei + 1;
		int zmin4 = hei;
	
		while (xplus4 < dimX && yplus4 < dimY && zmin4 >= 0 && getField(zmin4, yplus4, xplus4) == m) {
			xplus4++;
			yplus4++;
			zmin4--;
			count4++;
		}
		while (xmin4 >= 0 && ymin4 >= 0 && zplus4 < dimZ && getField(zplus4, ymin4, xmin4) == m) {
			xmin4--;
			ymin4--;
			zplus4++;
			count4++;
		}
		if (count4 >= win) {
			System.out.println("hasDiagonalOver (i, i, dimZ - i - 1) (" + m.toString() + "): true");
		}
		return count4 >= win;
	}
	
	/**
	 * checks for a mark whether it has won the game or not
	 * 
	 * @return
	 */
	
	// !!! I actually want the game class to only check for the player who made
	// the last move
	// whether he has made a winning move
	/*
	 * @pure
	 * 
	 * @requires m == Mark.XX || m == Mark.OO;
	 * 
	 * @ensures \result == hasWinX(m) || hasWinY(m) || hasWinZ(m) ||
	 * hasDiagonalXY(m) || hasDiagonalZX(m) || hasDiagonalZY(m) ||
	 * hasDiagonalOver(m);
	 */
	public boolean isWinner(Mark m) {
		return hasWinX(m) || hasWinY(m) || hasWinZ(m) || hasDiagonalXY(m) || hasDiagonalZX(m) || hasDiagonalZY(m)
				|| hasDiagonalOver(m);
	}
	
	/**
	 * checks whether a board has a winner by checking isWinner for all marks.
	 * 
	 * @return
	 */
	/*
	 * @pure
	 * 
	 * @requires isWinner(m) == Mark.XX || Mark.OO;
	 * 
	 * @ensures \result == (hasWinX(m) || hasWinY(m) || hasWinZ(m) ||
	 * hasDiagonalXY(m) || hasDiagonalZX(m) || hasDiagonalZY(m) ||
	 * hasDiagonalOver(m))== Mark.XX || Mark.OO
	 */
	public boolean hasWinner() {
		return isWinner(Mark.XX) || isWinner(Mark.OO);
	}
	
	/*
	 * checks whether the game is over by checking for a winner and checking
	 * whether the board is full
	 */
	/*
	 * @pure
	 * 
	 * @ensures \result == this.isFull() || this.hasWinner();
	 */
	public boolean gameOver() {
		return this.isFull() || this.hasWinner();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ to string methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public String toString() {
		String board = "";
	
		String line = "------+";
		for (int i = 0; i < dimX; i++) {
			line = line + "-----+";
		}
	
		for (int z = 0; z < dimZ; z++) {
			String s = firstLine() + "\n" + line + "\n";
			for (int y = 0; y < dimY; y++) {
				String row = String.format("%-6s%s", y, "|");
				for (int x = 0; x < dimX; x++) {
					row = row + "  " + getField(z, y, x).toString() + "  |";
				}
				s = s + row;
				s = s + "\n" + line + "\n";
			}
			board = "Z: " + z + "\n" + s + "\n" + "\n" + board;
		}
		return board;
	
	}
	
	public String firstLine() {
		String s = "y: x: |";
	
		for (int x = 0; x < dimX; x++) {
			String space = new String();
			String space2 = new String();
	
			if (x < 10) {
				space = space2 = "  ";
			} else if (x < 100) {
				space = " ";
				space2 = "  ";
			} else {
				space = space2 = " ";
	
			}
			s = s + space + x + space2 + "|";
		}
		return s;
	}
	
	/**
	 * resets all the places in the board to empty
	 */
	/*
	 * @ensures \forall (int z> 0 & z <dimZ && int y >0 & y < dimY && x >0 & x <
	 * dimX ); this.setFild(z,y,x,m) == Mark.Empty;
	 */
	public void reset() {
		for (int z = 0; z < dimZ; z++) {
			for (int y = 0; y < dimY; y++) {
				for (int x = 0; x < dimX; x++) {
					setField(z, y, x, Mark.EMPTY);
				}
			}
		}
	}
	
	/**
	 * you set a field by chosing an x and a y coordinate We didn't check the z
	 * as this is already done when a player tries to make a move
	 * 
	 * @param x:
	 *            x coordinate of move
	 * @param y:
	 *            y coordinate of move
	 * @param m:
	 *            the mark of the player who makes the move
	 */
	/*
	 * @requires this.isField(x,y) && getField(z,y,x) == Mark.Empty;
	 * 
	 * @ensures (\forall int z > 0 & z < dimZ ; this.getField(z,y,x) == Mark.XX
	 * || Mark.OO;)
	 */
	public void setField(int x, int y, Mark m) {
		for (int z = 0; z < dimZ; z++) {
			if (getField(z, y, x) == Mark.EMPTY) {
				fields[z][y][x] = m;
				row = y;
				col = x;
				hei = z;
				break;
			}
		}
	}
	
	public void setField(int z, int y, int x, Mark m) {
		fields[z][y][x] = m;
		row = y;
		col = x;
		hei = z;
	}
	
	// queries
	/*
	 * @pure
	 * 
	 * @ensures this.dimX >= 0
	 */
	public int getDimX() {
		return dimX;
	}
	
	/*
	 * @pure
	 * 
	 * @ensures this.dimX >= 0
	 */
	public int getDimY() {
		return dimY;
	}
	
	/*
	 * @pure
	 * 
	 * @ensures this.dimX >= 0
	 */
	public int getDimZ() {
		return dimZ;
	}
	
	/*
	 * @pure
	 * 
	 * @ensures this.dimX > 0
	 */
		public int getWin() {
			return win;
		}
	
	}