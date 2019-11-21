

import java.util.ArrayList;
import java.lang.Thread;
import java.net.Socket;
import java.io.IOException;


public class Player implements Runnable {
	private ArrayList<String> cardList = new ArrayList<String>(4);

	Socket server;

	private int playerId;

	// private byte[];

	
	public Player(String serverName) throws IOException {
		Socket server = new Socket(serverName, Server.DEFAULT_PORT);
	}

	public Player(
		int playerId, ArrayList<String> cardList, String serverName) {

		this.playerId = playerId;
		this.cardList = cardList;

	}



	public void run() {
		
	}
}