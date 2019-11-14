import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Server extends Thread {

	private ServerSocket server_socket;
	private ArrayList<Socket> socket_connections = new ArrayList<Socket>();
	private static int default_port = 8080;
	private static int client_capacity = 2;

	public Server() {
		try {
			server_socket = new ServerSocket(this.default_port);
			server_socket.setSoTimeout(20000);
			System.out.println("Server running.");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void run() {
		System.out.println("hi");
	}

	public static void main(String args[]) {

		try {
			Thread game_server = new Server();
			game_server.start();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}