package docmate.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLMedicineController implements Initializable {

    public static final String TAG = "DOCMATE";

    @FXML
    private ComboBox<String> medicineTypeComboBox;

    @FXML
    private TextField searchMedicineTextField;
    @FXML
    private Button searchMedicineButton;

    @FXML
    private Button addMedicineButton;

    @FXML
    private VBox medicineContentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openBrandMedicine();
        initMedicineTypeComboBox();
    }

    private void initMedicineTypeComboBox() {
        medicineTypeComboBox.getItems().addAll("Medicine Brand", "Generic Medicine");
        medicineTypeComboBox.setValue("Medicine Brand");

        medicineTypeComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if ("Medicine Brand".equals(newValue)) {
                        openBrandMedicine();
                    } else if ("Generic Medicine".equals(newValue)) {
                        openGenericMedicine();
                    }
                });
    }

    private void openBrandMedicine() {
        try {
            // Loading medicine brand scene
            FXMLLoader medicineBrandFXMLLoader = new FXMLLoader(getClass()
                    .getResource("../view/FXMLMedicineBrand.fxml"));
            Parent medicineBrandParent = medicineBrandFXMLLoader.load();

            // Injecting search utility into medicine brand controller
            FXMLMedicineBrandController fxmlMedicineBrandController = medicineBrandFXMLLoader.getController();
            fxmlMedicineBrandController.setSearchUtility(searchMedicineTextField, searchMedicineButton);

            medicineContentPane.getChildren().clear();
            medicineContentPane.getChildren().add(medicineBrandParent);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void openGenericMedicine() {
        try {
            // Loading medicine generic scene
            FXMLLoader medicineGenericFXMLLoader = new FXMLLoader(getClass()
                    .getResource("../view/FXMLMedicineGeneric.fxml"));
            Parent medicineGenericParent = medicineGenericFXMLLoader.load();

            // Injecting search utility into medicine generic controller
            FXMLMedicineGenericController fxmlMedicineGenericController = medicineGenericFXMLLoader.getController();
            fxmlMedicineGenericController.setSearchUtility(searchMedicineTextField, searchMedicineButton);

            medicineContentPane.getChildren().clear();
            medicineContentPane.getChildren().add(medicineGenericParent);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
