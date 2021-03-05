package docmate.controller;

import docmate.database.Database;
import docmate.model.Doctor;
import docmate.util.AlertMessage;
import docmate.util.LoginConfig;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLLoginController implements Initializable {

    public static final String TAG = "DOCMATE";

    private Stage primaryStage;
    private Scene registerScene;

    @FXML
    private ProgressIndicator loginProgressIndicator;

    @FXML
    private TextField loginEmailField;
    @FXML
    private HBox loginEmailError;
    @FXML
    private Text loginEmailErrorText;

    @FXML
    private PasswordField loginPasswordField;
    @FXML
    public HBox loginPasswordError;
    @FXML
    private Text loginPasswordErrorText;

    @FXML
    public CheckBox rememberMeCheckbox;
    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink openRegisterLink;

    private String loginEmail;
    private String loginPassword;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setRegisterScene(Scene registerScene) {
        this.registerScene = registerScene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(event -> logIn());
        openRegisterLink.setOnAction(event -> openRegister());
    }

    private void logIn() {
        if (validateData()) {
            readFromDatabase();
        }
    }

    private void readFromDatabase() {
        loginProgressIndicator.setVisible(true);

        new Thread(new Task<Doctor>() {

            @Override
            protected Doctor call() {
                Doctor doctor = null;

                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT * FROM DOCTOR where DoctorEmail = ?";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, loginEmail);

                        resultSet = preparedStatement.executeQuery();

                        // Setting retrieved data to Doctor object
                        if (resultSet.next()) {
                            doctor = Doctor.getInstance();

                            doctor.setId(resultSet.getInt("DoctorId"));
                            doctor.setName(resultSet.getString("DoctorName"));
                            doctor.setEmail(resultSet.getString("DoctorEmail"));
                            doctor.setPhone(resultSet.getString("DoctorPhone"));
                            doctor.setPassword(resultSet.getString("DoctorPassword"));
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    Database.getInstance().close(connection, preparedStatement, resultSet);
                }

                return doctor;
            }

            @Override
            protected void updateValue(Doctor doctor) {
                super.updateValue(doctor);

                if (doctor != null) {
                    if (loginPassword.equals(doctor.getPassword())) {
                        loginEmailField.clear();
                        loginPasswordField.clear();

                        // Saving login config
                        if (rememberMeCheckbox.isSelected()) {
                            LoginConfig.getInstance().writeLoginConfig(doctor);
                        } else {
                            LoginConfig.getInstance().clearLoginConfig();
                        }

                        loginProgressIndicator.setVisible(false);
                        Logger.getLogger(TAG).log(Level.INFO, "Logged in!");

                        // Opening main application
                        openDocMate();

                        // Closing launcher
                        primaryStage.close();
                    } else {
                        Logger.getLogger(TAG).log(Level.INFO, "Wrong password!");

                        loginProgressIndicator.setVisible(false);

                        AlertMessage alertMessage = new AlertMessage("Wrong password!");
                        alertMessage.showAndWait();
                    }
                } else {
                    Logger.getLogger(TAG).log(Level.INFO, "Wrong email!");

                    loginProgressIndicator.setVisible(false);

                    AlertMessage alertMessage = new AlertMessage("Wrong email!");
                    alertMessage.showAndWait();
                }
            }
        }).start();
    }

    private boolean validateData() {
        loginEmail = loginEmailField.getText().trim().toLowerCase();
        loginPassword = loginPasswordField.getText().trim();

        boolean isDataValid = true;

        clearError(loginEmailError, loginEmailErrorText);
        clearError(loginPasswordError, loginPasswordErrorText);

        // Email Validation
        if (loginEmail.isEmpty()) {
            setError(loginEmailError, loginEmailErrorText, "Email can not be empty");
            isDataValid = false;
        } else if (!loginEmail.matches("[a-zA-Z0-9+._%\\-]{1,256}" + "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")) {
            setError(loginEmailError, loginEmailErrorText, "Invalid email format");
            isDataValid = false;
        }

        // Password Validation
        if (loginPassword.isEmpty()) {
            setError(loginPasswordError, loginPasswordErrorText, "Password can not be empty");
            isDataValid = false;
        } else if (loginPassword.contains(" ")) {
            setError(loginPasswordError, loginPasswordErrorText,
                    "Password should not contain spaces");
            isDataValid = false;
        } else if (loginPassword.length() < 4) {
            setError(loginPasswordError, loginPasswordErrorText,
                    "Password requires at least 4 characters");
            isDataValid = false;
        } else if (loginPassword.length() > 30) {
            setError(loginPasswordError, loginPasswordErrorText,
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
     * Switches to register scene
     */
    private void openRegister() {
        primaryStage.setScene(registerScene);
    }

    /**
     * Opens docmate window
     */
    private void openDocMate() {
        try {
            Stage primaryStage = new Stage();
            primaryStage.getIcons().add(new Image("docmate/res/docmate_logo_96px.png"));
            primaryStage.setTitle("DocMate");

            // Loading docmate scene
            FXMLLoader docmateFXMLLoader = new FXMLLoader(getClass().getResource("../view/FXMLDocMate.fxml"));
            Parent docmateRoot = docmateFXMLLoader.load();
            Scene docmateScene = new Scene(docmateRoot);

            primaryStage.setScene(docmateScene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
