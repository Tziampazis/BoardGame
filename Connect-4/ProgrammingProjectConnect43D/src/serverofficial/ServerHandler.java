<<<<<<< HEAD
package serverofficial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import BordPlanning.*;
import serverofficial.Client.Level;

/**
 * @author Heleen
 *
 */
public class ServerHandler extends Thread implements Observer {

	private Socket sock;
	private BufferedReader in;
	private PrintWriter out;
	public boolean run;
	private Client client;
	
	boolean closing = false;
	boolean receivedWelcome = false;
	
	//--- info about server ---
	int serverFeatures;
	
	//TODO work with lock
	
//	Lock lock = new ReentrantLock();
	
	//--- game fields ---
	private Player player;
	private ServerPlayer opponent;
	private Board board = null;
	
	
	// ----------------------------- CONSTRUCTORS ----------------------------------
	/**
	 * Creates a new ServerHandler with a <code>Socket</code> sock and for a particular client.
	 * @param sock 		The socket through which the client connects with the server.
	 * @param client	The client that has the connection with the server.
	 */
	/*@ 
	 	requires sock != null;
		requires client != null;
	  	ensures this.sock = sock;
	 	ensures this.client = client;
	 */
	public ServerHandler(Socket sock, Client client) {
		this.sock = sock;
		this.client = client;
	}
	

	
	
	@Override
	public void run() {
		initialize();
		
		inputLoop();
		
	}



	/**
	 * This method runs a loop that keeps checking for input from the server.
	 * So every command that comes in from the server will be handled here, 
	 * by calling the appropriate methods.
	 * 
	 */
	/*@ pure
	 	requires in.readLine() != null;
	 	
	 */
	private void inputLoop() {
		String line;

		try {
			while ((line = in.readLine()) != null) {
				Scanner lineScanner = new Scanner(line);
				String line2 = lineScanner.nextLine();
				if (line2.startsWith("WELCOME")) {
					String[] parts = line2.split(" ");
					receiveWELCOME(parts);
				} else if (line2.startsWith("GAME_END")) {
					String[] parts = line2.split(" ");
					receiveGAMEEND(parts);
					
				} else if (line2.startsWith("GAME")) {
					String[] parts = line2.split(" ");
					receiveGAME(parts);
					
				} else if (line2.startsWith("MOVE_SUCCESS")) {
					String[] parts = line2.split(" ");
					receiveMOVESUCCESS(parts);
					
				} else if (line2.startsWith("LEFT")) {
					Scanner lineScanner2 = new Scanner(line2);
					lineScanner2.next();
					String line3 = lineScanner2.next();
					String[] parts = line2.split(" ");
					receiveLEFT(parts, line3);
					lineScanner2.close();
				} else if (line2.startsWith("ILLEGAL")) {
					client.getClientTUI().showErrorMessage(line2);
				}
			}
		} catch (IOException e1) {
			client.getClientTUI().showErrorMessage("Server disconnected");
			close();
		}
	}

	// ------------------- SETTING UP/CLOSING CONNECTION METHODS -----------------------------------

