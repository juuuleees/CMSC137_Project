/**
*	Separate Card object so it's easier to access card rank and
*	suit using get methods.
*
*	@author juuuleees
*/

public class Card {
	
	private String rank;
	private String suit;

	// getters
	public String get_suit() { return this.suit; }
	public String get_rank() { return this.rank; }

	// setters
	public void set_rank(String new_r) { this.rank = new_r; }
	public void set_suit(String new_s) { this.suit = new_s; }

	// constructor 
	public Card(String r, String s) {
		this.rank = r;
		this.suit = s;
	}

}