package application;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the server which connects all players
 * @author Gia-Phong Nguyen
 *
 */
public class Server extends Thread {

	private ServerSocket sSocket; // server socket
	private HashMap<Integer,ServerSideClientThread> clientThreads; // threads for each client
	private ExecutorService pool; // executes all the client threads
	private int nextID; // id of next new user, incremented automatically
	private ArrayList<String> log; // log of all instructions sent
	
	private UsersDatabase database; // database of all users
	private GameWorld world; // world map
	
	/**
	 * Initializes server and its threads
	 * @param port
	 */
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
		while(true) { // continiously waits for new users to join to assign id
			try {
				Socket socket = sSocket.accept();
				ServerSideClientThread ch = new ServerSideClientThread(socket, this, nextID);
				clientThreads.put(nextID,ch);
				pool.execute(ch);
				ch.sendToClient(nextID + " fill /setID"); // assigns id
				nextID++; // increment id
			} catch (IOException e) {
				break;
			}
		}
	}
	
	/**
	 * Ends the server and forces everyone to leave
	 */
	public void endServer() {
		for(int i:clientThreads.keySet()) {
			try {
				clientThreads.get(i).getSocket().close(); // closes all sockets
			} catch (IOException e) {
			}
			clientThreads.get(i).interrupt();
		}
		try {
			sSocket.close(); // closes server socket
		} catch (IOException e) {
			e.printStackTrace();
		}
		pool.shutdownNow(); // ends all threads
		Thread.currentThread().interrupt();
	}
	
	/**
	 * Interprets a message and does appropriate action
	 * @param str - message
	 */
	public void interpretMessage(String str) {
		log.add(str);
		String[] instruction = str.trim().split(" ");
		int id = Integer.parseInt(instruction[0]);
		String message = "";
		switch(instruction[2]) {
		case "/joined": // player has joined
			for(int i:clientThreads.keySet()) { // tells everyone this player has joined
				clientThreads.get(i).sendToClient("fill fill /global " + instruction[1] + " has entered the dungeon");
			}
			database.addUser(id, instruction[1], instruction[3]+" "+instruction[4]+" "+instruction[5]);
			spawnUser(id,instruction[3]+" "+instruction[4]+" "+instruction[5]);
			break;
		case "/left": // player has left
			for(int i:clientThreads.keySet()) { // tells everyone this player has left
				clientThreads.get(i).sendToClient("/global " + instruction[1] + " has left the dungeon");
			}
			database.removeUser(id);
			break;
		case "/tell": // private message
			message+="fill fill /noPretext [PRIVATE] You -> " + instruction[3] + ":";
			for(int i=4;i<instruction.length;i++) {
				message+=" " + instruction[i];
			}
			if(!database.userExists(instruction[3])) {
				sendToUser("fill fill /noPretext [GAME] user "+ instruction[3]+" could not be found",id);
			}
			else if(instruction.length<5) {
				sendToUser("fill fill /noPretext [GAME] /tell format: /tell <user> <message>",id);
			}
			else {
				sendToUser(str,database.getIDOfName(instruction[3]));
				sendToUser(message,id); // only sends instruction to receiver
			}
			break;
		case "/say": // tells everyone a room a message
			if(instruction.length<4)
				sendToUser("fill fill /noPretext [GAME] /say format: /say <message>",id);
			else
				sendToRoom(str,id);
			break;
		case "/move": // moves player
			moveUser(id,instruction[3]);
			break;
		case "/take":
			if(instruction.length<5)
				sendToUser("fill fill /noPretext [GAME] /take format: /take <item> <amount>",id);
			else {
				try {
					take(id,instruction[3],Integer.parseInt(instruction[4]));
				} catch(NumberFormatException e) {
					sendToUser("fill fill /noPretext [GAME] /take format: /take <item> <amount>",id);
				}
			}
			break;
		case "/showItems":
			if(instruction.length>3)
				sendToUser("fill fill /noPretext [GAME] /showItemsInRoom format: /showItemsInRoom <no-args>",id);
			else
				showItemsInRoom(id);
			break;
		default:
			for(int i:clientThreads.keySet()) {
				clientThreads.get(i).sendToClient(str);
			}
			break;
		}
	}
	
	/**
	 * Show the items in a room (currently only gold)
	 * @param id - id of user
	 */
	private void showItemsInRoom(int id) {
		int amount = world.goldAmount(database.getCoordsOfUserID(id));
		sendToUser("fill fill /noPretext [GAME] there is: "+amount+" gold in this room",id);
	}
	
	/**
	 * Takes item from dungeon room and updates client
	 * @param id - id of user
	 * @param item - item to take
	 * @param amount - amount of item to take
	 */
	private void take(int id, String item, int amount) {
		switch(item) {
		case "gold":
			int goldTaken = world.takeGold(amount, database.getCoordsOfUserID(id));
			sendToUser("fill fill /take gold " + goldTaken,id);
			break;
		default:
			sendToUser("fill fill /noPretext [GAME] <" + item + "> cannot be found in this room",id);
		}
	}
	
	/**
	 * Sends a message to a specific user
	 * @param message
	 * @param userID - user to send message to
	 */
	private void sendToUser(String message, int userID) {
		clientThreads.get(userID).sendToClient(message);
	}
	
	/**
	 * Sends message to all users in a room corresponding to another user
	 * @param message
	 * @param userID - user in room
	 */
	private void sendToRoom(String message, int userID) {
		for(User i:database.getUserInRoom(database.getCoordsOfUserID(userID))) {
			clientThreads.get(i.getID()).sendToClient(message);
		}
	}
	
	/**
	 * Spawns user into world
	 * @param userID
	 * @param coords - coordinates of the spawn location
	 */
	private void spawnUser(int userID, String coords) {
		world.setupStartRoom(coords);
		world.updateNearbyRooms(GameWorld.stringToArray(coords)); // updates world
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
	
	// North south east west up down | +x -x +y -y +z -z
	/**
	 * Moves the user in a direction and updates database
	 * @param userID
	 * @param str - movement
	 */
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
		world.updateNearbyRooms(GameWorld.stringToArray(newCoords)); // updates world
		
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
	
	/**
	 * Remvoes a user from the database
	 * @param id - id of user
	 */
	public void removeUser(int id) {
		interpretMessage(id + " " + database.getNameOfID(id) +" /left");
		clientThreads.remove(id);
	}
	
	/**
	 * @return Port of server
	 */
	public int getPort() {
		return sSocket.getLocalPort();
	}
	
	/**
	 * Gets the IPv4 of the server
	 * Code from stack overflow: https://stackoverflow.com/questions/40912417/java-getting-ipv4-address
	 * @return IPv4 as string
	 */
	public static String getIPv4() {
		String ip;
		try {
		    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		    while (interfaces.hasMoreElements()) {
		        NetworkInterface iface = interfaces.nextElement();
		        if (iface.isLoopback() || !iface.isUp())
		            continue;
		        Enumeration<InetAddress> addresses = iface.getInetAddresses();
		        while(addresses.hasMoreElements()) {
		            InetAddress addr = addresses.nextElement();
		            if (addr instanceof Inet4Address){
		            ip = addr.getHostAddress();
		            return ip;
		            }
		        }
		    }
		} catch (SocketException e) {
		    throw new RuntimeException(e);
		}
		return "null";
	}
}
