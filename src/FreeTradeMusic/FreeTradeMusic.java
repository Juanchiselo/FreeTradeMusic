package FreeTradeMusic;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import javax.xml.crypto.Data;
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
		loader = new FXMLLoader(getClass().getResource("layouts/MainWindow.fxml"));
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
	
	public static void main(String[] args) throws Exception{
		launch(args);
        Web3j web3 = Web3j.build(new HttpService());
        Web3ClientVersion clientversion = web3.web3ClientVersion().sendAsync().get();
        System.out.println("Client is running version: " + clientversion.getWeb3ClientVersion());

	}
}
