
import java.util.ArrayList;
import jav.lang.Thread;

public class Server {

	private ServerSocket server_socket;
	private ArrayList<Socket> socket_connections = new ArrayList<Socket>();
	private static int default_port = 8080;
	private static int client_capacity = 2;

	/*Server has */
	private ArrayList<Thread> player_thread_list = new ArrayList<Thread>();

	public Server() {
		
	}

	

}