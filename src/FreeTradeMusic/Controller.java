package FreeTradeMusic;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.math.BigInteger;
import java.security.*;

public class Controller
{
    // Login Scene Components.
    @FXML private TextField usernameLTextField;
    @FXML private PasswordField passwordLPasswordField;
    @FXML private Label errorLLabel;

    // Register Scene Components.
    @FXML private TextField usernameCATextField;
    @FXML private PasswordField passwordCAPasswordField;
    @FXML private PasswordField confirmPasswordCAPasswordField;
    @FXML private TextField emailCATextField;
    @FXML private Label errorCALabel;

    private final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    private boolean hasErrors;
    private Error error;

    /*EVENT HANDLERS*/
    // Login Scene Event Handlers
    /**
     * Event handler for the Register button.
     */
    public void onGoToRegister()
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
    public void onLogin()
    {
        hasErrors = true;
        String username = usernameLTextField.getText().trim();
        String password = passwordLPasswordField.getText().trim();

        if(!username.isEmpty() && !password.isEmpty())
        {
            password = hashPassword(password);
            error = DatabaseManager.getInstance().login(username, password);
            if(error == Error.NO_ERROR)
            {
                hasErrors = false;
                switchScene("MAIN_WINDOW");
            }
        }
        else
        {
            if(username.isEmpty() && password.isEmpty())
                error = Error.BOTH_EMPTY;
            else if(username.isEmpty())
                error = Error.USERNAME_EMPTY;
            else
                error = Error.PASSWORD_EMPTY;
        }
        setErrors("LOGIN", hasErrors);
    }

