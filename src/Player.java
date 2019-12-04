import java.util.ArrayList;
import java.util.Scanner;
import java.net.Socket;
import java.lang.Thread;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Player extends Thread {
	
	private ArrayList<String> cardList = new ArrayList<String>(4);
	private int playerId;
	private String cardsAtHandString = "";
	private String receivedString = "";
	private String submission = "";
	private boolean winner = false;
	private boolean waitingForStats = false;
	Socket serverSocket;

	// private Scanner scanner = new Scanner(System.in);

	public Player(String serverName) throws IOException {
		System.out.println("Instantiating Player...");

		/* Open a ClientSocket and connect to ServerSocket */
        System.out.println("Connecting to " + serverName 
        	+ " on port " + Server.DEFAULT_PORT);
        
		//creating a new socket for client and binding it to a port
		this.serverSocket = new Socket(serverName, Server.DEFAULT_PORT);

        System.out.println("Just connected to " 
        	+ serverSocket.getRemoteSocketAddress());
        
		System.out.println("Player instantiated ");
	}

	public Player(
		int playerId, ArrayList<String> cardList, String serverName) {
		System.out.println("Unused instanciator of Player");

		this.playerId = playerId;
		this.cardList = cardList;
	}

	@Override
	public void run() {
		System.out.println("Player Thread running as invoked from main...");

		System.out.println("\nInstructions:");
		System.out.println("\tEnter 1-4 to select card to pass");
		System.out.println("\tEnter 'p' to submit...four-of-a-kind\n");

		initializeCards();

		boolean shouldEnterP = false; // becomes true when someone won already

		boolean gameOver = false;
		while(!gameOver) {

			System.out.print("\nYour cards: ");
			for (int i=0; i<cardList.size(); i+=1) {
				System.out.print(cardList.get(i) + " ");
			}
			System.out.println("");
			if(shouldEnterP) {
				System.out.print("Enter 'p': ");
			} else {
				System.out.print("Action: ");
			}
			Scanner scanner = new Scanner(System.in);
			String action = scanner.next();
			
			System.out.println("cardList.size(): "
				+ cardList.size());

			submission = "--------"; // 8 bytes
			if(action.equals("p") || action.equals("P")) {
				// submit all cards to server
				submission = "";
				for (int i=0; i<cardList.size(); i+=1) {
					submission = submission + cardList.get(i);
				}
				System.out.println("Submission: " + submission);
				// After selecting action, send the card to the server

				// check if valid
				// Evaluate submission
				boolean validEntry = true;
				// checking if four-of-a-kind
				for(int i=2; i<submission.length(); i+=2) {
					if(submission.charAt(0) - submission.charAt(i)
						!= 0) {
						validEntry = false;
					}		
				}

				// Evaluate received From other player
				// maybe the other player aleady won
				boolean someoneWon = true;
				// checking if four-of-a-kind
				for(int i=2; i<receivedString.length(); i+=2) {
					if(receivedString.charAt(0) - receivedString.charAt(i)
						!= 0) {
						someoneWon = false;
					}		
				}

				if(validEntry) {
					System.out.println("\nYou win!!!!!!!\n");
					// wait for server validation
					this.winner = true;
					this.waitingForStats = true;
				} else if(someoneWon) {
					System.out.println("You submitted  P...");
					// wait for server validation
					System.out.println("Waiting for stats...");
					this.waitingForStats = true;
				} else {
					System.out.println("False alarm!");
					continue; // input card this time
				}

			} else if(action.equals("quit") || action.equals("exit")) {
				System.out.println("Exited.");
				System.exit(0);
			} else {
				int actionInt = Integer.parseInt(action);
				if(actionInt > cardList.size()) {
					System.out.println("Out of bounds!");
					continue; // ask action again
				} else {
					submission = "------" 
						+ cardList.remove(actionInt-1);
				}

				
			} // end checking player action

			System.out.println("Submission: " + submission);
			
			// After selecting action, send the card to the server
			sendBytes(submission);

			boolean receivedValid = false;

			while(!receivedValid) {
				receivedString = receiveBytes();
				// Evaluate received string
				
				// only accept cards while not winning
				if( !winner && receivedString.charAt(0) - '-' == 0) {
					// if the first byte is '-', the sending player's action
					// is just to pass a card to this player
					System.out.println("Received card: " + receivedString);
					// Just get the last 2 bytes out of 8, the card
					String receivedCard
						= String.valueOf(receivedString.charAt(6))
						+ String.valueOf(receivedString.charAt(7));
					// Add to cardList
					cardList.add(receivedCard);
					receivedValid = true;
				} else if (receivedString.charAt(7) - '-' == 0) {
					// if last byte received is '-', receiving stats
					System.out.println("Waiting for stats...");
					String stats = receiveBytes();
					for (int i=0; i<3; i+=1) {
						System.out.println("" + i + ". " 
							+ stats.substring(i*2,i*2+2));
					}
					receivedValid = true;
					gameOver = true;
				} else if(!waitingForStats) {
					// passing a winning combination, perhaps
					System.out.println("Not yet waiting for stats...");
					System.out.println("\nSomeone submitted: "
						+ receivedString);
					// check if validCardCombo
					// Evaluate submission
					boolean validCardCombo = true;
					// checking if four-of-a-kind
					for(int i=2; i<receivedString.length(); i+=2) {
						if(receivedString.charAt(0) - receivedString.charAt(i)
							!= 0) {
							validCardCombo = false;
						}		
					}
					if(!validCardCombo) {
						System.out.println("\nFalse alarm!\n");
						// wait again for a card
						receivedValid = false; // still not valid
							// I mean, receive bytes again, the card for you
					} else {
						// check who it sent that
						if(receivedString.equals(submission)) {
							System.out.println("Oh, it's you!");
							System.out.println("\nCongrats!!!\n");
							receivedValid = false; // wait for stats
								// I mean, receive bytes again
						} else {
							System.out.println("\nEnter 'p'!\n");
							validCardCombo = true; // received valid data, which
								// means thies player must enter 'p' also

							// flush!!! self!!!

							shouldEnterP = true;
							receivedValid = true; // dont receive again
								// but send action
						}					
					} // end Evaluate submission
				} else {
					// System.out.println("Oxxxx{zzzzzzzzzzzz>");
					// System.exit(0);
					// just waiting for stats...
				}
			} // while not receiving valid

		} // End while not game over

		System.out.println("Exiting the game.");
	}

	private void initializeCards() {

		System.out.println("Player is initializingCards");
		System.out.println("Player is waiting" 
			+ " for Server card initialization...");
		/* Wait for server to send cards in byte array format */

        try {
            System.out.println("Receiveing bytes from server "
                + serverSocket + "...");

            DataInputStream dataInputStream = new DataInputStream(
                this.serverSocket.getInputStream());
                    // A stream is a smaller river.

            // the next four bytes of this input stream,
            // interpreted as an int.
            // Sever sent eight bytes, sample = "KH4SAC2D"
	        int fourBytesPart1of2 = dataInputStream.readInt(); // get "KH4S"
	        int fourBytesPart2of2 = dataInputStream.readInt(); // get "AC2D"

            byte[] receivedBytesPart1 =  ByteBuffer.allocate(4)
            							.putInt(fourBytesPart1of2).array();
            byte[] receivedBytesPart2 =  ByteBuffer.allocate(4)
            							.putInt(fourBytesPart2of2).array();
          	
            String part1String = new String(receivedBytesPart1);
            String part2String = new String(receivedBytesPart2);

            String completeReceivedDataString = part1String + part2String;
            // System.out.println("completeReceivedDataString: " 
            	// + completeReceivedDataString);

            System.out.println("Saving received cards: "
            	+ completeReceivedDataString + "...");
            this.cardsAtHandString = completeReceivedDataString;

            for(int i=0; i<completeReceivedDataString.length(); i+=2) {
            	this.cardList.add(
            		"" + completeReceivedDataString.charAt(i)
            		+ completeReceivedDataString.charAt(i+1));
            }
            

            // //dataByteArray;
            //      ^          ...to be interpreted as the initialized cards

        } catch (IOException e) {
            // e.printStackTrace();
            // System.out.println("End of line exception!");
            System.out.println("The server stopped!");
            System.out.println("Server failed to send initializing cards!");
        }

        System.out.println("Player initialized with ...something...cards."
                + "");

	}

	private void sendBytes(String submission) {
		// convert string into byte array
		byte[] dataByteArray = submission.getBytes();
        
        try {
            System.out.println("Player sending bytes to server "
                + serverSocket + "...");

            DataOutputStream dataOutputStream = new DataOutputStream(
                serverSocket.getOutputStream());
                    // A stream is a smaller river.
            dataOutputStream.flush();
            dataOutputStream.write(dataByteArray);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveBytes() {
        String completeReceivedDataString = "";
        try {
            System.out.println("Receiveing bytes from Server "
                + serverSocket + "...");

            DataInputStream dataInputStream = new DataInputStream(
                this.serverSocket.getInputStream());
                    // A stream is a smaller river.

            // the next four bytes of this input stream,
            // interpreted as an int.
            // Sever sent eight bytes, sample = "KH4SAC2D"
            // Sever sent eight bytes, sample = "------2D"

            boolean doneReading = false;
            System.out.println("Player waiting for " 
                + "server response"); // passed card from another player");

            int fourBytesPart1of2 = 0;
            int fourBytesPart2of2 = 0;

            while (!doneReading) {
                try {
                    fourBytesPart1of2 = dataInputStream.readInt();
                    fourBytesPart2of2 = dataInputStream.readInt();
                    doneReading = true;
                } catch (Exception e) {
                    // e.printStackTrace();
                    try {
                        // Thread.sleep(1000);
                        // System.out.print("*");
                        System.out.println("The server has quit!");
                        System.out.println("Exiting...");
                        System.exit(0);
                    } catch(Exception ex) {
                        // ex.printStackTrace();
                    }
                }

            }

            // System.out.println("Here?");

            byte[] receivedBytesPart1 =  ByteBuffer.allocate(4)
                                        .putInt(fourBytesPart1of2).array();
            byte[] receivedBytesPart2 =  ByteBuffer.allocate(4)
                                        .putInt(fourBytesPart2of2).array();
            
            String part1String = new String(receivedBytesPart1);
            String part2String = new String(receivedBytesPart2);

            completeReceivedDataString = part1String + part2String;
            // System.out.println("completeReceivedDataString: " 
                // + completeReceivedDataString);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("End of line exception!");
        }

        return completeReceivedDataString;
    } // End receiveBytes()

	// /* Select card to pass. */
	// private Card selectCard() {
	// 	System.out.println("Select card to pass: ");
	// 	for (int i = 0; i < cardList.size(); i++) {
	// 		System.out.println("Card " + i + ": " + cardList.get(i).get_suit() + ", " + cardList.get(i).get_rank());
	// 	}
	// 	System.out.print("Choice: ");
	// 	String choice = scanner.next();

	// 	return cardList.get(Integer.parseInt(choice));

	// }

	// /* Pass a packet containing the player's card and player id to the server. */
	// private void passCard(Card selected_card) {
	// 	String action = "pass";
	// 	InfoPacket packet = new InfoPacket(this.playerId, action, selected_card);
	// 	System.out.println("Packet created. Preparing to send...");
	// }
	
}