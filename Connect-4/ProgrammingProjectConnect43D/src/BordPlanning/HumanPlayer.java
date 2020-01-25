package BordPlanning;

import java.util.Scanner;

public class HumanPlayer extends Player{

	public HumanPlayer(String name, Mark mark) {
		super(name, mark);
	}

	/*
	 * @ requires board != null; ensures board.isField(\result) &&
	 * board.isEmptyField(\result);
	 * 
	 */
	/**
	 * Asks the user to input the field where to place the next mark. This is
	 * done using the standard input/output. \
	 * 
	 * @param board
	 *            the game board
	 * @return the player's chosen field
	 */
	@Override
	public int[] determineMove(Board board) {
		String prompt = "> " + getName() + " (" + getMark().toString() + ")" + ", what is your choice (x y)? ";
		int[] choice = readInt(prompt);
		boolean valid = board.isField(choice[0], choice[1]) && board.hasEmptyField(choice[0], choice[1]);
		while (!valid) {
			System.out.println("ERROR: field (" + choice[0] + ", " + choice[1] + ") is no valid choice.");
			choice = readInt(prompt);
			valid = board.isField(choice[0], choice[1]) && board.hasEmptyField(choice[0], choice[1]);
		}
		return choice;
	}

	/**
	 * Writes a prompt to standard out and tries to read an int value from
	 * standard in. This is repeated until an int value is entered.
	 * 
	 * @param prompt
	 *            the question to prompt the user
	 * @return the first int value which is entered by the user
	 */
	private int[] readInt(String prompt)/* throws InvalidInputException */{
		int x = 0;
		int y = 0;
		
		boolean intRead = false;
		@SuppressWarnings("resource")
		Scanner line = new Scanner(System.in);
		do {
			System.out.print(prompt);
			try (Scanner scannerLine = new Scanner(line.nextLine());) {
				if (scannerLine.hasNextInt()) {
					
					x = scannerLine.nextInt();
				} if (scannerLine.hasNextInt()) {
					intRead = true;
					y = scannerLine.nextInt();
				} else {
					//TODO create invalid input exception
				//	throw new InvalidInputException();
				}
			}
		} while (!intRead);
		int[] xy = {x, y};
		return xy;
	}
}
