package application;

import java.util.HashMap;

/**
 * This class represents the world map, containing all the rooms of the world
 * @author Gia-Phong Nguyen
 *
 */
public class GameWorld {

	private HashMap<String,Room> rooms; // map of all rooms
	
	/**
	 * Constructor
	 * Initializes the map of rooms
	 */
	public GameWorld() {
		rooms = new HashMap<String, Room>();
	}	
	
	/**
	 * Method for converting an array representing coordinates to a string
	 * @param ar - array to convert
	 * @return String of coordinates
	 */
	public static String arrayToString(int[] ar) {
		String str = "";
		for(int i=0;i<ar.length;i++) {
			str+=" "+ar[i];
		}
		str = str.trim();
		return str;
	}
	
	/**
	 * Method for converting a string of coordinates to an array
	 * @param str - string to convert
	 * @return array representing coordinates
	 */
	public static int[] stringToArray(String str) {
		int[] ar = new int[3];
		String[] strAr = str.split(" ");
		for(int i=0;i<strAr.length;i++) {
			ar[i] = Integer.parseInt(strAr[i]);
		}
		return ar;
	}
	
	/**
	 * Generates a boolean based on whether or not i is higher than a random int fomr 1-10
	 * @param i - weight
	 * @return randomized boolean
	 */
	private boolean randomBool(int i) {
		return i>(int)(Math.random()*10);
	}
	
	/**
	 * Adds a single room to map if it doesnt already exist
	 * @param coords - coordinates of room
	 */
	public void setupStartRoom(String coords) {
		if(!rooms.containsKey(coords)) {
			rooms.put(coords, new Room(false));
		}
	}
	
	/**
	 * Initizlies all the nearby rooms of a current room if they don't already exist
	 * @param coord - coordinate of room
	 */
	public void updateNearbyRooms(int[] coord) {
		if(!rooms.containsKey((coord[0]+1)+" "+(coord[1])+" "+(coord[2]))) {
			rooms.put((coord[0]+1)+" "+(coord[1])+" "+(coord[2]), new Room(randomBool(3)));
		}
		if(!rooms.containsKey((coord[0]-1)+" "+(coord[1])+" "+(coord[2]))) {
			rooms.put((coord[0]-1)+" "+(coord[1])+" "+(coord[2]), new Room(randomBool(3)));
		}
		if(!rooms.containsKey((coord[0])+" "+(coord[1]+1)+" "+(coord[2]))) {
			rooms.put((coord[0])+" "+(coord[1]+1)+" "+(coord[2]), new Room(randomBool(3)));
		}
		if(!rooms.containsKey((coord[0])+" "+(coord[1]-1)+" "+(coord[2]))) {
			rooms.put((coord[0])+" "+(coord[1]-1)+" "+(coord[2]), new Room(randomBool(3)));
		}
		if(!rooms.containsKey((coord[0])+" "+(coord[1])+" "+(coord[2]+1))) {
			rooms.put((coord[0])+" "+(coord[1])+" "+(coord[2]+1), new Room(randomBool(3)));
		}
		if(!rooms.containsKey((coord[0])+" "+(coord[1])+" "+(coord[2]-1))) {
			rooms.put((coord[0])+" "+(coord[1])+" "+(coord[2]-1), new Room(randomBool(3)));
		}
	}
	
	/**
	 * Determines if a room is solid
	 * @param coords - coordinates of room
	 * @return true if room is solid
	 */
	public boolean isSolid(String coords) {
		return rooms.get(coords).isSolid();
	}
	
	/**
	 * Takes gold from room
	 * @param amount - amount to take
	 * @parm coords - coordinates of room
	 * @return amount taken
	 */
	public int takeGold(int amount, String coords) {
		Room room = rooms.get(coords);
		return room.takeGold(amount);
	}
	
	/**
	 * @param coords - coordinates of room
	 * @return gold in room
	 */
	public int goldAmount(String coords) {
		return rooms.get(coords).goldAmount();
	}
}
