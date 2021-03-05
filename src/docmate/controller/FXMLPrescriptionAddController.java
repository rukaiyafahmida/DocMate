package docmate.controller;

import docmate.database.Database;
import docmate.model.Doctor;
import docmate.model.Patient;
import docmate.model.PatientMedicine;
import docmate.util.AlertMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLPrescriptionAddController implements Initializable {

    public static final String TAG = "DOCMATE";

    private Stage primaryStage;
    private Patient patient;

    @FXML
    private TextField patientIDField;
    @FXML
    private HBox patientInfoPane;
    @FXML
    private ImageView patientImageView;
    @FXML
    private Text patientName;
    @FXML
    private Text patientAge;
    @FXML
    private Text patientSex;

    @FXML
    private TextArea symptomsTextArea;
    @FXML
    private TextArea observationTextArea;
    @FXML
    private TextArea testsTextArea;
    @FXML
    private TextArea diagnosisTextArea;
    @FXML
    private TextArea adviceTextArea;
    @FXML
    private TextField visitAgainTextField;

    @FXML
    private TextField medicineTypeField;
    @FXML
    private TextField medicineNameField;
    @FXML
    private TextField medicineStrengthField;
    @FXML
    private TextField medicineDosageTextField;
    @FXML
    private TextField medicineDurationTextField;
    @FXML
    private TextField medicineAdviceTextField;

    @FXML
    private Button medicineClearButton;
    @FXML
    private Button medicineAddButton;

    @FXML
    private TableView<PatientMedicine> medicineTableView;
    @FXML
    private TableColumn<PatientMedicine, ?> medicineNumberColumn;
    @FXML
    private TableColumn<PatientMedicine, ?> medicineTypeColumn;
    @FXML
    private TableColumn<PatientMedicine, ?> medicineNameColumn;
    @FXML
    private TableColumn<PatientMedicine, ?> medicineStrengthColumn;
    @FXML
    private TableColumn<PatientMedicine, ?> medicineDosageColumn;
    @FXML
    private TableColumn<PatientMedicine, ?> medicineDurationColumn;
    @FXML
    private TableColumn<PatientMedicine, ?> medicineAdviceColumn;

    @FXML
    private Button prescriptionCancelButton;
    @FXML
    private Button prescriptionSaveButton;

    private ObservableList<PatientMedicine> patientMedicineObservableList;

    private String symptoms;
    private String observation;
    private String tests;
    private String diagnosis;
    private String advice;
    private String visitAgain;

    PatientMedicine patientMedicine;

    int prescriptionId;
    int medicineId;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        setPatientData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientMedicineObservableList = FXCollections.observableArrayList();

        medicineClearButton.setOnAction(event -> clearMedicine());
        medicineAddButton.setOnAction(event -> addMedicine());
        prescriptionCancelButton.setOnAction(event -> cancelPrescription());
        prescriptionSaveButton.setOnAction(event -> savePrescription());
    }

    private void setPatientData() {
        patientIDField.setText(String.valueOf(patient.getId()));
        patientName.setText(patient.getName());
        patientAge.setText("Age: " + patient.getAge());
        patientSex.setText("Sex: " + patient.getSex());
        patientInfoPane.setVisible(true);
    }

    private void clearMedicine() {
        medicineTypeField.clear();
        medicineNameField.clear();
        medicineStrengthField.clear();
        medicineDosageTextField.clear();
        medicineDurationTextField.clear();
        medicineAdviceTextField.clear();
    }

    private void addMedicine() {
        String medicineType = medicineTypeField.getText().trim();
        String medicineName = medicineNameField.getText().trim();
        String strength = medicineStrengthField.getText().trim();
        String dosage = medicineDosageTextField.getText().trim();
        String duration = medicineDurationTextField.getText().trim();
        String advice = medicineAdviceTextField.getText().trim();

        new Thread(new Task<Boolean>() {

            @Override
            protected Boolean call() {
                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT MedicineId FROM MEDICINE WHERE BrandName = ?";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, medicineName);

                        resultSet = preparedStatement.executeQuery();


                        if (resultSet.next()) {
                            medicineId = resultSet.getInt("MedicineId");
                            Logger.getLogger(TAG).log(Level.INFO, medicineName + " : " + medicineId);
                        }
                    } else {
                        return false;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return false;
                } finally {
                    Database.getInstance().close(connection, preparedStatement);
                }

                return true;
            }

            @Override
            protected void updateValue(Boolean value) {
                super.updateValue(value);

                PatientMedicine patientMedicine = new PatientMedicine();

                patientMedicine.setMedicineId(medicineId);
                patientMedicine.setDosageForm(medicineType);
                patientMedicine.setBrandName(medicineName);
                patientMedicine.setStrength(strength);
                patientMedicine.setDrugDosages(dosage);
                patientMedicine.setDrugDuration(duration);
                patientMedicine.setDrugAdvice(advice);

                patientMedicineObservableList.add(patientMedicine);

                setPatientTable();
            }
        }).start();
    }

    private void setPatientTable() {
        medicineNumberColumn.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        medicineTypeColumn.setCellValueFactory(new PropertyValueFactory<>("dosageForm"));
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        medicineStrengthColumn.setCellValueFactory(new PropertyValueFactory<>("strength"));
        medicineDosageColumn.setCellValueFactory(new PropertyValueFactory<>("drugDosages"));
        medicineDurationColumn.setCellValueFactory(new PropertyValueFactory<>("drugDuration"));
        medicineAdviceColumn.setCellValueFactory(new PropertyValueFactory<>("drugAdvice"));

        medicineTableView.setItems(patientMedicineObservableList);
    }

    private void saveToDatabase() {
        symptoms = symptomsTextArea.getText().trim();
        tests = symptomsTextArea.getText().trim();
        diagnosis = symptomsTextArea.getText().trim();
        advice = adviceTextArea.getText().trim();
        observation = observationTextArea.getText().trim();
        visitAgain = visitAgainTextField.getText().trim();

        new Thread(new Task<Boolean>() {

            @Override
            protected Boolean call() {
                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "INSERT INTO PRESCRIPTION " +
                                "(PatientId, DoctorId, PrescriptionDate," +
                                "PrescribedTest, Diagnosis, PrescriptionAdvice, " +
                                "VisitAfter, Observation,Symptoms)" +
                                "VALUES(?,?,?,?,?,?,?,?,?)";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, patient.getId());
                        preparedStatement.setInt(2, Doctor.getInstance().getId());
                        preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));

                        if (!tests.isEmpty()) {
                            preparedStatement.setString(4, tests);
                        } else {
                            preparedStatement.setNull(4, Types.VARCHAR);
                        }
                        if (!diagnosis.isEmpty()) {
                            preparedStatement.setString(5, diagnosis);
                        } else {
                            preparedStatement.setNull(5, Types.VARCHAR);
                        }
                        if (!advice.isEmpty()) {
                            preparedStatement.setString(6, advice);
                        } else {
                            preparedStatement.setNull(6, Types.VARCHAR);
                        }
                        if (!visitAgain.isEmpty()) {
                            preparedStatement.setString(7, visitAgain);
                        } else {
                            preparedStatement.setNull(7, Types.VARCHAR);
                        }
                        if (!observation.isEmpty()) {
                            preparedStatement.setString(8, observation);
                        } else {
                            preparedStatement.setNull(8, Types.VARCHAR);
                        }
                        if (!symptoms.isEmpty()) {
                            preparedStatement.setString(9, symptoms);
                        } else {
                            preparedStatement.setNull(9, Types.VARCHAR);
                        }

                        preparedStatement.executeUpdate();

                        query = "SELECT MAX(PrescriptionId) AS PrescriptionId FROM PRESCRIPTION";
                        preparedStatement = connection.prepareStatement(query);
                        ResultSet resultSet2 = preparedStatement.executeQuery();

                        if (resultSet2.next()) {
                            prescriptionId = resultSet2.getInt("PrescriptionId");
                            resultSet2.close();
                        }
                    } else {
                        return false;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return false;
                } finally {
                    Database.getInstance().close(connection, preparedStatement);
                }

                return true;
            }

            @Override
            protected void updateValue(Boolean isSaved) {
                super.updateValue(isSaved);

                if (isSaved) {
                    symptomsTextArea.clear();
                    observationTextArea.clear();
                    adviceTextArea.clear();
                    diagnosisTextArea.clear();
                    visitAgainTextField.clear();
                    testsTextArea.clear();

                    addIdToMeds();
                }
            }
        }).start();
    }

    private void addIdToMeds() {
        for (PatientMedicine patientMedicine : patientMedicineObservableList) {
            patientMedicine.setPrescriptionId(prescriptionId);
        }

        saveToDatabaseMeds();
    }

    private void saveToDatabaseMeds() {
        new Thread(new Task<Boolean>() {

            @Override
            protected Boolean call() {
                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "  INSERT INTO PATIENT_MEDICINE(PrescriptionId,MedicineId," +
                                "DrugDosages,DrugDuration,DrugAdvice) VALUES(?,?,?,?,?)";

                        for (PatientMedicine patientMedicine : patientMedicineObservableList) {
                            preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setInt(1, patientMedicine.getPrescriptionId());
                            preparedStatement.setInt(2, patientMedicine.getMedicineId());
                            preparedStatement.setString(3, patientMedicine.getDrugDosages());
                            preparedStatement.setString(4, patientMedicine.getDrugDuration());
                            preparedStatement.setString(5, patientMedicine.getDrugAdvice());
                            preparedStatement.executeUpdate();
                        }
                    } else {
                        return false;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return false;
                } finally {
                    Database.getInstance().close(connection, preparedStatement);
                }

                return true;
            }

            @Override
            protected void updateValue(Boolean isSaved) {
                super.updateValue(isSaved);

                if (isSaved) {
                    symptomsTextArea.clear();
                    observationTextArea.clear();
                    adviceTextArea.clear();
                    diagnosisTextArea.clear();
                    visitAgainTextField.clear();
                    testsTextArea.clear();

                    // call save medicine


                    AlertMessage alertMessage = new AlertMessage("Patient saved!");
                    alertMessage.showAndWait();

                    // Closing this window
                    primaryStage.close();
                } else {
                    //savePatientProgress.setVisible(false);

                    AlertMessage alertMessage = new AlertMessage("Saving patient failed!");
                    alertMessage.showAndWait();
                }
            }
        }).start();

    }

    private void cancelPrescription() {
        primaryStage.close();
    }

    private void savePrescription() {
        saveToDatabase();
    }
}
