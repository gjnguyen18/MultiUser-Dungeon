package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class represents the reciever thread which recieves data from the server
 * Runs on the side to continuously recieve data
 * @author Gia-Phong Nguyen
 *
 */
public class ClientReciever extends Thread {

	private BufferedReader sIn2; // reads data stream
	private Client client; // client
	private Socket socket; // client socket

	/**
	 * Constructor for client
	 * @param client - attached client
	 * @param cSocket - client socket
	 */
	public ClientReciever(Client client, Socket cSocket) {
		this.client = client;
		try {
			sIn2 = new BufferedReader(new InputStreamReader(cSocket.getInputStream())); // sets up reader
			this.socket = cSocket;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while(true) { // continuously checks for new messages from server
				try {
					String message = sIn2.readLine();
					client.takeMessage(message); // sends message to client
				} catch (SocketException e) {
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (NullPointerException e) { // when server disconnects
			client.disconnect(true); // disconnect the client
		}
	}
	
	/**
	 * @return socket
	 */
	public Socket getSocket() {
		return socket;
	}
}
