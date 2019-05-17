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
	
	private TextField console;
	private VBox allTexts;
	private Client client;
	private TextField roomLinkField;
	private String roomLink;
	private ScrollPane scrollPane;
	private Label userCoords;
	private Button north,south,east,west,up,down;
	private Button exitButton;
	private Screen homeScreen;

	public GameScreen(Group root, Client client, Screen home) {
		super(root);
		this.client = client;
		this.homeScreen = home;
		roomLink = "";
		setupGUI();
		this.hide();
	}

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
		
		roomLinkField = new TextField("RoomLink");
		roomLinkField.setDisable(true);
		Driver.addCenterNode(roomLinkField, this, 520, 10, 270, 40);
		
		userCoords = new Label("Coordinates: x=0 y=0 z=0");
		Driver.addCenterNode(userCoords, this, 520, 50, 270, 40);
		
		exitButton = new Button("Exit");
		exitButton.setPrefSize(200, 50);
		Driver.addCenterNode(exitButton, this, 560, 460, 200, 50);
		exitButton.setOnAction(e -> {
			client.disconnect(false);
			exit();
		});
		
		north = new Button("north");
		north.setPrefSize(80, 40);
		Driver.addCenterNode(north, this, 620, 160, 80, 40);
		north.setOnAction(e -> {
			client.sendMessage("/move north");
		});
		
		south = new Button("south");
		south.setPrefSize(80, 40);
		Driver.addCenterNode(south, this, 620, 260, 80, 40);
		south.setOnAction(e -> {
			client.sendMessage("/move south");
		});
		
		east = new Button("east");
		east.setPrefSize(80, 40);
		Driver.addCenterNode(east, this, 710, 210, 80, 40);
		east.setOnAction(e -> {
			client.sendMessage("/move east");
		});
		
		west = new Button("west");
		west.setPrefSize(80, 40);
		Driver.addCenterNode(west, this, 530, 210, 80, 40);
		west.setOnAction(e -> {
			client.sendMessage("/move west");
		});
		
		up = new Button("up");
		up.setPrefSize(80, 40);
		Driver.addCenterNode(up, this, 620, 330, 80, 40);
		up.setOnAction(e -> {
			client.sendMessage("/move up");
		});
		
		down = new Button("down");
		down.setPrefSize(80, 40);
		Driver.addCenterNode(down, this, 620, 380, 80, 40);
		down.setOnAction(e -> {
			client.sendMessage("/move down");
		});
	}
	
	public void exit() {
		this.hide();
		homeScreen.show();
	}
	
	public void resetConsole() {
		allTexts.getChildren().clear();
		allTexts.getChildren().add(new Label(""));
	}
	
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
	
	public void setCoords(String x, String y, String z) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	userCoords.setText("Coordinates: x=" + x + " y=" + y + " z=" + z);
      }
    });
	}
	
	public void setupRoomLinkLabel(String str) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	roomLinkField.setText("Room Link: " + str);
    		roomLink = str;
      }
    });
	}
	
	public void setNorth(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	north.setDisable(b);
      }
    });
	}
	
	public void setSouth(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	south.setDisable(b);
      }
    });
	}
	
	public void setEast(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	east.setDisable(b);
      }
    });
	}
	
	public void setWest(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
    		west.setDisable(b);
      }
    });
	}
	
	public void setUp(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
    		up.setDisable(b);
      }
    });
	}
	
	public void setDown(boolean b) {
		Platform.runLater(new Runnable() {
      @Override
      public void run() {
      	down.setDisable(b);
      }
    });
	}
}
