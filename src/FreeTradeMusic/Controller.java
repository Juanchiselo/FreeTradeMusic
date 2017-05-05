package FreeTradeMusic;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigInteger;
import java.security.*;
import java.util.Collections;


public class Controller
{
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordPasswordField;

    public void onCreateAccount()
    {
        FreeTradeMusic.stage.setScene(FreeTradeMusic.createAccountScene);
        FreeTradeMusic.stage.setResizable(false);
    }

    public void onForgotPassword()
    {
        FreeTradeMusic.stage.setScene(FreeTradeMusic.mainWindow);
        FreeTradeMusic.stage.setResizable(true);
        System.out.println("Forgot password clicked.");
    }

    /**
     * The event handler for the login.
     */
    public void onLogin()
    {
        String username = usernameTextField.getText().trim();
        String password = hashPassword(passwordPasswordField.getText().trim());
        ObservableList<String> usernameTextFieldStyle = usernameTextField.getStyleClass();
        //ObservableList<String> passwordPasswordField = passwordPasswordField.getStyleClass();

        if(!username.isEmpty() && !password.isEmpty())
        {
            System.out.println("Username: " + username
                    + "\nPassword: " + password);

            if(DatabaseManager.getInstance().login(username, password))
            {
                FreeTradeMusic.stage.setScene(FreeTradeMusic.mainWindow);
                FreeTradeMusic.stage.setResizable(true);
                usernameTextFieldStyle.removeAll(Collections.singleton("error"));
            }
            else
                displayError("Wrong Username/Password",
                        "You entered a wrong username or password.");
        }
        else
        {
            if(!username.isEmpty())
            {
//                if (!usernameTextFieldStyle.contains(".error")) {
//                    usernameTextFieldStyle.add(".error");
//                }
            }

            displayError("Empty field",
                    "One of the fields is empty.");
        }
    }

    /**
     * Hashes the password using MD5.
     * @param password - The password as a plain string.
     * @return - The hashed password.
     */
    private String hashPassword(String password)
    {
        String hashedPassword = "";
        try
        {
            byte[] passwordBytes = password.getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(passwordBytes);
            BigInteger bigInteger = new BigInteger(1, digest);
            hashedPassword = bigInteger.toString(16).toUpperCase();
        }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage() + ".");
        }
        return hashedPassword;
    }

    /**
     * Displays error messages in the form of alerts.
     * @param title - The title of the alert.
     * @param errorMessage - The error message.
     */
    public void displayError(String title, String errorMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
