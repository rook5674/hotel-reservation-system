package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RoomBookingController {
    @FXML private ComboBox<Room> roomComboBox;
    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private Label nightsLabel;
    @FXML private Label totalLabel;
    @FXML private Label statusLabel;

    private Guest loggedInGuest;

    @FXML
    private void initialize() {
        loggedInGuest = SessionContext.currentGuest;
        if (loggedInGuest == null) {
            ScreenNavigator.goTo("RoleSelection.fxml");
            return;
        }

        for (Room room : Database.getAllRooms()) {
            if (room.isAvailable()) {
                roomComboBox.getItems().add(room);
            }
        }

        roomComboBox.setCellFactory(lv -> roomCell());
        roomComboBox.setButtonCell(roomCell());

        if (SessionContext.selectedRoom != null && SessionContext.selectedRoom.isAvailable()) {
            roomComboBox.setValue(SessionContext.selectedRoom);
        }

        checkInPicker.valueProperty().addListener((obs, oldValue, newValue) -> updatePreview());
        checkOutPicker.valueProperty().addListener((obs, oldValue, newValue) -> updatePreview());
        roomComboBox.valueProperty().addListener((obs, oldValue, newValue) -> updatePreview());
    }

    private ListCell<Room> roomCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                if (empty || room == null) {
                    setText(null);
                } else {
                    setText("Room " + room.getRoomNumber()
                            + " - " + room.getRoomType().getTypeName()
                            + " ($" + room.getRoomType().getBasePricePerNight() + "/night)");
                }
            }
        };
    }

    private void updatePreview() {
        Room room = roomComboBox.getValue();
        LocalDate in = checkInPicker.getValue();
        LocalDate out = checkOutPicker.getValue();

        if (room != null && in != null && out != null && out.isAfter(in)) {
            long nights = ChronoUnit.DAYS.between(in, out);
            nightsLabel.setText("Nights: " + nights);
            totalLabel.setText("Estimated Total: $" + String.format("%.2f", nights * room.getRoomType().getBasePricePerNight()));
        } else {
            nightsLabel.setText("Nights: —");
            totalLabel.setText("Estimated Total: —");
        }
    }

    @FXML
    private void handleConfirm() {
        try {
            Room room = roomComboBox.getValue();
            LocalDate in = checkInPicker.getValue();
            LocalDate out = checkOutPicker.getValue();

            if (room == null || in == null || out == null) {
                statusLabel.setText("Please choose a room, check-in date, and check-out date.");
                return;
            }

            boolean reserved;
            synchronized (Database.class) {
                reserved = loggedInGuest.makeReservation(room, in, out);
            }

            if (!reserved) {
                statusLabel.setText("Room is no longer available.");
                return;
            }

            UiUtil.showInfo("Reservation Confirmed", "Reservation confirmed for Room " + room.getRoomNumber() + ".");
            SessionContext.selectedRoom = null;
            ScreenNavigator.goTo("MyReservations.fxml");

        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void handleBack() {
        SessionContext.selectedRoom = null;
        ScreenNavigator.goTo("RoomBrowsing.fxml");
    }

    @FXML
    public void handleCancel() {
        handleBack();
    }
}
