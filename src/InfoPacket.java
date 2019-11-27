public class InfoPacket {
	// variable declaration
	private int player_id;
    private String action;
    private Card card;

    // getters
    public int get_player_id() { return this.player_id; }
    public String get_action() { return this.action; }
    public Card get_card() { return this.card; }

    // setters
    public void set_player_id(int id) { this.player_id = id; }
    public void set_action(String action) { this.action = action; }
    public void set_card(Card card) { this.card = card; }

	// Constructor
    public InfoPacket(int player_id, String action, Card card){
        this.player_id = player_id;
        this.action = action;
        this.card = card;
	}

}