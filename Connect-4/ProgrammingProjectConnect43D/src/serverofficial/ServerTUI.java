package serverofficial;

import java.util.*;

public class ServerTUI {

	Scanner scanny;
	
	public ServerTUI() {
		scanny = new Scanner(System.in);
	}
	
	public int getPort() {
		String port = "1234";
		System.out.println("input port:");
		if (scanny.hasNextLine()) {
			//TODO: make sure that the input is really only 4 integers.
			//TODO: if port does not exist return there is no server with this port.
			port = scanny.nextLine();
			if (!port.matches("\\d+")) {
				
			}
		}
		return Integer.parseInt(port);
	}
	
	public void showMessage(String msg) {
		System.out.println(msg);
	}
	
	public void showErrorMessage(String msg) {
		System.out.println("ERROR: " + msg);
		
	}

}
