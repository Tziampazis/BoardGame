package BordPlanning;

import java.util.*;
import java.util.Scanner;


import BordPlanning.Board;

public class ServerGame {
    
	public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private List<ServerPlayer> players;
    private int current;
    private int firstID;
  
    boolean gameOver;
    int[] move;

    public ServerGame(ServerPlayer s0, ServerPlayer s1) {
 //       Scanner scan = new Scanner(System.in);
        int x = 4;
        int y = 4;
        int z = 4;
        
//        int zcon = 4;
        
        int win = 0;    	
    	
    	board = new Board(x, y, z, win);
        players = new ArrayList<ServerPlayer>();
        players.add(s0);
        players.add(s1);
        current = 0;
        gameOver = false;
        
        move = null;
    }
    
//    public ServerGame(List<ServerPlayer> players, int firstID) {
//    	this(players.get(0), players.get(1));
//    	this.firstID = firstID;
//    }
//    
    public ServerGame(ServerPlayer s0, ServerPlayer s1, int firstID, int x, int y, int z, int win) {
    	 //       Scanner scan = new Scanner(System.in);
    	    
    	this.firstID = firstID;
    	board = new Board(x, y, z, win);
        players = new ArrayList<ServerPlayer>();
        players.add(s0);
        players.add(s1);
        current = 0;
    }
    
    public ServerGame(ServerPlayer s0, ServerPlayer s1, int i) {
   	 //       Scanner scan = new Scanner(System.in);
   	    	
    	board = new Board(i, i, i, i);
        players = new ArrayList<ServerPlayer>();
        players.add(s0);
        players.add(s1);
        current = 0;
    }
    
    public void start() {
        reset();
       
        for (Player p : players) {
    		if (p.getName().equals(Integer.toString(firstID))) {
    			current = players.indexOf(p);
    		}
    	}
        //TODO make it possible to start a new game -> send a new request.
        
    }

   
    private void reset() {
        current = 0;
        board.reset();
    }
    
    public void play() {
		players.get(current).makeMove(board);
		current++;
		current = current % NUMBER_PLAYERS;
		gameOver = board.gameOver();

    }

//    
//   public void makeMove(int x, int y, int ID) {
//	   if (players.get(current).getName().equals(Integer.toString(ID))) {
//		   int[] move = {x, y};
//		   players.get(current).setNextMove(move);
//	   } else {
//		   //throw illegal
//	   } 
//   }

    public boolean validMove(int x, int y) {
	    return board.isField(x, y) && board.hasEmptyField(x, y);
    }
    
    public String getCurrentPlayer() {
    	return players.get(current).getName();
    }
    
    public boolean gameOver() {
    	return gameOver;
    }
    
    public Board getBoard() {
    	return board;
    }
    
}
