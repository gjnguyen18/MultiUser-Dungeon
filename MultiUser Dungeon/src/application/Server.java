package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {

	private ServerSocket sSocket;
	private HashMap<Integer,ServerSideClientThread> clientThreads;
	private ExecutorService pool;
	private int nextID;
	private ArrayList<String> log;
	
	private UsersDatabase database;
	private GameWorld world;
	
	public Server(int port) {
		sSocket = null;
		clientThreads = new HashMap<Integer,ServerSideClientThread>();
		nextID = 0;
		world = new GameWorld();
		log = new ArrayList<String>();
		database = new UsersDatabase();
		
		try {
			sSocket = new ServerSocket(0); // use 0 to automatically find port
			System.out.println("Running Server");
			pool = Executors.newCachedThreadPool();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Socket socket = sSocket.accept();
				ServerSideClientThread ch = new ServerSideClientThread(socket, this, nextID);
				clientThreads.put(nextID,ch);
				pool.execute(ch);
				ch.sendToClient(nextID + " fill /setID");
				nextID++;
			} catch (IOException e) {
				break;
			}
		}
	}
	
	public void endServer() {
		for(int i:clientThreads.keySet()) {
			try {
				clientThreads.get(i).getSocket().close();
			} catch (IOException e) {
			}
			clientThreads.get(i).interrupt();
		}
		try {
			sSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pool.shutdownNow();
		Thread.currentThread().interrupt();
	}
	
	public void interpretMessage(String str) {
		log.add(str);
		String[] instruction = str.trim().split(" ");
		int id = Integer.parseInt(instruction[0]);
		String message = "";
		switch(instruction[2]) {
		case "/joined":
			for(int i:clientThreads.keySet()) {
				clientThreads.get(i).sendToClient("fill fill /global " + instruction[1] + " has entered the dungeon");
			}
			database.addUser(id, instruction[1], instruction[3]+" "+instruction[4]+" "+instruction[5]);
			spawnUser(id,instruction[3]+" "+instruction[4]+" "+instruction[5]);
			break;
		case "/left":
			for(int i:clientThreads.keySet()) {
				clientThreads.get(i).sendToClient("/global " + instruction[1] + " has left the dungeon");
			}
			database.removeUser(id);
			break;
		case "/tell":
			message+="fill fill /noPretext [PRIVATE] You -> " + instruction[3] + ":";
			for(int i=4;i<instruction.length;i++) {
				message+=" " + instruction[i];
			}
			sendToUser(message,id);
			sendToUser(str,database.getIDOfName(instruction[3]));
			break;
		case "/say":
			sendToRoom(str,id);
			break;
		case "/move":
			moveUser(id,instruction[3]);
			break;
		default:
			for(int i:clientThreads.keySet()) {
				clientThreads.get(i).sendToClient(str);
			}
			break;
		}
	}
	
	private void sendToUser(String message, int userID) {
		clientThreads.get(userID).sendToClient(message);
	}
	
	private void sendToRoom(String message, int userID) {
		for(User i:database.getUserInRoom(database.getCoordsOfUserID(userID))) {
			clientThreads.get(i.getID()).sendToClient(message);
		}
	}
	
	private void spawnUser(int userID, String coords) {
		world.setupStartRoom(coords);
		world.updateNearbyRooms(GameWorld.stringToArray(coords));
		int[] northCoords = GameWorld.stringToArray(coords);
		northCoords[0]+=1;
		int[] southCoords = GameWorld.stringToArray(coords);
		southCoords[0]-=1;
		int[] eastCoords = GameWorld.stringToArray(coords);
		eastCoords[1]+=1;
		int[] westCoords = GameWorld.stringToArray(coords);
		westCoords[1]-=1;
		int[] upCoords = GameWorld.stringToArray(coords);
		upCoords[2]+=1;
		int[] downCoords = GameWorld.stringToArray(coords);
		downCoords[2]-=1;
		clientThreads.get(userID).sendToClient("fill fill /spawn " + coords
				+ " " + world.isSolid(GameWorld.arrayToString(northCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(southCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(eastCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(westCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(upCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(downCoords))
		);
	}
	
	// North south east west up down / +x -x +y -y +z -z
	private void moveUser(int userID, String str) {
		int[] coords = GameWorld.stringToArray(database.getCoordsOfUserID(userID));
		switch(str) {
		case "north":
			coords[0]+=1;
			break;
		case "south":
			coords[0]-=1;
			break;
		case "east":
			coords[1]+=1;
			break;
		case "west":
			coords[1]-=1;
			break;
		case "up":
			coords[2]+=1;
			break;
		case "down":
			coords[2]-=1;
			break;
		}
		String newCoords = GameWorld.arrayToString(coords);
		database.moveUser(userID, newCoords);
		world.updateNearbyRooms(GameWorld.stringToArray(newCoords));
		
		int[] northCoords = GameWorld.stringToArray(newCoords);
		northCoords[0]+=1;
		int[] southCoords = GameWorld.stringToArray(newCoords);
		southCoords[0]-=1;
		int[] eastCoords = GameWorld.stringToArray(newCoords);
		eastCoords[1]+=1;
		int[] westCoords = GameWorld.stringToArray(newCoords);
		westCoords[1]-=1;
		int[] upCoords = GameWorld.stringToArray(newCoords);
		upCoords[2]+=1;
		int[] downCoords = GameWorld.stringToArray(newCoords);
		downCoords[2]-=1;
		clientThreads.get(userID).sendToClient("fill fill /reposition " + newCoords
				+ " " + world.isSolid(GameWorld.arrayToString(northCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(southCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(eastCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(westCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(upCoords))
				+ " " + world.isSolid(GameWorld.arrayToString(downCoords))
				+ " " + str
		);
	}
	
	public void removeUser(int id) {
		interpretMessage(id + " " + database.getNameOfID(id) +" /left");
		clientThreads.remove(id);
	}
	
	public int getPort() {
		return sSocket.getLocalPort();
	}
}
