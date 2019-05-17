package application;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

public class Client {

	private String ip;
	private int port;
	private Socket cSocket;
	private PrintStream tOut;
	private ClientReciever clientThread;
	private Scanner sc;
	private Server server;
	private int id;
	private String username;
	
	private GameScreen gameScreen;
	
	public boolean setupClient(String link, String name) {
		sc = new Scanner(System.in);
		try {
			if(link.equals("host")) {
				server = new Server(0);
				server.start();
				port = server.getPort();
				this.ip = "127.0.0.1";
				System.out.println("Server Link: "+LinkEncoders.convertToLink(ip,port));
				gameScreen.setupRoomLinkLabel(LinkEncoders.convertToLink(ip,port));
			}
			else {
				String[] data = LinkEncoders.decodeLink(link);
				this.ip = data[0];
				this.port = Integer.parseInt(data[1]);
				gameScreen.setupRoomLinkLabel(link);
			}
			
			cSocket = new Socket(this.ip, this.port);
			tOut = new PrintStream(cSocket.getOutputStream());
			
			clientThread = new ClientReciever(this, cSocket);
			clientThread.start();
			
			this.username = name;
		} catch (IOException | StringIndexOutOfBoundsException | IllegalArgumentException e) {
			return false;
		}
		return true;
	}
	
	public void sendMessage(String str) {
		tOut.println(this.id + " " + username + " " + str);
	}
	
	public void takeMessage(String str) {
		String[] instruction = str.trim().split(" ");
		String message = "";
		switch(instruction[2]) {
		case "/tell":
			message+="[PRIVATE] "+instruction[1]+ " -> You:";
			for(int i=4;i<instruction.length;i++) {
				message+=(" " + instruction[i]);
				}
			gameScreen.addMessage(message);
			break;
		case "/say":
			message+="[ROOM] "+instruction[1]+":";
			for(int i=3;i<instruction.length;i++) {
				message+=(" " + instruction[i]);
				}
			gameScreen.addMessage(message);
			break;
		case "/setID":
			this.id = Integer.parseInt(instruction[0]);
			int x = (int)(Math.random()*10)-5;
			int y = (int)(Math.random()*10)-5;
			int z = (int)(Math.random()*10)-5;
			sendMessage("/joined "+x+" "+y+" "+z);
			break;
		case "/exit":
			gameScreen.addMessage("GlOBAL: " + instruction[1] + " has left the room");
			break;
		case "/yell":
			message+="[WORLD] "+instruction[1] + ":";
			for(int i=3;i<instruction.length;i++) {
				message+=" " + instruction[i];
				}
			gameScreen.addMessage(message);
			break;
		case "/global":
			message+=("GlOBAL:");
			for(int i=3;i<instruction.length;i++) {
				message+=" " + instruction[i];
			}
			gameScreen.addMessage(message);
			break;
		case "/noPretext":
			for(int i=3;i<instruction.length;i++) {
				message+=instruction[i]+" ";
			}
			gameScreen.addMessage(message);
			break;
		case "/reposition":
			gameScreen.setCoords(instruction[3], instruction[4], instruction[5]);
			gameScreen.setNorth(Boolean.parseBoolean(instruction[6]));
			gameScreen.setSouth(Boolean.parseBoolean(instruction[7]));
			gameScreen.setEast(Boolean.parseBoolean(instruction[8]));
			gameScreen.setWest(Boolean.parseBoolean(instruction[9]));
			gameScreen.setUp(Boolean.parseBoolean(instruction[10]));
			gameScreen.setDown(Boolean.parseBoolean(instruction[11]));
			gameScreen.addMessage("You moved " + instruction[12] + " to: x=" + instruction[3] + 
					" y="  +instruction[4] + " z=" + instruction[5]);
			break;
		case "/spawn":
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
		default:
			break;
		}
	}
	
	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}
	
	public int getID() {
		return id;
	}
	
	public void disconnect(boolean showAlert) {
		try {
			if(clientThread!=null) {
				clientThread.getSocket().close();
				clientThread.interrupt();
				clientThread = null;
			}
			if(cSocket!=null) {
				cSocket.close();
				cSocket = null;
			}
			if(id==0 && server!=null)
				server.interrupt();
	    gameScreen.exit();
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
