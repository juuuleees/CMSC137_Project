

import java.util.ArrayList;
import java.lang.Thread;
import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;


public class Player extends Thread {
	private ArrayList<String> cardList = new ArrayList<String>(4);

	Socket serverSocket;
	Socket socket;

	private int playerId;

	// private byte[];

	
	public Player(String serverName) throws IOException {
		System.out.println("Instanciating Player...");

		/* Open a ClientSocket and connect to ServerSocket */
        System.out.println("Connecting to " + serverName 
        	+ " on port " + Server.DEFAULT_PORT);
        
		//creating a new socket for client and binding it to a port
		this.serverSocket = new Socket(serverName, Server.DEFAULT_PORT);

        System.out.println("Just connected to " 
        	+ serverSocket.getRemoteSocketAddress());
        
		System.out.println("Player instanciated ");
	}

	public Player(
		int playerId, ArrayList<String> cardList, String serverName) {
		System.out.println("Unused instanciator of Player");
		this.playerId = playerId;
		this.cardList = cardList;
	}

	@Override
	public void run() {
		initializeCards();

		System.out.println("Player exited the game.");
	}

	private void initializeCards() {

		/* Wait for server to send cards in byte array format */

        try {
            System.out.println("Receiveing bytes from server "
                + serverSocket + ".");

            DataInputStream dataInputStream = new DataInputStream(
                this.serverSocket.getInputStream());
                    // A stream is a smaller river.
            int dataLength = dataInputStream.readInt();
                // the next four bytes of this input stream,
                // interpreted as an int.
            byte[] dataByteArray = new byte[dataLength];
            if (dataLength > 0) {
                dataInputStream.readFully(dataByteArray);
            }
            
            //dataByteArray;
            //      ^          ...to be interpreted as the initialized cards

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Player initialized with ...something...cards."
                + "");
	}
}