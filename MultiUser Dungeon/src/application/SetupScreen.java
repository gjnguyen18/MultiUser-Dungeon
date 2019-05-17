package application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

public class SetupScreen extends Screen{
	
	private Screen homeScreen;
	private Screen gameScreen;
	
	private Label title;
	private TextField linkField, nameField;
	private Button back, start;
	
	private Client client;

	public SetupScreen(Group root, Client client, Screen back, Screen game) {
		super(root);
		this.client = client;
		this.homeScreen = back;
		this.gameScreen = game;
		setupGUI();
		this.hide();
	}

	private void setupGUI() {
		// sets up title label
		title = new Label("Join/Create Dungeon");
		title.setId("titleText");
		Driver.addCenterNode(title, this, 0, 50, Driver.screenWidth, 200);
		// sets up link textfield
		linkField = new TextField("Enter link to join (or type 'host' to create dungeon)");
		linkField.setPrefWidth(600);
		Driver.addCenterNode(linkField, this, 50, 210, Driver.screenWidth-100, 20);
		linkField.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent k) {
        if (k.getCode().equals(KeyCode.ENTER)) {
        	if(setupClient()) {
    				hide();
    				gameScreen.show();
    			}
        }
	    }
    });
		// sets up name textfield
		nameField = new TextField("Enter name for others to see");
		nameField.setPrefWidth(200);
		Driver.addCenterNode(nameField, this, 50, 250, Driver.screenWidth-100, 20);
		nameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent k) {
        if (k.getCode().equals(KeyCode.ENTER)) {
        	if(setupClient()) {
    				hide();
    				gameScreen.show();
    			}
        }
	    }
    });
		// sets up start button
		start = new Button("Start");
		start.setPrefSize(200, 50);
		Driver.addCenterNode(start, this, 0, 340, Driver.screenWidth, 50);
		start.setOnAction(e-> {
			if(setupClient()) {
				hide();
				gameScreen.show();
			}
		});
		// sets up start button
		back = new Button("Back");
		back.setPrefSize(200, 50);
		Driver.addCenterNode(back, this, 0, 400, Driver.screenWidth, 50);
		back.setOnAction(e-> {
			this.hide();
			homeScreen.show();
		});
	}
	
	public boolean setupClient() {
		String link = linkField.getText();
		if(nameField.getText().trim().equals("") || nameField.getText().trim().split(" ").length>1) {
	    Alert alert = new Alert(AlertType.INFORMATION, "Name must have no spaces and be at least 1 character", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return false;
		}
		if(client.setupClient(link,nameField.getText())) {
			return true;
		}
		Alert alert = new Alert(AlertType.INFORMATION, "Invalid Link", ButtonType.OK);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.show();
		return false;
	}
	
	@Override
	public void show() {
		super.show();
		linkField.setText("Enter link to join (or type 'host' to create dungeon)");
		nameField.setText("Enter name for others to see");
	}
}
