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
    @FXML private TextField usernameCATextField;
    @FXML private PasswordField passwordCAPasswordField;
    @FXML private PasswordField confirmPasswordCAPasswordField;
    @FXML private TextField emailCATextField;


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
        String password = passwordPasswordField.getText().trim();

        if(!username.isEmpty() && !password.isEmpty())
        {
            password = hashPassword(password);

            if(DatabaseManager.getInstance().login(username, password))
            {
                FreeTradeMusic.stage.setScene(FreeTradeMusic.mainWindow);
                FreeTradeMusic.stage.setResizable(true);
            }
            else
                displayError("Wrong Username/Password",
                        "You entered a wrong username or password.");
                // TODO: Put red borders on invalid fields.
        }
        else
            displayError("Empty Field",
                    "One or both fields are empty.");
    }

    public void onRegister()
    {
        String username = usernameCATextField.getText().trim();
        String password = passwordCAPasswordField.getText().trim();
        String confirmPassword = confirmPasswordCAPasswordField.getText().trim();
        String email = emailCATextField.getText().trim();

        if(!username.isEmpty()
                && !password.isEmpty()
                && !confirmPassword.isEmpty()
                && !email.isEmpty())
        {
            if(isInputValid(username, "USERNAME")
                    && DatabaseManager.getInstance().isUsernameAvailable(username))
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
                            FreeTradeMusic.stage.setScene(FreeTradeMusic.loginScene);
                            FreeTradeMusic.stage.setResizable(false);
                            // TODO: Let the user know his account was created.
                        }
                        else
                        {
                            displayError("Account Could Not Be Created",
                                    "Your account could not be created.");
                            // TODO: Let the user know why.
                        }
                    }
                    else
                    {
                        displayError("Invalid Field",
                                "One of your inputs is invalid.");
                        // TODO: Put red borders on invalid fields.
                    }
                }
                else
                    displayError("Passwords Do Not Match",
                            "Your passwords do not match. Please try again.");
            }
            else
                displayError("Username Not Available",
                        "The username you entered is not available. "
                        + "Please try another one.");
        }
        else
            displayError("Empty Field",
                    "One or more fields are empty.");
    }

    public void onCancelRegister()
    {
        FreeTradeMusic.stage.setScene(FreeTradeMusic.loginScene);
        FreeTradeMusic.stage.setResizable(false);
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
                regex = "^[a-z]+$";
                break;
            case "PASSWORD":
                regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
                break;
            case "EMAIL":
                regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
                break;
                /*
                    ^                 # start-of-string
                    (?=.*[0-9])       # a digit must occur at least once
                    (?=.*[a-z])       # a lower case letter must occur at least once
                    (?=.*[A-Z])       # an upper case letter must occur at least once
                    (?=.*[@#$%^&+=])  # a special character must occur at least once
                    (?=\S+$)          # no whitespace allowed in the entire string
                    .{8,}             # anything, at least eight places though
                    $                 # end-of-string
                 */
        }

        //return input.matches(regex);
        return true;
    }
}
