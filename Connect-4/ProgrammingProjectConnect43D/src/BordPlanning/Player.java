package BordPlanning;

import serverofficial.*;

public abstract class Player {

	  // -- Instance variables -----------------------------------------
	private Client client;
    private String name;
    protected Mark mark;//need to change this into ID of player

    // -- Constructors -----------------------------------------------
	    /*@
	    requires name != null;
	    requires mark == Mark.XX || mark== Mark.OO;
	    ensures this.getName() == name;
	    ensures this.getMark() == mark;
	  */
	 /**
	  * Creates a new Player object.
	  * 
	  */
	 public Player(String name, Mark mark) {
	     this.name = name;
	     this.mark = mark;
	 }
	// -- Queries ----------------------------------------------------

	    /**
	     * Returns the name of the player.
	     */
	    /*@ pure */ public String getName() {
	        return name;
	    }

	    /**
	     * Returns the mark of the player.
	     */
	    /*@ pure */ public Mark getMark() {
	        return mark;
	    }

	    /*@
	       requires board != null & !board.isFull();
	       ensures board.isField(\result) & board.isEmptyField(\result);

	     */
	    /**
	     * Determines the field for the next move.
	     * 
	     * @param board
	     *            the current game board
	     * @return the player's choice
	     */
	    public abstract int[] determineMove(Board board);

	    // -- Commands ---------------------------------------------------

	    /*@
	       requires board != null & !board.isFull();
	     */
	    /**
	     * Makes a move on the board. <br>
	     * 
	     * @param board
	     *            the current board
	     */
	    
	    public void makeMove(Board board) {
	        int[] choice = determineMove(board);
	        
	        int x = choice[0];
	        int y = choice[1];
	        
	        //TODO implement a client in the player class
//	        if (y < board.dimY || x < board.dimX) {
//	        	client.getClientTUI().showErrorMessage("this is not a valid move, please try again");
//	        	choice = determineMove(board);
//	        }
	        
	        board.setField(x, y, getMark());
	    }
	    
	    @Override
	    public boolean equals(Object o) {
	    	return ((Player) o).getName().equals(this.getName());
	    }
	    
	    @Override
	    public int hashCode() {
	    	int result = 12;
	    	for (int i = 0; i < this.getName().length(); i++) {
	    		result = result*this.getName().charAt(i);
	    	}
	    	return result;
	    }


}
