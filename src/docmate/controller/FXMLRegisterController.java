package docmate.controller;

import docmate.database.Database;
import docmate.util.AlertMessage;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLRegisterController implements Initializable {

    public static final String TAG = "DOCMATE";

    private Stage primaryStage;
    private Scene loginScene;

    @FXML
    private ProgressIndicator registerProgressIndicator;

    @FXML
    private TextField registerNameField;
    @FXML
    private HBox registerNameError;
    @FXML
    private Text registerNameErrorText;

    @FXML
    private TextField registerEmailField;
    @FXML
    private HBox registerEmailError;
    @FXML
    private Text registerEmailErrorText;

    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private HBox registerPasswordError;
    @FXML
    private Text registerPasswordErrorText;

    @FXML
    private Button registerButton;

    @FXML
    private Hyperlink openLoginLink;

    private String registerName;
    private String registerEmail;
    private String registerPassword;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerButton.setOnAction(event -> register());
        openLoginLink.setOnAction(event -> openLogin());
    }

    private void register() {
        if (validateData()) {
            writeToDatabase();
        }
    }

    private void writeToDatabase() {
        registerProgressIndicator.setVisible(true);

        new Thread(new Task<Boolean>() {

            @Override
            protected Boolean call() {
                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "INSERT INTO DOCTOR (DoctorName, DoctorEmail, DoctorPassword) " +
                                "VALUES (? , ?, ?)";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, registerName);
                        preparedStatement.setString(2, registerEmail);
                        preparedStatement.setString(3, registerPassword);

                        preparedStatement.executeUpdate();
                    } else {
                        return false;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    // TODO: 9/14/2020 Alert for email occupied
                    return false;
                } finally {
                    Database.getInstance().close(connection, preparedStatement);
                }

                return true;
            }

            @Override
            protected void updateValue(Boolean isRegistered) {
                super.updateValue(isRegistered);

                if (isRegistered) {
                    registerNameField.clear();
                    registerEmailField.clear();
                    registerPasswordField.clear();

                    registerProgressIndicator.setVisible(false);

                    AlertMessage alertMessage = new AlertMessage("Registration successful!");
                    alertMessage.showAndWait();

                    Logger.getLogger(TAG).log(Level.INFO, "Registered!");
                } else {
                    registerProgressIndicator.setVisible(false);

                    AlertMessage alertMessage = new AlertMessage("Registration failed!");
                    alertMessage.showAndWait();
                }
            }
        }).start();
    }

    private boolean validateData() {
        registerName = registerNameField.getText().trim();
        registerEmail = registerEmailField.getText().trim().toLowerCase();
        registerPassword = registerPasswordField.getText().trim();

        boolean isDataValid = true;

        clearError(registerNameError, registerNameErrorText);
        clearError(registerEmailError, registerEmailErrorText);
        clearError(registerPasswordError, registerPasswordErrorText);

        // Name validation
        if (registerName.isEmpty()) {
            setError(registerNameError, registerNameErrorText, "Name can not be empty");
            isDataValid = false;
        } else if (!registerName.matches("^[a-zA-Z .]*$")) {
            setError(registerNameError, registerNameErrorText,
                    "Name can only contain letters, dots and spaces");
            isDataValid = false;
        } else if (registerName.length() < 3) {
            setError(registerNameError, registerNameErrorText, "Name requires at least 3 characters");
            isDataValid = false;
        }

        // Email Validation
        if (registerEmail.isEmpty()) {
            setError(registerEmailError, registerEmailErrorText, "Email can not be empty");
            isDataValid = false;
        } else if (!registerEmail.matches("[a-zA-Z0-9+._%\\-]{1,256}" + "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")) {
            setError(registerEmailError, registerEmailErrorText, "Invalid email format");
            isDataValid = false;
        }

        // Password Validation
        if (registerPassword.isEmpty()) {
            setError(registerPasswordError, registerPasswordErrorText, "Password can not be empty");
            isDataValid = false;
        } else if (registerPassword.contains(" ")) {
            setError(registerPasswordError, registerPasswordErrorText,
                    "Password should not contain spaces");
            isDataValid = false;
        } else if (registerPassword.length() < 4) {
            setError(registerPasswordError, registerPasswordErrorText,
                    "Password requires at least 4 characters");
            isDataValid = false;
        } else if (registerPassword.length() > 30) {
            setError(registerPasswordError, registerPasswordErrorText,
                    "Password should not exceed 30 characters");
            isDataValid = false;
        }

        return isDataValid;
    }

    private void clearError(HBox errorBox, Text errorText) {
        errorBox.setVisible(false);
        errorText.setText("");
    }

    private void setError(HBox errorBox, Text errorText, String errorMessage) {
        errorBox.setVisible(true);
        errorText.setText(errorMessage);
    }

    /**
     * Switches to login scene
     */
    private void openLogin() {
        primaryStage.setScene(loginScene);
    }
}
