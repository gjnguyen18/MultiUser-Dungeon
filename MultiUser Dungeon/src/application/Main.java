package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application {
	Client client;
	@Override
	public void start(Stage primaryStage) {
		try {
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
	
	@Override
	public void stop(){
    client.disconnect(false);
    Thread.currentThread().interrupt();
    System.exit(0);
	}
}
