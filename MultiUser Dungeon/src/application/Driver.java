package application;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * This class represents the driver which sets everything up and puts it together
 * @author Gia-Phong Nguyen
 *
 */
public class Driver {
	
	public static int screenWidth = 800;
	public static int screenHeight = 600;

	private HomeScreen homeScreen;
	private SetupScreen setupScreen;
	private GameScreen gameScreen;
	
	private Client client;
	
	/**
	 * Initializes everything
	 * @param root - where all gui elements are added to
	 * @param client
	 */
	public Driver(Group root, Client client) {
		this.client = client;
		homeScreen = new HomeScreen(root);
		gameScreen = new GameScreen(root,client,homeScreen);
		setupScreen = new SetupScreen(root,client,homeScreen,gameScreen);
		homeScreen.setupStartButton(setupScreen);
		client.setGameScreen(gameScreen);
	}
	
	public void start() {
		homeScreen.show();
	}
	
	/**
	 * Utility method for centering an gui element at a desired location
	 * @param label - item to be centered
	 * @param screen - screen the item belongs to
	 * @param x - x position
	 * @param y - y position
	 * @param w - width
	 * @param h - height
	 */
	public static void addCenterNode(Node label, Screen screen, int x, int y, int w, int h) {
		BorderPane labelBox = new BorderPane(label);
		labelBox.setPrefSize(w, h);
		labelBox.setLayoutX(x);
		labelBox.setLayoutY(y);
		screen.addElement(labelBox);
	}
}
