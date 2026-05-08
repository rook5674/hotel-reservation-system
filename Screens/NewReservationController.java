package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;
import enumerations.ReservationStatus;
import exceptions.RoomNotAvailableException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NewReservationController {

    @FXML private ComboBox<Room> roomComboBox;
    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private Label nightsLabel;
    @FXML private Label errorLabel;

    private Guest loggedInGuest;

    public void setGuest(Guest guest) {
        this.loggedInGuest = guest;
    }

    @FXML
    public void initialize() {
        for (Room room : Database.getAllRooms()) {
            if (room.isAvailable()) {
                roomComboBox.getItems().add(room);
            }
        }

        // Show room number in dropdown
        roomComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                setText(empty || room == null ? null :
                        "Room " + room.getRoomNumber() +
                                " - " + room.getRoomNumber() +
                                " ($" + room.getRoomType().getBasePricePerNight() + "/night)");
            }
        });
        roomComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                setText(empty || room == null ? null :
                        "Room " + room.getRoomNumber() +
                                " - " + room.getRoomType().getTypeName());
            }
        });

        checkInPicker.valueProperty().addListener((obs, o, n) -> updateNights());
        checkOutPicker.valueProperty().addListener((obs, o, n) -> updateNights());
    }

    private void updateNights() {
        LocalDate in  = checkInPicker.getValue();
        LocalDate out = checkOutPicker.getValue();
        if (in != null && out != null && out.isAfter(in)) {
            long nights = ChronoUnit.DAYS.between(in, out);
            nightsLabel.setText("Nights: " + nights);
        } else {
            nightsLabel.setText("Nights: —");
        }
    }

    @FXML
    private void handleConfirm() {
        errorLabel.setText("");

        if (loggedInGuest == null) {
            errorLabel.setText("No guest is logged in.");
            return;
        }

        Room room     = roomComboBox.getValue();
        LocalDate in  = checkInPicker.getValue();
        LocalDate out = checkOutPicker.getValue();

        if (room == null || in == null || out == null) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        try {
            Reservation r = Database.createAndAddReservation(loggedInGuest, room, in, out);
            r.confirm();
            room.setAvailable(false);

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Reservation confirmed for Room " + room.getRoomNumber() + "!");

            // Reset form
            handleCancel();

        } catch (RoomNotAvailableException e) {
            errorLabel.setText("Room is not available.");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        roomComboBox.setValue(null);
        checkInPicker.setValue(null);
        checkOutPicker.setValue(null);
        errorLabel.setText("");
        nightsLabel.setText("Nights: —");
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}