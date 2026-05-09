package Screens;

import enumerations.PaymentMethod;
import enumerations.ReservationStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Database;
import models.Guest;
import models.Reservation;

import java.util.ArrayList;
import java.util.List;

public class CheckoutController {
    @FXML private ComboBox<Reservation> reservationComboBox;
    @FXML private VBox invoiceBox;
    @FXML private VBox paymentBox;
    @FXML private Label invoiceRoom;
    @FXML private Label invoiceDates;
    @FXML private Label invoiceNights;
    @FXML private Label invoiceTotal;
    @FXML private CheckBox cashCheck;
    @FXML private CheckBox creditCardCheck;
    @FXML private CheckBox onlineCheck;
    @FXML private Label errorLabel;

    private Guest loggedInGuest;

    @FXML
    public void initialize() {
        loggedInGuest = SessionContext.currentGuest;

        if (loggedInGuest == null) {
            ScreenNavigator.goTo("RoleSelection.fxml");
            return;
        }

        reservationComboBox.setCellFactory(listView -> reservationCell());
        reservationComboBox.setButtonCell(reservationCell());
        reservationComboBox.setOnAction(event -> loadInvoicePreview());

        hidePaymentSections();
        loadConfirmedReservations();

        if (SessionContext.selectedReservation != null) {
            reservationComboBox.setValue(SessionContext.selectedReservation);
            SessionContext.selectedReservation = null;
            loadInvoicePreview();
        }
    }

    private ListCell<Reservation> reservationCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);

                if (empty || reservation == null) {
                    setText(null);
                } else {
                    setText("Reservation #" + reservation.getReservationId()
                            + " - Room " + reservation.getRoom().getRoomNumber());
                }
            }
        };
    }

    private void loadConfirmedReservations() {
        reservationComboBox.getItems().clear();

        synchronized (Database.class) {
            for (Reservation reservation : Database.getAllReservations()) {
                boolean isOwner = reservation.getGuest().equals(loggedInGuest);
                boolean isConfirmed = reservation.getStatus() == ReservationStatus.CONFIRMED;

                if (isOwner && isConfirmed) {
                    reservationComboBox.getItems().add(reservation);
                }
            }
        }

        if (reservationComboBox.getItems().isEmpty()) {
            errorLabel.setText("No confirmed reservations are ready for checkout.");
        } else {
            errorLabel.setText("");
        }

        hidePaymentSections();
    }

    private void loadInvoicePreview() {
        Reservation reservation = reservationComboBox.getValue();

        if (reservation == null) {
            hidePaymentSections();
            return;
        }

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            hidePaymentSections();
            errorLabel.setText("Only CONFIRMED reservations can be checked out.");
            return;
        }

        double totalCost = reservation.calculateTotalCost();

        invoiceRoom.setText("Room: " + reservation.getRoom().getRoomNumber()
                + " (" + reservation.getRoom().getRoomType().getTypeName() + ")");
        invoiceDates.setText("Dates: " + reservation.getCheckInDate()
                + " → " + reservation.getCheckOutDate());
        invoiceNights.setText("Nights: " + reservation.getNumberOfNights());
        invoiceTotal.setText("Total: $" + String.format("%.2f", totalCost));

        showPaymentSections();
        errorLabel.setText("Wallet Balance: $" + String.format("%.2f", loggedInGuest.getBalance()));
    }

    @FXML
    public void handlePayment() {
        Reservation selectedReservation = reservationComboBox.getValue();

        if (selectedReservation == null) {
            errorLabel.setText("Please select a reservation first.");
            return;
        }

        if (selectedReservation.getStatus() != ReservationStatus.CONFIRMED) {
            errorLabel.setText("Only CONFIRMED reservations can be paid.");
            return;
        }

        List<PaymentMethod> methods = new ArrayList<>();

        if (cashCheck.isSelected()) {
            methods.add(PaymentMethod.CASH);
        }

        if (creditCardCheck.isSelected()) {
            methods.add(PaymentMethod.CREDIT_CARD);
        }

        if (onlineCheck.isSelected()) {
            methods.add(PaymentMethod.ONLINE);
        }

        if (methods.isEmpty()) {
            errorLabel.setText("Please select at least one payment method.");
            return;
        }

        double totalCost = selectedReservation.calculateTotalCost();

        if (loggedInGuest.getBalance() < totalCost) {
            errorLabel.setText("Insufficient wallet balance. Required: $"
                    + String.format("%.2f", totalCost)
                    + ", available: $"
                    + String.format("%.2f", loggedInGuest.getBalance()));
            return;
        }

        try {
            boolean paid;

            synchronized (Database.class) {
                paid = loggedInGuest.checkoutAndPay(selectedReservation, methods);
            }

            if (!paid) {
                errorLabel.setText("Payment failed. Check reservation status, ownership, or wallet balance.");
                return;
            }

            UiUtil.showInfo(
                    "Payment Successful",
                    "Payment successful!"
                            + "\nRoom " + selectedReservation.getRoom().getRoomNumber()
                            + "\nTotal paid: $" + String.format("%.2f", totalCost)
                            + "\nRemaining balance: $" + String.format("%.2f", loggedInGuest.getBalance())
            );

            reservationComboBox.setValue(null);
            cashCheck.setSelected(false);
            creditCardCheck.setSelected(false);
            onlineCheck.setSelected(false);
            errorLabel.setText("");

            loadConfirmedReservations();

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void hidePaymentSections() {
        invoiceBox.setVisible(false);
        invoiceBox.setManaged(false);
        paymentBox.setVisible(false);
        paymentBox.setManaged(false);
    }

    private void showPaymentSections() {
        invoiceBox.setVisible(true);
        invoiceBox.setManaged(true);
        paymentBox.setVisible(true);
        paymentBox.setManaged(true);
    }

    @FXML
    public void handleBack() {
        ScreenNavigator.goTo("GuestDashboard.fxml");
    }
}