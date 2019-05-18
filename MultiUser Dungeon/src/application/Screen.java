package application;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * This abstract class represents a screen. Multiple instances of classes which inherit screen
 * may exist at the same time, but only one would be showing at a time.
 * 
 * @author Gia-Phong Nguyen
 *
 */
public class Screen {

	private Group group; // this screen's group of elements
	private Group root; // root all gui

	/**
	 * Constructor for screen
	 * @param root - group of all elements
	 */
	public Screen(Group root) {
		group = new Group();
		this.root = root;
		root.getChildren().add(group);
	}

	/**
	 * Show all elements of screen
	 */
	public void show() {
		group.setVisible(true);
	}

	/**
	 * Hide all elements of screen
	 */
	public void hide() {
		group.setVisible(false);
	}
	
	/**
	 * Gets the root of the group
	 * @return the root
	 */
	protected Group getRoot() {
		return root;
	}

	/**
	 * Adds element to the screen
	 * @param n - node to be added
	 */
	public void addElement(Node n) {
		group.getChildren().add(n);
	}

	/**
	 * Removes element from the screen
	 * @param n - node to remove
	 */
	public void removeElement(Node n) {
		group.getChildren().remove(n);
	}
	
	/**
	 * Deletes this screen
	 */
	public void deleteScreen() {
		root.getChildren().remove(group);
		group = null;
	}
}
