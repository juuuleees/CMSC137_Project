import java.util.ArrayList;
import java.util.Scanner;
import java.net.Socket;
import java.lang.Thread;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;

public class Player extends Thread {
	
	private ArrayList<Card> cardList = new ArrayList<Card>(4);
	private boolean connected;
	private Socket serverSocket;
	private Socket socket;
	private int playerId;

	private Scanner scanner = new Scanner(System.in);

	public Player(String serverName) throws IOException {
		System.out.println("Instantiating Player...");

		/* Open a ClientSocket and connect to ServerSocket */
        System.out.println("Connecting to " + serverName 
        	+ " on port " + Server.DEFAULT_PORT);
        
		//creating a new socket for client and binding it to a port
		this.serverSocket = new Socket(serverName, Server.DEFAULT_PORT);

        System.out.println("Just connected to " 
        	+ serverSocket.getRemoteSocketAddress());
        
		System.out.println("Player instanciated ");
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

		/* Test options, comment out when not in use. */
		System.out.println("[1] Pass cards");
		System.out.println("[2] Display your cards");
		System.out.print("Choice: ");
		int choice = Integer.parseInt(scanner.next());
		if (choice == 1) {
			Card selected_card = selectCard();
			passCard(selected_card);
		} else if (choice == 2) {
			displayCards();
		} else {
			System.out.println("Invalid choice.");
		}

		System.out.println("Player exited the game.");
	}

	private void initializeCards() {

		/* Wait for server to send cards in byte array format */

        // try {
        //     System.out.println("Receiving bytes from server "
        //         + serverSocket + ".");

        //     DataInputStream dataInputStream = new DataInputStream(
        //         this.serverSocket.getInputStream());
        //             // A stream is a smaller river.
        //     int dataLength = dataInputStream.readInt();
        //         // the next four bytes of this input stream,
        //         // interpreted as an int.
        //     byte[] dataByteArray = new byte[dataLength];
        //     if (dataLength > 0) {
        //         dataInputStream.readFully(dataByteArray);
        //     }
            
        //     //dataByteArray;
        //     //      ^          ...to be interpreted as the initialized cards

        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // System.out.println("Player initialized with ...something...cards."
        //         + "");

        /* Tester values for the player's hand of cards. Use to test all card-related methods. */
        Card card1 = new Card("Ace", "Hearts");
        Card card2 = new Card("Ace", "Spades");
        Card card3 = new Card("Ace", "Clubs");
        Card card4 = new Card("Ace", "Diamonds");

        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);

	}

	/*	Display cards in hand. */
	private void displayCards() {
		for (int i = 0; i < cardList.size(); i++) {
			System.out.println(cardList.get(i).get_suit() + ", " + cardList.get(i).get_rank());
		}
	}

	/* Select card to pass. */
	private Card selectCard() {
		System.out.println("Select card to pass: ");
		for (int i = 0; i < cardList.size(); i++) {
			System.out.println("Card " + i + ": " + cardList.get(i).get_suit() + ", " + cardList.get(i).get_rank());
		}
		System.out.print("Choice: ");
		String choice = scanner.next();

		return cardList.get(Integer.parseInt(choice));

	}

	/* Pass a packet containing the player's card and player id to the server. */
	private void passCard(Card selected_card) {
		String action = "pass";
		InfoPacket packet = new InfoPacket(this.playerId, action, selected_card);
		System.out.println("Packet created. Preparing to send...");
	}
	
}