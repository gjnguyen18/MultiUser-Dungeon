package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

public class ClientReciever extends Thread {

	private BufferedReader sIn2;
	private Client client;
	private Socket socket;

	public ClientReciever(Client client, Socket cSocket) {
		this.client = client;
		try {
			sIn2 = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
			this.socket = cSocket;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while(true) {
				try {
					String message = sIn2.readLine();
					client.takeMessage(message);
				} catch (SocketException e) {
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (NullPointerException e) {
			client.disconnect(true);
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
}
