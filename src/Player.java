import java.net.Socket;
import java.util.ArrayList;

public class Player {
	
	private int playerId;
	private ArrayList<Card> cardList = new ArrayList<Card>(4);
	private boolean connected;

	// private byte[];

	// getters
	public int get_player_id() { return this.playerId; }
	public ArrayList<Card> get_card_list() { return this.cardList; }
	public boolean get_connection_status() { return this.connected; }

	// setters
	public void set_player_id(int pid) { this.playerId = pid; }
	public void set_card_list(ArrayList<Card> new_list) { this.cardList = new_list; }
	public void set_connection_status(boolean status) { this.connected = status; }

	public Player(int playerId, ArrayList<Card> cardList) {
		this.playerId = playerId;
		this.cardList = cardList;
	}

}