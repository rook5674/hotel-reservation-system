package Screens;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

import java.util.ArrayList;

public class RoomManagementController {
    @FXML private TextField roomNumberField;
    @FXML private TextField floorField;
    @FXML private ComboBox<RoomType> roomTypeComboBox;
    @FXML private CheckBox availableCheckBox;
    @FXML private Label statusLabel;

    @FXML private TableView<Room> roomsTable;
    @FXML private TableColumn<Room, Integer> roomNumberCol;
    @FXML private TableColumn<Room, Integer> floorCol;
    @FXML private TableColumn<Room, String> typeCol;
    @FXML private TableColumn<Room, Boolean> availableCol;

    @FXML
    private void initialize() {
        if (!(SessionContext.currentStaff instanceof Admin)) {
            ScreenNavigator.goTo("RoleSelection.fxml");
            return;
        }

        roomTypeComboBox.setItems(FXCollections.observableArrayList(Database.getAllRoomTypes()));

        roomNumberCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getRoomNumber()).asObject());
        floorCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getFloor()).asObject());
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRoomType().getTypeName()));
        availableCol.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isAvailable()));

        roomsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, room) -> {
            if (room != null) {
                roomNumberField.setText(String.valueOf(room.getRoomNumber()));
                floorField.setText(String.valueOf(room.getFloor()));
                roomTypeComboBox.setValue(room.getRoomType());
                availableCheckBox.setSelected(room.isAvailable());
                statusLabel.setText("Selected Room " + room.getRoomNumber() + ". Change the checkbox, then click UPDATE AVAILABILITY.");
            }
        });

        refreshRooms();
    }

    @FXML
    private void handleCreate() {
        try {
            int roomNumber = UiUtil.parseInt(roomNumberField.getText(), "Room number");
            int floor = UiUtil.parseInt(floorField.getText(), "Floor");
            RoomType type = roomTypeComboBox.getValue();

            if (type == null) {
                statusLabel.setText("Please select a room type.");
                return;
            }

            synchronized (Database.class) {
                Room room = Database.createAndAddRoom(roomNumber, floor, type);
                room.setAvailable(availableCheckBox.isSelected());
            }

            refreshRooms();
            statusLabel.setText("Room created successfully.");
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleUpdateAvailability() {
        try {
            Room selected = roomsTable.getSelectionModel().getSelectedItem();


            int roomNumber = selected != null
                    ? selected.getRoomNumber()
                    : UiUtil.parseInt(roomNumberField.getText(), "Room number");

            boolean newAvailability = availableCheckBox.isSelected();
            Admin admin = (Admin) SessionContext.currentStaff;

            boolean updated;
            synchronized (Database.class) {
                updated = admin.updateRoomAvailability(roomNumber, newAvailability);
            }

            if (updated) {
                refreshRooms();
                reselectRoom(roomNumber);
                statusLabel.setText("Room " + roomNumber + " availability updated to " + (newAvailability ? "Available" : "Unavailable") + ".");
            } else {
                statusLabel.setText("Room not found.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            Room selected = roomsTable.getSelectionModel().getSelectedItem();
            int roomNumber = selected != null
                    ? selected.getRoomNumber()
                    : UiUtil.parseInt(roomNumberField.getText(), "Room number");

            Admin admin = (Admin) SessionContext.currentStaff;
            boolean deleted;

            synchronized (Database.class) {
                deleted = admin.delete(roomNumber);
            }

            if (deleted) {
                refreshRooms();
                clearForm();
                statusLabel.setText("Room deleted.");
            } else {
                statusLabel.setText("Room not found.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void refreshRooms() {
        ArrayList<Room> snapshot;
        synchronized (Database.class) {
            snapshot = new ArrayList<>(Database.getAllRooms());
        }
        roomsTable.setItems(FXCollections.observableArrayList(snapshot));
    }

    private void reselectRoom(int roomNumber) {
        for (Room room : roomsTable.getItems()) {
            if (room.getRoomNumber() == roomNumber) {
                roomsTable.getSelectionModel().select(room);
                roomsTable.scrollTo(room);
                availableCheckBox.setSelected(room.isAvailable());
                return;
            }
        }
    }

    private void clearForm() {
        roomNumberField.clear();
        floorField.clear();
        roomTypeComboBox.setValue(null);
        availableCheckBox.setSelected(false);
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("AdminDashboard.fxml");
    }
}
