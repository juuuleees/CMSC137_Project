/**
* Documentations
*/

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.DataOutputStream;
import java.lang.Thread;

public class Server extends Thread {
	private static final int MIN_PLAYERS = 2; // 3 supposedly, else testing
	private static final int SECONDS = 1000;
	public static final int DEFAULT_PORT = 8080;
	public static final int MAX_PLAYERS = 13;
	private static final int TIMEOUT = 60 * 3 * SECONDS;
	private Integer countdown = TIMEOUT/SECONDS - 10;
    private Integer lastSeconds = 20; 

	private ArrayList<Thread> playerListenerThreadList = new ArrayList<Thread>(13);
	private ArrayList<Socket> socketConnectionList = new ArrayList<Socket>(13);
    private Scanner scanner = new Scanner(System.in);
    private boolean starting = false;

    private String statString = "--------";

    private ServerSocket serverSocket;
    private PlayerAcceptingThread playerAcceptingThread;
    private ArrayList<Integer> selectedCardList;

    public ArrayList<String> playerSubmissionsList = new ArrayList<String>();
    private ArrayList<Integer> submissionOrder = new ArrayList<Integer>();

    public Server() throws IOException {

    	System.out.println("Instatiating Server class");

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
        	this.serverSocket,
            this); // ...using the same ServerSocket of Server
      	

