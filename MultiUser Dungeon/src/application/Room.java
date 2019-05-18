package application;

/**
 * This class represents a room in the dungeon
 * @author Gia-Phong Nguyen
 *
 */
public class Room {

	private int[] coordinates; // coordinates of room
	private boolean isSolid; // whether the room is solid or not
	private int gold; // gold in room
	
	/**
	 * Constructor for room
	 * @param isSolid - set the room to solid
	 */
	public Room(boolean isSolid) {
		this.isSolid = isSolid;
		gold = (int)(Math.random()*100);
	}
	
	/**
	 * @return whether or not the room is solid
	 */
	public boolean isSolid() {
		return isSolid;
	}
	
	/**
	 * Takes gold from room
	 * @param amount - amount to take
	 * @return amount taken
	 */
	public int takeGold(int amount) {
		if(amount>gold) { // if amount to take exceeds gold
			int temp = gold;
			gold = 0;
			return temp;
		}
		else {
			gold -= amount;
			return amount;
		}
	}
	
	/**
	 * @return gold in room
	 */
	public int goldAmount() {
		return gold;
	}
}
