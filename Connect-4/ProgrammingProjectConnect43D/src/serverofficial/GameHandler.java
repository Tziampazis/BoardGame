package serverofficial;

import java.util.*;
import BordPlanning.*;

public class GameHandler extends Thread {

	private ClientHandler ch1;
	private ClientHandler ch2;
	
	private int currentID;
	private ServerGame game;
	
// the next two fields would have been used if we implemented dynamic boardsizes.
	private final static int DYNAMIC_BOARD = 0b1000;
	private final static int DYNAMIC_WIN = 0b10000;
	private final static int STANDARD_DIM = 4;
	private final static int STANDARD_WIN = 4;

	
	private List<ServerPlayer> playerlist;
	
	@Override
	public void run() {
		gameSetUp();	
		
	}
	
	public GameHandler(ClientHandler p1, ClientHandler p2) {
		
		ch1 = p1;
		ch2 = p2;
		
		p1.setGameHandler(this);
		p2.setGameHandler(this);
		
	}
	
	public void gameSetUp() {
		
		ServerPlayer p1 = new ServerPlayer(Integer.toString(ch1.getID()), Mark.XX);
		ServerPlayer p2 = new ServerPlayer(Integer.toString(ch2.getID()), Mark.OO);
		playerlist = new ArrayList<ServerPlayer>();
		
		playerlist.add(p1);
		playerlist.add(p2);

		List<Integer> ids = new ArrayList<Integer>();
		ids.add(ch1.getID());
		ids.add(ch2.getID());
		int firstID = ids.get((int) Math.floor(Math.random() * ids.size()));

		ch2.getServer().cmdGame(ch1.getClientName(), ch1.getID(), STANDARD_DIM, 
				STANDARD_DIM, STANDARD_DIM, firstID, STANDARD_WIN, ch2);
		ch1.getServer().cmdGame(ch2.getClientName(), ch2.getID(), STANDARD_DIM, STANDARD_DIM, 
				STANDARD_DIM, firstID, STANDARD_WIN, ch1);
		currentID = firstID;
		for (ClientHandler ch : new ClientHandler[]{ch1, ch2}) {
			if (ch.getID() == currentID) {
				ch.timer();
			}
		}
		
		game = new ServerGame(p1, p2, firstID, STANDARD_DIM, 
				STANDARD_DIM, STANDARD_DIM, STANDARD_WIN);
		game.start();
	}
	
	public void makeMove(int x, int y, ClientHandler ch) {
		if (ch.getID() != currentID || !game.validMove(x, y)) {
			ch.getTimer().cancel();
			ch.getServer().cmdReportIllegal(String.format("MOVE %d %d", x, y), ch);
		} else {
			int nextID = 0;
			for (ServerPlayer player : playerlist) {
				if (currentID == Integer.parseInt(player.getName())) {
					player.setNextMove(new int[]{x, y});
				} else {
					nextID = Integer.parseInt(player.getName());
				}
			} 
			
			game.play();
			ch2.getServer().cmdMoveSuccess(x, y, ch.getID(), nextID, ch2);
			ch1.getServer().cmdMoveSuccess(x, y, ch.getID(), nextID, ch1);
			if (game.gameOver()) {
				if (!game.getBoard().hasWinner()) {
					currentID = -1;
				}
				ch2.getServer().cmdGameEnd(currentID, ch2);
				ch1.getServer().cmdGameEnd(currentID, ch1);
			}
			currentID = nextID;
		}
		

		
	}
	
	public ClientHandler other(ClientHandler thisch) {
		ClientHandler other;
		if (thisch.equals(ch1)) {
			other = ch2;
		} else {
			other = ch1;
		}
		return other;
	}
	
	public int getCurrentID() {
		return currentID;
	}
	
	public ClientHandler getCh1() {
		return ch1;
	}
	
	public ClientHandler getCh2() {
		return ch2;
	}
	
	
}
