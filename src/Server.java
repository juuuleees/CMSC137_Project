/**
* Documentations
*/

import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Thread;

public class Server implements Runnable {
	public static final int MIN_PLAYERS = 1; // 3 supposedly
	public static final int SECONDS = 1000;
	public static final int DEFAULT_PORT = 8080;
	public static final int MAX_PLAYERS = 13;
	public static final int TIMEOUT = 60 * SECONDS;

	private ArrayList<Thread> playerThreadList = new ArrayList<Thread>(13);
	private ArrayList<Socket> socketConnectionList = new ArrayList<Socket>(13);
	private Scanner scanner = new Scanner(System.in);

    private boolean starting = false;
    private int connectionCount = 0;

	private ServerSocket serverSocket;

    public Server() throws IOException {
    	System.out.println("Instaciating Server class");

    	serverSocket = new ServerSocket(DEFAULT_PORT);
        serverSocket.setSoTimeout(TIMEOUT);

        /* Create a thread for accepting player connections */
		
    }

    @Override
	public void run() {
		
    	System.out.println("Server.run() invoked");

		/*
		* Create a thread to wait for minimum
        * player connections then starts game
        */

		System.out.println("Waiting for minimum players...");
		while (!starting) {
			if(connectionCount >= MIN_PLAYERS) { 
				// System.out.print("Press Enter to start: ");
				System.out.print("Enter 'start' to play: ");
				String answer = scanner.next();
				if(answer.equals("start")) {
					starting = true;
				} else {
					starting = false;
				}
			}
		}
		
    	System.out.println("Starting game...");


		
    	System.out.println("Server.run() ends here");
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