    // Register Scene Event Handlers.
    /**
     * The event handler for the Register button in the Register scene.
     */
    public void onRegister()
    {
        hasErrors = true;
        String username = usernameCATextField.getText().trim();
        String password = passwordCAPasswordField.getText().trim();
        String confirmPassword = confirmPasswordCAPasswordField.getText().trim();
        String email = emailCATextField.getText().trim();

        if(!username.isEmpty()
                && !password.isEmpty()
                && !confirmPassword.isEmpty()
                && !email.isEmpty())
        {
            error = DatabaseManager.getInstance().isUsernameAvailable(username);
            if(error == Error.NO_ERROR)
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
                            hasErrors = false;
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
                        }
                    }
                }
                else
                    error = Error.PASSWORDS_NO_MATCH;
            }
        }
        else
        {
            if(username.isEmpty() && password.isEmpty()
                    && confirmPassword.isEmpty() && email.isEmpty())
                error = Error.ALL_EMPTY;
            else
            {
                if(username.isEmpty())
                {
                    error = Error.USERNAME_EMPTY;
                    setErrors("REGISTER", hasErrors);
                }
                if(password.isEmpty())
                {
                    error = Error.PASSWORD_EMPTY;
                    setErrors("REGISTER", hasErrors);
                }
                if(confirmPassword.isEmpty())
                {
                    error = Error.CONFIRM_PASSWORD_EMPTY;
                    setErrors("REGISTER", hasErrors);
                }
                if(email.isEmpty())
                {
                    error = Error.EMAIL_EMPTY;
                    setErrors("REGISTER", hasErrors);
                }
            }
        }
        setErrors("REGISTER", hasErrors);
    }

    /**
     * Event handler for Cancel button in the
     * register window.
     */
    public void onCancelRegister()
    {
        switchScene("LOGIN");
    }

    // Main Scene Event Handlers.
    /**
     * Switches the scenes based on the given sceneName.
     * @param sceneName - The name of the scene to switch to.
     */
    private void switchScene(String sceneName)
    {
        Scene scene = null;
        boolean resizable = false;
        boolean maximized = false;

        switch (sceneName)
        {
            case "LOGIN":
                if(usernameLTextField != null
                        && passwordLPasswordField != null
                        && confirmPasswordCAPasswordField != null
                        && emailCATextField != null)
                {
                    usernameCATextField.clear();
                    passwordCAPasswordField.clear();
                    confirmPasswordCAPasswordField.clear();
                    emailCATextField.clear();
                }
                scene = FreeTradeMusic.loginScene;
                break;
            case "REGISTER":
                usernameLTextField.clear();
                passwordLPasswordField.clear();
                scene = FreeTradeMusic.createAccountScene;
                break;
            case "MAIN_WINDOW":
                usernameLTextField.clear();
                passwordLPasswordField.clear();
                scene = FreeTradeMusic.mainWindow;
                resizable = true;
                maximized = true;
                break;
        }

        FreeTradeMusic.stage.setScene(scene);
        FreeTradeMusic.stage.setResizable(resizable);
        FreeTradeMusic.stage.setMaximized(maximized);
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

    private void setErrors(String scene, boolean enable)
    {
        Label errorLabel = null;

        switch(scene)
        {
            case "LOGIN":
                errorLabel = errorLLabel;
                if(error == Error.BOTH_EMPTY || error == Error.BOTH_WRONG)
                {
                    usernameLTextField.pseudoClassStateChanged(errorClass, true);
                    passwordLPasswordField.pseudoClassStateChanged(errorClass, true);
                }
                else if(error == Error.USERNAME_EMPTY || error == Error.USERNAME_WRONG)
                {
                    usernameLTextField.pseudoClassStateChanged(errorClass, true);
                    passwordLPasswordField.pseudoClassStateChanged(errorClass, false);
                }
                else if(error == Error.PASSWORD_EMPTY || error == Error.PASSWORD_WRONG)
                {
                    usernameLTextField.pseudoClassStateChanged(errorClass, false);
                    passwordLPasswordField.pseudoClassStateChanged(errorClass, true);
                }
                else
                {
                    usernameLTextField.pseudoClassStateChanged(errorClass, false);
                    passwordLPasswordField.pseudoClassStateChanged(errorClass, false);
                }
                break;
            case "REGISTER":
                errorLabel = errorCALabel;
//                if(error == Error.ALL_EMPTY)
//                {
//                    usernameCATextField.pseudoClassStateChanged(errorClass, true);
//                    passwordCAPasswordField.pseudoClassStateChanged(errorClass, true);
//                    confirmPasswordCAPasswordField.pseudoClassStateChanged(errorClass, true);
//                    emailCATextField.pseudoClassStateChanged(errorClass, true);
//                }
//                if(error == Error.USERNAME_EMPTY || error == Error.USERNAME_NOT_AVAILABLE
//                        || error == Error.USERNAME_INVALID)
//                {
//                    usernameCATextField.pseudoClassStateChanged(errorClass, true);
//                }
//                if(error == Error.PASSWORD_EMPTY || error == Error.PASSWORD_INVALID
//                        || error == Error.PASSWORDS_NO_MATCH)
//                {
//                    passwordCAPasswordField.pseudoClassStateChanged(errorClass, true);
//                    confirmPasswordCAPasswordField.pseudoClassStateChanged(errorClass, true);
//                }
//                if(error == Error.EMAIL_EMPTY || error == Error.EMAIL_INVALID)
//                {
//                    emailCATextField.pseudoClassStateChanged(errorClass, true);
//                }
//                if(error == Error.NO_ERROR)
//                {
//                    usernameCATextField.pseudoClassStateChanged(errorClass, false);
//                    passwordCAPasswordField.pseudoClassStateChanged(errorClass, false);
//                    confirmPasswordCAPasswordField.pseudoClassStateChanged(errorClass, false);
//                    emailCATextField.pseudoClassStateChanged(errorClass, false);
//                }
                break;
            default:
                System.err.println("ERROR: " + scene + " is not a valid scene.");
        }

        if(errorLabel != null)
        {
            errorLabel.setText("ERROR: " + error.getDescription());
            errorLabel.setVisible(enable);
        }
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
        boolean valid = false;

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

        valid = input.matches(regex);
        if(!valid)
            setErrors("REGISTER", true);
        return valid;
    }
}
