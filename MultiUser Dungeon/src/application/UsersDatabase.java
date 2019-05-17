package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class UsersDatabase {
	
	HashMap<String,User> nameToUsers;
	HashMap<Integer,User> idToUsers;
	HashMap<String,ArrayList<User>> rooms;
	
	public UsersDatabase() {
		nameToUsers = new HashMap<String,User>();
		idToUsers = new HashMap<Integer,User>();
		rooms = new HashMap<String,ArrayList<User>>();
	}
	
	public void addUser(int id, String name, String coords) {
		User user = new User(id,name,coords);
		nameToUsers.put(name, user);
		idToUsers.put(id, user);
		if(!rooms.containsKey(coords)) {
			rooms.put(coords, new ArrayList<User>());
		}
		rooms.get(coords).add(user);
	}
	
	public void removeUser(int id) {
		User user = idToUsers.get(id);
		nameToUsers.remove(user.getName());
		idToUsers.remove(id);
		rooms.get(user.getCoords()).remove(user);
	}

	public ArrayList<User> getUserInRoom(String coords) {
		return rooms.get(coords);
	}
	
	public Set<Integer> getAllUserIDs() {
		return idToUsers.keySet();
	}
	
	public Set<String> getAllUsernames() {
		return nameToUsers.keySet();
	}
	
	public void moveUser(int id, String newCoords) {
		if(!rooms.containsKey(newCoords)) {
			rooms.put(newCoords, new ArrayList<User>());
		}
		User user = idToUsers.get(id);
		rooms.get(user.getCoords()).remove(user);
		rooms.get(newCoords).add(user);
		user.setCoords(newCoords);
	}
	
	public String getCoordsOfUserID(int id) {
		return idToUsers.get(id).getCoords();
	}
	
	public String getNameOfID(int id) {
		return idToUsers.get(id).getName();
	}
	
	public int getIDOfName(String name) {
		return nameToUsers.get(name).getID();
	}
	
	public int size() {
		return idToUsers.size();
	}
}
