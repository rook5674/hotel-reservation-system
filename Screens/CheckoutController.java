package Screens;

import enumerations.PaymentMethod;
import enumerations.ReservationStatus;
import exceptions.InvalidPaymentException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.*;

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

        reservationComboBox.setCellFactory(lv -> reservationCell());
        reservationComboBox.setButtonCell(reservationCell());
        reservationComboBox.setOnAction(e -> loadInvoicePreview());

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
            protected void updateItem(Reservation r, boolean empty) {
                super.updateItem(r, empty);
                setText(empty || r == null ? null :
                        "Reservation #" + r.getReservationId() + " - Room " + r.getRoom().getRoomNumber());
            }
        };
    }

    private void loadConfirmedReservations() {
        reservationComboBox.getItems().clear();

        for (Reservation r : Database.getAllReservations()) {
            boolean isOwner = r.getGuest().getUserName().equals(loggedInGuest.getUserName());
            if (isOwner && r.getStatus() == ReservationStatus.CONFIRMED) {
                reservationComboBox.getItems().add(r);
            }
        }

        if (reservationComboBox.getItems().isEmpty()) {
            errorLabel.setText("No confirmed reservations are ready for checkout.");
        } else {
            errorLabel.setText("");
        }

        hidePaymentSections();
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

    private void loadInvoicePreview() {
        Reservation r = reservationComboBox.getValue();
        if (r == null) {
            hidePaymentSections();
            return;
        }

        if (r.getStatus() != ReservationStatus.CONFIRMED) {
            hidePaymentSections();
            errorLabel.setText("Only CONFIRMED reservations can be checked out.");
            return;
        }

        invoiceRoom.setText("Room: " + r.getRoom().getRoomNumber()
                + " (" + r.getRoom().getRoomType().getTypeName() + ")");
        invoiceDates.setText("Dates: " + r.getCheckInDate() + " → " + r.getCheckOutDate());
        invoiceNights.setText("Nights: " + r.getNumberOfNights());
        invoiceTotal.setText("Total: $" + String.format("%.2f", r.calculateTotalCost()));

        showPaymentSections();
        errorLabel.setText("");
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
        if (cashCheck.isSelected()) methods.add(PaymentMethod.CASH);
        if (creditCardCheck.isSelected()) methods.add(PaymentMethod.CREDIT_CARD);
        if (onlineCheck.isSelected()) methods.add(PaymentMethod.ONLINE);

        if (methods.isEmpty()) {
            errorLabel.setText("Please select at least one payment method.");
            return;
        }

        try {
            Invoice invoice = Database.createAndAddInvoice(selectedReservation);
            invoice.pay(methods);

            UiUtil.showInfo("Payment Successful",
                    "Payment successful!\nRoom " + invoice.getReservation().getRoom().getRoomNumber()
                            + "\nTotal paid: $" + String.format("%.2f", invoice.getTotalAmount()));

            reservationComboBox.setValue(null);
            cashCheck.setSelected(false);
            creditCardCheck.setSelected(false);
            onlineCheck.setSelected(false);
            errorLabel.setText("");
            loadConfirmedReservations();

        } catch (InvalidPaymentException | IllegalStateException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void handleBack() {
        ScreenNavigator.goTo("GuestDashboard.fxml");
    }
}
