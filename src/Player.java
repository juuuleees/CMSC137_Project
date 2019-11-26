import java.net.Socket;
import java.util.ArrayList;
import java.lang.Thread;
import java.net.InetAddress;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;

public class Player extends Thread {
	
	private ArrayList<Card> cardList = new ArrayList<Card>(4);
	private boolean connected;
	private Socket serverSocket;
	private Socket socket;
	private int playerId;

	public Player(InetAddress serverName) {
		try {
			System.out.println("Instantiating Player...");

			/* Open a ClientSocket and connect to ServerSocket */
        	System.out.println("Connecting to " + serverName 
        		+ " on port " + Server.DEFAULT_PORT);
	        
			//creating a new socket for client and binding it to a port
			this.serverSocket = new Socket(serverName, Server.DEFAULT_PORT);
	
        	System.out.println("Just connected to " 
        		+ serverSocket.getRemoteSocketAddress());
	        
			System.out.println("Player instanciated ");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Player(
		int playerId, ArrayList<Card> cardList, String serverName) {
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
            System.out.println("Receiving bytes from server "
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