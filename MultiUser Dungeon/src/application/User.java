package application;

public class User {
	
	private int id;
	private String name;
	private String coords;
	
	public User(int id, String name, String coords) {
		this.id = id;
		this.name = name;
		this.coords = coords;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCoords() {
		return coords;
	}
	
	public void setCoords(String c) {
		coords = c;
	}
}