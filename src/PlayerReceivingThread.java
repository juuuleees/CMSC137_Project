
import java.lang.Thread;
import java.util.ArrayList;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;


public class PlayerReceivingThread {
	private ArrayList<Thread> playerThreadList;
	private ArrayList<Socket> socketConnectionList;
	private ServerSocket serverSocket;

	private int connectionCount = 0;

	public PlayerReceivingThread(
		ArrayList<Thread> playerThreadList,
		ArrayList<Socket> socketConnectionList,
		ServerSocket serverSocket
		) {

		this.playerThreadList = playerThreadList;
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
	        	playerThreadList.add(playerListenerThread);

	        	connectionCount += 1;
	        	System.out.println("Player " + i+1 + " joined.");
	        }
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	System.out.println("Timed out?");
	    }

	}

	public int getConnectionCount() {
		return connectionCount;
	}
}

	