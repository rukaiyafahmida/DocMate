package docmate.controller;

import docmate.database.Database;
import docmate.model.MedicineGeneric;
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

public class FXMLMedicineGenericController implements Initializable {

    private TextField searchMedicineTextField;
    private Button searchMedicineButton;

    @FXML
    private TableView<MedicineGeneric> genericMedicineTable;
    @FXML
    private TableColumn<MedicineGeneric, ?> genericIdColumn;
    @FXML
    private TableColumn<MedicineGeneric, ?> genericNameColumn;
    @FXML
    private TableColumn<MedicineGeneric, ?> therapeuticClassColumn;
    @FXML
    private TableColumn<MedicineGeneric, ?> indicationsColumn;
    @FXML
    private TableColumn<MedicineGeneric, ?> sideEffectsColumn;
    @FXML
    private TableColumn<MedicineGeneric, ?> brandsColumn;

    public void setSearchUtility(TextField searchMedicineTextField, Button searchMedicineButton) {
        this.searchMedicineTextField = searchMedicineTextField;
        this.searchMedicineButton = searchMedicineButton;

        searchMedicineButton.setOnAction(event -> searchGenericMedicine());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getGenericMedicines();
    }

    private void getGenericMedicines() {
        new Thread(new Task<ObservableList<MedicineGeneric>>() {
            @Override
            protected ObservableList<MedicineGeneric> call() {
                ObservableList<MedicineGeneric> medicineGenericObservableList = FXCollections.observableArrayList();
                MedicineGeneric medicineGeneric;

                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT G.GenericId, G.GenericName,G.TherapeuticClass," +
                                "G.Indication,G.SideEffect, ISNULL(M.NoOfMedicine,0)as NoOfMedicine " +
                                "FROM GENERIC_MEDICINE G FULL JOIN (SELECT GenericId, COUNT(GenericId) " +
                                "as NoOfMedicine FROM MEDICINE GROUP BY GenericId) M ON G.GenericId = M.GenericId";

                        preparedStatement = connection.prepareStatement(query);
                        resultSet = preparedStatement.executeQuery();

                        // Creating MedicineGeneric object with retrieved data
                        while (resultSet.next()) {
                            medicineGeneric = new MedicineGeneric();

                            medicineGeneric.setGenericId(resultSet.getInt("GenericId"));
                            medicineGeneric.setGenericName(resultSet.getString("GenericName"));
                            medicineGeneric.setTherapeuticClass(resultSet.getString("TherapeuticClass"));
                            medicineGeneric.setIndications(resultSet.getString("Indication"));
                            medicineGeneric.setSideEffects(resultSet.getString("SideEffect"));
                            medicineGeneric.setBrands(resultSet.getInt("NoOfMedicine"));

                            medicineGenericObservableList.add(medicineGeneric);
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    Database.getInstance().close(connection, preparedStatement, resultSet);
                }

                return medicineGenericObservableList;
            }

            @Override
            protected void updateValue(ObservableList<MedicineGeneric> medicineGenericObservableList) {
                super.updateValue(medicineGenericObservableList);

                if (medicineGenericObservableList != null) {
                    setGenericMedicineTable(medicineGenericObservableList);
                }
            }
        }).start();
    }

    private void searchGenericMedicine() {
        String searchKey = searchMedicineTextField.getText().toLowerCase();

        if (searchKey.isEmpty()) {
            getGenericMedicines();
            return;
        }

        new Thread(new Task<ObservableList<MedicineGeneric>>() {
            @Override
            protected ObservableList<MedicineGeneric> call() {
                ObservableList<MedicineGeneric> medicineGenericObservableList = FXCollections.observableArrayList();
                MedicineGeneric medicineGeneric;

                // Making database call
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = Database.getInstance().getConnection();

                    if (connection != null) {
                        String query = "SELECT G.GenericId, G.GenericName, G.TherapeuticClass,G.Indication, " +
                                "G.SideEffect, ISNULL(M.NoOfMedicine,0) as NoOfMedicine FROM GENERIC_MEDICINE G " +
                                "FULL JOIN (SELECT GenericId, COUNT(GenericId) as NoOfMedicine " +
                                "FROM MEDICINE GROUP BY GenericId) M ON G.GenericId = M.GenericId " +
                                "WHERE GenericName LIKE ?";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, searchKey + "%");

                        resultSet = preparedStatement.executeQuery();

                        // Creating MedicineGeneric object with retrieved data
                        while (resultSet.next()) {
                            medicineGeneric = new MedicineGeneric();

                            medicineGeneric.setGenericId(resultSet.getInt("GenericId"));
                            medicineGeneric.setGenericName(resultSet.getString("GenericName"));
                            medicineGeneric.setTherapeuticClass(resultSet.getString("TherapeuticClass"));
                            medicineGeneric.setIndications(resultSet.getString("Indication"));
                            medicineGeneric.setSideEffects(resultSet.getString("SideEffect"));
                            medicineGeneric.setBrands(resultSet.getInt("NoOfMedicine"));

                            medicineGenericObservableList.add(medicineGeneric);

                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    Database.getInstance().close(connection, preparedStatement, resultSet);
                }

                return medicineGenericObservableList;
            }

            @Override
            protected void updateValue(ObservableList<MedicineGeneric> medicineGenericObservableList) {
                super.updateValue(medicineGenericObservableList);

                if (medicineGenericObservableList != null) {
                    setGenericMedicineTable(medicineGenericObservableList);
                }
            }
        }).start();
    }

    private void setGenericMedicineTable(ObservableList<MedicineGeneric> medicineGenericObservableList) {
        genericIdColumn.setCellValueFactory(new PropertyValueFactory<>("genericId"));
        genericNameColumn.setCellValueFactory(new PropertyValueFactory<>("genericName"));
        therapeuticClassColumn.setCellValueFactory(new PropertyValueFactory<>("therapeuticClass"));
        indicationsColumn.setCellValueFactory(new PropertyValueFactory<>("indications"));
        sideEffectsColumn.setCellValueFactory(new PropertyValueFactory<>("sideEffects"));
        brandsColumn.setCellValueFactory(new PropertyValueFactory<>("brands"));

        genericMedicineTable.setItems(medicineGenericObservableList);

        genericMedicineTable.setRowFactory(tableView -> {
            TableRow<MedicineGeneric> tableRow = new TableRow<>();

            tableRow.setOnMouseClicked(event -> {
                if (!tableRow.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    MedicineGeneric medicineGeneric = tableRow.getItem();
                    // Opening medicine view
                }
            });

            return tableRow;
        });
    }
}
