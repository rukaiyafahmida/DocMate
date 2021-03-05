package docmate.controller;

import docmate.database.Database;
import javafx.concurrent.Task;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FXMLHomeController implements Initializable {

    public static final String TAG = "DOCMATE";

    @FXML
    private Text totalPatientText;
    @FXML
    private Button addPatientButton;
    @FXML
    private Text totalPrescriptionText;
    @FXML
    private Button addPrescriptionButton;
    @FXML
    private Text totalMedicineText;
    @FXML
    private Button addMedicineButton;

    private int totalPatient;
    private int totalMedicine;
    private int totalPrescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addPatientButton.setOnAction(event -> openPatientAdd());
        addPrescriptionButton.setOnAction(event -> openPrescriptionAdd());

        setTotal();
    }

    private void setTotal() {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT COUNT(MedicineId) as TotalMedicine, " +
                                "(SELECT COUNT(PrescriptionId) FROM PRESCRIPTION)as TotalPrescription, " +
                                "(SELECT COUNT(PatientId) FROM PATIENT)as TotalPatient FROM MEDICINE";

                        preparedStatement = connection.prepareStatement(query);
                        resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                            totalMedicine = resultSet.getInt("TotalMedicine");
                            totalPatient = resultSet.getInt("TotalPatient");
                            totalPrescription = resultSet.getInt("TotalPrescription");
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    Database.getInstance().close(connection, preparedStatement, resultSet);
                }

                return null;
            }

            @Override
            protected void updateValue(Void value) {
                super.updateValue(value);
                totalMedicineText.setText(String.valueOf(totalMedicine));
                totalPatientText.setText(String.valueOf(totalPatient));
                totalPrescriptionText.setText(String.valueOf(totalPrescription));
            }
        }).start();
    }

    private void openPatientAdd() {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.getIcons().add(new Image("docmate/res/docmate_logo_96px.png"));
            stage.setTitle("DocMate");

            // Loading add patient scene
            FXMLLoader patientNewFXMLLoader =
                    new FXMLLoader(getClass().getResource("../view/FXMLPatientAddEdit.fxml"));
            Parent patientNewParent = patientNewFXMLLoader.load();
            Scene patientNewScene = new Scene(patientNewParent);

            // Injecting stage into patient new controller
            FXMLPatientAddEditController fxmlPatientAddEditController = patientNewFXMLLoader.getController();
            fxmlPatientAddEditController.setPrimaryStage(stage);

            stage.setScene(patientNewScene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void openPrescriptionAdd() {
        try {
            Stage stage = new Stage();
            stage.getIcons().add(new Image("docmate/res/docmate_logo_96px.png"));
            stage.setTitle("DocMate");

            // Loading add prescription scene
            FXMLLoader prescriptionNewFXMLLoader = new FXMLLoader(getClass()
                    .getResource("../view/FXMLPrescriptionAdd.fxml"));
            Parent prescriptionNewRoot = prescriptionNewFXMLLoader.load();
            Scene prescriptionNewScene = new Scene(prescriptionNewRoot);

            // Injecting stage and patient object into prescription new controller
            FXMLPrescriptionAddController prescriptionAddController = prescriptionNewFXMLLoader.getController();
            prescriptionAddController.setPrimaryStage(stage);

            stage.setScene(prescriptionNewScene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
