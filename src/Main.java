
// import java.util.Scanner;
import java.io.IOException;

public class Main {
	public static void main(String args[]) {
		// final Scanner commandLineScanner = new Scanner(System.in);

		GraphicalUserInterface gui = new GraphicalUserInterface();
		boolean isHost = gui.askToBeHost();
		System.out.println("User is Host: " + isHost);

		if(isHost) {
			// Instanciate server
			try {
				Server server = new Server(gui); // bind a gui to server
				server.run();
				System.out.println("Server is running, waiting for players...");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			// Instanciate player
			// Player player = new Player(gui); // bind a gui to player
			// player.run();
		}
	}
}

/*

		try{
            int port = Integer.parseInt(args[0]);
            Thread t = new ChatServer(port);
            t.start();
        }catch(IOException e){
            //e.printStackTrace();
            System.out.println("\nUsage: java ChatServer <port-no.>\n"+
                    "Make sure to use valid ports (greater than 1023)");
        }catch(ArrayIndexOutOfBoundsException e){
            //e.printStackTrace();
            System.out.println("\nUsage: java ChatServer <port-no.>\n"+
                    "Insufficient arguments given.");
        }
*/