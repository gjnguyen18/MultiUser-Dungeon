package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * This class represents the database of users
 * Users can be referenced by id or by name
 * Also rooms are in this database and they contain users
 * 
 * @author Gia-Phong Nguyen
 *
 */
public class UsersDatabase {
	
	HashMap<String,User> nameToUsers; // map of users by name
	HashMap<Integer,User> idToUsers; // map of users by id
	HashMap<String,ArrayList<User>> rooms; // map of all rooms with users in them by coordinates
	
	/**
	 * Constructor for user database
	 */
	public UsersDatabase() {
		nameToUsers = new HashMap<String,User>();
		idToUsers = new HashMap<Integer,User>();
		rooms = new HashMap<String,ArrayList<User>>();
	}
	
	/**
	 * Adds a user to the database
	 * @param id
	 * @param name
	 * @param coords
	 */
	public void addUser(int id, String name, String coords) {
		User user = new User(id,name,coords);
		nameToUsers.put(name, user);
		idToUsers.put(id, user);
		if(!rooms.containsKey(coords)) {  // creates room if it doesn't already exit
			rooms.put(coords, new ArrayList<User>());
		}
		rooms.get(coords).add(user);
	}
	
	/**
	 * Removes user from database by id
	 * @param id - id of user
	 */
	public void removeUser(int id) {
		User user = idToUsers.get(id);
		nameToUsers.remove(user.getName());
		idToUsers.remove(id);
		rooms.get(user.getCoords()).remove(user);
	}

	/**
	 * Gets all the users in a room
	 * @param coords - coordinates of room
	 * @return list of users
	 */
	public ArrayList<User> getUserInRoom(String coords) {
		return rooms.get(coords);
	}
	
	/**
	 * @return set of all ids
	 */
	public Set<Integer> getAllUserIDs() {
		return idToUsers.keySet();
	}
	
	/**
	 * @return set of all usernames
	 */
	public Set<String> getAllUsernames() {
		return nameToUsers.keySet();
	}
	
	/**
	 * Relocate a user to a new position and updates database
	 * @param id - id of user
	 * @param newCoords - new coordinates of user
	 */
	public void moveUser(int id, String newCoords) {
		if(!rooms.containsKey(newCoords)) { // creates room if it doesn't already exit
			rooms.put(newCoords, new ArrayList<User>());
		}
		User user = idToUsers.get(id);
		rooms.get(user.getCoords()).remove(user); // removes user from current room
		rooms.get(newCoords).add(user);
		user.setCoords(newCoords);
	}
	
	/**
	 * Gets the coordinates of a user by id
	 * @param id - id of user
	 * @return coordinates of user
	 */
	public String getCoordsOfUserID(int id) {
		return idToUsers.get(id).getCoords();
	}
	
	/**
	 * Gets the name of a user by id
	 * @param id - id of user
	 * @return name of user
	 */
	public String getNameOfID(int id) {
		return idToUsers.get(id).getName();
	}
	
	/**
	 * Gets the id of a user by name
	 * @param name - name of user
	 * @return id of user
	 */
	public int getIDOfName(String name) {
		return nameToUsers.get(name).getID();
	}
	
	/**
	 * @return number of users in the database
	 */
	public int size() {
		return idToUsers.size();
	}
	
	/**
	 * @param id - id of user
	 * @return true if user exists
	 */
	public boolean userExists(int id) {
		return idToUsers.containsKey(id);
	}
	
	/**
	 * @param name - name of user
	 * @return true if user exists
	 */
	public boolean userExists(String name) {
		return nameToUsers.containsKey(name);
	}
}
