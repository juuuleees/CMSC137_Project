
import java.lang.Thread;
import java.util.ArrayList;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;


public class PlayerAcceptingThread {
	private ArrayList<Thread> playerListenerThreadList;
	private ArrayList<Socket> socketConnectionList;
	private ServerSocket serverSocket;

	private int connectionCount = 0;

	public PlayerAcceptingThread(
		ArrayList<Thread> playerListenerThreadList,
		ArrayList<Socket> socketConnectionList,
		ServerSocket serverSocket
		) {

		this.playerListenerThreadList = playerListenerThreadList;
		this.socketConnectionList = socketConnectionList;
		this.serverSocket = serverSocket;
	}

	public void run() {
		/*
        * Instanciate threads listening for connecting players
        * Wait for every connections
        * Then instanciate the PlayerListenerThread
        */
        try {
	        for (int i=0; i < Server.MAX_PLAYERS; i+=1) {
	        	Socket clientSocket = serverSocket.accept(); // a player connects
	        	
	        	PlayerListenerThread playerListenerThread
	        		= new PlayerListenerThread(serverSocket, clientSocket);
	        	playerListenerThreadList.add(playerListenerThread);

	        	connectionCount += 1;
	        	System.out.println("Player " + i + " joined.");
	        }
	    } catch (IOException e) {
	    	// e.printStackTrace();
	    	System.out.println("Timed out?");
	    } finally{
		    System.out.print("The server is now full.");
		    System.out.print(" From now on, the Server's PlayerAcceptingThread");
		    System.out.println(" will stop accepting player connections!");
	    }
	}

	public int getConnectionCount() {
		return connectionCount;
	}
}

	