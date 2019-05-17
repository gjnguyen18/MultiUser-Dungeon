package application;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeScreen extends Screen{
	
	private Label title;
	private Button start;

	public HomeScreen(Group root) {
		super(root);
		setupGUI();
	}

	private void setupGUI() {
		// sets up title label
		title = new Label("MultiUser Dungeon");
		title.setId("titleText");
		Driver.addCenterNode(title, this, 0, 50, Driver.screenWidth, 200);
		// sets up start button
		start = new Button("Start");
		start.setId("titleButton");
		start.setPrefSize(300, 150);
		Driver.addCenterNode(start, this, 0, 300, Driver.screenWidth, 200);
	}
	
	public void setupStartButton(Screen screen) {
		start.setOnAction(e -> {
				this.hide();
				screen.show();
		});
	}
}
