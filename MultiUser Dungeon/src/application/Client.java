package application;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

/**
 * This class represents the client, it sends data to the server
 * @author Gia-Phong Nguyen
 *
 */
public class Client {

	private String ip; // ip address of server
	private int port; // port of server
	private Socket cSocket; // client side socket
	private PrintStream tOut; // stream of data to send to server
	private ClientReciever clientThread; // thread which recieves data from server
	private Server server; // your server if you host
	private int id; // id of client
	private String username; // username of client
	private GameScreen gameScreen; // game screen
	
	/**
	 * Connects the client to the server
	 * @param link - server link
	 * @param name - username
	 * @return true of connection reaches
	 */
	public boolean setupClient(String link, String name) {
		try {
			if(link.equals("host")) { // if client is host
				server = new Server(0); // starts server
				server.start();
				port = server.getPort(); // gets port
				this.ip = Server.getIPv4(); // ip for network
				System.out.println("Server Link: "+LinkEncoders.convertToLink(ip,port)); 
				gameScreen.setupRoomLinkLabel(LinkEncoders.convertToLink(ip,port)); // gets link
			}
			else {
				String[] data = LinkEncoders.decodeLink(link); // converts link to ip and port
				this.ip = data[0];
				this.port = Integer.parseInt(data[1]);
				gameScreen.setupRoomLinkLabel(link);
			}
			
			cSocket = new Socket(this.ip, this.port); // creates client socket
			tOut = new PrintStream(cSocket.getOutputStream()); // data stream to server
			
			clientThread = new ClientReciever(this, cSocket); // creates client receiver thread
			clientThread.start();
			this.username = name;
		} catch (IOException | StringIndexOutOfBoundsException | IllegalArgumentException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Sends a message to the server to be interpreted
	 * @param str - message
	 */
	public void sendMessage(String str) {
		tOut.println(this.id + " " + username + " " + str);
	}
	
	/**
	 * Takes a message and interprets it
	 * These messages apply changes to the game screen whether it be the console or movement
	 * @param str - message to be interpreted
	 */
	public void takeMessage(String str) {
		String[] instruction = str.trim().split(" ");
		String message = "";
		switch(instruction[2]) {
		case "/tell": // private message
			message+="[PRIVATE] "+instruction[1]+ " -> You:";
			for(int i=4;i<instruction.length;i++) {
				message+=(" " + instruction[i]);
				}
			gameScreen.addMessage(message);
			break;
		case "/say": // message to everyone in room
			message+="[ROOM] "+instruction[1]+":";
			for(int i=3;i<instruction.length;i++) {
				message+=(" " + instruction[i]);
				}
			gameScreen.addMessage(message);
			break;
		case "/setID": // when client first connects, it is sent this message right away to set up id
			this.id = Integer.parseInt(instruction[0]);
			int x = (int)(Math.random()*10)-5;
			int y = (int)(Math.random()*10)-5;
			int z = (int)(Math.random()*10)-5;
			sendMessage("/joined "+x+" "+y+" "+z); // sets random position for client and tells server client joined
			break;
		case "/exit": // exits program
			gameScreen.addMessage("GlOBAL: " + instruction[1] + " has left the room");
			break;
		case "/yell": // when client yells to the world
			message+="[WORLD] "+instruction[1] + ":";
			for(int i=3;i<instruction.length;i++) {
				message+=" " + instruction[i];
				}
			gameScreen.addMessage(message);
			break;
		case "/global": // global message
			message+=("GlOBAL:");
			for(int i=3;i<instruction.length;i++) {
				message+=" " + instruction[i];
			}
			gameScreen.addMessage(message);
			break;
		case "/noPretext": // other custom things for console to show
			for(int i=3;i<instruction.length;i++) {
				message+=instruction[i]+" ";
			}
			gameScreen.addMessage(message);
			break;
		case "/reposition": // repositions the user
			gameScreen.setCoords(instruction[3], instruction[4], instruction[5]);
			gameScreen.setNorth(Boolean.parseBoolean(instruction[6]));
			gameScreen.setSouth(Boolean.parseBoolean(instruction[7]));
			gameScreen.setEast(Boolean.parseBoolean(instruction[8]));
			gameScreen.setWest(Boolean.parseBoolean(instruction[9]));
			gameScreen.setUp(Boolean.parseBoolean(instruction[10]));
			gameScreen.setDown(Boolean.parseBoolean(instruction[11]));
//			gameScreen.addMessage("You moved " + instruction[12] + " to: x=" + instruction[3] + 
//					" y="  +instruction[4] + " z=" + instruction[5]);
			break;
		case "/spawn": // spawns user
			gameScreen.addMessage("You have spawned at: x=" + instruction[3] + 
					" y="  +instruction[4] + " z=" + instruction[5]);
			gameScreen.setCoords(instruction[3], instruction[4], instruction[5]);
			gameScreen.setNorth(Boolean.parseBoolean(instruction[6]));
			gameScreen.setSouth(Boolean.parseBoolean(instruction[7]));
			gameScreen.setEast(Boolean.parseBoolean(instruction[8]));
			gameScreen.setWest(Boolean.parseBoolean(instruction[9]));
			gameScreen.setUp(Boolean.parseBoolean(instruction[10]));
			gameScreen.setDown(Boolean.parseBoolean(instruction[11]));
			break;
		case "/take":
			switch(instruction[3]) {
			case "gold":
				int amount = Integer.parseInt(instruction[4]);
				gameScreen.incrementGold(amount);
				gameScreen.addMessage("You got "+amount+" gold!");
				break;
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * @param gameScreen Game Screen
	 */
	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}
	
	/**
	 * @return id of client
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Disconnects from the server and exits to home screen
	 * @param showAlert - show alert if showAlert is true
	 */
	public void disconnect(boolean showAlert) {
		try {
			if(clientThread!=null) {
				clientThread.interrupt();
				clientThread = null;
			}
			if(cSocket!=null) {
				cSocket.close(); // closes socket to server
				cSocket = null;
			}
			if(id==0 && server!=null)
				server.interrupt(); // if client is also host, ends the server
	    gameScreen.exit(); // reterns to home screen
	    System.out.println("disconnected");
	    if(id!=0 && showAlert) {
				Platform.runLater(new Runnable() {
		      @Override
		      public void run() {
				    Alert alert = new Alert(AlertType.INFORMATION, "Host Disconnected", ButtonType.OK);
						alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
						alert.show();
		      }
		    });
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
