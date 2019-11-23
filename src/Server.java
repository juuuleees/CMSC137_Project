/**
* Documentations
*/

import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server extends Player implements Runnable {
	
	private static final int SECONDS = 1000;
	private static int defaultPort = 8080;
	private static int maxPlayers = 13;
	private static int timeout = 60 * SECONDS; 

	private ServerSocket serverSocket;
	
	private ArrayList<Thread> playerThreadList = new ArrayList<Thread>(13);
	private ArrayList<Socket> socketConnectionList = new ArrayList<Socket>(13);

    public Server() throws IOException {
        // binding a socket to a port
        serverSocket = new ServerSocket(this.defaultPort);
        serverSocket.setSoTimeout( this.timeout);
    }

    @Override
	public void run() {
		
	}

}