        // System.out.println("Invoking this.playerAcceptingThr"
        	// + "ead.start(); at Constructor");
      	// this.playerAcceptingThread.start();

    }

	public void run () {
			
        System.out.println("Server thread running as invoked from main...");

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
                if(answer.equals("play") || answer.equals("PLAY")) {
                    starting = true;
                } else {
                    starting = false;
                }
            }

            System.out.print(""); // bounded wait
            // System.out.println("Still waiting for players..."); // flooding
        }
        
        System.out.println("Starting game...");

        // System.out.println("Testing Server.selectCards()");
        System.out.println("Selecting cards...");
        int playerCount = this.playerAcceptingThread.getConnectionCount();
        this.selectedCardList = selectCards(playerCount);
        System.out.println("Selected the cards: "
            + selectedCardList.toString());

        // Collections.shuffle(<list>);
        System.out.println("Instanciating the selected cards...");
        ArrayList<String> deckList = pickCards(selectedCardList);
        System.out.println("Selected: " + deckList.toString());
        System.out.println("Shuffling...");
        Collections.shuffle(deckList);
        System.out.println("Shuffled: " + deckList.toString());

        System.out.println("Distributing cards...");
        int playerIndex = 0;
        int deckIndex = 0;
        for(Socket playerSocket : socketConnectionList) {
            // For each player socket
            playerIndex += 1;
            System.out.println("For player #" + playerIndex + " "
                + playerSocket.getRemoteSocketAddress());

            // From the shuffled deckList, give four cards each
            String packetString = "";
            for (int i=0; i<4; i+=1) {
                packetString = packetString + deckList.get(deckIndex + i);
            }
            deckIndex += 4; // Increment, since 4 cards have been used

            System.out.println("Sending packetString: " + packetString);
            // sample: packetString = "ABCD1234";
            // Converting string to byte array
            byte[] dataByteArray = packetString.getBytes();
            
            // send cards
            sendBytes(playerSocket, dataByteArray);

            // System.out.println("Test one player.");
            // break;
        
        } // End for each player socket

        // start accepting actions from players
        System.out.println("Starting player listener" 
            + "threads for each player...");
        for (int i=0; i<playerCount; i+=1) {
            this.playerListenerThreadList.get(i).start();
            int index = i + 1;
            System.out.println("Ready for player no. " + index + "...");
        }

        boolean gameOver = false;
            System.out.println("Waiting for players' action...");
        
        int entryCount = 0;
        

        while(!gameOver) {
            System.out.println("------------ playerSubmissionsList.size(): "
                + playerSubmissionsList.size());
            // if received player action
            if (entryCount < playerSubmissionsList.size()) {
                // playerSubmissionsList is being updated
                // by this class' PlayerListenerThreadList elements

                // Evaluate player entry
                System.out.println("Evaluating entry #" + entryCount);

                String[] entryTokens = playerSubmissionsList
                    .get(entryCount).split(":");
                int playerId = Integer.parseInt(entryTokens[0]);
                String playerEntry = entryTokens[1];
                // possible player entries:
                // ------3D, passing 3 of diamonds
                // 3S3H3C3D, passing 4 of a kind
                // 4S3S3C3D, passing 4, but not the same kind
                if(playerEntry.charAt(0) - '-' == 0) {
                    // if the first byte is '-', the player's action
                    // is just to pass a card
                    System.out.println("player " + playerId
                        + " passed a card: " + playerEntry);

                    // Identify the player whom to pass the cards
                    int recipient = playerId == playerCount? 0: playerId;
                        // first player has id 1, not zero
                        // So, eg. playerid 1 will pass card to playerId2
                        // where playerId2 has index 1
                    System.out.println("Sender: " + playerId);
                    System.out.println("recipient: " + recipient);
                    
                    // block forwarding bytes when someone already won
                    System.out.println("submissionOrder.size(): "
                        + submissionOrder.size());
                    if (this.submissionOrder.size() > 0) {
                        // if someone won
                        // Send stats!!! or... the winning combo?
                        // get the winning previous entry
                        System.out.println("Sending to all the winning cards");
                        String[] entryTokensPrev = playerSubmissionsList
                            .get(entryCount-1).split(":");
                        String playerEntryPrev = entryTokensPrev[1];
                        // Send that to everyone instead
                        for (int i=0; i<socketConnectionList.size(); i+=1) {
                            sendBytes(socketConnectionList.get(i),
                                playerEntryPrev);
                        }
                    } else {
                        // normal operation, no winners yet
                        sendBytes(socketConnectionList
                            .get(recipient), playerEntry);
                    }

                } else {
                    // perhaps received a winning combination
                    System.out.println("player " + playerId
                        + " passed his cards: " + playerEntry);
                    // Share to everyone the cards submitted
                    // Identify the player whom to pass the cards
                    for (int i=0; i<socketConnectionList.size(); i+=1) {
                        sendBytes(socketConnectionList.get(i), playerEntry);
                    }
                    // kaso, but the other player flushes the stream?

                    // Evaluate submission
                    boolean winningCombo = true;
                    // checking if four-of-a-kind
                    for(int i=2; i<playerEntry.length(); i+=2) {
                        if (playerEntry.charAt(0) - playerEntry.charAt(i) != 0)
                        {
                            winningCombo = false;
                        }       
                    }
                    // if first entry to attempt submit that is valid
                    // of if someone already submitted a valid          
                    System.out.println("Winnig combo: " + winningCombo);        
                    System.out.println("submissionOrder.size() : "
                        + submissionOrder.size() );     
                    if(winningCombo || submissionOrder.size() > 0) {
                        // if the kind of card is same for all four
                        // eg. 2H2D2C2S
                        // wait for everyone to submit
                        if (submissionOrder.size() == playerCount) {
                            // game over! exit while. print stats
                            System.out.println("All player submitted.");
                            gameOver = true; // when all submitted
                        } else {
                            // record the submission order
                            System.out.println("Adding player "
                                + playerId + " to submission order");
                            System.out.println("With index "
                                + submissionOrder.size());
                            submissionOrder.add(playerId);
                        }
                    } else {
                        System.out.println("\nFalse alarm!");
                        // gameOver = true;
                    }
                }

                entryCount += 1;
            } // if received an entry from player

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            countdown -= 1;
            if (countdown <= 0){
                System.out.print("...");
            } else {
                System.out.print(countdown.toString() + "...");
            }
        } // End while not game over


        System.out.println("Game stats:");
        for(int i=0; i<submissionOrder.size(); i+=1) {
            System.out.println("\t" + i + ". player no. "
                + submissionOrder.get(i));

            // report only the top 3
            if(i < 3) {
                statString = statString + submissionOrder.get(i).toString();
            }
        }
        statString = statString + "--"; // last byte "--", meaning stats
        
        // send stats to all
        for (int i=0; i<socketConnectionList.size(); i+=1) {
            sendBytes(socketConnectionList.get(i), statString);
        }

        System.out.println("10 seconds before ending...");
        try {
            Thread.sleep(10 * 1000);
        } catch(Exception e) {
            e.printStackTrace();
        }

        Thread timer = new Thread() {
            public void run() {
                for(int t=0; t<20; t+=1) {
                    try {
                        Thread.sleep(1000);
                        Server.this.lastSeconds -= 1;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timer.start();

        // send stats to all again for 20 seconds
        while(this.lastSeconds > 0) {
            for (int i=0; i<socketConnectionList.size(); i+=1) {
                sendBytes(socketConnectionList.get(i), statString);
            }
        }

        System.out.println("Ending game...");
        // System.out.println("SERVER FINISHED RUNNING!");
        System.exit(0);
	}

	/*
	* This method selects which kind will be used
	* 1      		A
	* 2-9,0			2-10
	* 10,11,12 		J,Q,K
	*/
	private ArrayList<Integer> selectCards(int setCount) {
        System.out.println("Selecting random " 
            + setCount + " set of four-of-a-kind cards for the "
            + "corresponding count of players...");

        ArrayList<Integer> cardKindList = new ArrayList<Integer>(setCount);
        Random random = new Random();

        // System.out.println("cardKindList.size(): " + cardKindList.size());
        while (cardKindList.size() < setCount) {
            // System.out.println("Selected: " + cardKindList.toString());

            int randomInt = random.nextInt(13); // randomize 0 to 12
            // System.out.println("Generated: " + randomInt);
            // System.out.println("!cardKindList.contains(randomInt): "
                // + !cardKindList.contains(randomInt));
            if (!cardKindList.contains(randomInt)) {
                cardKindList.add(randomInt);
                // System.out.println("Adding number." + randomInt);
            }
        }

        return cardKindList;
    } // End selectCards()

    private ArrayList<String> pickCards(ArrayList<Integer> cardKindList) {
        ArrayList<String> pickedCards
            = new ArrayList<String>(cardKindList.size() * 4);
        /* Card kind legend:
        * This method selects which kind will be used
        * 1             A
        * 2-9,0         2-10
        * 10,11,12      J,Q,K
        */
        // Conversion
        for (Integer cardKind : cardKindList) {
            // Conversion
            String kind = "";
            if (cardKind == 1) {
                kind = "A";
            } else if (cardKind == 10) {
                kind = "J";
            } else if (cardKind == 11) {
                kind = "Q";
            } else if (cardKind == 12) {
                kind = "K";
            } else { // 1-9, 0
                kind = cardKind.toString();
            }
            // Instanciating the four suit cards<String> of this kind
            pickedCards.add(kind + "C"); // clover
            pickedCards.add(kind + "S"); // spade
            pickedCards.add(kind + "H"); // heart
            pickedCards.add(kind + "D"); // diamond
        }
        return pickedCards;
    } // End pickCards()

    private void sendBytes(Socket playerSocket, byte[] dataByteArray) {
        
        try {
            System.out.println("Server sending bytes to client "
                + playerSocket + "...");

            DataOutputStream dataOutputStream = new DataOutputStream(
                playerSocket.getOutputStream());
                    // A stream is a smaller river.
            dataOutputStream.flush();
            dataOutputStream.write(dataByteArray);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendBytes(Socket playerSocket, String submission) {
        // convert string into byte array
        byte[] dataByteArray = submission.getBytes();
        
        try {
            System.out.println("Sever sending bytes to player "
                + playerSocket + "...");

            DataOutputStream dataOutputStream = new DataOutputStream(
                playerSocket.getOutputStream());
                    // A stream is a smaller river.
            dataOutputStream.flush();
            dataOutputStream.write(dataByteArray);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}