	/**
	 * With this method the connection with the <code>Server</code>.
	 * The input stream will read all the messages comming in from the <code>Server</code> 
	 * the outputstream will send out all messages to the <code>Server</code>.
	 * With setting run to true the <code>Client</code> can wait for the connection to be setup.
	 */
	/*@ 
	  	requires sock != null;
	  	requires sock.getInputStream() != null;
	  	requires sock.getOutputStream() != null;
	  	ensures run = true;
	  	
	 */
	private void initialize() {
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true); //the true creates an autoflush
			run = true;
		} catch (IOException e) {
			client.getClientTUI().showErrorMessage("initializing failed");
			run = false;
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Call this method to close all connections made with the <code>Server</code>.
	 * The socket will be closed and the output and input stream will be closed too.
	 */
	/*@ 
	 	requires sock != null;
	 	requires out != null;
	 	requires in != null;
	 	ensures run = false;
	 */
	public void close() {
		try {
			sock.close();
			out.close();
			in.close();
			run = false;
			System.out.println("connection with server was ended");
			
			
		} catch (IOException e) {
			client.getClientTUI().showErrorMessage("Error while closing clienthandler");
			e.printStackTrace();
		}
		
	}
	
	//------------------------------ SEND MESSAGE METHODS -----------------------------------------
	/**
	 * This method sends a message to the inputStream of the <code>ClientHandler</code>
	 *	of the <code>Server</code>. 
	 * This is only used for commands, like HELLO and MOVE.
	 * @param msg The command with its arguments that is send to the <code>ClientHandler</code>
	 */
	/*@ pure
	  	requires msg != null;
	  	requires out != null;
	 */
	public void send(String msg) {
		//prints to the Server
		out.println(msg);
	}
	
	//------------------------ RECEIVE COMMANDS METHODS --------------------------------------
	
	/**
	 * This method is called by the <code>inputLoop()</code> when a WELCOME command comes in.
	 * the client will receive an ID and a thinking time and the serversFeatures save that
	 * @param parts A string Array with the command that came in from the
	 * 	server cut in separate parts.
	 */
	/*@ 
	 	requires client != null;
	 	requires parts.length == 4;
	 	requires parts != null;
	 	requires parts[1].matches("\\d+");
	 	requires parts[2].matches("\\d+");
	 	requires parts[3].matches("\\d+");
	 	ensures receivedWelcome = true;
	 	
	 */
	public void receiveWELCOME(String[] parts) {
		client.setID(Integer.parseInt(parts[1]));
		client.setThinkingTime(Long.parseLong(parts[2]));
		serverFeatures = Integer.parseInt(parts[3]);
		receivedWelcome = true;
		
	}
	
	/**
	 * This method is called by the <code>inputLoop()</code> when a GAME command comes in.
	 * it saves the <code>opponent</code> as a <code>Player</code>
	 * and creates a <code>Player</code> for himself, but first checks what kind of player it is.
	 * the incomming dimx, dimy, dimz, turnID and winsize are used to create a new board
	 * @param parts the Command with its arguments that come in from the <code>Server</code>
	 */
	/*@ 
	  	requires parts.length == 8;
	  	requires parts[3].matches("\\d+");
	  	requires parts[4].matches("\\d+");
	  	requires parts[5].matches("\\d+");
	  	requires parts[6].matches("\\d+");
	  	requires parts[7].matches("\\d+");
	  	ensures player != null;
	 	ensures opponent != null;
	 */
	public void receiveGAME(String[] parts) {
		//TODO check whether there are enough arguments
		String opponentName = parts[1];
		opponent = new ServerPlayer(opponentName, Mark.XX);
		if (client.getLevel().equals(Level.HUMAN)) {
			player = new HumanPlayer(client.getName(), Mark.OO);
		} else if (client.getLevel().equals(Level.EASY)) {
			player = new ComputerPlayer(Mark.OO, new NaiveStrategy());
		} else if (client.getLevel().equals(Level.HARD)) {
			player = new ComputerPlayer(Mark.OO, new SmartStrategy());
		} else { 
			player = new HumanPlayer(client.getName(), Mark.OO);
		}
		
		int dimx = Integer.parseInt(parts[3]);
		int dimy = Integer.parseInt(parts[4]);
		int dimz = Integer.parseInt(parts[5]);
		int turnID = Integer.parseInt(parts[6]);
		int winsize = Integer.parseInt(parts[7]);
		
		gameInit(dimx, dimy, dimz, winsize);
		game(opponent.getName(), turnID);
	}
	
	/**
	 * This method is called by the <code>inputLoop()</code> when a MOVESUCCESS command comes in.
	 * It means that the made move has come in correctly, and changes the state of the board.
	 * @param parts the command with the arguments comming in from the <code>Server</code>
	 */
	/*@ requires parts.length == 5;
	 	requires parts[1].matches("\\d+");
	 	requires parts[2].matches("\\d+");
	 	requires parts[3].matches("\\d+");
	 	requires parts[4].matches("\\d+");
	 	requires opponent != null;
	 	ensures \old(board) != board;
	 	
	 */
	public void receiveMOVESUCCESS(String[] parts) {
		//TODO check for right amount of arguments and stuff;
		
		
		int x = Integer.parseInt(parts[1]);
		int y = Integer.parseInt(parts[2]);
		int moveID = Integer.parseInt(parts[3]);
		int nextID = Integer.parseInt(parts[4]);
		
		setMove(x, y, moveID);
		if (!board.gameOver()) {
			game(opponent.getName(), nextID);
		}
		
		
	}
	
	/**
	 * This methodis called by the <code>inputLoop()</code> when a LEFT command comes in.
	 * The left message will be printed to the <code>clientTUI</code>.
	 * @param parts the command with the argument split in an <code>String</code> 
	 * 		<code>array</code> at a space
	 * @param msg the reason why the player left or was kicked.
	 */
	/*@ 
	  	requires parts.length >= 2;
	  	requires parts != null;
		requires msg != null;
		requires parts[1].matches("\\d+");
		requires client != null;
	 */
	private void receiveLEFT(String[] parts, String msg) {
		int leftID = Integer.parseInt(parts[1]);
		String why = msg;
		
		client.getClientTUI().leftOpponent(leftID, why);
		//TODO check whether a game_end is being received
		
	}
	
	/**
	 * This method is called by the <code>inputLoop()</code> when a GAME_END command comes in.
	 * The game will be ended and the board will be reset().
	 * @param parts
	 */
	/*@ requires parts.length = 2;
	 	requires parts != null;
	 	requires client != null;
	 	requires parts[1].matches("\\d+");
	 	ensures \old(board) != board; 
	 */
	private void receiveGAMEEND(String[] parts) {
		int winnerID = Integer.parseInt(parts[1]);
		client.getClientTUI().gameEnd(winnerID, client.getID());
		gameReset();
		client.readMenu();
		
	}
	
	
	
	//---------------------------- GAME METHODS -------------------------------
	
	/**
	 * starts the in-game-clientTUI to make a move or get a hint or let the
	 * <code>Player</code> quit. depending on whether they are an AI or a human player.
	 * 
	 * @param x	the x dimension of the board
	 * @param y	the y dimension of the board
	 * @param z	the z dimension of the board
	 * @param win	the winsize of the board/game
	 * @param oppName	the name of the opponent to be able to show that in the tui
	 * @param turnID	the ID of the first player to play.
	 */
	/*@ requires oppName != null;
	 	requires turnID != null;
	 	requires board != null;
	 	requires player != null;
	 	requires client != null;
	 	ensures turn = TurnID == client.getID();
	 	ensures boardString != null;
	 
	 */
	public void game(String oppName, int turnID) {
		String boardString = board.toString();
		boolean turn = turnID == client.getID();
		//TODO systemtest test whether they get back to the main menu
		if (!board.gameOver()) {
			// TODO note get mark will be o for the player himself, and x for
			// his opponent for both players.
			client.getClientTUI().board(boardString, oppName, turn, 
					client.getName(), player.getMark());

			int[] move;

			if (client.getLevel().equals(Level.HUMAN)) {
				String choice = client.getClientTUI().game(turn);
				if (choice.equals("MOVE")) {
					move = makeMove(board, client.getClientTUI().move());
					client.cmdMove(move[0], move[1]);
				} else if (choice.equals("HINT")) {
					// sh.hint();
					boolean use = hint();
					if (!use) {
						game(oppName, turnID);
					}
					
				} else if (choice.equals("QUIT")) {
					close();
				} 
				//TODO note that if the result equals WAITING it does not do anything
			} else {
				move = makeMoveAI(board /*, client.getLevel()*/);
				client.cmdMove(move[0], move[1]);
			}

		}
	}

	/**
	 * creates the board and start a new game.
	 * @param x the x dimension of the board
	 * @param y the y dimension of the board
	 * @param z the z dimension of the board
	 * @param win the winsize of the board
	 */
	/*@ requires x >= 0;
	 	requires y >= 0;
	 	requires z >= 0;
	 	requires win >= 0;
	 	requires board != null;
	 	ensures \old(board) != board;
	 */
	public void gameInit(int x, int y, int z, int win) {
		board = new Board(x, y, z, win);
	//	board.addObserver(this);
		board.reset();
		startGame();
	}
	
	
	/**
	 * This method resets the board field and sets every game related field to null.
	 * it as well sets <code>client.inGame</code> to <code>false</code>
	 */
	/*@ requires board != null;
	 	ensures opponent == null;
	 	ensures player == null;
	 	ensures \old(board) != board;
	 */
	public void gameReset() {
		board.reset();
		opponent = null;
		player = null; 
		endGame();
		
		//TODO what should happen when the game is reset?
	}
	
	/**
	 * this method will generate the move that the AI would make in the current game situation.
	 * It does this by using a SmartStrategy.
	 * @return whether the client uses the move generated.
	 */
	/*@
	 	requires board != null;
	 	requires client != null;
	 	requires player != null;
	 	ensures copy.equals(board);
	 	ensures easy != null;
	 	ensures \old(board) == board;
	 	ensures choice[0] >= 0;
	 	ensures choice[1] >= 0;
	 	
	 */
	public boolean hint() {
		Player easy = new ComputerPlayer(player.getMark(), new SmartStrategy());
		Board copy = board.deepCopy();
		int[] choice = easy.determineMove(copy);
		Boolean use = client.getClientTUI().showHint(choice);
		if (use) {
			client.cmdMove(choice[0], choice[1]);
			return true;
		} else {
			return false;
		}
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Make move methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * this method will check whether the move that is wanted to be made is valid.
	 * And if it is valid it returns the move.
	 * @param moveBoard the board on which the move has to be made
	 * @param orMove the move that is wanted to be made
	 * @return	the inputted move or a new version that is valid.
	 */
	/*@ 
	 	requires moveBoard != null;
	 	requires orMove.length == 2;
	 	requires orMove[0] >= 0;
	 	requires orMove[1] >= 0;
	 	ensures (moveBoard.isField(choice[0], choice[1])) && 
				(moveBoard.hasEmptyField(choice[0], choice[1]));
		ensures choice.length = 2;
		ensures choice[0] >= 0;
		ensures choice[1] >= 0;
	 */
	public int[] makeMove(Board moveBoard, int[] orMove) {
		int[] choice = orMove;
		boolean valid = (moveBoard.isField(choice[0], choice[1])) && 
				(moveBoard.hasEmptyField(choice[0], choice[1]));
		while (!valid) {
			//TODO check whether this error message is allowed:
			client.getClientTUI().showErrorMessage("Error: field (" + choice[0] + ", " + 
					choice[1] + ") is no valid choice.");
			choice = client.getClientTUI().move();
			valid = moveBoard.isField(choice[0], choice[1]) && 
					moveBoard.hasEmptyField(choice[0], choice[1]);
		}
		return choice;
		
	}
	

	/**
	 * This method makes a move for a player with as <code>level</code> 
	 * <code>Level.EASY</code> or <code>Level.HARD</code>.
	 * it returns a move generated by the <code>Player</code> <code>player</code> 
	 * that was created in the method <code>receiveGAME()</code>
	 * @param board the board on which is played
	 * @return move that was made by AI
	 */
	/*@ requires board != null;
	 	requires player != null;
	 	ensures choice.length = 2;
		ensures choice[0] >= 0;
		ensures choice[1] >= 0;
	 */
	public int[] makeMoveAI(Board board /*, Level level*/) {
		int[] choice;
		choice = player.determineMove(board);
		//TODO check whether this still works
//		if (level.equals(Level.EASY)) {
//			Player easy = new ComputerPlayer(player.getMark(), new NaiveStrategy());
//			choice = easy.determineMove(board);
//		} else if (level.equals(Level.HARD)) {
//			Player hard = new ComputerPlayer(player.getMark(), new SmartStrategy());
//			choice = hard.determineMove(board);
//		} else { 
//			choice = null;
			//TODO throw exception
		//}
		return choice;
		
	}
	
	/**
	 * This method will actually make the move after being called by the 
	 * <code>receiveMOVESUCCESS()</code> method
	 * on coordinates (x,y) with the mark of the given id.
	 * @param x the x coordinate of the move that is made
	 * @param y the y coordinate of the move that is made
	 * @param id the id of the player that made the move
	 */
	/*@ 
	 	requires x >= 0;
	 	requires y >= 0;
	 	requires id != null;
	 	requires player != null;
	 	requires client != null;
	 	requires opponent != null;
	 	requires board != null;
	 	ensures board.getField(x,y) == m;
	 */
	public void setMove(int x, int y, int id) {
		Mark m;
		if (id == client.getID()) {
			m = player.getMark();
		} else {
			m = opponent.getMark();
		}
		board.setField(x, y, m);
	}
	
	
	// ------------------------------- CLIENT QUERIES ------------------------------------
	/**
	 * This sets the inGame boolean in the <code>Client</code> class to true.
	 */
	/*@ 
	  	requires client != null;
	  	ensures inGame() = true;
	 */
	public void startGame() {
		client.startGame();
	}
	
	/**
	 * This sets the inGame boolean in the <code>Client</code> class to false.
	 */
	/*@ 
	  	requires client != null;
	  	ensures inGame() = false;
	 */
	public void endGame() {
		client.endGame();
	}
	
	/**
	 * gets the value of the boolean whether a <code>Client</code> is in a <code>Game</code> or not.
	 * @return <code>boolean</code> <code>inGame</code> of <code>client</code>.
	 */
	/*@ pure
	 	requires client != null;
	 */
	public boolean inGame() {
		return client.inGame();
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("update reached");
		if (arg0.equals(board)) {
			
			client.getClientTUI().justBoard(board);
		}
		
	}
}
	
	
	


=======
  package serverofficial;
  
  import java.io.BufferedReader;
  import java.io.IOException;
  import java.io.InputStreamReader;
  import java.io.PrintWriter;
  import java.net.Socket;
  import java.util.Observable;
  import java.util.Observer;
  import java.util.Scanner;
  
  import BordPlanning.*;
  import serverofficial.Client.Level;
  
  /**
   * @author Heleen
   *
   */
  public class ServerHandler extends Thread implements Observer {
  
  	private /*@spec_public*/ Socket sock;
  	private /*@spec_public*/ BufferedReader in;
  	private /*@spec_public*/ PrintWriter out;
  	public boolean run;
  	private /*@spec_public*/ Client client;
  	
  	//--- info about server ---
  int serverFeatures;
  
  //TODO work with lock
  
  //	Lock lock = new ReentrantLock();
  
  //--- game fields ---
  private /*@spec_public*/ Player player;
  private /*@spec_public*/ ServerPlayer opponent;
  private /*@spec_public*/  Board board = null;
  
  private /*@spec_public*/ boolean receivedWelcome = false;
  
  
  // ----------------------------- CONSTRUCTORS ----------------------------------
  /**
   * Creates a new ServerHandler with a <code>Socket</code> sock and for a particular client.
   * @param sock 		The socket through which the client connects with the server.
   * @param client	The client that has the connection with the server.
   */
  /*@ 
   	requires sock != null;
  	requires client != null;
    ensures this.sock == sock;
   	ensures this.client == client;
   */
  public ServerHandler(Socket sock, Client client) {
  	this.sock = sock;
  	this.client = client;
  }
  
  
  
  
  @Override
  public void run() {
  	initialize();
  	
  	inputLoop();
  	
  }
  
  
  
  /**
   * This method runs a loop that keeps checking for input from the server.
   * So every command that comes in from the server will be handled here, 
   * by calling the appropriate methods.
   * 
   */
  /*@ 
   	requires in.readLine() != null;
   	pure
   */
  private void inputLoop() {
  	String line;
  
  	try {
  		while ((line = in.readLine()) != null) {
  			Scanner lineScanner = new Scanner(line);
  			String line2 = lineScanner.nextLine();
  			if (line2.startsWith("WELCOME")) {
  				String[] parts = line2.split(" ");
  				receiveWELCOME(parts);
  			} else if (line2.startsWith("GAME_END")) {
  				String[] parts = line2.split(" ");
  				receiveGAMEEND(parts);
  				
  			} else if (line2.startsWith("GAME")) {
  				String[] parts = line2.split(" ");
  				receiveGAME(parts);
  				
  			} else if (line2.startsWith("MOVE_SUCCESS")) {
  				String[] parts = line2.split(" ");
  				receiveMOVESUCCESS(parts);
  				
  			} else if (line2.startsWith("LEFT")) {
  				Scanner lineScanner2 = new Scanner(line2);
  				lineScanner2.next();
  				String line3 = lineScanner2.next();
  				String[] parts = line2.split(" ");
  				receiveLEFT(parts, line3);
  				lineScanner2.close();
  			} else if (line2.startsWith("ILLEGAL")) {
  				client.getClientTUI().showErrorMessage(line2);
  			}
  		}
  	} catch (IOException e1) {
  		client.getClientTUI().showErrorMessage("Server disconnected");
  		close();
  	}
  }
  
  // ------------------- SETTING UP/CLOSING CONNECTION METHODS -----------------------------------
  
  /**
   * With this method the connection with the <code>Server</code>.
   * The input stream will read all the messages comming in from the <code>Server</code> 
   * the outputstream will send out all messages to the <code>Server</code>.
   * With setting run to true the <code>Client</code> can wait for the connection to be setup.
   */
  /*@ 
    	requires sock != null;
    	requires sock.getInputStream() != null;
    	requires sock.getOutputStream() != null;
    	ensures run == true;
    	
   */
  private void initialize() {
  	try {
  		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
  		out = new PrintWriter(sock.getOutputStream(), true); //the true creates an autoflush
  		run = true;
  	} catch (IOException e) {
  		client.getClientTUI().showErrorMessage("initializing failed");
  		run = false;
  		e.printStackTrace();
  	}
  	
  }
  
  /**
   * Call this method to close all connections made with the <code>Server</code>.
   * The socket will be closed and the output and input stream will be closed too.
   */
  /*@ 
   	requires sock != null;
   	requires out != null;
   	requires in != null;
   	ensures run == false;
   */
  public void close() {
  	try {
  		sock.close();
  		out.close();
  		in.close();
  		run = false;
  		System.out.println("connection with server was ended");
  		
  		
  	} catch (IOException e) {
  		client.getClientTUI().showErrorMessage("Error while closing clienthandler");
  		e.printStackTrace();
  	}
  	
  }
  
  //------------------------------ SEND MESSAGE METHODS -----------------------------------------
  /**
   * This method sends a message to the inputStream of the <code>ClientHandler</code>
   *	of the <code>Server</code>. 
   * This is only used for commands, like HELLO and MOVE.
   * @param msg The command with its arguments that is send to the <code>ClientHandler</code>
   */
  /*@ 
    	requires msg != null;
    	requires out != null;
    	pure;
   */
  public void send(String msg) {
  	//prints to the Server
  	out.println(msg);
  }
  
  //------------------------ RECEIVE COMMANDS METHODS --------------------------------------
  
  /**
   * This method is called by the <code>inputLoop()</code> when a WELCOME command comes in.
   * the client will receive an ID and a thinking time and the serversFeatures save that
   * @param parts A string Array with the command that came in from the
   * 	server cut in separate parts.
   */
  /*@ 
   	requires client != null;
   	requires parts.length == 4;
   	requires parts != null;
   	requires parts[1].matches("\\d+");
   	requires parts[2].matches("\\d+");
   	requires parts[3].matches("\\d+");
   	ensures receivedWelcome == true;
   	
   */
  public void receiveWELCOME(String[] parts) {
  	client.setID(Integer.parseInt(parts[1]));
  	client.setThinkingTime(Long.parseLong(parts[2]));
  	serverFeatures = Integer.parseInt(parts[3]);
  	receivedWelcome = true;
  	
  }
  
  /**
   * This method is called by the <code>inputLoop()</code> when a GAME command comes in.
   * it saves the <code>opponent</code> as a <code>Player</code>
   * and creates a <code>Player</code> for himself, but first checks what kind of player it is.
   * the incomming dimx, dimy, dimz, turnID and winsize are used to create a new board
   * @param parts the Command with its arguments that come in from the <code>Server</code>
   */
  /*@ 
    	requires parts.length == 8;
    	requires parts[3].matches("\\d+");
    	requires parts[4].matches("\\d+");
    	requires parts[5].matches("\\d+");
    	requires parts[6].matches("\\d+");
    	requires parts[7].matches("\\d+");
    	ensures player != null;
   	ensures opponent != null;
   */
  public void receiveGAME(String[] parts) {
  	String opponentName = parts[1];
  	opponent = new ServerPlayer(opponentName, Mark.XX);
  	if (client.getLevel().equals(Level.HUMAN)) {
  		player = new HumanPlayer(client.getName(), Mark.OO);
  	} else if (client.getLevel().equals(Level.EASY)) {
  		player = new ComputerPlayer(Mark.OO, new NaiveStrategy());
  	} else if (client.getLevel().equals(Level.HARD)) {
  		player = new ComputerPlayer(Mark.OO, new SmartStrategy());
  	} else { 
  		player = new HumanPlayer(client.getName(), Mark.OO);
  	}
  	
  	int dimx = Integer.parseInt(parts[3]);
  	int dimy = Integer.parseInt(parts[4]);
  	int dimz = Integer.parseInt(parts[5]);
  	int turnID = Integer.parseInt(parts[6]);
  	int winsize = Integer.parseInt(parts[7]);
  	
  	gameInit(dimx, dimy, dimz, winsize);
  	game(opponent.getName(), turnID);
  }
  
  /**
   * This method is called by the <code>inputLoop()</code> when a MOVESUCCESS command comes in.
   * It means that the made move has come in correctly, and changes the state of the board.
   * @param parts the command with the arguments comming in from the <code>Server</code>
   */
  /*@ requires parts.length == 5;
   	requires parts[1].matches("\\d+");
   	requires parts[2].matches("\\d+");
   	requires parts[3].matches("\\d+");
   	requires parts[4].matches("\\d+");
   	requires opponent != null;
   	ensures \old(board) != board;
   	
   */
  public void receiveMOVESUCCESS(String[] parts) { 	
  	
  	int x = Integer.parseInt(parts[1]);
  	int y = Integer.parseInt(parts[2]);
  	int moveID = Integer.parseInt(parts[3]);
  	int nextID = Integer.parseInt(parts[4]);
  	
  	setMove(x, y, moveID);
  	if (!board.gameOver()) {
  		game(opponent.getName(), nextID);
  	}
  	
  	
  }
  
  /**
   * This methodis called by the <code>inputLoop()</code> when a LEFT command comes in.
   * The left message will be printed to the <code>clientTUI</code>.
   * @param parts the command with the argument split in an <code>String</code> 
   * 		<code>array</code> at a space
   * @param msg the reason why the player left or was kicked.
   */
  /*@ 
    	requires parts.length >= 2;
    	requires parts != null;
  	requires msg != null;
  	requires parts[1].matches("\\d+");
  	requires client != null;
   */
  private void receiveLEFT(String[] parts, String msg) {
  	int leftID = Integer.parseInt(parts[1]);
  	String why = msg;
  	
  	client.getClientTUI().leftOpponent(leftID, why);
  	
  }
  
  /**
   * This method is called by the <code>inputLoop()</code> when a GAME_END command comes in.
   * The game will be ended and the board will be reset().
   * @param parts
   */
  /*@ requires parts.length == 2;
   	requires parts != null;
   	requires client != null;
   	requires parts[1].matches("\\d+");
   	ensures \old(board) != board; 
   */
  private void receiveGAMEEND(String[] parts) {
  	int winnerID = Integer.parseInt(parts[1]);
  	client.getClientTUI().gameEnd(winnerID, client.getID(), board.toString());
  	gameReset();
  	client.readMenu();
  	
  }
  
  
  
  //---------------------------- GAME METHODS -------------------------------
  
  /**
   * starts the in-game-clientTUI to make a move or get a hint or let the
   * <code>Player</code> quit. depending on whether they are an AI or a human player.
   * 
   * @param x	the x dimension of the board
   * @param y	the y dimension of the board
   * @param z	the z dimension of the board
   * @param win	the winsize of the board/game
   * @param oppName	the name of the opponent to be able to show that in the tui
   * @param turnID	the ID of the first player to play.
   */
  /*@ requires oppName != null;
   	requires turnID >= 0;
   	requires board != null;
   	requires player != null;
   	requires getClient() != null;
   
   */
  public void game(String oppName, int turnID) {
  	String boardString = board.toString();
  	boolean turn = turnID == client.getID();
  	//TODO systemtest test whether they get back to the main menu
  	if (!board.gameOver()) {
  		// TODO note get mark will be o for the player himself, and x for
  		// his opponent for both players.
  		client.getClientTUI().board(boardString, oppName, turn, 
  				client.getName(), player.getMark());
  
  		int[] move;
  
  		if (client.getLevel().equals(Level.HUMAN)) {
  			String choice = client.getClientTUI().game(turn);
  			if (choice.equals("MOVE")) {
  				move = makeMove(board, client.getClientTUI().move());
  				client.cmdMove(move[0], move[1]);
  			} else if (choice.equals("HINT")) {
  				// sh.hint();
  				boolean use = hint();
  				if (!use) {
  					game(oppName, turnID);
  				}
  				
  			} else if (choice.equals("QUIT")) {
  				close();
  			} 
  			//TODO note that if the result equals WAITING it does not do anything
  		} else {
  			move = makeMoveAI(board /*, client.getLevel()*/);
  			client.cmdMove(move[0], move[1]);
  		}
  
  	}
  }
  
  /**
   * creates the board and start a new game.
   * @param x the x dimension of the board
   * @param y the y dimension of the board
   * @param z the z dimension of the board
   * @param win the winsize of the board
   */
  /*@ requires x >= 0;
   	requires y >= 0;
   	requires z >= 0;
   	requires win >= 0;
   	requires board != null;
   	ensures \old(board) != board;
   */
  public void gameInit(int x, int y, int z, int win) {
  	board = new Board(x, y, z, win);
  //		board.addObserver(this);
  	board.reset();
  	startGame();
  }
  
  
 
  /*@ requires board != null;
   	ensures opponent == null;
   	ensures player == null;
   	ensures \old(board) != board;
   */
  /**
   * This method resets the board field and sets every game related field to null.
   * it as well sets <code>client.inGame</code> to <code>false</code>
   */
  public void gameReset() {
  	board.reset();
  	opponent = null;
  	player = null; 
  	endGame();
  	
  	//TODO what should happen when the game is reset?
  }
  
  
  /*@
   	  requires board != null;
   	  requires getClient() != null;
   	  requires player != null;
   	  ensures \old(board) == board;
   	
   */
  /**
   * this method will generate the move that the AI would make in the current game situation.
   * It does this by using a SmartStrategy.
   * @return whether the client uses the move generated.
   */
  public boolean hint() {
  	Player easy = new ComputerPlayer(player.getMark(), new SmartStrategy());
  	Board copy = board.deepCopy();
  	int[] choice = easy.determineMove(copy);
  	Boolean use = client.getClientTUI().showHint(choice);
  	if (use) {
  		client.cmdMove(choice[0], choice[1]);
  		return true;
  	} else {
  		return false;
  	}
  }
  
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Make move methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
  /**
   * this method will check whether the move that is wanted to be made is valid.
   * And if it is valid it returns the move.
   * @param moveBoard the board on which the move has to be made
   * @param orMove the move that is wanted to be made
   * @return	the inputted move or a new version that is valid.
   */
  /*@ 
   	requires moveBoard != null;
   	requires orMove.length == 2;
   	requires orMove[0] >= 0;
   	requires orMove[1] >= 0;
   	ensures (moveBoard.isField(\result[0], \result[1])) && 
  			(moveBoard.hasEmptyField(\result[0], \result[1]));
  	ensures \result.length == 2;
  	ensures \result[0] >= 0;
  	ensures \result[1] >= 0;
   */
  public int[] makeMove(Board moveBoard, int[] orMove) {
  	int[] choice = orMove;
  	boolean valid = (moveBoard.isField(choice[0], choice[1])) && 
  			(moveBoard.hasEmptyField(choice[0], choice[1]));
  	while (!valid) {
  		//TODO check whether this error message is allowed:
  		client.getClientTUI().showErrorMessage("Error: field (" + choice[0] + ", " + 
  				choice[1] + ") is no valid choice.");
  		choice = client.getClientTUI().move();
  		valid = moveBoard.isField(choice[0], choice[1]) && 
  				moveBoard.hasEmptyField(choice[0], choice[1]);
  	}
  	return choice;
  	
  }
  
  
  /**
   * This method makes a move for a player with as <code>level</code> 
   * <code>Level.EASY</code> or <code>Level.HARD</code>.
   * it returns a move generated by the <code>Player</code> <code>player</code> 
   * that was created in the method <code>receiveGAME()</code>
   * @param board the board on which is played
   * @return move that was made by AI
   */
  /*@ requires board != null;
   	requires player != null;
   	ensures \result.length == 2;
  	ensures \result[0] >= 0;
  	ensures \result[1] >= 0;
   */
  public int[] makeMoveAI(Board board /*, Level level*/) {
  	int[] choice;
  	choice = player.determineMove(board);
  	//TODO check whether this still works
  //		if (level.equals(Level.EASY)) {
  //			Player easy = new ComputerPlayer(player.getMark(), new NaiveStrategy());
  //			choice = easy.determineMove(board);
  //		} else if (level.equals(Level.HARD)) {
  //			Player hard = new ComputerPlayer(player.getMark(), new SmartStrategy());
  //			choice = hard.determineMove(board);
  //		} else { 
  //			choice = null;
  		//TODO throw exception
  	//}
  	return choice;
  	
  }
  
  /**
   * This method will actually make the move after being called by the 
   * <code>receiveMOVESUCCESS()</code> method
   * on coordinates (x,y) with the mark of the given id.
   * @param x the x coordinate of the move that is made
   * @param y the y coordinate of the move that is made
   * @param id the id of the player that made the move
   */
  /*@ 
   	requires x >= 0;
   	requires y >= 0;
   	requires id >= 0;
   	requires player != null;
   	requires client != null;
   	requires opponent != null;
   	requires board != null;
   	ensures (\exists Integer z; z >= 0 && z<board.getDimZ(); board.getField(x, y, z) == opponent.getMark());
   */
  public void setMove(int x, int y, int id) {
  	Mark m;
  	if (id == client.getID()) {
  		m = player.getMark();
  	} else {
  		m = opponent.getMark();
  	}
  	board.setField(x, y, m);
  }
  
  
  // ------------------------------- CLIENT QUERIES ------------------------------------
  /**
   * This sets the inGame boolean in the <code>Client</code> class to true.
   */
  /*@ 
    	requires getClient() != null;
    	ensures inGame() == true;
   */
  public void startGame() {
  	client.startGame();
  }
  
  /**
   * This sets the inGame boolean in the <code>Client</code> class to false.
   */
  /*@ 
    	requires getClient() != null;
    	ensures inGame() == false;
   */
  public void endGame() {
  	client.endGame();
  }
  
  /**
   * gets the value of the boolean whether a <code>Client</code> is in a <code>Game</code> or not.
   * @return <code>boolean</code> <code>inGame</code> of <code>client</code>.
   */
  /*@ requires getClient() != null;
  	  pure
   	
   */
  public boolean inGame() {
  	return client.inGame();
  }
  
  
  @Override
  public void update(Observable arg0, Object arg1) {
    System.out.println("update reached");
  	if (arg0.equals(board)) {
  	   client.getClientTUI().justBoard(board.toString());
  		}
  		
  	}
  
  /**
   * @return client 
   */
  /*@
   	pure
   */
  public Client getClient() {
	  return client;
  }
  }
  	
  	
  	
  
  
>>>>>>> branch 'master' of https://Tziampazis@bitbucket.org/chsoftwaresystems/implementationproject.git
