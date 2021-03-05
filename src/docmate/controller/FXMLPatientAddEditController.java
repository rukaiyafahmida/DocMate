package docmate.controller;

import docmate.database.Database;
import docmate.model.Doctor;
import docmate.model.Patient;
import docmate.util.AlertMessage;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FXMLPatientAddEditController implements Initializable {

    public static final String TAG = "DOCMATE";

    private Stage primaryStage;
    private Patient patient;

    @FXML
    private Text titleText;

    @FXML
    private TextField patientNameField;
    @FXML
    private HBox patientNameError;
    @FXML
    private Text patientNameErrorText;

    @FXML
    private DatePicker patientDOBPicker;
    @FXML
    private HBox patientDOBError;
    @FXML
    private Text patientDOBErrorText;

    @FXML
    private ComboBox<String> patientSexComboBox;
    @FXML
    private HBox patientSexError;
    @FXML
    private Text patientSexErrorText;

    @FXML
    private TextField patientPhoneField;
    @FXML
    private HBox patientPhoneError;
    @FXML
    private Text patientPhoneErrorText;

    @FXML
    private HBox patientEmailError;
    @FXML
    private TextField patientEmailField;
    @FXML
    private Text patientEmailErrorText;

    @FXML
    private TextField patientAddressField;
    @FXML
    private TextArea patientCommentsArea;

    @FXML
    private Button saveOrUpdatePatientButton;

    @FXML
    private ProgressIndicator savePatientProgress;

    private String patientName;
    private LocalDate patientDOB;
    private String patientSex;
    private String patientPhone;
    private String patientEmail;
    private String patientAddress;
    private String patientComments;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        setEditPatientData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPatientSexComboBox();
        saveOrUpdatePatientButton.setOnAction(event -> saveOrUpdatePatient());
    }

    private void initPatientSexComboBox() {
        patientSexComboBox.getItems().addAll("Male", "Female", "Other");
    }

    private void setEditPatientData() {
        patientNameField.setText(patient.getName());
        patientDOBPicker.setValue(patient.getDateOfBirth());
        patientSexComboBox.setValue(patient.getSex());

        if (patient.getPhone() != null) {
            patientPhoneField.setText(patient.getPhone());
        }
        if (patient.getEmail() != null) {
            patientEmailField.setText(patient.getEmail());
        }
        if (patient.getAddress() != null) {
            patientAddressField.setText(patient.getAddress());
        }
        if (patient.getComments() != null) {
            patientCommentsArea.setText(patient.getComments());
        }

        titleText.setText("Edit Patient");
        saveOrUpdatePatientButton.setText("Update");
    }

    private void saveOrUpdatePatient() {
        if (validateData()) {
            if (patient == null) {
                saveToDatabase();
            } else {
                updateInDatabase();
            }
        }
    }

    /**
     * Saves new patient to database
     */
    private void saveToDatabase() {
        savePatientProgress.setVisible(true);

        new Thread(new Task<Boolean>() {

            @Override
            protected Boolean call() {
                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "INSERT INTO PATIENT (PatientName, PatientDOB, PatientSex, " +
                                "PatientPhone, PatientAddress, PatientEmail, PatientComments, " +
                                "PatientRegistrationDate, DoctorId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, patientName);
                        preparedStatement.setDate(2, Date.valueOf(patientDOB));
                        preparedStatement.setString(3, patientSex);
                        if (!patientPhone.isEmpty()) {
                            preparedStatement.setString(4, patientPhone);
                        } else {
                            preparedStatement.setNull(4, Types.VARCHAR);
                        }
                        if (!patientAddress.isEmpty()) {
                            preparedStatement.setString(5, patientAddress);
                        } else {
                            preparedStatement.setNull(5, Types.VARCHAR);
                        }
                        if (!patientEmail.isEmpty()) {
                            preparedStatement.setString(6, patientEmail);
                        } else {
                            preparedStatement.setNull(6, Types.VARCHAR);
                        }
                        if (!patientComments.isEmpty()) {
                            preparedStatement.setString(7, patientComments);
                        } else {
                            preparedStatement.setNull(7, Types.VARCHAR);
                        }
                        preparedStatement.setDate(8, Date.valueOf(LocalDate.now()));
                        preparedStatement.setInt(9, Doctor.getInstance().getId());

                        preparedStatement.executeUpdate();
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
                    patientNameField.clear();
                    patientCommentsArea.clear();
                    patientAddressField.clear();
                    patientEmailField.clear();
                    patientPhoneField.clear();
                    patientDOBPicker.setValue(null);
                    patientSexComboBox.setValue(null);

                    savePatientProgress.setVisible(false);

                    AlertMessage alertMessage = new AlertMessage("Patient saved!");
                    alertMessage.showAndWait();

                    // Closing this window
                    primaryStage.close();
                } else {
                    savePatientProgress.setVisible(false);

                    AlertMessage alertMessage = new AlertMessage("Saving patient failed!");
                    alertMessage.showAndWait();
                }
            }
        }).start();
    }

    /**
     * Updates old user in database
     */
    private void updateInDatabase() {
        savePatientProgress.setVisible(true);

        new Thread(new Task<Boolean>() {

            @Override
            protected Boolean call() {
                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "UPDATE PATIENT SET PatientName = ? ,PatientDOB =  ? ,PatientSex =  ?, " +
                                "PatientPhone = ?, PatientAddress=?, PatientEmail =?, PatientComments = ? " +
                                "WHERE PatientId = ?";

                        preparedStatement = connection.prepareStatement(query);

                        preparedStatement.setString(1, patientName);
                        preparedStatement.setDate(2, Date.valueOf(patientDOB));
                        preparedStatement.setString(3, patientSex);

                        if (!patientPhone.isEmpty()) {
                            preparedStatement.setString(4, patientPhone);
                        } else {
                            preparedStatement.setNull(4, Types.VARCHAR);
                        }
                        if (!patientAddress.isEmpty()) {
                            preparedStatement.setString(5, patientAddress);
                        } else {
                            preparedStatement.setNull(5, Types.VARCHAR);
                        }
                        if (!patientEmail.isEmpty()) {
                            preparedStatement.setString(6, patientEmail);
                        } else {
                            preparedStatement.setNull(6, Types.VARCHAR);
                        }
                        if (!patientComments.isEmpty()) {
                            preparedStatement.setString(7, patientComments);
                        } else {
                            preparedStatement.setNull(7, Types.VARCHAR);
                        }

                        preparedStatement.setInt(8, patient.getId());

                        preparedStatement.executeUpdate();
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
            protected void updateValue(Boolean isUpdated) {
                super.updateValue(isUpdated);

                if (isUpdated) {
                    patientNameField.clear();
                    patientCommentsArea.clear();
                    patientAddressField.clear();
                    patientEmailField.clear();
                    patientPhoneField.clear();
                    patientDOBPicker.setValue(null);
                    patientSexComboBox.setValue(null);

                    savePatientProgress.setVisible(false);

                    AlertMessage alertMessage = new AlertMessage("Patient updated!");
                    alertMessage.showAndWait();

                    // Closing this window
                    primaryStage.close();
                } else {
                    savePatientProgress.setVisible(false);

                    AlertMessage alertMessage = new AlertMessage("Saving update failed!");
                    alertMessage.showAndWait();
                }
            }
        }).start();
    }

    private boolean validateData() {
        patientName = patientNameField.getText().trim();
        patientDOB = patientDOBPicker.getValue();
        patientSex = patientSexComboBox.getValue();
        patientPhone = patientPhoneField.getText().trim();
        patientEmail = patientEmailField.getText().trim().toLowerCase();
        patientAddress = patientAddressField.getText().trim();
        patientComments = patientCommentsArea.getText().trim();

        boolean isDataValid = true;

        clearError(patientNameError, patientNameErrorText);
        clearError(patientDOBError, patientDOBErrorText);
        clearError(patientSexError, patientSexErrorText);
        clearError(patientPhoneError, patientPhoneErrorText);
        clearError(patientEmailError, patientEmailErrorText);

        // Name validation
        if (patientName.isEmpty()) {
            setError(patientNameError, patientNameErrorText, "Name can not be empty");
            isDataValid = false;
        } else if (!patientName.matches("^[a-zA-Z .]*$")) {
            setError(patientNameError, patientNameErrorText,
                    "Name can only contain letters, dots and spaces");
            isDataValid = false;
        } else if (patientName.length() < 3) {
            setError(patientNameError, patientNameErrorText, "Name requires at least 3 characters");
            isDataValid = false;
        }

        // DOB validation
        if (patientDOB == null) {
            setError(patientDOBError, patientDOBErrorText, "Date of birth can not be empty");
            isDataValid = false;
        }

        // Sex validation
        if (patientSex == null) {
            setError(patientSexError, patientSexErrorText, "Sex can not be empty");
            isDataValid = false;
        }

        // Phone validation
        if (!patientPhone.isEmpty() && patientPhone.length() < 11) {
            setError(patientPhoneError, patientPhoneErrorText, "Phone requires at least 11 digits");
            isDataValid = false;
        } else if (!patientPhone.matches("^[0-9]*$")) {
            setError(patientPhoneError, patientPhoneErrorText,
                    "Phone can only contain numbers");
            isDataValid = false;
        }

        // Email Validation
        if (!patientPhone.isEmpty() && !patientEmail.matches("[a-zA-Z0-9+._%\\-]{1,256}" + "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")) {
            setError(patientEmailError, patientEmailErrorText, "Invalid email format");
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
}
