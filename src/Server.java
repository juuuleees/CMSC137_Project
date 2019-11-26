/**
* Documentations
*/

import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Thread;

public class Server extends Thread {
	private static final int MIN_PLAYERS = 2; // 3 supposedly, else testing
	// private static final int SECONDS = 1000;
	public static final int DEFAULT_PORT = 8080;
	public static final int MAX_PLAYERS = 13;
	private static final int TIMEOUT = 60 * Main.SECONDS;
	private static final int TOTAL_CARDS = 52;

	private ArrayList<Thread> playerListenerThreadList = new ArrayList<Thread>(13);
	private ArrayList<Socket> socketConnectionList = new ArrayList<Socket>(13);
	private ArrayList<Card> deck = new ArrayList<Card>(TOTAL_CARDS);

	private Scanner scanner = new Scanner(System.in);

    private boolean starting = false;

	private ServerSocket serverSocket;
	private PlayerAcceptingThread playerAcceptingThread;

    public Server() throws IOException {

    	System.out.println("Instaciating Server class");

    	serverSocket = new ServerSocket(DEFAULT_PORT);
        serverSocket.setSoTimeout(TIMEOUT);

        System.out.println("Port: " + DEFAULT_PORT);
        System.out.println("Timeout: " + TIMEOUT/Main.SECONDS + " sec");

        /*
        * Create a thread kind class for accepting player connections
		* for Server's ArrayList of PlayerListenerThread,
		* aka this.playerListenerThreadList
		* This class also updates Server's socketConnectionList (ArrayList),
		* filling it with succesful connections...
		* It's called a PlayerAcceptingThread.
		* This will be run USING START() METHOD later by Server's run method.
        */
        this.playerAcceptingThread = new PlayerAcceptingThread(
        	this.playerListenerThreadList,
        	this.socketConnectionList,
        	this.serverSocket); // ...using the same ServerSocket of Server
      	

        // System.out.println("Invoking this.playerAcceptingThr"
        	// + "ead.start(); at Constructor");
      	// this.playerAcceptingThread.start();

    }

    /* Instantiate the deck. */

    public void instantiateCards() {
    	System.out.println("Instantiating deck...");
    	int total_suits = 4;
    	String suit = "";
    	String rank = "";

    	while (total_suits > 0) {

    		for (int i = 1; i <= TOTAL_CARDS/4; i++) {

    			if (i == 1) {
    				rank = "Ace";
    			} else if (i == 11) {
    				rank = "Jack";
    			} else if (i == 12) {
    				rank = "Queen";
    			} else if (i == 13) {
    				rank = "King";
    			} else {
    				rank = Integer.toString(i);
    			}

    			if (total_suits == 4) {
    				suit = "Diamonds";
    			} else if (total_suits == 3) {
    				suit = "Hearts";
    			} else if (total_suits == 2) {
    				suit = "Spades";
    			} else if (total_suits == 1) {
    				suit = "Clubs";
    			}

    			// System.out.println(suit + ", " + rank);

    			Card new_card = new Card(rank, suit);
    			deck.add(new_card);

    		}

    		System.out.println();
    		total_suits--;

    	}

    	for (int i = 0; i < deck.size(); i++) {
    		System.out.println(deck.get(i).get_rank() + ", " + deck.get(i).get_suit());
    	}

    }

	public void run () {
		
    	System.out.println("Server thread running...");
    	instantiateCards();

		/*
		* Create a thread to wait for minimum
        * player connections then starts game
        */

		System.out.println("Waiting for minimum players ("
			+ MIN_PLAYERS + ")...");

		// INVOKE a Thread extending class which accepts client connections
		this.playerAcceptingThread.start();
		// Use start() not run() for running threads!
		// Also, do not start a thread repeatedly, e.g. in a loop

		while (!starting) {
			
			if(this.playerAcceptingThread.getConnectionCount()
				>= MIN_PLAYERS) {

				// System.out.print("Press Enter to start: ");
				System.out.print("\nEnter 'play' to start game!\n");
				String answer = scanner.next();
				if(answer.equals("play")) {
					starting = true;
				} else {
					starting = false;
				}
			}

			System.out.print(""); // bounded wait
			// System.out.println("Still waiting for players..."); // flooding
		}
		
    	System.out.println("Starting game...");

    	// instantiateCards();

    	System.out.println("Distributing cards");
    	int i = 0;
    	for(Socket player : socketConnectionList) {

    		System.out.println("player " + player.getRemoteSocketAddress());

    		System.out.println("Test one player.");
    		// break;
    	}
		
    	System.out.println("Ending game...");
    	System.exit(0);
	}

	/*
	* This method selects which kind will be used
	* 1      		A
	* 2-9,0			2-10
	* 10,11,12 		J,Q,K
	*/
	public void selectCards(int setCount) {
		
	}

}