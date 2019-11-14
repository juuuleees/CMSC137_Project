import java.io.*;
import java.net.*;
import java.util.*;

public class InfoPacket {
	// variable declaration
	private int player_id;
    private String action;
    private String card_rank;
    private String card_suit;

	// Constructor
    public InfoPacket(int player_id, String action, String card_rank, String card_suit){
        this.player_id = player_id;
        this.action = action;
        this.card_rank = card_rank;
        this.card_suit = card_suit;
	}

}