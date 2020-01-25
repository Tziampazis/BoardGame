package serverofficial;

import java.util.NoSuchElementException;
import java.util.Scanner;

import BordPlanning.Board;
import BordPlanning.Mark;

public class ClientTUI /* extends Observer */ extends Thread {

	Scanner scanner;
	Boolean closing = false;

	@Override
	public void run() {

		while (!closing) {

		}

		close();
	}

	public ClientTUI() {
		scanner = new Scanner(System.in);
	}

	//--------------------- methods for the setup of a client ---------------------
	/**
	 * Asks through the console for the name of the client
	 * the name is passed to the method that calls this method.
	 */
	public String getClientName() {
		String name = null;
		System.out.println("input name:");
		if (scanner.hasNextLine()) {
			name = scanner.nextLine();
		}
		return name;
	}

	/**
	 * First asks the client whether he is a computer or a human player
	 * if he is a computer player he asks for an easy or a hard player.
	 * The method returns a string which corresponds with the chosen level.
	 */
	public String getLevel() {
		String level = null;
		System.out.println("Are you a human or a computer player? (h, c)");
		while (scanner.hasNextLine()) {
			String next = scanner.nextLine();
			Scanner linescanner = new Scanner(next);
			if (next.startsWith("h")) {
				level = "human";
				break;
			} else if (next.startsWith("c")) {
				System.out.println("Which level? (easy, hard)");
				while (scanner.hasNextLine()) {
					String next2 = scanner.nextLine();
					if (next2.equals("easy")) {
						level = "easy";
						break;
					} else if (next2.equals("hard")) {
						scanner.nextLine();
						level = "hard";
						break;
					} else {
						System.out.println("please input easy or hard");
					}
				}

				break;
			} else {
				System.out.println("please input h for human player or c for computer player");
			}
			linescanner.close();
		}
		return level;
	}

	/**
	 * asks for the ipaddress that the clients wants to connect to
	 * and returns this to the calling method.
	 */
	public String getIP() {
		String ipaddress = null;
		System.out.println("input ip-address:");
		if (scanner.hasNextLine()) {
			ipaddress = scanner.nextLine();
		}
		return ipaddress;
	}

	/*
	 * Asks the client for the port he wants to connect to.
	 * If the inputed value is not an integer it will return 1234.
	 */
	public int getPort() {
		int port = 1234;
		System.out.println("input port:");
		while (scanner.hasNextInt()) {
			port = scanner.nextInt();
			break;
		}
		return port;
	}

	// ------------------------------ basic menu ------------------------------------
	/**
	 * shows the basic menu and asks the user what he wants to do. 
	 * if he wants to play a game he puts in a 1 if he wants to quit he puts in 0
	 * when a game is requested it returns "REQUEST" for quit it returns "QUIT"
	 * The calling method will handle these strings.
	 */
	public String showMenu() {
		String result = null;
		boolean chosen = false;
		System.out.println("Welcome in the menu");
		System.out.println("1 - play a game");
		System.out.println("0 - quit the game");

		while (!chosen) {
			while (!(scanner.hasNextLine()));
			String choice = scanner.nextLine();
			if (choice.equals("1")) {
				result = "REQUEST";
				chosen = true;
				break;
			} else if (choice.equals("0")) {
				result = "QUIT";
				chosen = true;
				break;
			} else {
				System.out.println("no valid input please try again");
			}
		}
		return result;
	}

	// --------------------------------- Show error message ----------------------------
	/**
	 * this prints an error message to the console of the client
	 */
	public void showErrorMessage(String msg) {
		System.out.println("ERROR: " + msg);

	}

	// -------------------------------- Game TUI's -----------------------------------------
	
