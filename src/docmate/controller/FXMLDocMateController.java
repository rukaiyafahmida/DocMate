package docmate.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLDocMateController implements Initializable {

    public static final String TAG = "DOCMATE";

    private Stage primaryStage;

    @FXML
    private ImageView profileImageButton;

    @FXML
    private Button homeButton;
    @FXML
    private Button addButton;
    @FXML
    private Button patientButton;
    @FXML
    private Button prescriptionButton;
    @FXML
    private Button medicineButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button settingsButton;

    @FXML
    private VBox mainContentPane;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Opening home initially
        openHome();

        // Setting event handler
        homeButton.setOnAction(event -> openHome());
        patientButton.setOnAction(event -> openPatient());
        prescriptionButton.setOnAction(event -> openPrescription());
        medicineButton.setOnAction(event -> openMedicine());
        profileButton.setOnAction(event -> openProfile());
        settingsButton.setOnAction(event -> openSettings());
    }

    private void openHome() {
        try {
            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(FXMLLoader.load(getClass()
                    .getResource("../view/FXMLHome.fxml")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void openPatient() {
        try {
            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(FXMLLoader.load(getClass()
                    .getResource("../view/FXMLPatient.fxml")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void openPrescription() {
        try {
            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(FXMLLoader.load(getClass()
                    .getResource("../view/FXMLPrescription.fxml")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void openMedicine() {
        try {
            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(FXMLLoader.load(getClass()
                    .getResource("../view/FXMLMedicine.fxml")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void openProfile() {
        try {
            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(FXMLLoader.load(getClass()
                    .getResource("../view/FXMLProfile.fxml")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void openSettings() {
        try {
            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(FXMLLoader.load(getClass()
                    .getResource("../view/FXMLSettings.fxml")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
