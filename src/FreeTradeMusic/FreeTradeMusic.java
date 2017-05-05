package FreeTradeMusic;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;


public class FreeTradeMusic extends Application
{
	// TODO - Jose: Move all this to the controller, maybe create a GUIManager.
	public static Controller controller;
	public static Scene mainWindow;
	public static Scene loginScene;
	public static Scene createAccountScene;
	public static Stage stage;

	@Override
	public void start(Stage primaryStage) throws IOException
	{
		// Loads the FXML for the Login Scene and creates the Scene.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/Login.fxml"));
		loginScene = new Scene(loader.load(), 500, 500);

		// Loads the FXML for the Chat Scene and creates the Scene.
		loader = new FXMLLoader(getClass().getResource("layouts/FreeTradeMusic.fxml"));
		mainWindow = new Scene(loader.load(), 1280, 720);

		// Loads the FXML for the Chat Scene and creates the Scene.
		loader = new FXMLLoader(getClass().getResource("layouts/CreateAccount.fxml"));
		createAccountScene = new Scene(loader.load(), 500, 720);

		// Saves a reference of the Controller object so
		// the Listener thread can access it.
		controller = loader.getController();

		// Saves a reference of the Stage object so
		// the Controller class can access it.
		// It also sets the stage.
		stage = primaryStage;
		//stage.getIcons().add(new Image(FreeTradeMusic.class.getResourceAsStream("Drawable/Icon.png")));
		stage.setTitle("Free Trade Music");
		stage.setResizable(false);
		stage.setScene(loginScene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
