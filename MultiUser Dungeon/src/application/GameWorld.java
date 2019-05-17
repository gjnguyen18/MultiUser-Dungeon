package application;

import java.util.HashMap;

public class GameWorld {

	private HashMap<String,Room> rooms;
	
	public GameWorld() {
		rooms = new HashMap<String, Room>();
	}	
	
	public static String arrayToString(int[] ar) {
		String str = "";
		for(int i=0;i<ar.length;i++) {
			str+=" "+ar[i];
		}
		str = str.trim();
		return str;
	}
	
	public static int[] stringToArray(String str) {
		int[] ar = new int[3];
		String[] strAr = str.split(" ");
		for(int i=0;i<strAr.length;i++) {
			ar[i] = Integer.parseInt(strAr[i]);
		}
		return ar;
	}
	
	private boolean randomBool(int i) {
		return i>(int)(Math.random()*10);
	}
	
	public void setupStartRoom(String coords) {
		if(!rooms.containsKey(coords)) {
			rooms.put(coords, new Room(false));
		}
	}
	
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
	
	public boolean isSolid(String coords) {
		return rooms.get(coords).isSolid();
	}
}
