/**
* Documentations
*/

import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server extends Player implements Runnable {
	private static final int SECONDS = 1000;
	private static final int DEFAULT_PORT = 8080;
	private static final int MAX_PLAYERS = 13;
	private static final int TIMEOUT = 60 * SECONDS;

	private ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
        // binding a socket to a port	
	private ArrayList<Thread> playerThreadList = new ArrayList<Thread>(13);
	private ArrayList<Socket> socketConnectionList = new ArrayList<Socket>(13);
	private GraphicalUserInterface gui;

    private int connectionCount = 0;

    public Server(GraphicalUserInterface gui) throws IOException {
    	super(); // Server is also a Player
        this.gui = gui;
        serverSocket.setSoTimeout(TIMEOUT);

        // what else to do at construction?
    }

    @Override
	public void run() {
		System.out.println("Waiting for other players to join...");
		while (true) {
			// if(connectionCount >= 3) {
				// real code, 3 players should join, if the server won't play
			// if(connectionCount >= 2) {
				// or just wait for 2, since server is also a player
				// but the suggestion was to not overload the server

			if(connectionCount >= 0) { // testing 1 or 0
				this.gui.allowStart();
			}
		}
	}

	public void acceptPlayers() {
		/*
        * Instanciate threads listening for connecting players
        * Wait for every connections
        * Then instanciate the PlayerListenerThread
        */
        try {
	        for (int i=0; i < MAX_PLAYERS; i+=1) {
	        	Socket clientSocket = serverSocket.accept(); // a player connects
	        	
	        	PlayerListenerThread playerListenerThread
	        		= new PlayerListenerThread(serverSocket, clientSocket);
	        	playerThreadList.add(playerListenerThread);

	        	connectionCount += 1;
	        	System.out.println("Player " + i+1 + " joined.");
	        }
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}


	/*
	* This method selects which kind will be used
	* 1      		A
	* 2-9,0			2-10
	* 10,11,12 		J,Q,K
	*/
	public void selectCards(int setCount) {
		
	}

}