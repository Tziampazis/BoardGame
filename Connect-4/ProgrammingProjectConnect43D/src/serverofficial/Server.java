package serverofficial;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	//private static String name;
	
	private static List<ClientHandler> clients;
	private static final int FEATURES = 0;
	private ServerTUI serverTUI;
	
	private GameHandler gh;
	private ClientHandler ch;
	private ServerSocket server;
	private int port;
	
	private boolean running;

	
	public static void main(String[] args) {
		new Server();
	}
	// ---------------- general server methods -------------------------------------------
	public Server() {
		serverTUI = new ServerTUI();
		setUpPort();
		setUpSocket();
		
		setUpCh();
	}
	
	public void setUpPort() {
		this.port = serverTUI.getPort();
	}
	
	public void setUpCh() {
		//initialize the client handler and the list with clients
		ch = null;
		clients = new ArrayList<ClientHandler>();

		while (running) {
			//create a client handler when a client connects
			try {
				ch = new ClientHandler(server.accept(), this);
				ch.start();
				
				clients.add(ch);
			} catch (IOException e) {
				serverTUI.showErrorMessage("clienthandler could not be created");
			}
			
			//when a new client connects a message is send to the server and to the other clients
			for (ClientHandler cl : clients) {
				if (cl.getRun() && !ch.equals(cl)) {
					serverTUI.showMessage("A new client joined the server");
				}			
			}
		}
	}
	
	public void setUpSocket() {
		try {
			server = new ServerSocket(port);
			running = true;
			serverTUI.showMessage("waiting for clients...");
		} catch (IOException e1) {
			serverTUI.showErrorMessage("serversocket could not be created");
			setUpPort();
			setUpSocket();
			setUpCh();
		} 
	}
	
//------------------------ interface cmd methods --------------------------------
	public void cmdWelcome(int assignedUserID, long allowedThinkTime, 
			int capabilities, ClientHandler ch3) {
		String message = String.format("WELCOME %d %d %d", assignedUserID, 
				allowedThinkTime, capabilities);
		serverTUI.showMessage(message);
		ch3.send(message);
	}

	public void cmdGame(String nameOtherPlayer, int otherPlayerID, int playFieldX, int playFieldY, 
			int playFieldZ, int playerWhoHasNextTurnID, int sequenceLengthOfWin, ClientHandler ch3) {
		String message = String.format("GAME %s %d %d %d %d %d %d", nameOtherPlayer, otherPlayerID, 
				playFieldX, playFieldY, playFieldZ, playerWhoHasNextTurnID, sequenceLengthOfWin);
		serverTUI.showMessage(message);
		ch3.send(message);
		
	}


	public void cmdMoveSuccess(int moveX, int moveY, int actorID, 
			int playerWhoHasNextTurnID, ClientHandler ch3) {
		String msg = String.format("MOVE_SUCCESS %d %d %d %d", moveX, 
				moveY, actorID, playerWhoHasNextTurnID);
		serverTUI.showMessage(msg);
		ch3.send(msg);
		
	}

	public void cmdGameEnd(int winnerID, ClientHandler ch3) {
		String msg = String.format("GAME_END %d", winnerID);
		ch3.send(msg);
		serverTUI.showMessage(msg);
		
	}

	public void cmdReportIllegal(String theIllegalCommandWithParameters, ClientHandler ch3) {
		ch3.send("ILLEGAL" + theIllegalCommandWithParameters);
		serverTUI.showMessage("ILLEGAL " + theIllegalCommandWithParameters);
	}

	public void cmdPlayerLeft(int leftPlayerID, String reason, ClientHandler ch3) {
		String msg = String.format("LEFT %d %s", leftPlayerID, reason);
		ch3.send(msg);
		serverTUI.showMessage(msg);
	}
	
	// --------------------- set methods ----------------------------
	public void setGameHandler(ClientHandler ch1, ClientHandler ch2) {
		gh = new GameHandler(ch1, ch2);
		gh.start();
	}
	
	//--------------------------- get methods ----------------------------
	public GameHandler getGameHandler() {
		return gh;
	}
	
	public ServerTUI getServerTUI() {
		return serverTUI;
		}
	}
