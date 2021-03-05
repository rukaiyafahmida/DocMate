package docmate.controller;

import docmate.database.Database;
import docmate.model.PatientMedicine;
import docmate.model.Prescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static j2html.TagCreator.*;

public class FXMLPrescriptionViewController implements Initializable {

    public static final String TAG = "DOCMATE";

    private Stage stage;
    private Prescription prescription;

    @FXML
    private ImageView patientImageView;
    @FXML
    private Text patientNameText;
    @FXML
    private Text patientIdText;
    @FXML
    private Text patientAgeText;
    @FXML
    private Text patientSexText;

    @FXML
    private Button prescriptionCloseButton;
    @FXML
    private Button prescriptionPrintButton;

    @FXML
    private WebView prescriptionWebView;

    private WebEngine webEngine;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
        setPatientData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = new WebEngine();

        prescriptionPrintButton.setOnAction(event -> printPrescription());
        prescriptionCloseButton.setOnAction(event -> stage.close());
    }

    private void setPatientData() {
        patientNameText.setText(prescription.getPatientName());
        patientIdText.setText("ID: " + prescription.getPatientId());
        patientAgeText.setText("Age: " + prescription.getPatientAge());
        patientSexText.setText("Sex: " + prescription.getPatientSex());

        getPatientMedicines();
    }

    private void getPatientMedicines() {
        new Thread(new Task<ObservableList<PatientMedicine>>() {

            @Override
            protected ObservableList<PatientMedicine> call() {
                ObservableList<PatientMedicine> patientMedicineObservableList = FXCollections.observableArrayList();
                PatientMedicine patientMedicine;

                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT PM.PrescriptionId, PM.MedicineId, M.BrandName, M.DosageForm, " +
                                "M.Strength, PM.DrugAdvice, PM.DrugDosages, PM.DrugDuration FROM PRESCRIPTION P " +
                                "JOIN PATIENT_MEDICINE PM ON P.PrescriptionId = PM.PrescriptionId " +
                                "INNER JOIN MEDICINE M ON PM.MedicineId = M.MedicineId WHERE PM.PrescriptionId = ?";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, prescription.getPrescriptionId());

                        resultSet = preparedStatement.executeQuery();

                        // Creating PatientMedicine object with retrieved data
                        while (resultSet.next()) {
                            patientMedicine = new PatientMedicine();

                            patientMedicine.setPrescriptionId(resultSet.getInt("PrescriptionId"));
                            patientMedicine.setMedicineId(resultSet.getInt("MedicineId"));
                            patientMedicine.setDosageForm(resultSet.getString("DosageForm"));
                            patientMedicine.setBrandName(resultSet.getString("BrandName"));
                            patientMedicine.setStrength(resultSet.getString("Strength"));
                            patientMedicine.setDrugDosages(resultSet.getString("DrugDosages"));
                            patientMedicine.setDrugDuration(resultSet.getString("DrugDuration"));
                            patientMedicine.setDrugAdvice(resultSet.getString("DrugAdvice"));

                            patientMedicineObservableList.add(patientMedicine);
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    Database.getInstance().close(connection, preparedStatement, resultSet);
                }

                return patientMedicineObservableList;
            }

            @Override
            protected void updateValue(ObservableList<PatientMedicine> patientMedicineObservableList) {
                super.updateValue(patientMedicineObservableList);

                if (patientMedicineObservableList != null) {
                    setPrescriptionView(patientMedicineObservableList);
                }
            }
        }).start();
    }

    private void setPrescriptionView(ObservableList<PatientMedicine> patientMedicineObservableList) {
        File file = new File("Prescription.html");

        try {
            boolean isFileCreated = file.createNewFile();
            PrintStream printStream = new PrintStream(file);
            printStream.print(createPrescription(patientMedicineObservableList));
            webEngine = prescriptionWebView.getEngine();
            webEngine.load(file.toURI().toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void printPrescription() {
        Printer printer = Printer.getDefaultPrinter();
        printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.EQUAL);
        PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
        printerJob.showPrintDialog(null);
        webEngine.print(printerJob);
        printerJob.endJob();
    }

    private String createPrescription(ObservableList<PatientMedicine> patientMedicineObservableList) {
        return html(
                head(title("Prescription")),
                body(
                        hr(),

                        table().with(tr().with(
                                td().with(span("Patient ID: " + prescription.getPatientId())),
                                td().with(span("Name: " + prescription.getPatientName())),
                                td().with(span("Age: " + prescription.getPatientAge() + " Years")),
                                td().with(span("Sex: " + prescription.getPatientSex())),
                                td().with(span("Date: " + prescription.getDate())))
                        ).withStyle("width:100%;"),

                        hr(),

                        div(
                                p(b("Symptoms"), br(), span(prescription.getSymptoms())),
                                p(b("Observation"), br(), span(prescription.getObservation())),
                                p(b("Tests"), br(), span(prescription.getTests())),
                                p(b("Diagnosis"), br(), span(prescription.getDiagnosis())),
                                p(b("Advice"), br(), span(prescription.getAdvice())),
                                p(b("Visit again"), br(), span(prescription.getVisitAgain()))
                        ).withStyle("display:inline; float:left; width:40%;"),

                        div(h1("Rx"),
                                ol(each(patientMedicineObservableList, patientMedicine -> tr(
                                        li(i(patientMedicine.getDosageForm() + " "),
                                                b(" " + patientMedicine.getBrandName()),
                                                span(" " + patientMedicine.getStrength() + " "),
                                                ul(
                                                        li(patientMedicine.getDrugDosages()
                                                                + " - " + patientMedicine.getDrugDuration()
                                                        ).withStyle("list-style: none"),
                                                        li(patientMedicine.getDrugAdvice()
                                                        ).withStyle("list-style: none")
                                                ).withStyle("padding-left: 16px"))
                                        ))
                                )
                        ).withStyle("display:inline; float:left; width:60%;")
                )
        ).render();
    }
}
