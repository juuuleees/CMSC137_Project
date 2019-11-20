
import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		final Scanner commandLineScanner = new Scanner(System.in);

		GraphicalUserInterface gui = new GraphicalUserInterface();
		//boolean isHost = gui.askToBeHost();
		//System.out.println("Is host: " + isHost);

        // System.out.print("Would you like to host a new game [y/n]?: ");
        // String answer = commandLineScanner.next();
        
        // if (answer.equals("y")) {
        // 	isHost = true;
        // } else if (answer.equals("n")) {
        // 	isHost = false;
        // } else {
        // 	System.out.println("Incorrect input!");
        // 	System.exit(1);
        // }



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