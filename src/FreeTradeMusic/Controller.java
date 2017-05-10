package FreeTradeMusic;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.math.BigInteger;
import java.security.*;
import java.sql.SQLException;


public class Controller
{
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordPasswordField;
    @FXML private TextField usernameCATextField;
    @FXML private PasswordField passwordCAPasswordField;
    @FXML private PasswordField confirmPasswordCAPasswordField;
    @FXML private TextField emailCATextField;

    /**
     * Switches the scenes based on the given sceneName.
     * @param sceneName - The name of the scene to switch to.
     */
    private void switchScene(String sceneName)
    {
        Scene scene = null;
        boolean resizable = false;

        switch (sceneName)
        {
            case "LOGIN":
                if(usernameTextField != null
                        && passwordPasswordField != null
                        && confirmPasswordCAPasswordField != null
                        && emailCATextField != null)
                {
                    usernameCATextField.clear();
                    passwordCAPasswordField.clear();
                    confirmPasswordCAPasswordField.clear();
                    emailCATextField.clear();
                }
                scene = FreeTradeMusic.loginScene;
                resizable = false;
                break;
            case "REGISTER":
                usernameTextField.clear();
                passwordPasswordField.clear();
                scene = FreeTradeMusic.createAccountScene;
                resizable = false;
                break;
            case "MAIN_WINDOW":
                usernameTextField.clear();
                passwordPasswordField.clear();
                scene = FreeTradeMusic.mainWindow;
                resizable = true;
                break;
        }

        FreeTradeMusic.stage.setScene(scene);
        FreeTradeMusic.stage.setResizable(resizable);
    }

    /**
     * Event handler for the Register button.
     */
    public void onCreateAccount()
    {
        switchScene("REGISTER");
    }

    /**
     * Event handler for the Forgot Password? button.
     */
    public void onForgotPassword()
    {
        System.out.println("Forgot password clicked.");
    }

    /**
     * The event handler for the Login button.
     */
    public void onLogin() throws SQLException {
        String username = usernameTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();

        if(!username.isEmpty() && !password.isEmpty())
        {
            password = hashPassword(password);

            if(DatabaseManager.getInstance().login(username, password))
                switchScene("MAIN_WINDOW");
            else
                alertUser("Wrong Username/Password",
                        "You entered a wrong username or password.",
                        "ERROR");
                // TODO: Put red borders on invalid fields.
        }
        else
            alertUser("Empty Field",
                    "One or both fields are empty.",
                    "ERROR");
    }

    /**
     * The event handler for the Logout button in the MainWindow scene.
     */
    public void onLogout()
    {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Logout");
//        alert.setHeaderText("Logout");
//        alert.setContentText("Are you sure you want to logout?");
//        alert.showAndWait();
        switchScene("LOGIN");
    }

    /**
     * The event handler for the Register button in the Register scene.
     */
    public void onRegister() throws SQLException {
        String username = usernameCATextField.getText().trim();
        String password = passwordCAPasswordField.getText().trim();
        String confirmPassword = confirmPasswordCAPasswordField.getText().trim();
        String email = emailCATextField.getText().trim();

        if(!username.isEmpty()
                && !password.isEmpty()
                && !confirmPassword.isEmpty()
                && !email.isEmpty())
        {
            if(DatabaseManager.getInstance().isUsernameAvailable(username))
            {
                if(password.equals(confirmPassword))
                {
                    if(isInputValid(username, "USERNAME")
                            && isInputValid(password, "PASSWORD")
                            && isInputValid(email, "EMAIL"))
                    {
                        password = hashPassword(password);

                        if(DatabaseManager.getInstance().register(username, password, email))
                        {
                            alertUser("Account Created Successfully",
                                    "Your account was created successfully.",
                                    "INFORMATION");
                            switchScene("LOGIN");
                        }
                        else
                        {
                            alertUser("Account Could Not Be Created",
                                    "Your account could not be created.",
                                    "ERROR");
                            // TODO: Let the user know why.
                        }
                    }
                    else
                    {
                        alertUser("Invalid Field",
                                "One of your inputs is invalid.",
                                "ERROR");
                        // TODO: Put red borders on invalid fields.
                    }
                }
                else
                    alertUser("Passwords Do Not Match",
                            "Your passwords do not match. Please try again.",
                            "ERROR");
            }
            else
                alertUser("Username Not Available",
                        "The username you entered is not available. "
                        + "Please try another one.", "ERROR");
        }
        else
            alertUser("Empty Field",
                    "One or more fields are empty.",
                    "ERROR");
    }

    /**
     * Event handler for Cancel button in the
     * register window.
     */
    public void onCancelRegister()
    {
        switchScene("LOGIN");
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
    private void alertUser(String title, String errorMessage, String type)
    {
        Alert alert = new Alert(Alert.AlertType.NONE);

        switch (type)
        {
            case "INFORMATION":
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            case "ERROR":
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            case "CONFIRMATION":
                alert = new Alert(Alert.AlertType.CONFIRMATION);
        }

        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    /**
     * Checks if a user given input is valid.
     * @param input - The input given by the user.
     * @param type - The type the input should be.
     * @return - Returns whether is valid or not.
     */
    private boolean isInputValid(String input, String type)
    {
        String regex = "";

        switch (type.toUpperCase())
        {
            case "USERNAME":
                regex = "^[a-zA-Z0-9]+$";
                break;
            case "PASSWORD":
                regex = "^.{8,}$";
                break;
            case "EMAIL":
                regex = "^[a-zA-Z0-9._]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$";
                break;
        }

        return input.matches(regex);
    }
}
