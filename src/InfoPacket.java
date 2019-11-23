public class InfoPacket {
	// variable declaration
	private int player_id;
    private String action;
    private Card card;
    // private String card_rank;
    // private String card_suit;

	// Constructor
    public InfoPacket(int player_id, String action, Card card){
        this.player_id = player_id;
        this.action = action;
        this.card = card;
        // this.card_rank = card_rank;
        // this.card_suit = card_suit;
	}

}