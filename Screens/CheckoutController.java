package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.*;
import enumerations.PaymentMethod;
import enumerations.ReservationStatus;
import exceptions.InvalidPaymentException;

import java.util.ArrayList;
import java.util.List;

public class CheckoutController {

    @FXML private ComboBox<Reservation> reservationComboBox;
    @FXML private VBox    invoiceBox;
    @FXML private VBox    paymentBox;
    @FXML private Label   invoiceRoom;
    @FXML private Label   invoiceDates;
    @FXML private Label   invoiceNights;
    @FXML private Label   invoiceTotal;
    @FXML private CheckBox cashCheck;
    @FXML private CheckBox creditCardCheck;
    @FXML private CheckBox onlineCheck;
    @FXML private Label   errorLabel;

    private Invoice currentInvoice;
    private Guest loggedInGuest;

    public void setGuest(Guest guest) {
        this.loggedInGuest = guest;
        loadConfirmedReservations();
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

        reservationComboBox.setOnAction(e -> loadInvoice());
    }

    private void loadConfirmedReservations() {
        reservationComboBox.getItems().clear();
        for (Reservation r : Database.getAllReservations()) {
            boolean isOwner = loggedInGuest == null ||
                    r.getGuest().getUserName().equals(loggedInGuest.getUserName());
            if (isOwner && r.getStatus() == ReservationStatus.CONFIRMED) {
                reservationComboBox.getItems().add(r);
            }
        }
    }

    private void loadInvoice() {
        Reservation r = reservationComboBox.getValue();
        if (r == null) return;

        currentInvoice = Database.createAndAddInvoice(r);

        invoiceRoom.setText("Room: " + r.getRoom().getRoomNumber()
                + " (" + r.getRoom().getRoomType().getTypeName() + ")");
        invoiceDates.setText("Dates: " + r.getCheckInDate()
                + "  →  " + r.getCheckOutDate());
        invoiceNights.setText("Nights: " + r.getNumberOfNights());
        invoiceTotal.setText("Total: $"
                + String.format("%.2f", currentInvoice.getTotalAmount()));

        invoiceBox.setVisible(true);
        paymentBox.setVisible(true);
        errorLabel.setText("");
    }

    @FXML
    private void handlePayment() {
        if (currentInvoice == null) {
            errorLabel.setText("Please select a reservation first.");
            return;
        }

        List<PaymentMethod> methods = new ArrayList<>();
        if (cashCheck.isSelected())       methods.add(PaymentMethod.CASH);
        if (creditCardCheck.isSelected()) methods.add(PaymentMethod.CREDIT_CARD);
        if (onlineCheck.isSelected())     methods.add(PaymentMethod.ONLINE);

        if (methods.isEmpty()) {
            errorLabel.setText("Please select at least one payment method.");
            return;
        }

        try {
            currentInvoice.pay(methods);

            showAlert("Payment successful!\nRoom " +
                    currentInvoice.getReservation().getRoom().getRoomNumber() +
                    "\nTotal paid: $" +
                    String.format("%.2f", currentInvoice.getTotalAmount()));

            // Reset screen
            reservationComboBox.getItems().remove(reservationComboBox.getValue());
            reservationComboBox.setValue(null);
            invoiceBox.setVisible(false);
            paymentBox.setVisible(false);
            cashCheck.setSelected(false);
            creditCardCheck.setSelected(false);
            onlineCheck.setSelected(false);
            currentInvoice = null;

        } catch (InvalidPaymentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Successful");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}