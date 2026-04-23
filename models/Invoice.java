package models;
// Invoice.java
import exceptions.InvalidPaymentException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import enumerations.PaymentMethod;

public class Invoice implements interfaces.Payable {

    private static int idCounter = 1;

    private int invoiceId;
    private Reservation reservation;
    private double totalAmount;
    private List<PaymentMethod> paymentMethods;   // supports multiple methods
    private LocalDate paymentDate;
    private boolean isPaid;

    public Invoice(Reservation reservation) {
        validateReservation(reservation);

        this.invoiceId      = idCounter++;
        this.reservation    = reservation;
        this.totalAmount    = reservation.calculateTotalCost();
        this.paymentMethods = new ArrayList<>();
        this.paymentDate    = null;
        this.isPaid         = false;
    }

    // --- Validation ---

    private void validateReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null.");
        }
    }

    private void validatePaymentMethod(PaymentMethod method) {
        if (method == null) {
            throw new InvalidPaymentException("Payment method cannot be null.");
        }
    }

    private void validateTotalAmount(double amount) {
        if (amount < 0) {
            throw new InvalidPaymentException("Total amount cannot be negative.");
        }
    }

    // --- Business Logic ---

    /**
     * Adds a payment method and marks the invoice as paid.
     * Supports multiple payment methods per checkout.
     */
    public void pay(PaymentMethod method) {
        if (isPaid) {
            throw new InvalidPaymentException("Invoice #" + invoiceId + " has already been paid.");
        }
        validatePaymentMethod(method);

        this.paymentMethods.add(method);
        this.paymentDate = LocalDate.now();
        this.isPaid = true;

        // Complete the linked reservation
        reservation.complete();
    }

    /**
     * Pay with multiple methods at once (e.g., partial cash + card).
     */
    public void pay(List<PaymentMethod> methods) {
        if (isPaid) {
            throw new InvalidPaymentException("Invoice #" + invoiceId + " has already been paid.");
        }
        if (methods == null || methods.isEmpty()) {
            throw new InvalidPaymentException("At least one payment method is required.");
        }
        for (PaymentMethod m : methods) {
            validatePaymentMethod(m);
        }

        this.paymentMethods.addAll(methods);
        this.paymentDate = LocalDate.now();
        this.isPaid = true;

        reservation.complete();
    }

    // --- Getters & Setters ---

    public int getInvoiceId() { return invoiceId; }

    public Reservation getReservation() { return reservation; }

    public double getTotalAmount() { return totalAmount; }

    public void setTotalAmount(double totalAmount) {
        validateTotalAmount(totalAmount);
        this.totalAmount = totalAmount;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return Collections.unmodifiableList(paymentMethods);
    }

    public LocalDate getPaymentDate() { return paymentDate; }

    public boolean isPaid() { return isPaid; }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + invoiceId +
                ", reservationId=" + reservation.getReservationId() +
                ", guest=" + reservation.getGuest().getUserName() +
                ", totalAmount=" + totalAmount +
                ", paymentMethods=" + paymentMethods +
                ", paymentDate=" + paymentDate +
                ", paid=" + isPaid +
                '}';
    }

    @Override
    public double calculateTotal() {
        return totalAmount;
    }

    
}