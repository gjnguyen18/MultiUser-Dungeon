package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerSideClientThread extends Thread{
	private Socket socket;
	private Server server;
	private int id;
	
	private BufferedReader sIn2;
	private PrintStream tOut;

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
			while(true) {
				try {
					String message = sIn2.readLine();
					server.interpretMessage(message);
				} catch(SocketException e) {
					throw new NullPointerException();
				}
			}
		} catch( NullPointerException e) {
			server.removeUser(id);
			if(id==0) {
				server.endServer();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	protected void sendToClient(String message) {
		tOut.println(message);
	}
	
	public Socket getSocket() {
		return socket;
	}
}
