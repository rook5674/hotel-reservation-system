package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AmenityManagementController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ListView<String> amenityListView;
    @FXML private Label feedbackLabel;

    private ObservableList<AmenityRecord> amenityItems = FXCollections.observableArrayList();
    private ObservableList<String> displayItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList(
                "WIFI", "TV", "MINI_BAR", "AIR_CONDITIONING", "HAIR_DRYER", "BALCONY", "BREAKFAST_INCLUDED"
        ));
        amenityListView.setItems(displayItems);

        amenityListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int i = newVal.intValue();
            if (i >= 0 && i < amenityItems.size()) {
                idField.setText(String.valueOf(amenityItems.get(i).id));
                nameField.setText(amenityItems.get(i).name);
                typeComboBox.setValue(amenityItems.get(i).type);
            }
        });
    }

    @FXML
    private void createAmenity() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText();
            String type = typeComboBox.getValue();

            if (name.isBlank() || type == null) {
                feedbackLabel.setText("Please fill all fields.");
                return;
            }

            for (AmenityRecord r : amenityItems) {
                if (r.id == id) {
                    feedbackLabel.setText("ID already exists.");
                    return;
                }
            }

            amenityItems.add(new AmenityRecord(id, name, type));
            refreshList();
            clearFields();
            feedbackLabel.setText("Amenity created.");

        } catch (NumberFormatException e) {
            feedbackLabel.setText("ID must be a number.");
        }
    }

    @FXML
    private void updateAmenityName() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String newName = nameField.getText();

            if (newName.isBlank()) {
                feedbackLabel.setText("Name cannot be empty.");
                return;
            }

            for (AmenityRecord r : amenityItems) {
                if (r.id == id) {
                    r.name = newName;
                    refreshList();
                    feedbackLabel.setText("Name updated.");
                    return;
                }
            }

            feedbackLabel.setText("Amenity not found.");

        } catch (NumberFormatException e) {
            feedbackLabel.setText("ID must be a number.");
        }
    }

    @FXML
    private void deleteAmenity() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            for (int i = 0; i < amenityItems.size(); i++) {
                if (amenityItems.get(i).id == id) {
                    amenityItems.remove(i);
                    refreshList();
                    clearFields();
                    feedbackLabel.setText("Amenity deleted.");
                    return;
                }
            }

            feedbackLabel.setText("Amenity not found.");

        } catch (NumberFormatException e) {
            feedbackLabel.setText("ID must be a number.");
        }
    }

    @FXML
    private void readAmenity() {
        refreshList();
        feedbackLabel.setText(amenityItems.isEmpty() ? "No amenities found." : "List refreshed.");
    }

    @FXML
    private void clearFields() {
        idField.clear();
        nameField.clear();
        typeComboBox.setValue(null);
        amenityListView.getSelectionModel().clearSelection();
        feedbackLabel.setText("");
    }

    private void refreshList() {
        displayItems.clear();
        for (AmenityRecord r : amenityItems) {
            displayItems.add("ID: " + r.id + " | Name: " + r.name + " | Type: " + r.type);
        }
    }

    private static class AmenityRecord {
        int id;
        String name;
        String type;

        AmenityRecord(int id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }
    }
}