package BordPlanning;

import java.util.List;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

import BordPlanning.Board;

public class Game2 extends Thread {
    
	public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private Player[] players;
    private int current;
    private int firstID;

    public Game2(Player s0, Player s1) {
 //       Scanner scan = new Scanner(System.in);
        int x = 4;
        int y = 4;
        int z = 4;
        
        int zcon = 4;
        
        int win = 0;
    	
//		Scanner scan = new Scanner(System.in);
//		System.out.println("What is your x - dimension?");
//		if (scan.hasNextInt()) {
//			x = scan.nextInt();
//		} 
//		System.out.println("What is your y - dimension?");
//		if (scan.hasNextInt()) {
//			y = scan.nextInt();
//		}
//		System.out.println("What is your z - dimension?");
//		if (scan.hasNextInt()) {
//			z = scan.nextInt();
//			if (z == -1) {
//				zcon = Integer.MAX_VALUE;
//			}
//		}
//		
//		System.out.println("What is your win condition?");
//		while ((win < x || win < y || win < zcon) && win == 0) {
//			if (scan.hasNextInt()) {
//				win = scan.nextInt();
//				if ((win < x || win < y || win < zcon) && win > 0) {
//					break;
//				}
//				System.out.println("win should be smaller than x, y or z and bigger than zero \n please try again");
//			}
//		}
    	
    	
    	
    	
    	board = new Board(x, y, z, win);
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
    }
    
    public Game2(List<ServerPlayer> players, int firstID) {
    	this(players.get(0), players.get(1));
    	this.firstID = firstID;
    }
    
    public Game2(Player s0, Player s1, int x, int y, int z, int win) {
    	 //       Scanner scan = new Scanner(System.in);
    	    	
    	board = new Board(x, y, z, win);
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
    }
    
    public Game2(Player s0, Player s1, int i) {
   	 //       Scanner scan = new Scanner(System.in);
   	    	
    	board = new Board(i, i, i, i);
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
    }
    
    public void start() {
        reset();
        play();
        
    }
    //Need to fix it
    private boolean readBoolean(String prompt, String yes, String no) {
        String answer;
        do {
            System.out.print(prompt);
            try (Scanner in = new Scanner(System.in)) {
                answer = in.hasNextLine() ? in.nextLine() : null;
            }
        } while (answer == null || (!answer.equals(yes) && !answer.equals(no)));
        	
        return answer.equals(yes);
    }

   
    private void reset() {
        current = 0;
        board.reset();
    }
    
    private void play() {
    	current = (int) Math.round(Math.random()) ;//random (boolean 0.0..1.0) + round (0..1)
    	update();
    	while (!board.gameOver()){
    		players[current].makeMove(board);
    		current++;
    		current = current % NUMBER_PLAYERS;
    		update();
    	}
        printResult();
    }
    
    private void update() {
        System.out.println("\ncurrent game situation: \n\n" + board.toString()
                + "\n");
    }
    
    private void printResult() {
        if (board.hasWinner()) {
            Player winner = board.isWinner(players[0].getMark()) ? players[0]
                    : players[1];
            System.out.println("Player " + winner.getName() + " ("
                    + winner.getMark().toString() + ") has won!");
        } else {
            System.out.println("Draw. There is no winner!");
        }
    }
    
}
