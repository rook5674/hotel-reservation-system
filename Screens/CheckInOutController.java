package Screens;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

import java.util.ArrayList;

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
            ScreenNavigator.goTo("RoleSelection.fxml");
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

        try {
            Receptionist receptionist = (Receptionist) SessionContext.currentStaff;
            boolean checkedIn;

            synchronized (Database.class) {
                checkedIn = receptionist.checkInGuest(selected);
            }

            refreshReservations();
            statusLabel.setText(checkedIn
                    ? "Guest checked in."
                    : "Only CONFIRMED reservations can be checked in.");
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
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
            boolean checkedOut;

            synchronized (Database.class) {
                checkedOut = receptionist.checkOutGuest(selected);
            }

            refreshReservations();
            statusLabel.setText(checkedOut
                    ? "Guest checked out and reservation completed."
                    : "Room is already available, so checkout cannot be completed.");
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void refreshReservations() {
        ArrayList<Reservation> snapshot;
        synchronized (Database.class) {
            snapshot = new ArrayList<>(Database.getAllReservations());
        }
        reservationTable.setItems(FXCollections.observableArrayList(snapshot));
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("ReceptionistDashboard.fxml");
    }
}
