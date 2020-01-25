package serverofficial;

import java.io.*;
import java.net.Socket;
import java.util.*;
import exceptions.*;

public class ClientHandler extends Thread {

	//Info about connection
	private Socket sock;
	private BufferedReader in;
	private PrintWriter out;
	private Server server;
	
	//The amount of players on the connected list and gives number.
	private static int count = 0;
	private static List<ClientHandler> waitinglist = new ArrayList<ClientHandler>();
	
	private boolean run;
	private Boolean receivedRequest = false;
	private Boolean receivedHello = false;

	private ClientHandler opponent;
	private GameHandler gh = null;
	
	private Timer timer;
	
	final static int SERVER_FEATURES = 0;
	
	//information about client:
	private String clientName = null;
	private int clientID;
	private int clientCapabilities = 0;
	private long thinkingTime = 180000; // or 180000 for human or 10000 for AI
	private boolean clientInGame;
//	private ServerPlayer player;
	
	final static long AI_THINK_TIME = 10000;
	final static long HUMAN_THINK_TIME = 180000;
	final static int MAX_PLAYERS = 2;
	
	// ----------------------- general clienthandler methods --------------------------------
	
	/**
	 * creates a new clienthandler
	 * @param sock will be set to the sock of this client handler
	 * @param server will be set as the server of this clienthandler
	 */
	public ClientHandler(Socket sock, Server server) {
		
		this.sock = sock;
		
		this.server = server;
		
	}
	
	@Override
	public void run() {
		initialize();
		
		try {
			inputLoop();
		} catch (IllegalCommandException e) {	
		} catch (IllegalMoveException e) {
		} catch (IllegalArgumentsException iae) {
		}
		
		close();
	}

	private void inputLoop() throws IllegalCommandException, 
				IllegalMoveException, IllegalArgumentsException {
		String line;
		
		try {
			while (true) {
				if ((line = in.readLine()) != null) {
					server.getServerTUI().showMessage(this.clientName + ": " + line);
					if (line.startsWith("HELLO")) {
						if (receivedHello) {
							throw new IllegalCommandException(line);
						}
						String[] parts = line.split(" ");
						if (parts.length != 4) {
							throw new IllegalArgumentsException(Arrays.toString(parts));
						}
						receiveHELLO(parts);
						receivedHello = true;
					} else if (line.startsWith("REQUEST")) {
						if (!receivedHello) {
							throw new IllegalCommandException(line);
						}
						String[] parts = line.split(" ");
						if (parts.length > 2) {
							throw new IllegalArgumentsException(line);
						}
						receiveREQUEST();
						receivedRequest = true;
					} else if (line.startsWith("MOVE")) {
						if (!receivedRequest) {
							throw new IllegalCommandException(line);
						}
						String[] parts = line.split(" ");
						if (parts.length != 3) {
							throw new IllegalArgumentsException(Arrays.toString(parts));
						}
						receiveMOVE(parts);
					}
				}
				
			}
		} catch (IOException e1) {
			server.getServerTUI().showMessage("client " + clientName + " disconnected");
			if (!(gh == null)) {
				server.cmdPlayerLeft(clientID, "connection with player lost", gh.other(this));
				server.cmdGameEnd(-1, gh.other(this));
			}
			
		} catch (IllegalCommandException ice) {
			server.cmdReportIllegal(ice.getMessage(), this);
		} catch (IllegalArgumentsException iae) {
			server.cmdReportIllegal(iae.getMessage(), this);
		}
		
	}

	private void initialize() {
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true); //the true creates an autoflush
			run = true;
		} catch (IOException e) {
			server.getServerTUI().showMessage("initializing failed");
			run = false;
		}
		
	}
	
	public void close() {
		try {
			server.getServerTUI().showErrorMessage("connection with client " + clientName + " was ended");
			sock.close();
			out.close();
			in.close();
			
		} catch (IOException e) {
			server.getServerTUI().showErrorMessage("Error while closing clienthandler");
		}
		
	}
	
	// ----------------------- receive commands methods ---------------------------
	
	public void receiveHELLO(String[] parts) throws IllegalCommandException {
		
		clientName = parts[1];
		
		if (parts[2].equals("false")) {
			thinkingTime = HUMAN_THINK_TIME;
		} else if (parts[2].equals("true")) {
			thinkingTime = AI_THINK_TIME;
		}
		clientCapabilities = Integer.parseInt(parts[3]);
		clientID = count;
		server.cmdWelcome(count, thinkingTime, SERVER_FEATURES, this);
		
		count++;
	}
	
	/*@pure*/public void receiveREQUEST() {
		
		
		if (waitinglist.size() < MAX_PLAYERS - 1) {
			waitinglist.add(this);
			server.getServerTUI().showMessage("Added " + clientName + " to the waitinglist");
		} else if (waitinglist.size() == (MAX_PLAYERS - 1)) {
			opponent = waitinglist.get(0);
			
			waitinglist.clear();
			
			server.setGameHandler(this, opponent);
//			
		}
	}
	
	public void receiveMOVE(String[] parts) throws IllegalMoveException {
		try{
			if (gh.getCurrentID() == clientID) {
				
				int x = Integer.parseInt(parts[1]);
				int y = Integer.parseInt(parts[2]);
				
				server.getGameHandler().makeMove(x, y, this);
			} else {
				throw new IllegalMoveException(Arrays.toString(parts));
			}
		} catch (IllegalMoveException ime) {
			server.cmdReportIllegal(ime.getMessage(), this);
		}
		
	}

	
	//------------------------- timer ------------------------------------
	
	public void timer() {
		timer = new Timer(); 
		server.getServerTUI().showMessage("Timer was started");
		timer.schedule(new TimerTask() {
			@Override
			public void run(){				
				ClientHandler currentCH;
				if (gh.getCh1().getName().equals(clientName)) {
					currentCH = gh.getCh1();
				} else {
					currentCH = gh.getCh2();
				}
				gh.other(currentCH).getServer().cmdPlayerLeft(currentCH.getID(), 
						"Player_Timeout", gh.other(currentCH));
				gh.other(currentCH).getServer().cmdGameEnd(-1, gh.other(currentCH));
				currentCH.getServer().cmdGameEnd(-1, currentCH);				
				
				}
			}, thinkingTime);
			
		
	}
	
	public void send(String msg) {
		out.println(msg);
	}
	
	//  --------------------------- queries ---------------------------------------
	public int getID() {
		return clientID;
	}
	
	public String getClientName() {
		return clientName;
	}

	
	public Timer getTimer() {
		return timer;
	}
	
	public boolean getRun() {
		return run;
	}
	
	public boolean inGame() {
		return clientInGame;
	}
	
	public Server getServer() {
		return server;
	}
	
	//------------------------------ setters -------------------------

	public void startGame() {
		clientInGame = true;
	}
	
	public void endGame() {
		clientInGame = false;
	}
	
	public void setGameHandler(GameHandler gameh) {
		this.gh = gameh;
	}


}