	/**
	 * game goes through the game menu where a person can say that they want to make a move
	 * get a hint or quit the game. 
	 * it checks whether the input is an integer and then parses it to confirm the choice
	 * the user and sends the corresponding string to the calling method.
	 * if it is not the turn of the client that calls this clientTUI the method will return "WAITING"
	 */
	public String game(Boolean turn) {
		String result = null;

		if (turn) {
			System.out.println("1 - make a move");
			System.out.println("2 - get a hint");
			System.out.println("0 - quit the game");
			boolean chosen = false;
			while (!chosen) {
				try {
					System.out.println("please make a choice...");
					while (!(scanner.hasNextLine())) {
						System.out.println();
					}
					String choice = scanner.nextLine();
					while (!choice.matches("\\d+")) {
						System.out.println("invalid input, try again");
						choice = scanner.nextLine();		
					}
					int choiceI = Integer.parseInt(choice);
					
					switch (choiceI) {
						case 1:
							result = "MOVE";
							chosen = true;
							break;
						case 2:
							result = "HINT";
							chosen = true;
							break;
						case 0:
							result = "QUIT";
							chosen = true;
							break;
						default:
							System.out.println("this is no valid input, please try again");
							break;
					}
				} catch (NoSuchElementException nse) {
					System.out.println("Error occured in the menu");
				}   
			}
		} else {  
			System.out.println("Waiting for opponent to make move...");
			result = "WAITING";
		}
		return result;
	}
	
	/**
	 * print the board with a line above saying who you are playing against and the current situation.
	 */
	public void board(String board, String oppName, Boolean turn, String myName, Mark myMark) {
		System.out.println("You are playing against " + oppName);
		System.out.println("Current situation:");
		justBoard(board);
		if (turn) {
			System.out.println("It is your turn, " + myName + " (" + myMark + ")!");
		} else {
			System.out.println("It your opponents turn, please wait for him to make a move");
		}

	}
	/**
	 * prints just the board to the console
	 */
	public void justBoard(String board) {
		System.out.println(board);
	}
	
	/**
	 * Ask for a move and return this to the calling method
	 */
	public int[] move() {
		System.out.println("Please enter your move (x y)");

		int x = 0;
		int y = 0;

		boolean intRead = false;
		do {
			try (Scanner scannerLine = new Scanner(scanner.nextLine());) {
				if (scannerLine.hasNextInt()) {
					x = scannerLine.nextInt();
				}
				if (scannerLine.hasNextInt()) {
					y = scannerLine.nextInt();
					intRead = true;
				} else {
					System.out.println("no valid input please try again");
				}
			}
		} while (!intRead);
		int[] xy = { x, y };
		return xy;
	}

	/**
	 * shows the move that a game would make and asks whether user wants to make it
	 * @param move the move that the AI would make
	 * @return whether the client wants to use the hint.
	 */
	public boolean showHint(int[] move) {
		boolean use = false;
		System.out.println("I would advise you to place your mark at "
				+ "(" + move[0] + ", " + move[1] + "), good luck!");
		System.out.println("Do you want to make this move? y/n");
		while (scanner.hasNextLine()) {
			String choice = scanner.nextLine();
			if (choice.equals("y")) {
				use = true;
				System.out.println("move made");
				break;
			} else if (choice.equals("n")) {
				use = false;
				System.out.println("Okay, make your move");
				break;
			} else {
				System.out.println("no valid input please try again");
			}
		}
		return use;
	}

	//------------------------------------ Reasons the game is over -----------------------------
	/**
	 * shows that the opponent left and why
	 * @param ID
	 * @param why
	 */
	public void leftOpponent(int ID, String why) {
		System.out.println("Your opponent left or was kicked with the following reason: " + why);

	}

	/**
	 * shows that game end depending wether the winning ID equals the player ID it shows 
	 * a certain message and it reprints the board.
	 * @param winID the id of the winning player
	 * @param playerID the id of the player that called the clientTUI
	 * @param board the current board state.
	 */
	public void gameEnd(int winID, int playerID, String board) {
		if (winID == playerID) {
			justBoard(board);
			System.out.println("The player with id " + winID + " won the game!");
			System.out.println("congratulations, you won!");
		} else if (winID == -1) {
			justBoard(board);
			System.out.println("Draw, no one won");
		} else {
			justBoard(board);
			System.out.println("The player with id " + winID + " won the game!");
			System.out.println("your opponent won, better luck next time");
		}
		System.out.println("You will be returned to the main menu");
	}


	/**
	 * closes the connection
	 */
	public void close() {
		System.out.println("you are now leaving the programme, thank you for using it");
		scanner.close();
	}
	
	

}
