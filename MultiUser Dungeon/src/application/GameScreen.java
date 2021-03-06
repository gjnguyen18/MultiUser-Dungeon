package application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GameScreen extends Screen{
	
	// all of these are gui elements
	private TextField console;
	private VBox allTexts;
	private Client client;
	private TextField roomLinkField;
	private String roomLink;
	private ScrollPane scrollPane;
	private Label userCoords, userGold;
	private Button north,south,east,west,up,down;
	private Button exitButton;
	private Screen homeScreen;
	
	private int gold;

	/**
	 * Sets up gui elements and client
	 * @param root - where gui is displayed from
	 * @param client
	 * @param home - home screen
	 */
	public GameScreen(Group root, Client client, Screen home) {
		super(root);
		this.client = client;
		this.homeScreen = home;
		roomLink = "";
		setupGUI();
		this.hide();
	}

	/**
	 * Sets up all the gui elements
	 */
	private void setupGUI() {
		// sets up textfield console
		console = new TextField();
		console.setPrefWidth(600);
		Driver.addCenterNode(console, this, 10, 550, Driver.screenWidth-20, 20);
		console.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent k) {
        if (k.getCode().equals(KeyCode.ENTER)) {
        	client.sendMessage(console.getText());
        	console.setText("");
        }
	    }
    });
		// sets up messages box
		allTexts = new VBox();
		allTexts.setPadding(new Insets(10, 10, 10, 10));
		allTexts.getChildren().add(new Label(""));
		scrollPane = new ScrollPane(allTexts);
		scrollPane.setPrefSize(500, 500);
		scrollPane.setLayoutX(10);
		scrollPane.setLayoutY(10);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		addElement(scrollPane);
		// sets up room link field
		roomLinkField = new TextField("RoomLink");
		roomLinkField.setDisable(true);
		Driver.addCenterNode(roomLinkField, this, 520, 10, 270, 40);
		// sets up user coordinates label
		userCoords = new Label("Coordinates: x=0 y=0 z=0");
		Driver.addCenterNode(userCoords, this, 520, 50, 270, 40);
		// sets up user coordinates label
		userGold= new Label("Your Gold: 0g");
		Driver.addCenterNode(userGold, this, 520, 80, 270, 40);
		// sets up exit button
		exitButton = new Button("Exit");
		exitButton.setPrefSize(200, 50);
		Driver.addCenterNode(exitButton, this, 560, 460, 200, 50);
		exitButton.setOnAction(e -> {
			client.disconnect(false);
			exit();
		});
		// sets up move north button
		north = new Button("north");
		north.setPrefSize(80, 40);
		Driver.addCenterNode(north, this, 620, 160, 80, 40);
		north.setOnAction(e -> {
			client.sendMessage("/move north");
		});
		// sets up move south button
		south = new Button("south");
		south.setPrefSize(80, 40);
		Driver.addCenterNode(south, this, 620, 260, 80, 40);
		south.setOnAction(e -> {
			client.sendMessage("/move south");
		});
		// sets up move east button
		east = new Button("east");
		east.setPrefSize(80, 40);
		Driver.addCenterNode(east, this, 710, 210, 80, 40);
		east.setOnAction(e -> {
			client.sendMessage("/move east");
		});
		// sets up move west button
		west = new Button("west");
		west.setPrefSize(80, 40);
		Driver.addCenterNode(west, this, 530, 210, 80, 40);
		west.setOnAction(e -> {
			client.sendMessage("/move west");
		});
		// sets up move up button
		up = new Button("up");
		up.setPrefSize(80, 40);
		Driver.addCenterNode(up, this, 620, 330, 80, 40);
		up.setOnAction(e -> {
			client.sendMessage("/move up");
		});
		// sets up move down button
		down = new Button("down");
		down.setPrefSize(80, 40);
		Driver.addCenterNode(down, this, 620, 380, 80, 40);
		down.setOnAction(e -> {
			client.sendMessage("/move down");
		});
	}
	
	/**
	 * Exits to home screen
	 */
	public void exit() {
		this.hide();
		homeScreen.show();
	}
	
	/**
	 * Clears the console of previous text
	 */
	public void resetConsole() {
		allTexts.getChildren().clear();
		allTexts.getChildren().add(new Label(""));
	}
	
	/**
	 * Adds a message to the buttom of the console
	 * @param msg - message to be added
	 */
	public void addMessage(String msg) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	Label msgLabel = new Label(msg);
      	msgLabel.setId("consoleText");
      	msgLabel.setPrefWidth(470);
      	msgLabel.setWrapText(true);
    		allTexts.getChildren().add(allTexts.getChildren().size()-1,msgLabel);
    		scrollPane.setVvalue(1D);
      }
    });
	}
	
	/**
	 * Sets the coordinates label
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setCoords(String x, String y, String z) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	userCoords.setText("Coordinates: x=" + x + " y=" + y + " z=" + z);
      }
    });
	}
	
	/**
	 * Sets up the room link on the top right
	 * @param str - room link
	 */
	public void setupRoomLinkLabel(String str) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	roomLinkField.setText("Room Link: " + str);
    		roomLink = str;
      }
    });
	}
	
	/**
	 * Increments the user's gold
	 * @param amount - amount to increment
	 */
	public void incrementGold(int amount) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	gold+=amount;
    		userGold.setText("Gold: "+gold+"g");
      }
    });
	}
	
	/**
	 * Disables/Enables moving north
	 * @param b - whether or not direction is valid
	 */
	public void setNorth(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	north.setDisable(b);
      }
    });
	}
	
	/**
	 * Disables/Enables moving south
	 * @param b - whether or not direction is valid
	 */
	public void setSouth(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	south.setDisable(b);
      }
    });
	}
	
	/**
	 * Disables/Enables moving east
	 * @param b - whether or not direction is valid
	 */
	public void setEast(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	east.setDisable(b);
      }
    });
	}
	
	/**
	 * Disables/Enables moving west
	 * @param b - whether or not direction is valid
	 */
	public void setWest(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
    		west.setDisable(b);
      }
    });
	}
	
	/**
	 * Disables/Enables moving up
	 * @param b - whether or not direction is valid
	 */
	public void setUp(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
    		up.setDisable(b);
      }
    });
	}
	
	/**
	 * Disables/Enables moving down
	 * @param b - whether or not direction is valid
	 */
	public void setDown(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	down.setDisable(b);
      }
    });
	}
}
