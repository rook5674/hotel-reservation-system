package Screens;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

public class CheckInOutController {
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> idCol;
    @FXML private TableColumn<Reservation, String> guestCol;
    @FXML private TableColumn<Reservation, String> roomCol;
    @FXML private TableColumn<Reservation, String> checkInCol;
    @FXML private TableColumn<Reservation, String> checkOutCol;
    @FXML private TableColumn<Reservation, String> statusCol;
    @FXML private TableColumn<Reservation, Boolean> roomAvailableCol;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        if (!(SessionContext.currentStaff instanceof Receptionist)) {
            ScreenNavigator.goTo("Login.fxml");
            return;
        }

        idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getReservationId()).asObject());
        guestCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGuest().getUserName()));
        roomCol.setCellValueFactory(c -> new SimpleStringProperty("Room " + c.getValue().getRoom().getRoomNumber()));
        checkInCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCheckInDate().toString()));
        checkOutCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCheckOutDate().toString()));
        statusCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus().toString()));
        roomAvailableCol.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getRoom().isAvailable()));

        refreshReservations();
    }

    @FXML
    private void handleCheckIn() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a reservation.");
            return;
        }

        Receptionist receptionist = (Receptionist) SessionContext.currentStaff;
        if (receptionist.checkInGuest(selected)) {
            refreshReservations();
            statusLabel.setText("Guest checked in.");
        } else {
            statusLabel.setText("Only CONFIRMED reservations can be checked in.");
        }
    }

    @FXML
    private void handleCheckOut() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a reservation.");
            return;
        }

        try {
            Receptionist receptionist = (Receptionist) SessionContext.currentStaff;
            if (receptionist.checkOutGuest(selected)) {
                refreshReservations();
                statusLabel.setText("Guest checked out and reservation completed.");
            } else {
                statusLabel.setText("Room is already available, so checkout cannot be completed.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void refreshReservations() {
        reservationTable.setItems(FXCollections.observableArrayList(Database.getAllReservations()));
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("ReceptionistDashboard.fxml");
    }
}
