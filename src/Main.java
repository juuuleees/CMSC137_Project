import java.net.InetAddress;
import java.util.Scanner;
import java.io.IOException;

public class Main {
	public static final String HOST_IP = new String("192.168.0.25");	
	public static final int SECONDS = 1000; // 1 thousand milliseconds

	public static void main(String args[]) {

		final Scanner scanner = new Scanner(System.in);
		System.out.print("Host a game [y/n]?: ");
		String hosting = scanner.next();
		boolean isHost = true;
		if(hosting.equals("y") || hosting.equals("Y")) {
			isHost = true;
		} else if(hosting.equals("n") || hosting.equals("N")) {
			isHost = false;
		} else {
			System.out.println("Invalid input!");
			System.exit(0);
		}

		if(isHost) {
			// Instantiate server
			try {
	
				Server server = new Server();
				server.start();
	
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("Default port unavailable!");
			}

		} else {

			// Instantiate player
			try {
				// InetAddress host_ip_addr = InetAddress.getByName(HOST_IP_STRING);
				Player player = new Player(HOST_IP); 
				System.out.println("Player thread running now...");
				player.start();
			} catch (IOException e) {
				System.out.println("Server not running!");
			}						
		}
	}
}