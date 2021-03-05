package docmate.controller;

import docmate.database.Database;
import docmate.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FXMLPatientController implements Initializable {

    public static final String TAG = "DOCMATE";

    @FXML
    private TextField searchPatientTextField;
    @FXML
    private Button searchPatientButton;
    @FXML
    private Button addPatientButton;

    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, ?> patientIdColumn;
    @FXML
    private TableColumn<Patient, ?> patientNameColumn;
    @FXML
    private TableColumn<Patient, ?> patientAgeColumn;
    @FXML
    private TableColumn<Patient, ?> patientSexColumn;
    @FXML
    private TableColumn<Patient, ?> patientPrescriptionsColumn;
    @FXML
    private TableColumn<Patient, ?> patientRegistrationDateColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addPatientButton.setOnAction(event -> openPatientAdd());
        searchPatientButton.setOnAction(event -> searchPatient());

        getPatients();
    }

    private void getPatients() {
        new Thread(new Task<ObservableList<Patient>>() {

            @Override
            protected ObservableList<Patient> call() {
                ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();
                Patient patient;

                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT S.PatientId, S.PatientName, S.PatientDOB, S.PatientSex, " +
                                "ISNULL(S.PatientPhone, 'None') as PatientPhone, ISNULL(S.PatientAddress, " +
                                "'None') as PatientAddress, ISNULL(S.PatientEmail, 'None') as PatientEmail," +
                                "S.PatientRegistrationDate, ISNULL(S.PatientComments, 'None') as PatientComments, " +
                                "ISNULL(C.NumberOfPrescriptions,0) as NumberOfPrescriptions FROM PATIENT S LEFT " +
                                "JOIN (SELECT PatientId, COUNT(PatientId) as NumberOfPrescriptions " +
                                "FROM PRESCRIPTION GROUP BY PatientId) C ON S.PatientId = C.PatientId";

                        preparedStatement = connection.prepareStatement(query);
                        resultSet = preparedStatement.executeQuery();

                        // Creating Patient object with retrieved data
                        while (resultSet.next()) {
                            patient = new Patient();

                            patient.setId(resultSet.getInt("PatientId"));
                            patient.setName(resultSet.getString("PatientName"));
                            patient.setDateOfBirth(resultSet.getDate("PatientDOB").toLocalDate());
                            patient.setSex(resultSet.getString("PatientSex"));
                            patient.setNumberOfPrescriptions(resultSet.getInt("NumberOfPrescriptions"));
                            patient.setRegistrationDate(resultSet.getDate("PatientRegistrationDate")
                                    .toLocalDate());
                            patient.setPhone(resultSet.getString("PatientPhone"));
                            patient.setEmail(resultSet.getString("PatientEmail"));
                            patient.setAddress(resultSet.getString("PatientAddress"));
                            patient.setComments(resultSet.getString("PatientComments"));

                            patientObservableList.add(patient);
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    Database.getInstance().close(connection, preparedStatement, resultSet);
                }

                return patientObservableList;
            }

            @Override
            protected void updateValue(ObservableList<Patient> patientObservableList) {
                super.updateValue(patientObservableList);

                if (patientObservableList != null) {
                    setPatientTable(patientObservableList);
                }
            }
        }).start();
    }

    private void searchPatient() {
        String searchKey = searchPatientTextField.getText().toLowerCase();

        if (searchKey.isEmpty()) {
            getPatients();
            return;
        }

        new Thread(new Task<ObservableList<Patient>>() {

            @Override
            protected ObservableList<Patient> call() {
                ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();
                Patient patient;

                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT S.PatientId, S.PatientName,S.PatientDOB,S.PatientSex, S.PatientPhone, " +
                                "S.PatientAddress, S.PatientEmail, S.PatientRegistrationDate, S.PatientComments, " +
                                "ISNULL(C.NoOfPrescription, 0) as NumberOfPrescriptions FROM PATIENT S " +
                                "LEFT JOIN (SELECT PatientId, COUNT(PatientId) as NoOfPrescription " +
                                "FROM PRESCRIPTION GROUP BY PatientId) C ON S.PatientId = C.PatientId " +
                                "WHERE S.PatientName LIKE ? OR S.PatientId LIKE ?";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, searchKey + "%");
                        preparedStatement.setString(2, searchKey);

                        resultSet = preparedStatement.executeQuery();

                        // Creating Patient object with retrieved data
                        while (resultSet.next()) {
                            patient = new Patient();

                            patient.setId(resultSet.getInt("PatientId"));
                            patient.setName(resultSet.getString("PatientName"));
                            patient.setDateOfBirth(resultSet.getDate("PatientDOB").toLocalDate());
                            patient.setSex(resultSet.getString("PatientSex"));
                            patient.setNumberOfPrescriptions(resultSet.getInt("NumberOfPrescriptions"));
                            patient.setRegistrationDate(resultSet.getDate("PatientRegistrationDate")
                                    .toLocalDate());
                            patient.setPhone(resultSet.getString("PatientPhone"));
                            patient.setEmail(resultSet.getString("PatientEmail"));
                            patient.setAddress(resultSet.getString("PatientAddress"));
                            patient.setComments(resultSet.getString("PatientComments"));

                            patientObservableList.add(patient);
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    Database.getInstance().close(connection, preparedStatement, resultSet);
                }

                return patientObservableList;
            }

            @Override
            protected void updateValue(ObservableList<Patient> patientObservableList) {
                super.updateValue(patientObservableList);

                if (patientObservableList != null) {
                    setPatientTable(patientObservableList);
                }
            }
        }).start();
    }

    private void setPatientTable(ObservableList<Patient> patientObservableList) {
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patientAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        patientSexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        patientPrescriptionsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPrescriptions"));
        patientRegistrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        patientTable.setItems(patientObservableList);

        patientTable.setRowFactory(tableView -> {
            TableRow<Patient> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if (!tableRow.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Patient patient = tableRow.getItem();
                    // Opening patient view
                    openPatientView(patient);
                }
            });

            return tableRow;
        });
    }

    private void openPatientView(Patient patient) {
        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);

            primaryStage.getIcons().add(new Image("docmate/res/docmate_logo_96px.png"));
            primaryStage.setTitle("DocMate");

            // Loading patient view scene
            FXMLLoader patientViewFXMLLoader =
                    new FXMLLoader(getClass().getResource("../view/FXMLPatientView.fxml"));
            Parent patientViewParent = patientViewFXMLLoader.load();
            Scene patientViewScene = new Scene(patientViewParent);

            // Injecting stage and data into patient view controller
            FXMLPatientViewController fxmlPatientViewController = patientViewFXMLLoader.getController();
            fxmlPatientViewController.setPrimaryStage(primaryStage);
            fxmlPatientViewController.setPatient(patient);

            primaryStage.setScene(patientViewScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
}
