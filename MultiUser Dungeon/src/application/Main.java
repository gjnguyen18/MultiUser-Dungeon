package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * This class is where the application is launched from, the main
 * @author Gia-Phong Nguyen
 *
 */
public class Main extends Application {
	Client client;
	@Override
	public void start(Stage primaryStage) {
		try {
			// sets up screen
			Pane pane = new Pane();
			pane.setPrefSize(Driver.screenWidth,Driver.screenHeight);
			pane.setId("background");
			Group root = new Group();
			pane.getChildren().add(root);
			Scene scene = new Scene(pane,Driver.screenWidth,Driver.screenHeight);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("MultiUser Dungeon");
			primaryStage.setResizable(false);
			
			// sets up and starts the program
			client = new Client();
			Driver driver = new Driver(root, client);
			driver.start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Makes sure to properly disconnect from server on exiting
	 */
	@Override
	public void stop(){
    client.disconnect(false);
    Thread.currentThread().interrupt();
    System.exit(0);
	}
}
