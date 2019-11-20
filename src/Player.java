

import java.util.ArrayList;

public class Player {
	private int playerId;

	private ArrayList<String> cardList = new ArrayList<String>(4);

	// private byte[];

	public Player(int playerId, ArrayList<String> cardList) {
		this.playerId = playerId;
		this.cardList = cardList;
	}

	public Player() {
		
	}
}