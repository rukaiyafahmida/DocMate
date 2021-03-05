package docmate.controller;

import docmate.model.Patient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLPatientViewController implements Initializable {

    public static final String TAG = "DOCMATE";

    private Stage primaryStage;
    private Patient patient;

    @FXML
    private Text patientName;
    @FXML
    private Text patientId;
    @FXML
    private Text patientAge;
    @FXML
    private Text patientSex;
    @FXML
    private Text patientPhone;
    @FXML
    private Text patientEmail;
    @FXML
    private Text patientAddress;
    @FXML
    private Text patientComments;
    @FXML
    private Text patientPrescriptions;
    @FXML
    private Text patientRegistrationDate;

    @FXML
    private Button editInfoButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button addPrescriptionButton;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        setPatientData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editInfoButton.setOnAction(event -> openEditInfo());
        historyButton.setOnAction(event -> openHistory());
        addPrescriptionButton.setOnAction(event -> openPrescriptionAdd());
    }

    private void setPatientData() {
        patientName.setText(patient.getName());
        patientId.setText("ID: " + patient.getId());
        patientAge.setText("Age: " + patient.getAge());
        patientSex.setText("Sex: " + patient.getSex());

        patientPrescriptions.setText("Prescriptions: " + patient.getNumberOfPrescriptions());
        patientRegistrationDate.setText("Registered: " + patient.getRegistrationDate());

        patientPhone.setText("Phone: " + patient.getPhone());
        patientEmail.setText("Email: " + patient.getEmail());
        patientAddress.setText("Address: " + patient.getAddress());
        patientComments.setText("Comments: " + patient.getComments());
    }

    private void openEditInfo() {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.getIcons().add(new Image("docmate/res/docmate_logo_96px.png"));
            stage.setTitle("DocMate");

            // Loading edit patient scene
            FXMLLoader patientNewFXMLLoader =
                    new FXMLLoader(getClass().getResource("../view/FXMLPatientAddEdit.fxml"));
            Parent patientNewParent = patientNewFXMLLoader.load();
            Scene patientNewScene = new Scene(patientNewParent);

            // Injecting stage and patient object into patient new controller
            FXMLPatientAddEditController fxmlPatientAddEditController = patientNewFXMLLoader.getController();
            fxmlPatientAddEditController.setPrimaryStage(stage);
            fxmlPatientAddEditController.setPatient(patient);

            stage.setScene(patientNewScene);
            stage.setResizable(false);
            stage.show();

            // Closing this window
            primaryStage.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void openHistory() {

    }

    private void openPrescriptionAdd() {
        try {
            Stage stage = new Stage();
            stage.getIcons().add(new Image("docmate/res/docmate_logo_96px.png"));
            stage.setTitle("DocMate");

            // Loading prescription new scene
            FXMLLoader prescriptionNewFXMLLoader = new FXMLLoader(getClass()
                    .getResource("../view/FXMLPrescriptionAdd.fxml"));
            Parent prescriptionNewRoot = prescriptionNewFXMLLoader.load();
            Scene prescriptionNewScene = new Scene(prescriptionNewRoot);

            // Injecting stage and patient object into prescription new controller
            FXMLPrescriptionAddController prescriptionAddController = prescriptionNewFXMLLoader.getController();
            prescriptionAddController.setPrimaryStage(stage);
            prescriptionAddController.setPatient(patient);

            stage.setScene(prescriptionNewScene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
