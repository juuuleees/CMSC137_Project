/**
* The PlayerListenerThread class extends the
* the class Thread.
* 
* It listens to Client requests.
*
*
* @author  cjldadios
* @version 1.0
* @since   2019 
*/

import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;

public class PlayerListenerThread extends Thread {

    private ServerSocket serverSocket;
    private Socket clientSocket; // player may be referred as client

    public PlayerListenerThread(ServerSocket serverSocket,
                                    Socket clientSocket) {

        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        System.out.println("Client from "
            + this.clientSocket.getRemoteSocketAddress() 
            + " has connected on "
            + this.serverSocket.getLocalPort() + ".");
        System.out.println("Instanciating a PlayerListenerThread");
    }

	@Override
	public void run() {
		while(true) {
            
		}
	}

    public InfoPacket readClientInput() {
        try {

            System.out.println("Receiving packet from player at " + this.clientSocket + ".");

            InputStream inputStream = this.clientSocket.getInputStream();
            ObjectInputStream packetInputStream = new ObjectInputStream(inputStream);

            InfoPacket incomingPacket = (InfoPacket) packetInputStream.readObject();
            System.out.println("player_id: " + incomingPacket.get_player_id() +
                               "\naction: " + incomingPacket.get_action() + 
                               "\nCard: " + incomingPacket.get_card().get_rank() + incomingPacket.get_card().get_suit());

            return incomingPacket;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public byte[] readBytes() {
        try {
            System.out.println("Receiveing bytes from client "
                + this.clientSocket + ".");

            DataInputStream dataInputStream = new DataInputStream(
                this.clientSocket.getInputStream());
                    // A stream is a smaller river.
            int dataLength = dataInputStream.readInt();
                // the next four bytes of this input stream,
                // interpreted as an int.
            byte[] dataByteArray = new byte[dataLength];
            if (dataLength > 0) {
                dataInputStream.readFully(dataByteArray);
            }
            
            return dataByteArray;

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public void sendBytes(byte[] dataByteArray,
                                    int dataStart, int dataLength) {
        try {
            System.out.println("Sending bytes to client "
                + this.clientSocket + ".");

            DataOutputStream dataOutputStream = new DataOutputStream(
                this.clientSocket.getOutputStream());
                    // A stream is a smaller river.
            dataOutputStream.write(dataByteArray, dataStart, dataLength);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/*
* readBytes() and sendBytes() was adopted from
*   https://stackoverflow.com/questions/2878867/
*       how-to-send-an-array-of-bytes-over-a-tcp-connection-java-programming
*   This was used to send data in bytes, not in UTF.
*/