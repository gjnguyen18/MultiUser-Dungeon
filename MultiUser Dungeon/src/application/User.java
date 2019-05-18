package application;

/**
 * This class represents the user in the database
 * @author Gia-Phong Nguyen
 *
 */
public class User {
	
	private int id; // id of user
	private String name; // name of user
	private String coords; // room coordinates of user
	
	/**
	 * Constructor for user
	 * @param id
	 * @param name
	 * @param coords
	 */
	public User(int id, String name, String coords) {
		this.id = id;
		this.name = name;
		this.coords = coords;
	}
	
	/**
	 * @return id of user
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * @return name of user
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return coordinates of user
	 */
	public String getCoords() {
		return coords;
	}
	
	/**
	 * @param coordinates to set the user to
	 */
	public void setCoords(String c) {
		coords = c;
	}
}