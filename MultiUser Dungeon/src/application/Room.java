package application;

/**
 * This class represents a room in the dungeon
 * @author Gia-Phong Nguyen
 *
 */
public class Room {

	private int[] coordinates; // coordinates of room
	private boolean isSolid; // whether the room is solid or not
	
	/**
	 * Constructor for room
	 * @param isSolid - set the room to solid
	 */
	public Room(boolean isSolid) {
		this.isSolid = isSolid;
	}
	
	/**
	 * @return whether or not the room is solid
	 */
	public boolean isSolid() {
		return isSolid;
	}
}
