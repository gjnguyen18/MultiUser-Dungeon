package application;

import java.util.ArrayList;

public class Room {

	private ArrayList<String> usersInRoom;
	private int[] coordinates;
	private boolean isSolid;
	
	public Room(boolean isSolid) {
		this.isSolid = isSolid;
		if(!isSolid)
			usersInRoom = new ArrayList<String>();
	}
	
	public ArrayList<String> getUsersInRoom() {
		return usersInRoom;
	}
	
	public boolean isSolid() {
		return isSolid;
	}
}
