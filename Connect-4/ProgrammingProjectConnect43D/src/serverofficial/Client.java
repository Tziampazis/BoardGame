package serverofficial;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements connect4.Connect4Client {

  //Information about the client and the connection
	  private String name;
	  private boolean ai;
	  static final int FEATURES = 0;
	  private Level level;
	  public int id;
	  private long thinkingTime;
	  public enum Level {
	      EASY, HARD, HUMAN
	    } 
	  private Boolean inGame;
	  
	 //information about the connection with the server
	 private int port;
	 private String ipaddress;
	 private Socket sock;
	 ServerHandler sh;
	
	 ClientTUI clientTUI;
	 private boolean helloSend = false;

	 /*
	  * main method creates a new client that sets up all connections
	  */
	public static void main(String[] args) {
		new Client();
	}

	/*
	 * The constructor creates a new client with a socket setup,
	 * a new clientTUI (which is a thread). it then sends a hello, if this
	 * was not yet send by the catch of an IOException.
	 * and it starts the menu
	 */
	public Client() {
		sock = null;

		clientTUI = new ClientTUI();

		setUp();

		setUpSocket();

		if (!helloSend) {
			cmdHello(name, FEATURES, ai);
		}

		readMenu();

	}

	/*
	 * Setup client, getting its name, level, ipAddress and port number
	 * and assigning them 
	 */
	public void setUp() {
		
		name = clientTUI.getClientName();

		String result = clientTUI.getLevel();
		if (result.equals("human")) {
			setLevel(Level.HUMAN);
		} else if (result.equals("easy")) {
			setLevel(Level.EASY);
		} else if (result.equals("hard")) {
			setLevel(Level.HARD);
		}

		ai = !getLevel().equals(Level.HUMAN);
		ipaddress = clientTUI.getIP();
		port = clientTUI.getPort();

	}

	/*
	 * Setting up the connection with the server through a socket.
	 * If it does not work an IOException is thrown and the client 
	 * is asked to input values again.
	 */
	public void setUpSocket() {
		try {
			sock = new Socket(ipaddress, port);
		} catch (UnknownHostException e) {
			clientTUI.showErrorMessage("could not find host, please try again");
			// TODO make this error exception message;
			setUp();
		} catch (IOException e) {
			System.out.println("port is not in use, please try again");
			clientTUI = new ClientTUI();
			setUp();
			setUpSocket();
			cmdHello(name, FEATURES, ai);
			readMenu();

		}
		
		sh = new ServerHandler(sock, this);
		sh.start(); // hij zit op een aparte thread anders blokkeert hij het
					// hele programma

		while (!sh.run) {
		    
		}
	}

	//--------------------------------- interface cmds -----------------------------
	/*
	 * sends the HELLO command to the server to indicate it wants a connection.
	 */
	@Override
	public void cmdHello(String username, int clientCapabilities, boolean isAI) {
		sh.send(String.format("HELLO %s %b %d", username, isAI, clientCapabilities));
		helloSend = true;
		// TODO check for valid input or assume input is correct.
	}

	/*
	 * sends the MOVE command to the server to indicate what move the player wants to make.
	 */
	@Override
	public void cmdMove(int x, int y) {
		sh.send(String.format("MOVE %d %d", x, y));
	}

	// -------------------------------------- menu ------------------------------

	/*
	 * shows the menu through the client TUI
	 */
	public void readMenu() {

		String choice;
		choice = clientTUI.showMenu();

		if (choice.equals("REQUEST")) {
			sh.send("REQUEST");
		} else if (choice.equals("QUIT")) {
			sh.close();
		} // TODO implement the quit + implement what should happen to other
			// player
	}

	// ------------------------------- Queries ------------------------------------

	/*
	 * returns ID
	 */
	public int getID() {
		return id;
	}

	/*
	 * returns name
	 */
	public String getName() {
		return name;
	}

	/*
	 * returns the client TUI 
	 */
	public ClientTUI getClientTUI() {
		return clientTUI;
	}

	/*
	 * returns the level
	 */
	public Level getLevel() {
		return level;
	}

	/*
	 * returns whether client is in game
	 */
	/*@ pure */public boolean inGame() {
		return inGame;
	}

	/*
	 * returns the thinking time
	 */
	public long getThinkingTime() {
		return thinkingTime;
	}
	
	// ---------------------------- methods ----------------------------------
	
	public void setID(int id) {
	    this.id = id;
	  }
	
	public void setThinkingTime(long time) {
	    thinkingTime = time;
	  }
	public void setLevel(Level level) {
	     this.level = level;
	   }

	 public void startGame() {
	     inGame = true;
	   }

	 public void endGame() {
	     inGame = false;
	   }
}
