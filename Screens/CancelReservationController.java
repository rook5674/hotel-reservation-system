package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.*;
import enumerations.ReservationStatus;

public class CancelReservationController {

    @FXML private ComboBox<Reservation> reservationComboBox;
    @FXML private VBox   detailsBox;
    @FXML private Label  detailRoom;
    @FXML private Label  detailDates;
    @FXML private Label  detailNights;
    @FXML private Label  detailCost;
    @FXML private Label  detailStatus;
    @FXML private Label  errorLabel;

    private Guest loggedInGuest;

    public void setGuest(Guest guest) {
        this.loggedInGuest = guest;
        loadCancellableReservations();
    }

    @FXML
    public void initialize() {
        // Show reservation info in dropdown
        reservationComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation r, boolean empty) {
                super.updateItem(r, empty);
                setText(empty || r == null ? null :
                        "Reservation #" + r.getReservationId() +
                                " - Room " + r.getRoom().getRoomNumber());
            }
        });
        reservationComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Reservation r, boolean empty) {
                super.updateItem(r, empty);
                setText(empty || r == null ? null :
                        "Reservation #" + r.getReservationId() +
                                " - Room " + r.getRoom().getRoomNumber());
            }
        });

        reservationComboBox.setOnAction(e -> showDetails());
    }

    private void loadCancellableReservations() {
        reservationComboBox.getItems().clear();
        for (Reservation r : Database.getAllReservations()) {
            boolean isOwner = loggedInGuest == null ||
                    r.getGuest().getUserName().equals(loggedInGuest.getUserName());
            boolean isCancellable = r.getStatus() == ReservationStatus.PENDING ||
                    r.getStatus() == ReservationStatus.CONFIRMED;

            if (isOwner && isCancellable) {
                reservationComboBox.getItems().add(r);
            }
        }
    }

    private void showDetails() {
        Reservation r = reservationComboBox.getValue();
        if (r == null) return;

        detailRoom.setText("Room: " + r.getRoom().getRoomNumber()
                + " (" + r.getRoom().getRoomType().getTypeName() + ")");
        detailDates.setText("Dates: " + r.getCheckInDate()
                + "  →  " + r.getCheckOutDate());
        detailNights.setText("Nights: " + r.getNumberOfNights());
        detailCost.setText("Total Cost: $"
                + String.format("%.2f", r.calculateTotalCost()));
        detailStatus.setText("Status: " + r.getStatus());
        detailsBox.setVisible(true);
        errorLabel.setText("");
    }

    @FXML
    private void handleCancel() {
        Reservation r = reservationComboBox.getValue();
        if (r == null) {
            errorLabel.setText("Please select a reservation first.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancellation");
        confirm.setContentText("Cancel reservation #" + r.getReservationId()
                + " for Room " + r.getRoom().getRoomNumber() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                r.cancel();
                reservationComboBox.getItems().remove(r);
                detailsBox.setVisible(false);
                errorLabel.setText("");
                showAlert("Reservation #" + r.getReservationId() + " cancelled successfully.");
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cancelled");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}