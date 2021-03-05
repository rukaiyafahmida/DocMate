package docmate.controller;

import docmate.database.Database;
import docmate.model.MedicineBrand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLMedicineBrandController implements Initializable {

    public static final String TAG = "DOCMATE";

    private TextField searchMedicineTextField;
    private Button searchMedicineButton;

    @FXML
    private TableView<MedicineBrand> brandMedicineTable;
    @FXML
    private TableColumn<MedicineBrand, ?> medicineIdColumn;
    @FXML
    private TableColumn<MedicineBrand, ?> brandNameColumn;
    @FXML
    private TableColumn<MedicineBrand, ?> genericNameColumn;
    @FXML
    private TableColumn<MedicineBrand, ?> strengthColumn;
    @FXML
    private TableColumn<MedicineBrand, ?> dosageFormColumn;
    @FXML
    private TableColumn<MedicineBrand, ?> manufacturerColumn;

    public void setSearchUtility(TextField searchMedicineTextField, Button searchMedicineButton) {
        this.searchMedicineTextField = searchMedicineTextField;
        this.searchMedicineButton = searchMedicineButton;

        searchMedicineButton.setOnAction(event -> searchBrandMedicine());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getBrandMedicines();
    }

    private void getBrandMedicines() {
        new Thread(new Task<ObservableList<MedicineBrand>>() {
            @Override
            protected ObservableList<MedicineBrand> call() {
                ObservableList<MedicineBrand> medicineObservableListObservableList = FXCollections.observableArrayList();
                MedicineBrand medicineBrand;

                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT M.MedicineId, M.BrandName, M.Strength, M.DosageForm, " +
                                "MAN.ManufacturerName, MAN.ManufacturerId, G.GenericId, G.GenericName " +
                                "FROM MEDICINE M LEFT JOIN MANUFACTURER MAN ON M.ManufacturerId = MAN.ManufacturerId " +
                                "LEFT JOIN GENERIC_MEDICINE G ON G.GenericId=M.GenericId";

                        preparedStatement = connection.prepareStatement(query);
                        resultSet = preparedStatement.executeQuery();

                        // Creating MedicineBrand object with retrieved data
                        while (resultSet.next()) {
                            medicineBrand = new MedicineBrand();

                            medicineBrand.setMedicineId(resultSet.getInt("MedicineId"));
                            medicineBrand.setBrandName(resultSet.getString("BrandName"));
                            medicineBrand.setGenericId(resultSet.getInt("GenericId"));
                            medicineBrand.setManufacturerId(resultSet.getInt("ManufacturerId"));
                            medicineBrand.setGenericName(resultSet.getString("GenericName"));
                            medicineBrand.setManufacturerName(resultSet.getString("ManufacturerName"));
                            medicineBrand.setStrength(resultSet.getString("Strength"));
                            medicineBrand.setDosageForm(resultSet.getString("DosageForm"));

                            medicineObservableListObservableList.add(medicineBrand);
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    Database.getInstance().close(connection, preparedStatement, resultSet);
                }

                return medicineObservableListObservableList;
            }

            @Override
            protected void updateValue(ObservableList<MedicineBrand> medicineBrandObservableList) {
                super.updateValue(medicineBrandObservableList);

                if (medicineBrandObservableList != null) {
                    setBrandMedicineTable(medicineBrandObservableList);
                } else {
                    Logger.getLogger(TAG).log(Level.INFO, "Nothing!");
                }
            }
        }).start();
    }

    private void searchBrandMedicine() {
        String searchKey = searchMedicineTextField.getText().toLowerCase();

        if (searchKey.isEmpty()) {
            getBrandMedicines();
            return;
        }

        new Thread(new Task<ObservableList<MedicineBrand>>() {
            @Override
            protected ObservableList<MedicineBrand> call() {
                ObservableList<MedicineBrand> medicineBrandObservableList = FXCollections.observableArrayList();
                MedicineBrand medicineBrand;

                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT BrandName, MedicineId, Strength, DosageForm, GenericName, M.GenericId, " +
                                "ManufacturerName, SideEffect, M.ManufacturerId FROM MANUFACTURER MAN JOIN MEDICINE M " +
                                "ON MAN.ManufacturerId = M.ManufacturerId JOIN GENERIC_MEDICINE G " +
                                "ON G.GenericId = M.GenericId WHERE BrandName LIKE ?";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, searchKey + "%");

                        resultSet = preparedStatement.executeQuery();

                        // Creating MedicineBrand object with retrieved data
                        while (resultSet.next()) {
                            medicineBrand = new MedicineBrand();

                            medicineBrand.setMedicineId(resultSet.getInt("MedicineId"));
                            medicineBrand.setBrandName(resultSet.getString("BrandName"));
                            medicineBrand.setGenericId(resultSet.getInt("GenericId"));
                            medicineBrand.setManufacturerId(resultSet.getInt("ManufacturerId"));
                            medicineBrand.setGenericName(resultSet.getString("GenericName"));
                            medicineBrand.setManufacturerName(resultSet.getString("ManufacturerName"));
                            medicineBrand.setStrength(resultSet.getString("Strength"));
                            medicineBrand.setDosageForm(resultSet.getString("DosageForm"));

                            medicineBrandObservableList.add(medicineBrand);
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    Database.getInstance().close(connection, preparedStatement, resultSet);
                }

                return medicineBrandObservableList;
            }

            @Override
            protected void updateValue(ObservableList<MedicineBrand> medicineBrandObservableList) {
                super.updateValue(medicineBrandObservableList);

                if (medicineBrandObservableList != null) {
                    setBrandMedicineTable(medicineBrandObservableList);
                }
            }
        }).start();
    }

    private void setBrandMedicineTable(ObservableList<MedicineBrand> medicineBrandObservableList) {
        medicineIdColumn.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        brandNameColumn.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        genericNameColumn.setCellValueFactory(new PropertyValueFactory<>("genericName"));
        strengthColumn.setCellValueFactory(new PropertyValueFactory<>("strength"));
        dosageFormColumn.setCellValueFactory(new PropertyValueFactory<>("dosageForm"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturerName"));

        brandMedicineTable.setItems(medicineBrandObservableList);

        brandMedicineTable.setRowFactory(tableView -> {
            TableRow<MedicineBrand> tableRow = new TableRow<>();

            tableRow.setOnMouseClicked(event -> {
                if (!tableRow.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    MedicineBrand medicineBrand = tableRow.getItem();
                    // Opening medicine view
                }
            });

            return tableRow;
        });
    }
}
