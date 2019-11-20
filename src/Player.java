

import java.util.ArrayList;

public class Player {
	private ArrayList<String> cardList = new ArrayList<String>(4);
	
	private GraphicalUserInterface gui;
	private int playerId;

	// private byte[];

	public Player(int playerId, ArrayList<String> cardList,
		GraphicalUserInterface gui) {

		this.playerId = playerId;
		this.cardList = cardList;
		this.gui = gui;
	}

	public Player() {
		
	}
}