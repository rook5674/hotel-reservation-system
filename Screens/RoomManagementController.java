package Screens;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

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
            ScreenNavigator.goTo("Login.fxml");
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

            if (Database.getRoomByNumber(roomNumber) != null) {
                statusLabel.setText("Room number already exists.");
                return;
            }

            Room room = Database.createAndAddRoom(roomNumber, floor, type);
            room.setAvailable(availableCheckBox.isSelected());
            refreshRooms();
            statusLabel.setText("Room created successfully.");
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleUpdateAvailability() {
        try {
            int roomNumber = UiUtil.parseInt(roomNumberField.getText(), "Room number");
            Admin admin = (Admin) SessionContext.currentStaff;

            if (admin.updateRoomAvailability(roomNumber, availableCheckBox.isSelected())) {
                refreshRooms();
                statusLabel.setText("Room availability updated.");
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
            int roomNumber = selected != null ? selected.getRoomNumber() : UiUtil.parseInt(roomNumberField.getText(), "Room number");

            Admin admin = (Admin) SessionContext.currentStaff;
            if (admin.delete(roomNumber)) {
                refreshRooms();
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
        roomsTable.setItems(FXCollections.observableArrayList(Database.getAllRooms()));
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("AdminDashboard.fxml");
    }
}
