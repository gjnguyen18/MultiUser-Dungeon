package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class represents the server side thread which sends data to the client
 * @author Gia-Phong Nguyen
 *
 */
public class ServerSideClientThread extends Thread{
	private Socket socket; // socket which sends to client
	private Server server; // server
	private int id; // id of the client
	
	private BufferedReader sIn2; // reader for data from client
	private PrintStream tOut; // out stream to client reciever

	/**
	 * Constructor for server side thread
	 * @param socket - socket connecting to client
	 * @param server
	 * @param id - id of client
	 */
	public ServerSideClientThread(Socket socket, Server server, int id) {
		this.socket = socket;
		this.server = server;
		this.id = id;
		
		try {
			sIn2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			tOut = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while(true) { // continously checks for new messages
				try {
					String message = sIn2.readLine();
					server.interpretMessage(message);
				} catch(SocketException e) {
					throw new NullPointerException();
				}
			}
		} catch( NullPointerException e) { // when sockets closes
			server.removeUser(id);
			if(id==0) {
				server.endServer();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Sends a message to the client
	 * @param message
	 */
	protected void sendToClient(String message) {
		tOut.println(message);
	}
	
	/**
	 * Gets the socket
	 * @return socket
	 */
	public Socket getSocket() {
		return socket;
	}
}
