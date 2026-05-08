package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class RoomTypeManagementController {

    @FXML private TextField idField;
    @FXML private TextField typeNameField;
    @FXML private TextField occupancyField;
    @FXML private TextField priceField;
    @FXML private ListView<String> roomTypeListView;
    @FXML private Label feedbackLabel;

    private ObservableList<RoomTypeRecord> roomTypeItems = FXCollections.observableArrayList();
    private ObservableList<String> displayItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        roomTypeListView.setItems(displayItems);

        roomTypeListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int i = newVal.intValue();
            if (i >= 0 && i < roomTypeItems.size()) {
                idField.setText(String.valueOf(roomTypeItems.get(i).id));
                typeNameField.setText(roomTypeItems.get(i).typeName);
                occupancyField.setText(String.valueOf(roomTypeItems.get(i).maxOccupancy));
                priceField.setText(String.valueOf(roomTypeItems.get(i).pricePerNight));
            }
        });
    }

    @FXML
    private void createRoomType() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String typeName = typeNameField.getText();
            int maxOccupancy = Integer.parseInt(occupancyField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            if (typeName.isBlank()) {
                feedbackLabel.setText("Type name cannot be empty.");
                return;
            }

            if (maxOccupancy <= 0 || price < 0) {
                feedbackLabel.setText("Occupancy must be > 0 and price must be >= 0.");
                return;
            }

            for (RoomTypeRecord r : roomTypeItems) {
                if (r.id == id) {
                    feedbackLabel.setText("ID already exists.");
                    return;
                }
            }

            roomTypeItems.add(new RoomTypeRecord(id, typeName, maxOccupancy, price));
            refreshList();
            clearFields();
            feedbackLabel.setText("Room type created.");

        } catch (NumberFormatException e) {
            feedbackLabel.setText("ID, occupancy, and price must be valid numbers.");
        }
    }

    @FXML
    private void updateRoomTypePrice() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            double newPrice = Double.parseDouble(priceField.getText().trim());

            if (newPrice < 0) {
                feedbackLabel.setText("Price cannot be negative.");
                return;
            }

            for (RoomTypeRecord r : roomTypeItems) {
                if (r.id == id) {
                    r.pricePerNight = newPrice;
                    refreshList();
                    feedbackLabel.setText("Price updated.");
                    return;
                }
            }

            feedbackLabel.setText("Room type not found.");

        } catch (NumberFormatException e) {
            feedbackLabel.setText("ID and price must be valid numbers.");
        }
    }

    @FXML
    private void deleteRoomType() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            for (int i = 0; i < roomTypeItems.size(); i++) {
                if (roomTypeItems.get(i).id == id) {
                    roomTypeItems.remove(i);
                    refreshList();
                    clearFields();
                    feedbackLabel.setText("Room type deleted.");
                    return;
                }
            }

            feedbackLabel.setText("Room type not found.");

        } catch (NumberFormatException e) {
            feedbackLabel.setText("ID must be a number.");
        }
    }

    @FXML
    private void readRoomType() {
        refreshList();
        feedbackLabel.setText(roomTypeItems.isEmpty() ? "No room types found." : "List refreshed.");
    }

    @FXML
    private void clearFields() {
        idField.clear();
        typeNameField.clear();
        occupancyField.clear();
        priceField.clear();
        roomTypeListView.getSelectionModel().clearSelection();
        feedbackLabel.setText("");
    }

    private void refreshList() {
        displayItems.clear();
        for (RoomTypeRecord r : roomTypeItems) {
            displayItems.add("ID: " + r.id + " | Name: " + r.typeName
                    + " | Occupancy: " + r.maxOccupancy + " | Price: " + r.pricePerNight);
        }
    }

    private static class RoomTypeRecord {
        int id;
        String typeName;
        int maxOccupancy;
        double pricePerNight;

        RoomTypeRecord(int id, String typeName, int maxOccupancy, double pricePerNight) {
            this.id = id;
            this.typeName = typeName;
            this.maxOccupancy = maxOccupancy;
            this.pricePerNight = pricePerNight;
        }
    }
}