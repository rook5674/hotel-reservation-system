package Screens;

import enumerations.AmenityType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

public class AmenityManagementController {
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField newNameField;
    @FXML private ComboBox<AmenityType> typeComboBox;
    @FXML private Label statusLabel;

    @FXML private TableView<Amenity> amenityTable;
    @FXML private TableColumn<Amenity, Integer> idCol;
    @FXML private TableColumn<Amenity, String> nameCol;
    @FXML private TableColumn<Amenity, AmenityType> typeCol;

    @FXML
    private void initialize() {
        if (!(SessionContext.currentStaff instanceof Admin)) {
            ScreenNavigator.goTo("Login.fxml");
            return;
        }

        typeComboBox.setItems(FXCollections.observableArrayList(AmenityType.values()));

        idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        typeCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getType()));

        amenityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, amenity) -> {
            if (amenity != null) {
                idField.setText(String.valueOf(amenity.getId()));
                nameField.setText(amenity.getName());
                newNameField.setText(amenity.getName());
                typeComboBox.setValue(amenity.getType());
            }
        });

        refreshAmenities();
    }

    @FXML
    private void handleCreate() {
        try {
            int id = UiUtil.parseInt(idField.getText(), "ID");
            String name = nameField.getText().trim();
            AmenityType type = typeComboBox.getValue();

            if (type == null) {
                statusLabel.setText("Please select an amenity type.");
                return;
            }

            Admin admin = (Admin) SessionContext.currentStaff;
            admin.createAmenity(id, name, type);
            refreshAmenities();
            statusLabel.setText("Amenity created.");
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleUpdateName() {
        try {
            String oldName = nameField.getText().trim();
            String newName = newNameField.getText().trim();

            Admin admin = (Admin) SessionContext.currentStaff;
            if (admin.updateAmenityName(oldName, newName)) {
                refreshAmenities();
                statusLabel.setText("Amenity name updated.");
            } else {
                statusLabel.setText("Amenity not found.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            Amenity selected = amenityTable.getSelectionModel().getSelectedItem();
            String name = selected != null ? selected.getName() : nameField.getText().trim();

            Admin admin = (Admin) SessionContext.currentStaff;
            if (admin.deleteAmenity(name)) {
                refreshAmenities();
                statusLabel.setText("Amenity deleted.");
            } else {
                statusLabel.setText("Amenity not found.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void refreshAmenities() {
        amenityTable.setItems(FXCollections.observableArrayList(Database.getAllAmenities()));
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("AdminDashboard.fxml");
    }
}
